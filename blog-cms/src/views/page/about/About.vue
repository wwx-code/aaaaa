<template>
    <div>
        <!--面包屑导航-->
        <Breadcrumb parentTitle="页面管理"/>

        <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
            <el-form-item label="标题" prop="title" style="width: 50%">
                <el-input v-model="form.title" placeholder="请输入标题"></el-input>
            </el-form-item>

            <el-form-item label="正文" prop="content">
                <mavon-editor v-model="form.content"></mavon-editor>
            </el-form-item>

            <el-form-item style="text-align: right;">
                <el-button type="primary" icon="el-icon-check" @click="submit">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import Breadcrumb from "@/components/Breadcrumb";
    import {getAbout, updateAbout} from "@/api/about";
    export default {
        name: "About",
        components: {Breadcrumb},
        data() {
            return {
                form: {
                    title: '',
                    content: '',
                },
                formRules: {
                    title: [{required: true, message: '请输入标题', trigger: 'change'}],
                    content: [{required: true, message: '请输入内容', trigger: 'change'}],
                }
            }
        },
        created() {
            this.getData()
        },
        methods: {
            getData() {
                getAbout().then(res => {
                    this.form.title = res.data.data.title
                    this.form.content = res.data.data.content
                    this.msgSuccess(res.data.msg)
                })
            },
            submit() {
                this.$refs.formRef.validate(valid => {
                    if (valid){
                        updateAbout(this.form).then(res => {
                            this.msgSuccess(res.data.msg)
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>