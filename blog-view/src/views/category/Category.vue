<template>
    <div>
        <div class="ui top segment" style="text-align: center">
            <h2 class="m-text-500">分类 {{ categoryName }} 下的文章</h2>
        </div>
        <BlogList :getBlogList="getBlogList" :blogList="blogList" :totalPage="totalPage"/>
    </div>
</template>

<script>
    import BlogList from "@/components/blog/BlogList";
    import {getBlogListByCategoryName} from "@/api/category";
    export default {
        name: "Category",
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
                if (this.$route.name === 'category') {
                    this.getBlogList()
                }
            }
        },
        created() {
            this.getBlogList()
        },
        computed: {
            categoryName() {
                return this.$route.params.name
            }
        },
        methods: {
            getBlogList(pageName){
                getBlogListByCategoryName(pageName, this.categoryName).then(res => {
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