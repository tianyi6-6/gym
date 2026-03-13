<template>
  <div class="admin-profile-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>管理员信息修改</span>
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
              <img v-if="adminInfo.avatar" :src="adminInfo.avatar" class="avatar">
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
          <el-form :model="adminForm" :rules="infoRules" ref="infoForm" label-width="100px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="adminForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="adminForm.name"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="adminForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="adminForm.email"></el-input>
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
import { getAdminInfo, updateAdminInfo, changeAdminPassword, uploadAdminAvatar } from '@/api'

export default {
  name: 'AdminProfile',
  data() {
    return {
      adminInfo: {
        id: null,
        username: '',
        name: '',
        phone: '',
        email: '',
        avatar: ''
      },
      adminForm: {
        id: null,
        username: '',
        name: '',
        phone: '',
        email: ''
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
        ]
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
    this.loadAdminInfo()
  },
  methods: {
    loadAdminInfo() {
      getAdminInfo().then(res => {
        this.adminInfo = res.data
        // 确保头像URL使用正确的路径
        if (this.adminInfo.avatar) {
          // 移除可能的域名部分，使用相对路径
          if (this.adminInfo.avatar.startsWith('http')) {
            this.adminInfo.avatar = this.adminInfo.avatar.replace(/^https?:\/\/[^\/]+/, '')
          }
          // 添加/api前缀，确保通过代理访问
          if (!this.adminInfo.avatar.startsWith('/api')) {
            this.adminInfo.avatar = '/api' + this.adminInfo.avatar
          }
        }
        this.adminForm = { ...res.data }
      })
    },
    handleAvatarChange(file) {
      this.avatarFile = file.raw
      // 预览图片
      const reader = new FileReader()
      reader.onload = (e) => {
        this.adminInfo.avatar = e.target.result
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
      
      uploadAdminAvatar(formData).then(res => {
        this.$message.success('头像上传成功')
        // 确保返回的头像URL通过代理访问
        if (res.data) {
          // 添加/api前缀，确保通过代理访问
          if (!res.data.startsWith('/api')) {
            this.adminInfo.avatar = '/api' + res.data
          } else {
            this.adminInfo.avatar = res.data
          }
        }
        this.avatarFile = null
      }).catch(error => {
        console.error('头像上传失败:', error)
        this.$message.error('头像上传失败，请稍后重试')
      }).finally(() => {
        this.uploadLoading = false
      })
    },
    saveInfo() {
      this.$refs.infoForm.validate((valid) => {
        if (valid) {
          this.saveInfoLoading = true
          updateAdminInfo(this.adminForm).then(() => {
            this.$message.success('信息保存成功')
            this.loadAdminInfo() // 重新加载管理员信息
          }).catch(error => {
            console.error('信息保存失败:', error)
            this.$message.error(error.response?.data?.message || '信息保存失败，请稍后重试')
          }).finally(() => {
            this.saveInfoLoading = false
          })
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
          changeAdminPassword(this.passwordForm).then(() => {
            this.$message.success('密码修改成功，请重新登录')
            // 这里可以添加登出逻辑
            this.passwordForm = {
              oldPassword: '',
              newPassword: '',
              confirmPassword: ''
            }
          }).catch(error => {
            console.error('密码修改失败:', error)
            this.$message.error(error.response?.data?.message || '密码修改失败，请检查原密码是否正确')
          }).finally(() => {
            this.changePasswordLoading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-profile-page {
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