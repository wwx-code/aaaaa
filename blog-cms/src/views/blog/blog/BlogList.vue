<template>
    <div>
        <!--面包屑导航-->
        <Breadcrumb parentTitle="博客管理"/>

        <!--搜索-->
        <el-row>
            <el-col :span="8">
                <el-input placeholder="请输入标题" v-model="queryInfo.title" :clearable="true"
                          @keyup.native.enter="search" size="small" style="min-width: 500px">
                    <el-select v-model="queryInfo.categoryId" slot="prepend" placeholder="请选择分类" :clearable="true"
                               @change="search" style="width: 160px">
                        <el-option :label="item.categoryName" :value="item.id" v-for="item in categoryList"
                                   :key="item.id"></el-option>
                    </el-select>
                    <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
                </el-input>
            </el-col>
        </el-row>

        <!--展示表格-->
        <el-table :data="blogList" style="width: 100%">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column label="标题" prop="title"></el-table-column>
            <el-table-column label="分类" prop="category.categoryName" width="150"></el-table-column>
            <el-table-column label="置顶" width="80">
                <template v-slot="scope">
                    <el-switch v-model="scope.row.isTop" @change="blogTopChanged(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="推荐" width="80">
                <template v-slot="scope">
                    <el-switch v-model="scope.row.isRecommend" @change="blogRecommendChanged(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="可见性" width="100">
                <template v-slot="scope">
                    <el-link icon="el-icon-edit" :underline="false" @click="editBlogVisibility(scope.row)">
                        {{ scope.row.isPublished ? (scope.row.password !== '' ? '密码保护' : '公开') : '私密' }}
                    </el-link>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" width="170">
                <template v-slot="scope">{{ scope.row.createTime}}</template>
            </el-table-column>
            <el-table-column label="最近更新" width="170">
                <template v-slot="scope">{{ scope.row.updateTime}}</template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" size="mini" @click="goBlogEditPage(scope.row.id)">编辑
                    </el-button>
                    <el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red"
                                   @confirm="deleteBlogById(scope.row.id)">
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

        <!--编辑可见性状态对话框-->
        <el-dialog title="博客可见性" width="30%" :visible.sync="dialogVisible">
            <!--内容主体-->
            <el-form label-width="50px" @submit.native.prevent>
                <el-form-item>
                    <el-radio-group v-model="radio">
                        <el-radio :label="1">公开</el-radio>
                        <el-radio :label="2">私密</el-radio>
                        <el-radio :label="3">密码保护</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="密码" v-if="radio===3">
                    <el-input v-model="visForm.password"></el-input>
                </el-form-item>
                <el-form-item v-if="radio!==2">
                    <el-row>
                        <el-col :span="6">
                            <el-switch v-model="visForm.isAppreciation" active-text="赞赏"></el-switch>
                        </el-col>
                        <el-col :span="6">
                            <el-switch v-model="visForm.isRecommend" active-text="推荐"></el-switch>
                        </el-col>
                        <el-col :span="6">
                            <el-switch v-model="visForm.isCommentEnabled" active-text="评论"></el-switch>
                        </el-col>
                        <el-col :span="6">
                            <el-switch v-model="visForm.isTop" active-text="置顶"></el-switch>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
            <!--底部-->
            <span slot="footer">
				<el-button @click="dialogVisible=false">取 消</el-button>
				<el-button type="primary" @click="saveVisibility">保存</el-button>
			</span>
        </el-dialog>

    </div>
</template>

<script>
    import Breadcrumb from "@/components/Breadcrumb";
    import {getDataByQuery, deleteBlogById, updateTop, updateRecommend, updateVisibility} from '@/api/blog'

    export default {
        name: "BlogList",
        components: {Breadcrumb},
        data() {
            return {
                queryInfo: {
                    title: '',
                    categoryId: null,
                    pageNum: 1,
                    pageSize: 10
                },
                blogList: [],
                categoryList: [],
                total: 0,
                dialogVisible: false,
                blogId: 0,
                radio: 1,
                visForm: {
                    isAppreciation: false,
                    isRecommend: false,
                    isCommentEnabled: false,
                    isTop: false,
                    isPublished: false,
                    password: '',
                }
            }
        },
        created() {
            this.getData()
        },
        methods: {
            getData() {
                // this.$axios.get("/blogs", this.queryInfo).then(res => {
                getDataByQuery(this.queryInfo).then(res => {
                    this.blogList = res.data.data.blogs.records
                    this.categoryList = res.data.data.categories
                    this.total = res.data.data.blogs.total
                    this.msgSuccess(res.data.msg);
                })
            },
            search(){
                this.queryInfo.pageNum = 1
                this.queryInfo.pageSize = 10
                this.getData()
            },
            //切换博客置顶状态
            blogTopChanged(row) {
                updateTop(row.id,row.isTop).then(res => {
                    this.msgSuccess(res.data.msg);
                })
            },
            //切换博客推荐状态
            blogRecommendChanged(row) {
                updateRecommend(row.id, row.isRecommend).then(res => {
                    if (res.data.code == 200) {
                        this.msgSuccess(res.data.msg);
                    } else {
                        this.msgError(res.data.msg)
                    }
                }).catch(() => {
                    this.msgError("请求失败")
                })
            },
            //编辑博客可见性
            editBlogVisibility(row) {
                this.visForm = {
                    isAppreciation: row.isAppreciation,
                    isRecommend: row.isRecommend,
                    isCommentEnabled: row.isCommentEnabled,
                    isTop: row.isTop,
                    isPublished: row.isPublished,
                    password: row.password,
                }
                this.blogId = row.id
                this.radio = this.visForm.isPublished ? (this.visForm.password !== '' ? 3 : 1) : 2
                this.dialogVisible = true
            },
            //修改博客可见性
            saveVisibility() {
                if (this.radio === 3 && (this.visForm.password === '' || this.visForm.password === null)) {
                    return this.msgError("密码保护模式必须填写密码！")
                }
                if (this.radio === 2) {
                    this.visForm.isAppreciation = false
                    this.visForm.isRecommend = false
                    this.visForm.isCommentEnabled = false
                    this.visForm.isTop = false
                    this.visForm.isPublished = false
                } else {
                    this.visForm.isPublished = true
                }
                if (this.radio !== 3) {
                    this.visForm.password = ''
                }
                updateVisibility(this.blogId, this.visForm).then(res => {
                    console.log(res)
                    if (res.data.code == 200) {
                        this.msgSuccess(res.data.msg)
                        this.getData()
                        this.dialogVisible = false
                    } else {
                        this.msgError(res.data.msg)
                    }
                })
            },
            //监听 pageSize 改变事件
            handleSizeChange(newSize) {
                this.queryInfo.pageSize = newSize
                this.getData()
            },
            //监听页码改变的事件
            handleCurrentChange(newPage) {
                this.queryInfo.pageNum = newPage
                this.getData()
            },
            //编辑博客
            goBlogEditPage(id) {
                this.$router.push(`/blogs/edit/${id}`)
            },
            //根据id删除博客
            deleteBlogById(id) {
                this.$confirm('此操作将永久删除该博客<strong style="color: red">及其所有评论</strong>，是否删除?<br>建议将博客置为<strong style="color: red">私密</strong>状态！', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    dangerouslyUseHTMLString: true
                }).then(() => {
                    deleteBlogById(id).then(res => {
                        if (res.data.code == 200) {
                            this.msgSuccess(res.data.msg)
                            this.getData()
                        } else {
                            this.msgError(res.data.msg)
                        }
                    }).catch(() => {
                        this.msgError("操作失败")
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>