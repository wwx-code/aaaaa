<template>
    <div>
        <Breadcrumb parentTitle="友链管理"/>

        <!--添加友链-->
        <el-row>
            <el-button type="primary" size="small" icon="el-icon-plus"  @click="addDialogVisible=true">添加友链</el-button>
        </el-row>

        <el-table :data="friendList">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column label="头像" width="80">
                <template v-slot="scope">
                    <el-avatar shape="square" :size="60" fit="contain" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column label="昵称" prop="nickname"></el-table-column>
            <el-table-column label="描述" prop="description"></el-table-column>
            <el-table-column label="站点" prop="website"></el-table-column>
            <el-table-column label="是否公开" width="80">
                <template v-slot="scope">
                    <el-switch v-model="scope.row.isPublished" @change="friendPublishedChanged(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="浏览次数" prop="views"></el-table-column>
            <el-table-column label="创建时间" prop="createTime"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
                    <el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @confirm="deleteFriendById(scope.row.id)">
                        <el-button size="mini" type="danger" icon="el-icon-delete" slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <!--分页-->
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
                       :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
                       layout="total, sizes, prev, pager, next, jumper" background>
        </el-pagination>

        <!--添加友链对话框-->
        <el-dialog title="添加友链" width="40%" :visible.sync="addDialogVisible" @close="addDialogClosed">
            <el-form :model="addForm" ref="addFormRef" :rules="formRules" label-width="80px">
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="addForm.nickname"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="addForm.description"></el-input>
                </el-form-item>
                <el-form-item label="网站" prop="website">
                    <el-input v-model="addForm.website"></el-input>
                </el-form-item>
                <el-form-item label="头像URL" prop="avatar">
                    <el-input v-model="addForm.avatar"></el-input>
                </el-form-item>
                <el-form-item label="是否公开" prop="isPublished">
                    <el-switch v-model="addForm.isPublished"></el-switch>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveFriend">确 定</el-button>
            </div>
        </el-dialog>

        <!--编辑友链对话框-->
        <el-dialog title="编辑友链" width="40%" :visible.sync="editDialogVisible" @close="editDialogClosed">
            <!--内容主体-->
            <el-form :model="editForm" :rules="formRules" ref="editFormRef" label-width="80px">
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="editForm.nickname"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="editForm.description"></el-input>
                </el-form-item>
                <el-form-item label="网站" prop="website">
                    <el-input v-model="editForm.website"></el-input>
                </el-form-item>
                <el-form-item label="头像URL" prop="avatar">
                    <el-input v-model="editForm.avatar"></el-input>
                </el-form-item>
                <el-form-item label="是否公开" prop="isPublished">
                    <el-switch v-model="editForm.isPublished"></el-switch>
                </el-form-item>
            </el-form>
            <!--底部-->
            <span slot="footer">
				<el-button @click="editDialogVisible=false">取 消</el-button>
				<el-button type="primary" @click="editFriend">确 定</el-button>
			</span>
        </el-dialog>
    </div>
</template>

<script>
    import Breadcrumb from "@/components/Breadcrumb";
    import {
        getFriendsByQuery, updatePublished, saveFriend, updateFriend,
        deleteFriendById, getFriendInfo, updateContent
    } from "@/api/friend";
    export default {
        name: "FriendList",
        components: {Breadcrumb},
        data() {
            return {
                queryInfo: {
                    pageNum: 1,
                    pageSize: 10
                },
                addForm: {
                    nickname: '',
                    description: '',
                    website: '',
                    avatar: '',
                    isPublished: true
                },
                editForm: {
                    nickname: '',
                    description: '',
                    website: '',
                    avatar: '',
                    isPublished: true
                },
                addDialogVisible: false,
                editDialogVisible: false,
                total: 0,
                friendList: [],
                formRules: {
                    nickname: [{required: true, message: '请输入昵称', trigger: 'blur'}],
                    description: [{required: true, message: '请输入描述', trigger: 'blur'}],
                    website: [{required: true, message: '请输入网站', trigger: 'blur'}],
                    avatar: [{required: true, message: '请输入头像URL', trigger: 'blur'}],
                }
            }
        },
        created() {
            this.getFriendList()
        },
        methods: {
            //初始化页面获取友链信息
            getFriendList() {
                getFriendsByQuery(this.queryInfo).then(res => {
                    console.log(res)
                    this.friendList = res.data.data.records
                    this.total = res.data.data.total
                    this.msgSuccess(res.data.msg)
                })
            },
            showEditDialog(row) {
                this.editForm = {...row}
                this.editDialogVisible = true
            },
            handleSizeChange(newSize) {
                this.queryInfo.pageSize = newSize
                this.getFriendList()
            },
            handleCurrentChange(newPage) {
                this.queryInfo.pageNum = newPage
                this.getFriendList()
            },
            addDialogClosed() {
                this.$refs.addFormRef.resetFields()
            },
            editDialogClosed() {
                this.editForm = {}
                this.$refs.editFormRef.resetFields()
            },
            //更新友链公开状态
            friendPublishedChanged(row) {
                updatePublished(row.id,row.isPublished).then(res => {
                    this.msgSuccess(res.data.msg)
                })
            },
            //根据id删除友链
            deleteFriendById(id) {
                deleteFriendById(id).then(res => {
                    this.msgSuccess(res.data.msg)
                    this.getFriendList()
                })
            },
            //新增友链信息
            saveFriend() {
                this.$refs.addFormRef.validate(valid => {
                    if (valid){
                        saveFriend(this.addForm).then(res => {
                            this.getFriendList()
                            this.msgSuccess(res.data.msg)
                            this.addDialogVisible = false
                        })
                    }
                })
            },
            //更新友链信息
            editFriend() {
                this.$refs.editFormRef.validate(valid => {
                    if (valid){
                        updateFriend(this.editForm).then(res => {
                            this.getFriendList()
                            this.msgSuccess(res.data.msg)
                            this.editDialogVisible = false
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>