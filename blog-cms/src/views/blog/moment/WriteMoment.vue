<template>
    <div>
        <!--面包屑导航-->
        <Breadcrumb parentTitle="博客管理"/>

        <el-form :model="form" label-position="top">
            <el-form-item label="动态内容" prop="content">
                <div id="vditor"></div>
            </el-form-item>

            <el-form-item label="点赞数" prop="likes" style="width: 50%">
                <el-input v-model="form.likes" type="number" placeholder="可选，默认为 0"></el-input>
            </el-form-item>

            <el-form-item label="创建时间" prop="createTime">
                <el-date-picker v-model="form.createTime" type="datetime" placeholder="可选，默认此刻" :editable="false" ></el-date-picker>
            </el-form-item>

            <el-form-item style="text-align: right;">
                <el-button type="info" @click="submit(false)">保存草稿</el-button>
                <el-button type="primary" @click="submit(true)">发布动态</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import Breadcrumb from "@/components/Breadcrumb";
    import Vditor from 'vditor'
    import {getMomentById, saveMoment, updateMoment} from "@/api/moment";
    export default {
        name: "WriteMoment",
        components: {Breadcrumb},
        data() {
            return {
                vditor: null,
                form: {
                    content: '',
                    likes: 0,
                    createTime: null,
                    isPublished: false
                }
            }
        },
        mounted() {
            this.initVditor()
        },
        methods: {
            initVditor() {
                const options = {
                    height: 320,
                    mode: 'sv',//分屏渲染
                    outline: false,//大纲
                    cache: {//不缓存到localStorage
                        enable: false,
                    },
                    resize: {//可调整高度
                        enable: true
                    },
                    after: () => {
                        if (this.$route.params.id) {
                            this.getMoment(this.$route.params.id)
                        }
                    }
                }
                this.vditor = new Vditor('vditor', options)
            },
            getMoment(id) {
                getMomentById(id).then(res => {
                    console.log(res)
                    if (res.data.code == 200) {
                        this.form = res.data.data
                        this.vditor.setValue(this.form.content)
                    } else {
                        this.msgError(res.msg)
                    }
                })
            },
            submit(isPublished) {
                this.form.content = this.vditor.getValue()
                this.form.isPublished = isPublished
                console.log(this.$route.params.id)
                if (this.$route.params.id) {
                    updateMoment(this.form).then(res => {
                        if (res.data.code == 200) {
                            this.msgSuccess(res.data.msg)
                            this.$router.push('/moments')
                        } else {
                            this.msgError(res.data.msg)
                        }
                    }).catch(() => {
                        this.msgError('请求失败')
                    })
                } else {
                    saveMoment(this.form).then(res => {
                        if (res.data.code == 200) {
                            this.msgSuccess(res.data.msg)
                            this.$router.push('/moments')
                        } else {
                            this.msgError(res.data.msg)
                        }
                    }).catch(() => {
                        this.msgError('请求失败')
                    })
                }
            }
        }
    }
</script>

<style scoped>

</style>