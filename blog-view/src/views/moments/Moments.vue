<template>
    <div>
        <div class="ui top attached segment" style="text-align: center">
            <h2 class="m-text-500">我的动态</h2>
        </div>

        <div class="ui attached segment m-padding-bottom-large">
            <div class="moments">
                <div class="moment" v-for="(moment,index) in momentList" :key="index">
                    <div class="avatar">
<!--                        <img :src="$store.state.introduction.avatar">-->
                        <img src="../../assets/avatar.jpg">
                    </div>
                    <div class="ui card">
                        <div class="content m-top">
                            <span style="font-weight: 700">{{ $store.state.introduction.name }}</span>
                            <span class="right floated">{{ moment.createTime }}</span>
                        </div>
                        <div class="content typo" :class="{'privacy':!moment.isPublished}" v-viewer v-html="moment.content"></div>
                        <div class="extra content">
                            <a class="left floated" @click="like(moment.id)">
<!--                                <i class="heart icon" :class="isLike(moment.id)?'like-color':'outline'"></i>{{ moment.likes }}-->
                                <i class="heart icon" :class="isLike = false ?'like-color':'outline'"></i>{{ moment.likes }}
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-count="totalPage"
                           layout="prev, pager, next" background hide-on-single-page class="pagination">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {getMomentListByPageNum, likeMoment} from "@/api/moment";
    export default {
        name: "Moments",
        data() {
            return {
                //用localStorage本地存储已点赞的动态id数组
                /*likeMomentIds: JSON.parse(window.localStorage.getItem('likeMomentIds') || '[]'),*/
                momentList: [],
                pageNum: 1,
                totalPage: 0,
                isLike: false,
            }
        },
        created() {
            this.getMomentList()
        },
        methods: {
            getMomentList() {
                getMomentListByPageNum(this.pageNum).then(res => {
                    this.momentList = res.data.records
                    this.totalPage = res.data.pages
                })
            },
            handleCurrentChange(newPage) {
                this.scrollToTop()
                this.pageNum = newPage
                this.getMomentList()
            },
            like(id) {
                likeMoment(id).then(res => {
                    this.isLike = true
                    this.$notify({
                        title: res.msg,
                        type: 'success'
                    })
                })
            }
        }
    }
</script>

<style scoped>
    .avatar {
        margin-left: -62.5px;
        float: left !important;
    }

    .avatar img {
        height: 45px;
        width: 45px;
        border-radius: 500px;
    }

    .moments {
        margin-left: 26px !important;
        padding-left: 40px !important;
        border-left: 1px solid #dee5e7 !important;
    }

    .moment {
        margin-top: 30px;
    }

    .moment:first-child {
        margin-top: 0 !important;
    }

    .card {
        width: 100% !important;
    }

    .card:before {
        border-width: 0 0 1px 1px !important;
        transform: translateX(-50%) translateY(-50%) rotate(45deg) !important;
        bottom: auto !important;
        right: auto !important;
        top: 22px !important;
        left: 0 !important;
        position: absolute !important;
        content: '' !important;
        background-image: none !important;
        z-index: 2 !important;
        width: 12px !important;
        height: 12px !important;
        transition: background .1s ease !important;
        background-color: inherit !important;
        border-style: solid !important;
        border-color: #d4d4d5 !important;
    }

    .content.m-top {
        padding: 10px 14px !important;
    }

    .content .right.floated {
        font-size: 12px !important;
    }

    .content.typo * {
        font-size: 14px !important;
    }

    .extra.content {
        padding: 5px 14px !important;
    }

    .extra.content a {
        color: rgba(0, 0, 0, 0.7) !important;
        font-size: 12px !important;
    }

    .extra.content a:hover {
        color: red !important;
    }

    .extra.content .like-color {
        color: red !important;
    }

    .extra.content i {
        font-size: 12px !important;
    }

    .pagination {
        text-align: center;
        margin-top: 3em;
    }

    .privacy {
        background: repeating-linear-gradient(145deg, #f2f2f2, #f2f2f2 15px, #fff 0, #fff 30px) !important;
    }
</style>