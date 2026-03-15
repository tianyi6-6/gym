<template>
  <div class="register-container">
    <div class="register-box">
      <h2 class="title">用户注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerForm" class="register-form">
        <el-form-item prop="role">
          <el-radio-group v-model="registerForm.role" @change="handleRoleChange">
            <el-radio label="user">普通用户</el-radio>
            <el-radio label="coach">教练</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="registerForm.name" placeholder="姓名" prefix-icon="el-icon-user-solid"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="registerForm.phone" placeholder="手机号 (可选)" prefix-icon="el-icon-phone"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="邮箱 (可选)" prefix-icon="el-icon-message"></el-input>
        </el-form-item>
        
        <template v-if="registerForm.role === 'user'">
          <el-form-item prop="memberLevel">
            <el-select v-model="registerForm.memberLevel" placeholder="选择会员等级" style="width: 100%">
              <el-option label="普通会员" value="普通会员"></el-option>
              <el-option label="银卡会员" value="银卡会员"></el-option>
              <el-option label="金卡会员" value="金卡会员"></el-option>
              <el-option label="钻石会员" value="钻石会员"></el-option>
            </el-select>
          </el-form-item>
        </template>
        
        <template v-if="registerForm.role === 'coach'">
          <el-form-item prop="specialty">
            <el-input v-model="registerForm.specialty" type="textarea" :rows="2" placeholder="专长 (如：瑜伽、力量训练、有氧运动等)" prefix-icon="el-icon-medal"></el-input>
          </el-form-item>
          <el-form-item prop="experience">
            <el-input v-model="registerForm.experience" type="textarea" :rows="2" placeholder="经验介绍" prefix-icon="el-icon-document"></el-input>
          </el-form-item>
          <div class="score-section">
            <div class="score-title">综合能力评分 (0-100)</div>
            <div class="score-grid">
              <div class="score-item">
                <div class="score-label">专业能力</div>
                <el-slider v-model="registerForm.professionalScore" :min="0" :max="100" show-input></el-slider>
              </div>
              <div class="score-item">
                <div class="score-label">教学经验</div>
                <el-slider v-model="registerForm.teachingScore" :min="0" :max="100" show-input></el-slider>
              </div>
              <div class="score-item">
                <div class="score-label">沟通能力</div>
                <el-slider v-model="registerForm.communicationScore" :min="0" :max="100" show-input></el-slider>
              </div>
              <div class="score-item">
                <div class="score-label">服务态度</div>
                <el-slider v-model="registerForm.serviceScore" :min="0" :max="100" show-input></el-slider>
              </div>
              <div class="score-item score-item-full">
                <div class="score-label">业绩表现</div>
                <el-slider v-model="registerForm.performanceScore" :min="0" :max="100" show-input></el-slider>
              </div>
            </div>
          </div>
        </template>
        
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleRegister" :loading="loading">注册</el-button>
        </el-form-item>
        <div class="login-link">
          已有账号？<el-link type="primary" @click="goToLogin">去登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { registerUser, registerCoach } from '@/api'

export default {
  name: 'Register',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        phone: '',
        email: '',
        role: this.$route.query.role || 'user',
        memberLevel: '普通会员',
        specialty: '',
        experience: '',
        professionalScore: 80,
        teachingScore: 80,
        communicationScore: 80,
        serviceScore: 80,
        performanceScore: 80
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        memberLevel: [
          { required: true, message: '请选择会员等级', trigger: 'change' }
        ],
        specialty: [
          { required: true, message: '请输入专长', trigger: 'blur' }
        ],
        experience: [
          { required: true, message: '请输入经验介绍', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleRoleChange(role) {
      this.$refs.registerForm.clearValidate()
      this.registerForm.memberLevel = role === 'user' ? '普通会员' : ''
      this.registerForm.specialty = ''
      this.registerForm.experience = ''
      this.registerForm.professionalScore = 80
      this.registerForm.teachingScore = 80
      this.registerForm.communicationScore = 80
      this.registerForm.serviceScore = 80
      this.registerForm.performanceScore = 80
    },
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const { confirmPassword, ...dataToRegister } = this.registerForm
          const registerApi = this.registerForm.role === 'user' ? registerUser : registerCoach
          registerApi(dataToRegister).then(res => {
            if (res.code === 200) {
              this.$message.success('注册成功，请登录')
              this.$router.push('/login')
            } else {
              this.$message.error(res.message || '注册失败')
            }
          }).catch(error => {
            console.error('注册失败:', error)
            this.$message.error('注册失败，请稍后重试')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },
    goToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url('~@/assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 20px 0;
  box-sizing: border-box;
}

.register-box {
  width: 450px;
  max-height: 90vh;
  padding: 30px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  overflow-y: auto;
  box-sizing: border-box;
}

.register-box::-webkit-scrollbar {
  width: 6px;
}

.register-box::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.register-box::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.register-box::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.register-form {
  margin-top: 15px;
}

.register-form .el-form-item {
  margin-bottom: 18px;
}

.score-section {
  margin-top: 10px;
  margin-bottom: 20px;
}

.score-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.score-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.score-item {
  padding: 8px;
  background: #f5f7fa;
  border-radius: 6px;
  box-sizing: border-box;
}

.score-item-full {
  grid-column: 1 / -1;
}

.score-label {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
  font-weight: 500;
}

.score-item .el-slider {
  margin-top: 5px;
}

.login-link {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}

@media screen and (-ms-high-contrast: active), (-ms-high-contrast: none) {
  .score-grid {
    display: -ms-grid;
    -ms-grid-columns: 1fr 1fr;
    -ms-grid-rows: auto auto auto;
  }
  
  .score-item:nth-child(1) {
    grid-column: 1;
    grid-row: 1;
    -ms-grid-column: 1;
    -ms-grid-row: 1;
  }
  
  .score-item:nth-child(2) {
    grid-column: 2;
    grid-row: 1;
    -ms-grid-column: 2;
    -ms-grid-row: 1;
  }
  
  .score-item:nth-child(3) {
    grid-column: 1;
    grid-row: 2;
    -ms-grid-column: 1;
    -ms-grid-row: 2;
  }
  
  .score-item:nth-child(4) {
    grid-column: 2;
    grid-row: 2;
    -ms-grid-column: 2;
    -ms-grid-row: 2;
  }
  
  .score-item-full {
    grid-column: 1 / -1;
    grid-row: 3;
    -ms-grid-column: 1;
    -ms-grid-column-span: 2;
    -ms-grid-row: 3;
  }
}

@-moz-document url-prefix() {
  .score-item {
    box-sizing: border-box;
    -moz-box-sizing: border-box;
  }
}
</style>
