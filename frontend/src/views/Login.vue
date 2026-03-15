<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">私人健身房管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" class="login-form">
        <el-form-item prop="role">
          <el-select v-model="loginForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="教练" value="coach"></el-option>
            <el-option label="用户" value="user"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="forgot-password" style="text-align: right; margin-bottom: 15px;">
            <el-link type="primary" @click="goToForgotPassword">忘记密码？</el-link>
          </div>
          <el-button type="primary" style="width: 100%" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
        <div class="register-link">
          还没有账号？<el-link type="primary" @click="goToRegister">去注册</el-link>
        </div>
      </el-form>
      <div class="tips">
        <p>测试账号：</p>
        <p>管理员：admin / 123456</p>
        <p>教练：zhang / 123456</p>
        <p>用户：zhangsan / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        role: 'admin'
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(res => {
            console.log('登录成功，返回数据:', res.data)
            
            const userInfo = {
              id: res.data.userInfo?.id,
              username: res.data.username,
              name: res.data.name,
              role: res.data.role,
              ...res.data.userInfo
            }
            
            this.$store.dispatch('login', { token: '', userInfo })
            
            const targetPath = res.data.role === 'admin' ? '/admin' : 
                             res.data.role === 'coach' ? '/coach' : '/user'
            
            this.loading = false
            
            setTimeout(() => {
              this.$router.push(targetPath).catch(err => {
                if (err.name !== 'NavigationDuplicated') {
                  console.error('路由跳转错误:', err)
                }
              })
            }, 100)
          }).catch((error) => {
            console.error('登录失败:', error)
            this.loading = false
          })
        }
      })
    },
    goToRegister() {
      this.$router.push('/register')
    },
    goToForgotPassword() {
      this.$router.push('/forgot-password')
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('~@/assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.login-form {
  margin-top: 20px;
}

.tips {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 5px;
  font-size: 12px;
  color: #666;
}

.tips p {
  margin: 5px 0;
}

.register-link {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}
</style>

