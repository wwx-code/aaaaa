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
                        <div class="three wide column m-mobile-hide">
                            <RandomBlog :randomBlogList="randomBlogList" :class="{'m-display-none':focusMode}"/>
                            <Tags :tagList="tagList" :class="{'m-display-none':focusMode}"/>
                            <!--只在文章页面显示目录-->
                            <Tocbot v-if="$route.name==='blog'"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Nav from "@/components/index/Nav";
    import {mapState} from 'vuex'
    export default {
        name: "Index",
        components: {Nav},
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
                hitokoto: {},
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
        },
        methods: {}
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