<template>
    <div>
        <!--顶部导航-->
        <Nav :blogName="siteInfo.blogName" :categoryList="categoryList"/>

        <div class="main">
            <div class="m-padded-tb-big">
                <div class="ui container">
                    <div class="ui stackable grid">
                        <!--左侧-->

                        <!--中间-->
                        <div class="ten wide column">
                            <keep-alive include="Home">
                                <router-view/>
                            </keep-alive>
                        </div>

                        <!--右侧-->
                        <div class="four wide column m-mobile-hide right floated">
                            <RandomBlog :randomBlogList="randomBlogList" :class="{'m-display-none':focusMode}"/>
                            <Tags :tagList="tagList" :class="{'m-display-none':focusMode}"/>
                            <!--只在文章页面显示目录-->
<!--                            <Tocbot v-if="$route.name==='blog'"/>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {getSite} from '@/api/index'
    import Nav from "@/components/index/Nav";
    import Tags from "@/components/sidebar/Tags";
    import RandomBlog from "@/components/sidebar/RandomBlog";
    import Tocbot from "@/components/sidebar/Tocbot";
    import {mapState} from 'vuex'
    import {SAVE_CLIENT_SIZE} from "../store/mutations-types";
    export default {
        name: "Index",
        components: {Nav, Tags, Tocbot, RandomBlog},
        data() {
            return {
                siteInfo: {
                    blogName: 'yhblog'
                },
                categoryList: [],
                tagList: [],
                randomBlogList: [],
                badges: [],
                newBlogList: [],
            }
        },
        computed: {
            ...mapState(['focusMode'])
        },
        watch: {
            //路由改变时，页面滚动至顶部
            '$route.path'() {
                this.scrollToTop()
            }
        },
        created() {
            this.getSite()
        },
        mounted() {
            //保存可视窗口大小
            this.$store.commit(SAVE_CLIENT_SIZE, {clientHeight: document.body.clientHeight, clientWidth: document.body.clientWidth})
            window.onresize = () => {
                this.$store.commit(SAVE_CLIENT_SIZE, {clientHeight: document.body.clientHeight, clientWidth: document.body.clientWidth})
            }
        },
        methods: {
            getSite() {
                getSite().then(res => {
                    // this.siteInfo = res.data.siteInfo
                    // this.badges = res.data.badges
                    this.newBlogList = res.data.newBlogList
                    this.categoryList = res.data.categoryList
                    this.tagList = res.data.tagList
                    this.randomBlogList = res.data.randomBlogList
                    // this.$store.dispatch('saveSiteInfo', this.siteInfo)
                    // this.$store.dispatch('saveIntroduction', res.data.introduction)
                    // document.title = this.$route.meta.title + this.siteInfo.webTitleSuffix

                    document.title = this.$route.meta.title
                })
            },
        }
    }
</script>

<style scoped>
    .site {
        display: flex;
        min-height: 100vh; /* 没有元素时，也把页面撑开至100% */
        flex-direction: column;
    }

    .main {
        margin-top: 40px;
        flex: 1;
    }

    .main .ui.container {
        width: 1400px !important;
        margin-left: auto !important;
        margin-right: auto !important;
    }

    .ui.grid .three.column {
        padding: 0;
    }

    .ui.grid .ten.column {
        padding-top: 0;
    }

    .m-position-sticky {
        position: sticky !important;
        top: 68px;
    }

    .m-display-none {
        display: none !important;
    }
</style>