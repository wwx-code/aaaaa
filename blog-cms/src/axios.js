import axios from "axios";
import Element from "element-ui"
import router from './router'
import store from './store'

axios.defaults.baseURL="http://localhost:8080/admin"

//前置拦截
axios.interceptors.request.use(config => {
    if(localStorage.getItem("token")) {
        config.headers = {
            "Authorization": localStorage.getItem("token")
        }
    }
    return config
})

//后置拦截
axios.interceptors.response.use(response => {
        let res = response.data;

        if (res.code == 200){
            return response
        }else {
            console.log(res)
            // 弹窗异常信息
            Element.Message.error(res.msg)
            // 直接拒绝往下面返回结果信息
            return Promise.reject(response.data.msg)
        }
    },
    error => {

        //返回的结果为错误类型
        if (error.response.data){
            error.message = error.response.data.msg
        }

        //401：未认证
        if (error.response.status == 401){
            //删除token和userInfo信息
            store.commit("REMOVE_INFO")
            //跳转至登录页面
            router.push("/login")
        }

        Element.Message.error(error.message, {duration: 3 * 1000})
        return Promise.reject(error)
    }
)