<template>
    <div ref="nav" class="ui fixed inverted stackable pointing menu" :class="{'transparent':$route.name==='home'}">
        <div class="ui container">
            <router-link to="/">
                <h3 class="ui header item m-blue">{{ blogName }}</h3>
            </router-link>
            <router-link to="/home" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='home'}">
                <i class="home icon"></i>首页
            </router-link>
            <el-dropdown trigger="click" @command="categoryRoute">
				<span class="el-dropdown-link item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='category'}">
					<i class="idea icon"></i>分类<i class="caret down icon"></i>
				</span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="category.name" v-for="(category,index) in categoryList" :key="index">{{ category.name }}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <router-link to="/archives" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='archives'}">
                <i class="clone icon"></i>归档
            </router-link>
            <router-link to="/moments" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='moments'}">
                <i class="comment alternate outline icon"></i>动态
            </router-link>
            <router-link to="/friends" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='friends'}">
                <i class="users icon"></i>友人帐
            </router-link>
            <router-link to="/about" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='about'}">
                <i class="info icon"></i>关于我
            </router-link>
            <el-row>
                <el-col :span="8">
                    <el-input placeholder="search..." v-model="queryString" :clearable="true"
                              @keyup.native.enter="search" size="small" style="min-width: 500px">
                        <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
                    </el-input>
                </el-col>
            </el-row>
            <button class="ui menu black icon button m-right-top m-mobile-show" @click="toggle">
                <i class="sidebar icon"></i>
            </button>
        </div>
    </div>
</template>

<script>
    import {getSearchBlogList} from "@/api/blog";
    import {mapState} from 'vuex'
    export default {
        name: "Nav",
        props: {
            blogName: {
                type: String,
                required: true
            },
            categoryList: {
                type: Array,
                required: true
            },
        },
        data() {
            return {
                mobileHide: true,
                queryString: '',
                queryResult: [],
                timer: null
            }
        },
        computed: {
            ...mapState(['clientSize'])
        },
        watch: {
            //路由改变时，收起导航栏
            '$route.path'() {
                this.mobileHide = true
            }
        },
        mounted() {
            //监听页面滚动位置，改变导航栏的显示
            window.addEventListener('scroll', () => {
                //首页且不是移动端
                if (this.$route.name === 'home') {
                    if (window.scrollY > this.clientSize.clientHeight / 2) {
                        this.$refs.nav.classList.remove('transparent')
                    } else {
                        this.$refs.nav.classList.add('transparent')
                    }
                }
            })
            //监听点击事件，收起导航菜单
            document.addEventListener('click', (e) => {
                //遍历冒泡
                let flag = e.path.some(item => {
                    if (item === this.$refs.nav) return true
                })
                //如果导航栏是打开状态，且点击的元素不是Nav的子元素，则收起菜单
                if (!this.mobileHide && !flag) {
                    this.mobileHide = true
                }
            })
        },
        methods: {
            toggle() {
                this.mobileHide = !this.mobileHide
            },
            //进入分类
            categoryRoute(name) {
                this.$router.push(`/category/${name}`)
            },
            //搜索
            search(queryString, callback) {
                if (queryString == null
                    || queryString.trim() === ''
                    || queryString.indexOf('%') !== -1
                    || queryString.indexOf('_') !== -1
                    || queryString.indexOf('[') !== -1
                    || queryString.indexOf('#') !== -1
                    || queryString.indexOf('*') !== -1
                    || queryString.trim().length > 20) {
                    return
                }
                getSearchBlogList(queryString).then(res => {
                    this.queryResult = res.data
                    if (this.queryResult.length === 0) {
                        this.queryResult.push({title: '无相关搜索结果'})
                    }
                    callback(this.queryResult)
                })
            }
        }
    }
</script>

<style scoped>
    .ui.fixed.menu .container {
        width: 1400px !important;
        margin-left: auto !important;
        margin-right: auto !important;
    }

    .ui.fixed.menu {
        transition: .3s ease-out;
    }

    .ui.inverted.pointing.menu.transparent {
        background: transparent !important;
    }

    .ui.inverted.pointing.menu.transparent .active.item:after {
        background: transparent !important;
        transition: .3s ease-out;
    }

    .ui.inverted.pointing.menu.transparent .active.item:hover:after {
        background: transparent !important;
    }

    .el-dropdown-link {
        outline-style: none !important;
        outline-color: unset !important;
        height: 100%;
        cursor: pointer;
    }

    .el-dropdown-menu {
        margin: 7px 0 0 0 !important;
        padding: 0 !important;
        border: 0 !important;
        background: #1b1c1d !important;
    }

    .el-dropdown-menu__item {
        padding: 0 15px !important;
        color: rgba(255, 255, 255, .9) !important;
    }

    .el-dropdown-menu__item:hover {
        background: rgba(255, 255, 255, .08) !important;
    }

    .el-popper .popper__arrow::after {
        content: none !important;
    }

    .popper__arrow {
        display: none !important;
    }

    .m-search {
        min-width: 220px;
        padding: 0 !important;
    }

    .m-search input {
        color: rgba(255, 255, 255, .9);;
        border: 0px !important;
        background-color: inherit;
        padding: .67857143em 2.1em .67857143em 1em;
    }

    .m-search i {
        color: rgba(255, 255, 255, .9) !important;
    }

    .m-search-item {
        min-width: 350px !important;
    }

    .m-search-item li {
        line-height: normal !important;
        padding: 8px 10px !important;
    }

    .m-search-item li .title {
        text-overflow: ellipsis;
        overflow: hidden;
        color: rgba(0, 0, 0, 0.87);
    }

    .m-search-item li .content {
        text-overflow: ellipsis;
        font-size: 12px;
        color: rgba(0, 0, 0, .70);
    }
</style>