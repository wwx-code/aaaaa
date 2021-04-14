<template>
    <div>
        <div class="ui padded attached segment m-padded-tb-large">
            <div class="ui large red right corner label" v-if="blog.isTop">
                <i class="arrow alternate circle up icon"></i>
            </div>
            <div class="ui middle aligned mobile reversed stackable">
                <div class="ui grid m-margin-lr">
                    <!--标题-->
                    <div class="row m-padded-tb-small">
                        <h2 class="ui header m-center">{{ blog.title }}</h2>
                    </div>
                    <!--文章简要信息-->
                    <div class="row m-padded-tb-small">
                        <div class="ui horizontal link list m-center">
                            <div class="item m-datetime">
                                <i class="small calendar icon"></i><span>{{ blog.createTime }}</span>
                            </div>
                            <div class="item m-views">
                                <i class="small eye icon"></i><span>{{ blog.views }}</span>
                            </div>
                            <div class="item m-common-black">
                                <i class="small pencil alternate icon"></i><span>字数≈{{ blog.words }}字</span>
                            </div>
                            <div class="item m-common-black">
                                <i class="small clock icon"></i><span>阅读时长≈{{ blog.readTime }}分</span>
                            </div>
                            <a class="item m-common-black" @click.prevent="bigFontSize=!bigFontSize">
                                <div data-inverted="" data-tooltip="点击切换字体大小" data-position="top center">
                                    <i class="font icon"></i>
                                </div>
                            </a>
                            <a class="item m-common-black" @click.prevent="changeFocusMode">
                                <div data-inverted="" data-tooltip="专注模式" data-position="top center">
                                    <i class="book icon"></i>
                                </div>
                            </a>
                        </div>
                    </div>
                    <!--分类-->
                    <router-link :to="`/category/${blog.category.categoryName}`" class="ui orange large ribbon label" v-if="blog.category">
                        <i class="small folder open icon"></i><span class="m-text-500">{{ blog.category.categoryName }}</span>
                    </router-link>
                    <!--文章Markdown正文   js-toc-content:目录生成-->
                    <div class="markdown-body js-toc-content" v-html="blog.content"></div>
<!--                    <div class="typo js-toc-content m-padded-tb-small line-numbers match-braces rainbow-braces" v-viewer :class="{'m-big-fontsize':bigFontSize}" v-html="blog.content"></div>-->
                    <!--赞赏-->
                    <el-popover placement="top" width="220" trigger="click" style="margin: 2em auto" v-if="blog.isAppreciation">
                        <div class="ui orange basic label" style="width: 100%">
                            <div class="image">
                                <div style="font-size: 12px;text-align: center;margin-bottom: 5px;">一毛是鼓励</div>
                                <img :src="$store.state.siteInfo.reward" alt="" class="ui rounded bordered image" style="width: 100%">
                                <div style="font-size: 12px;text-align: center;margin-top: 5px;">一块是真爱</div>
                            </div>
                        </div>
                        <el-button slot="reference" class="ui orange inverted circular button m-text-500">赞赏</el-button>
                    </el-popover>
                    <!--横线-->
                    <el-divider></el-divider>
                    <!--标签-->
                    <div class="row m-padded-tb-no">
                        <div class="column m-padding-left-no">
                            <router-link :to="`/tag/${tag.tagName}`" class="ui tag label m-text-500 m-margin-small" :style="{background:tag.color,color:'#fff'}" v-for="(tag,index) in blog.tags" :key="index">{{ tag.tagName }}</router-link>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import {getBlogById} from "@/api/blog";
    import 'github-markdown-css'
    // import CommentList from "@/components/comment/CommentList";
    import {mapState} from "vuex";
    import {SET_FOCUS_MODE} from '../../store/mutations-types';
    export default {
        name: "Blog",
        data() {
            return {
                blog: {},
                bigFontSize: false,
            }
        },
        computed: {
            blogId() {
                return parseInt(this.$route.params.id)
            },
            ...mapState(['siteInfo', 'focusMode'])
        },
        beforeRouteEnter(to, from, next) {
            //路由到博客文章页面之前，应将文章的渲染完成状态置为 false
            next(vm => {
                // 当 beforeRouteEnter 钩子执行前，组件实例尚未创建
                // vm 就是当前组件的实例，可以在 next 方法中把 vm 当做 this用
                vm.$store.dispatch('setIsBlogRenderComplete', false)
            })
        },
        beforeRouteLeave(to, from, next) {
            this.$store.commit(SET_FOCUS_MODE, false)
            // 从文章页面路由到其它页面时，销毁当前组件的同时，要销毁tocbot实例
            // 否则tocbot一直在监听页面滚动事件，而文章页面的锚点已经不存在了，会报"Uncaught TypeError: Cannot read property 'className' of null"
            tocbot.destroy()
            next()
        },
        beforeRouteUpdate(to, from, next) {
            // 一般有两种情况会触发这个钩子
            // ①当前文章页面跳转到其它文章页面
            // ②点击目录跳转锚点时，路由hash值会改变，导致当前页面会重新加载，这种情况是不希望出现的
            // 在路由 beforeRouteUpdate 中判断路径是否改变
            // 如果跳转到其它页面，to.path!==from.path 就放行 next()
            // 如果是跳转锚点，path不会改变，hash会改变，to.path===from.path, to.hash!==from.path 不放行路由跳转，就能让锚点正常跳转
            if (to.path !== from.path) {
                this.$store.commit(SET_FOCUS_MODE, false)
                //在当前组件内路由到其它博客文章时，要重新获取文章
                this.getBlog(to.params.id)
                //只要路由路径有改变，且停留在当前Blog组件内，就把文章的渲染完成状态置为 false
                this.$store.dispatch('setIsBlogRenderComplete', false)
                next()
            }
        },
        created() {
            this.getBlog()
        },
        methods: {
            getBlog(id = this.blogId) {
                //密码保护的文章，需要发送密码验证通过后保存在localStorage的Token
                // const blogToken = window.localStorage.getItem(`blog${id}`)
                //如果有则发送博主身份Token
                // const adminToken = window.sessionStorage.getItem('adminToken')
                // const token = adminToken ? adminToken : (blogToken ? blogToken : '')
                getBlogById(id).then(res => {
                    this.blog = res.data
                    document.title = this.blog.title + this.siteInfo.webTitleSuffix

                    var markdownIt = require("markdown-it")
                    var md = new markdownIt()

                    //对content进行渲染
                    var result = md.render(this.blog.content)
                    this.blog.content = result
                    //v-html渲染完毕后，渲染代码块样式
                    this.$nextTick(() => {
                        Prism.highlightAll()
                        //将文章渲染完成状态置为 true
                        this.$store.dispatch('setIsBlogRenderComplete', true)
                    })
                })
            },
            changeFocusMode() {
                this.$store.commit(SET_FOCUS_MODE, !this.focusMode)
            }
        }
    }
</script>

<style scoped>
    .el-divider {
        margin: 1rem 0 !important;
    }

    h1::before, h2::before, h3::before, h4::before, h5::before, h6::before {
        display: block;
        content: " ";
        height: 55px;
        margin-top: -55px;
        visibility: hidden;
    }

    .markdown-body {
        width: 100%;
    }
</style>