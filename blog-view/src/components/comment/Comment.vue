<template>
    <div>
        <CommentForm v-if="parentCommentId===-1"/>
        <sui-comment-group>
            <h3 is="sui-header" dividing>Comments | {{ commentCount }} 条评论</h3>
            <h3 class="ui header" v-if="commentCount===0">快来抢沙发！</h3>

            <div v-for="comment in comments" :key="comment.id">
                <sui-comment>
<!--                    <sui-comment-avatar :src="comment.avatar" />-->
                    <sui-comment-avatar v-if="comment.adminComment" :src="comment.avatar" />
                    <sui-comment-avatar v-else src="/img/visitor.jpg" />
                    <sui-comment-content>
                        <a is="sui-comment-author">{{ comment.nickname }}</a>
                        <div class="ui black left pointing label" v-if="comment.adminComment">博主</div>
                        <sui-comment-metadata>
                            <div>{{ comment.createTime }}</div>
                            <div>
                                <el-button size="mini" type="primary" @click="setReply(comment.id)">回复</el-button>
                            </div>
                        </sui-comment-metadata>
                        <sui-comment-text>
                            <p>{{ comment.content }}</p>
                        </sui-comment-text>
                    </sui-comment-content >
                    <sui-comment-group v-if="comment.replyComments.length>0">
                        <div v-for="reply in comment.replyComments" :key="reply.id">
                            <sui-comment>
                                <!--TODO 评论头像设置-->
<!--                                <sui-comment-avatar :src="reply.avatar" />-->
                                <sui-comment-avatar v-if="reply.adminComment" :src="reply.avatar" />
                                <sui-comment-avatar v-else src="/img/visitor.jpg" />
                                <sui-comment-content>
                                    <a is="sui-comment-author">{{ reply.nickname }}</a>
                                    <div class="ui black left pointing label" v-if="reply.adminComment">博主</div>
                                    <sui-comment-metadata>
                                        <div>{{ reply.createTime }}</div>
                                        <div>
                                            <el-button size="mini" type="primary" @click="setReply(reply.id)">回复</el-button>
                                        </div>
                                    </sui-comment-metadata>
                                    <sui-comment-text>
                                        <a :href="`#comment-${reply.parentCommentId}`">@{{ reply.parentCommentNickname }}</a>
                                        {{ reply.content }}
                                    </sui-comment-text>
                                    <sui-comment-actions>
                                        <sui-comment-action>Reply</sui-comment-action>
                                    </sui-comment-actions>
                                </sui-comment-content>
                            </sui-comment>
                            <CommentForm v-if="parentCommentId===reply.id"/>
                        </div>
                    </sui-comment-group>
                </sui-comment>
                <CommentForm v-if="parentCommentId===comment.id"/>
            </div>
        </sui-comment-group>
    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import CommentForm from "./CommentForm";
    export default {
        name: "Comment",
        components: {CommentForm},
        computed: {
            ...mapState(['commentCount', 'comments', 'parentCommentId'])
        },
        methods: {
            setReply(id) {
                this.$store.dispatch('setParentCommentId', id)
            }
        }
    }
</script>

<style scoped>

</style>