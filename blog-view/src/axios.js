import axios from "axios";
import Element from "element-ui"
import router from './router'
import store from './store'
// import NProgress from 'nprogress'
// import 'nprogress/nprogress.css'

axios.defaults.baseURL="http://localhost:8080/"

//前置拦截
axios.interceptors.request.use(config => {
    const identification = window.localStorage.getItem('identification')
    //identification存在，且是基于baseURL的请求
    if (identification && !(config.url.startsWith('http://') || config.url.startsWith('https://'))) {
        config.headers.identification = identification
    }
    return config
})

//后置拦截
axios.interceptors.response.use(response => {
        let res = response.data;
        const identification = config.headers.identification
        if (identification) {
            //保存身份标识到localStorage
            window.localStorage.setItem('identification', identification)
        }

        if (res.code == 200){
            return response
        }else {
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

        Element.Message.error(error.message, {duration: 3 * 1000})
        return Promise.reject(error)
    }
)