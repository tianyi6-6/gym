<template>
  <div class="forgot-password-container">
    <div class="forgot-password-box">
      <h2 class="title">忘记密码</h2>
      <el-form :model="forgotForm" :rules="rules" ref="forgotForm" class="forgot-form">
        <el-form-item prop="username">
          <el-input v-model="forgotForm.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="forgotForm.email" placeholder="邮箱" prefix-icon="el-icon-message"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleForgotPassword" :loading="loading">发送验证码</el-button>
        </el-form-item>
        <div class="back-to-login">
          想起密码了？<el-link type="primary" @click="goToLogin">返回登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ForgotPassword',
  data() {
    return {
      forgotForm: {
        username: '',
        email: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleForgotPassword() {
      this.$refs.forgotForm.validate((valid) => {
        if (valid) {
          this.loading = true
          // 模拟发送验证码
          setTimeout(() => {
            this.$message.success('验证码已发送到您的邮箱')
            this.loading = false
          }, 1000)
        }
      })
    },
    goToLogin() {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.forgot-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('~@/assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.forgot-password-box {
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

.forgot-form {
  margin-top: 20px;
}

.back-to-login {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}
</style>