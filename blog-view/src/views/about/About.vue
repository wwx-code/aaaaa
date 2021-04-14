<template>
    <div>
        <div class="ui top attached segment" style="text-align: center">
            <h2 class="m-text-500">关于我</h2>
        </div>

        <div class="ui segment">
            <el-divider></el-divider>

            <div class="markdown-body" v-html="content"></div>
        </div>
    </div>
</template>

<script>
    import {getAbout} from "@/api/about"
    import 'github-markdown-css'
    export default {
        name: "About",
        data() {
            return {
                content: ''
            }
        },
        created() {
            this.getAbout()
        },
        methods: {
            getAbout() {
                getAbout().then(res => {
                    var markdownIt = require("markdown-it")
                    var md = new markdownIt()

                    //对content进行渲染
                    var result = md.render(res.data.content)
                    this.content = result
                })
            }
        }
    }
</script>

<style scoped>

</style>