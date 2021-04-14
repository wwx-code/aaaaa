<template>
    <div>
        <div class="ui top attached segment" style="text-align: center">
            <h2 class="m-text-500">友情链接</h2>
        </div>

        <div class="ui attached segment">
            <div class="ui link four doubling cards">
                <sui-card v-for="(item,index) in friendList" :key="index" :href="item.website">
<!--                    <a ><sui-image :src="item.avatar"/></a>-->
                    <div class="ui small image image-wrap">
                        <img src="../../assets/avatar.jpg">
                    </div>
                    <sui-card-content class="ui center aligned">
                        <sui-card-header><a>{{ item.nickname }}</a></sui-card-header>
                        <sui-card-meta>
                            <a class="m-text-500">{{ item.description }}</a>
                        </sui-card-meta>
                    </sui-card-content>
                </sui-card>
            </div>
        </div>

        <div class="ui segment">
            <el-divider></el-divider>

            <div class="markdown-body" v-html="content"></div>
        </div>

    </div>
</template>

<script>
    import {getFriendList, getFriendInfo, addViewsByNickname} from "@/api/friend";
    import 'github-markdown-css'
    export default {
        name: "Friends",
        data() {
            return {
                friendList: [],
                content: ''
            }
        },
        created() {
            this.getData()
        },
        methods: {
            getData() {
                getFriendList().then(res => {
                    this.friendList = res.data
                })
                getFriendInfo().then(res => {
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
.image-wrap{
    width: 100%!important;
}
</style>