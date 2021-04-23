<template>
    <div class="form">
        <h3>
            发表评论
            <el-button class="m-small" size="mini" type="primary" @click="$store.dispatch('setParentCommentId', -1)" v-show="parentCommentId!==-1">取消回复</el-button>
        </h3>
        <el-form :inline="true" ref="formRef" :model="commentForm" label-width="80px" :rules="formRules" size="small">
            <el-input type="textarea" v-model="commentForm.content" class="textarea" placeholder="发一条友善的评论"
                      :rows="5" maxlength="250" :validate-event="false"></el-input>
            <el-form-item prop="nickname">
                <!-- validate-event属性的作用是: 输入时不触发表单验证.提交时再验证,你也可以设置成动态验证 -->
                <el-input v-model="commentForm.nickname"  placeholder="昵称（必填）" :validate-event="false"></el-input>
            </el-form-item>
            <el-form-item prop="email">
                <el-input v-model="commentForm.email" placeholder="邮箱（必填）" :validate-event="false"></el-input>
            </el-form-item>
            <el-form-item prop="website">
                <el-input v-model="commentForm.website" placeholder="网站（选填）"></el-input>
            </el-form-item>
            <el-form-item label="订阅回复">
                <el-switch v-model="commentForm.isNotice" inactive-value=false></el-switch>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" size="small" @click="postForm">发表评论</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import {checkEmail} from "@/common/reg";
    export default {
        name: "CommentForm",
        computed: {
            ...mapState(['parentCommentId', 'commentForm', 'commentQuery'])
        },
        data() {
            return {
                formRules: {
                    nickname: [
                        {required: true, message: '请输入评论昵称'},
                        {max: 18, message: '昵称不可多于15个字符'}
                    ],
                    email: [
                        {required: true, message: '请输入评论邮箱'},
                        {validator: checkEmail}
                    ],
                }
            }
        },
        methods: {
            postForm() {
                const adminToken = window.sessionStorage.getItem('adminToken')
                if (adminToken) {
                    //博主登录后，sessionStorage中会存储token，在后端设置属性，可以不校验昵称、邮箱
                    if (this.commentForm.content === '' || this.commentForm.content.length > 250) {
                        return this.$notify({
                            title: '评论失败',
                            message: '评论内容有误',
                            type: 'warning'
                        })
                    } else {
                        return this.$store.dispatch('submitCommentForm', adminToken)
                    }
                }
                // const blogToken = window.localStorage.getItem(`blog${this.commentQuery.blogId}`)
                this.$refs.formRef.validate(valid => {
                    if (!valid || this.commentForm.content === '' || this.commentForm.content.length > 250) {
                        this.$notify({
                            title: '评论失败',
                            message: '请正确填写评论',
                            type: 'warning'
                        })
                    } else {
                        this.$store.dispatch('submitCommentForm', '')
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .form h3 {
        margin: 5px;
        font-weight: 500 !important;
    }

    .form .m-small {
        margin-left: 5px;
        padding: 4px 5px;
    }

    .el-form .textarea {
        margin-top: 5px;
        margin-bottom: 15px;
    }
</style>