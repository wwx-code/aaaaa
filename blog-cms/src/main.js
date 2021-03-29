import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//element-ui
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//axios
import axios from 'axios'

Vue.use(Element)

Vue.prototype.$axios = axios

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
