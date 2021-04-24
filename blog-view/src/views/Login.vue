<template>
    <div class="login_container">
        <div class="login_box">
            <!--头像-->
            <div class="avatar_box">
                <img src="/img/avatar.jpg" alt="">
            </div>
            <el-form ref="loginForm" :model="loginForm" :rules="rules" label-width="80px" class="login_form">
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" prefix-icon="el-icon-user-solid"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" show-password @keyup.native.enter="login('loginForm')"></el-input>
                </el-form-item>
                <el-form-item class="btns">
                    <el-button type="primary" @click="login('loginForm')">登录</el-button>
                    <el-button @click="resetForm('loginForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            login(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // const _this = this
                        this.$axios.post("admin/loginViews",this.loginForm).then(res => {
                            const jwt = res.data
                            window.sessionStorage.setItem("adminToken",jwt)
                            this.$router.push("/home")
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName){
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    .login_container {
        height: 100%;
        width: 100%;
        background-image: url("../assets/login_bg.jpg");
        background-size: cover;
        background-position: center;
        position: relative;
    }

    .login_box {
        width: 450px;
        height: 300px;
        background-color: #fff;
        border-radius: 3px;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
    }

    .login_box .avatar_box {
        height: 130px;
        width: 130px;
        border: 1px solid #eee;
        border-radius: 50%;
        padding: 10px;
        box-shadow: 0 0 10px #ddd;
        position: absolute;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: #fff;
    }

    .login_box .avatar_box img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #eee;
    }

    .login_form {
        margin-left: -40px;
        position: absolute;
        bottom: 0;
        width: 100%;
        padding: 0 20px;
        box-sizing: border-box;
    }

    .btns {
        display: flex;
        justify-content: flex-end;
    }
</style>