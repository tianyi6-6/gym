<template>
  <div class="coach-profile-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>教练信息修改</span>
      </div>
      
      <div class="profile-content">
        <!-- 头像上传区域 -->
        <div class="avatar-section">
          <h3>头像设置</h3>
          <div class="avatar-uploader">
            <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :on-change="handleAvatarChange"
              :before-upload="beforeAvatarUpload"
              accept="image/*"
            >
              <img v-if="coachInfo.avatar" :src="coachInfo.avatar" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-button type="primary" size="small" @click="uploadAvatar" :loading="uploadLoading" :disabled="!avatarFile">
              上传头像
            </el-button>
            <div class="avatar-hint">支持 JPG、PNG 格式，大小不超过 2MB</div>
          </div>
        </div>
        
        <!-- 基本信息编辑区域 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <el-form :model="coachForm" :rules="infoRules" ref="infoForm" label-width="100px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="coachForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="coachForm.name"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="coachForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="coachForm.email"></el-input>
            </el-form-item>
            <el-form-item label="专业领域" prop="specialty">
              <el-input v-model="coachForm.specialty"></el-input>
            </el-form-item>
            <el-form-item label="简介" prop="description">
              <el-input v-model="coachForm.description" type="textarea"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveInfo" :loading="saveInfoLoading">保存信息</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <!-- 密码修改区域 -->
        <div class="password-section">
          <h3>密码修改</h3>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" @input="checkPasswordStrength"></el-input>
              <div class="password-strength" v-if="passwordForm.newPassword">
                <span :class="['strength-item', getStrengthClass(0)]"></span>
                <span :class="['strength-item', getStrengthClass(1)]"></span>
                <span :class="['strength-item', getStrengthClass(2)]"></span>
                <span class="strength-text">{{ passwordStrengthText }}</span>
              </div>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword" :loading="changePasswordLoading">修改密码</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CoachProfile',
  data() {
    return {
      coachInfo: {
        id: null,
        username: '',
        name: '',
        phone: '',
        email: '',
        specialty: '',
        description: '',
        avatar: ''
      },
      coachForm: {
        id: null,
        username: '',
        name: '',
        phone: '',
        email: '',
        specialty: '',
        description: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      avatarFile: null,
      uploadLoading: false,
      saveInfoLoading: false,
      changePasswordLoading: false,
      passwordStrength: 0,
      passwordStrengthText: '',
      infoRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的11位手机号码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
        ],
        specialty: [{ required: true, message: '请输入专业领域', trigger: 'blur' }]
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '新密码长度至少为6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.passwordForm.newPassword) {
                callback(new Error('两次输入的密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted() {
    this.loadCoachInfo()
  },
  methods: {
    loadCoachInfo() {
      // 从localStorage获取教练信息
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      this.coachInfo = { ...userInfo }
      this.coachForm = { ...userInfo }
      
      // 确保头像URL使用正确的路径
      if (this.coachInfo.avatar) {
        // 移除可能的域名部分，使用相对路径
        if (this.coachInfo.avatar.startsWith('http')) {
          this.coachInfo.avatar = this.coachInfo.avatar.replace(/^https?:\/\/[^\/]+/, '')
        }
        // 添加/api前缀，确保通过代理访问
        if (!this.coachInfo.avatar.startsWith('/api')) {
          this.coachInfo.avatar = '/api' + this.coachInfo.avatar
        }
      }
    },
    handleAvatarChange(file) {
      this.avatarFile = file.raw
      // 预览图片
      const reader = new FileReader()
      reader.onload = (e) => {
        this.coachInfo.avatar = e.target.result
      }
      reader.readAsDataURL(file.raw)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isJPG) {
        this.$message.error('只能上传 JPG/PNG 格式的图片')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB')
        return false
      }
      return true
    },
    uploadAvatar() {
      if (!this.avatarFile) {
        this.$message.warning('请选择要上传的头像')
        return
      }
      
      this.uploadLoading = true
      const formData = new FormData()
      formData.append('avatar', this.avatarFile)
      formData.append('username', this.coachInfo.username)
      
      // 实现教练头像上传API
      fetch('/api/coach/upload-avatar', {
        method: 'POST',
        body: formData
      })
      .then(response => response.json())
      .then(res => {
        if (res.code === 200) {
          this.$message.success('头像上传成功')
          // 确保返回的头像URL通过代理访问
          if (res.data) {
                // 添加/api前缀，确保通过代理访问
                if (!res.data.startsWith('/api')) {
                  this.coachInfo.avatar = '/api' + res.data
                } else {
                  this.coachInfo.avatar = res.data
                }
                // 更新localStorage中的用户信息
                const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
                userInfo.avatar = this.coachInfo.avatar
                localStorage.setItem('userInfo', JSON.stringify(userInfo))
                
                // 更新Vuex store中的用户信息
                this.$store.commit('SET_USER_INFO', userInfo)
                
                // 通知MainLayout组件更新头像
                this.$emit('avatar-updated', this.coachInfo.avatar)
                
                // 重新加载MainLayout中的头像
                if (window.mainLayout && window.mainLayout.loadAvatar) {
                  window.mainLayout.loadAvatar()
                }
              }
          this.avatarFile = null
        } else {
          this.$message.error(res.message || '头像上传失败')
        }
      })
      .catch(error => {
        console.error('头像上传失败:', error)
        this.$message.error('头像上传失败，请稍后重试')
      })
      .finally(() => {
        this.uploadLoading = false
      })
    },
    saveInfo() {
      this.$refs.infoForm.validate((valid) => {
        if (valid) {
          this.saveInfoLoading = true
          // 这里需要实现教练信息更新API
          // updateCoachInfo(this.coachForm).then(() => {
          //   this.$message.success('信息保存成功')
          //   this.loadCoachInfo() // 重新加载教练信息
          // }).catch(error => {
          //   console.error('信息保存失败:', error)
          //   this.$message.error(error.response?.data?.message || '信息保存失败，请稍后重试')
          // }).finally(() => {
          //   this.saveInfoLoading = false
          // })
          
          // 模拟保存成功
          setTimeout(() => {
            this.$message.success('信息保存成功')
            // 更新localStorage中的用户信息
            localStorage.setItem('userInfo', JSON.stringify(this.coachForm))
            this.saveInfoLoading = false
          }, 1000)
        }
      })
    },
    checkPasswordStrength() {
      const password = this.passwordForm.newPassword
      if (!password) {
        this.passwordStrength = 0
        this.passwordStrengthText = ''
        return
      }
      
      let strength = 0
      if (password.length >= 6) strength++
      if (/[A-Z]/.test(password)) strength++
      if (/[0-9]/.test(password)) strength++
      if (/[^A-Za-z0-9]/.test(password)) strength++
      
      this.passwordStrength = Math.min(strength, 3)
      
      const strengthTexts = ['弱', '中', '强']
      this.passwordStrengthText = strengthTexts[this.passwordStrength - 1] || ''
    },
    getStrengthClass(index) {
      if (index < this.passwordStrength) {
        return 'strength-' + ['weak', 'medium', 'strong'][index]
      }
      return ''
    },
    changePassword() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          this.changePasswordLoading = true
          // 这里需要实现教练密码修改API
          // changeCoachPassword(this.passwordForm).then(() => {
          //   this.$message.success('密码修改成功，请重新登录')
          //   // 这里可以添加登出逻辑
          //   this.passwordForm = {
          //     oldPassword: '',
          //     newPassword: '',
          //     confirmPassword: ''
          //   }
          // }).catch(error => {
          //   console.error('密码修改失败:', error)
          //   this.$message.error(error.response?.data?.message || '密码修改失败，请检查原密码是否正确')
          // }).finally(() => {
          //   this.changePasswordLoading = false
          // })
          
          // 模拟密码修改成功
          setTimeout(() => {
            this.$message.success('密码修改成功，请重新登录')
            this.passwordForm = {
              oldPassword: '',
              newPassword: '',
              confirmPassword: ''
            }
            this.changePasswordLoading = false
          }, 1000)
        }
      })
    }
  }
}
</script>

<style scoped>
.coach-profile-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.avatar-section,
.info-section,
.password-section {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

h3 {
  margin-bottom: 20px;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-uploader-icon {
  width: 100px;
  height: 100px;
  line-height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  text-align: center;
  font-size: 24px;
  color: #999;
}

.avatar-hint {
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

.password-strength {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-top: 10px;
}

.strength-item {
  width: 60px;
  height: 4px;
  background: #f0f0f0;
  border-radius: 2px;
}

.strength-weak {
  background: #ff4d4f;
}

.strength-medium {
  background: #faad14;
}

.strength-strong {
  background: #52c41a;
}

.strength-text {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}
</style>