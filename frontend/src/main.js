import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.config.productionTip = false
Vue.use(ElementUI)

axios.defaults.baseURL = '/api'
axios.defaults.timeout = 10000
axios.defaults.headers.common['Content-Type'] = 'application/json;charset=UTF-8'

axios.interceptors.request.use(
  config => {
    const role = store.state.userInfo && store.state.userInfo.role
    if (role) {
      config.headers['X-Role'] = role
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

axios.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else {
      ElementUI.Message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    if (error.response && error.response.status === 401) {
      ElementUI.Message.error('未登录或角色信息缺失')
      store.dispatch('logout')
      router.push('/login')
    } else if (error.response && error.response.status === 403) {
      ElementUI.Message.error('权限不足')
    } else {
      ElementUI.Message.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

