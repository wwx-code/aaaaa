<template>
    <div>
        <div class="ui top segment" style="text-align: center">
            <h2 class="m-text-500">标签 {{tagName}} 下的文章 </h2>
        </div>
        <BlogList :blogList="blogList" :getBlogList="getBlogList" :totalPage="totalPage"></BlogList>
    </div>
</template>

<script>
    import BlogList from "@/components/blog/BlogList";
    import {getBlogListByTagName} from "@/api/tag";
    export default {
        name: "Tag",
        components: {BlogList},
        data() {
            return {
                blogList: [],
                totalPage: 0
            }
        },
        watch: {
            //在当前组件被重用时，要重新获取博客列表
            '$route.fullPath'() {
                if (this.$route.name === 'tag') {
                    this.getBlogList()
                }
            }
        },
        created() {
            this.getBlogList()
        },
        computed: {
            tagName() {
                return this.$route.params.name
            }
        },
        methods: {
            getBlogList(pageName){
                getBlogListByTagName(pageName, this.tagName).then(res => {
                    this.blogList = res.data.records
                    this.totalPage = res.data.pages
                    //高亮显示
                    this.$nextTick(() => {
                        Prism.highlightAll()
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>