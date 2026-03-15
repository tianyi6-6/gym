<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="logo">私人健身房管理系统</div>
      <div class="user-info">
        <div class="avatar-container" @click="handleAvatarClick">
          <div v-if="loading" class="avatar-loading">
            <el-icon class="loading-icon"><i class="el-icon-loading"></i></el-icon>
          </div>
          <img 
            v-else 
            :src="avatarUrl || 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjNzc3IiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lam9pbj0icm91bmQiPjxwYXRoIGQ9Ik0xMiAyQzYuNDggMiAyIDYuNDggMiAxMnM0LjQ4IDEwIDEwIDEwIDEwLTQuNDggMTAtMTAtNC40OC0xMC0xMC0xMFptMCAxNmMtMi4yMSAwLTQtMS43OS00LTRzMS43OS00IDQtNCA0IDEuNzkgNCA0LTEuNzkgNC00IDR6bTAgLTljLTEuNiAwLTMtMS40LTMtM3MxLjQtMyAzLTMgMyAxLjQgMyAzLTEuNCAzLTMtMy0zem0wIDZoLTR2LTJoNHYyem0wIDR2LTRoLTJ2NGgyem0wIDR2LTRoLTJ2NGgyeiIvPjwvc3ZnPg=='" 
            :alt="userInfo.name || userInfo.username" 
            class="avatar"
            @error="handleAvatarError"
          >
        </div>
        <span>{{ userInfo.name || userInfo.username }}</span>
        <el-button type="text" @click="handleLogout">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu
          :default-active="activeMenu"
          router
          class="menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF">
          <!-- 管理员菜单 -->
          <template v-if="userInfo.role === 'admin'">
            <el-menu-item index="/admin">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/admin/course">
              <i class="el-icon-s-goods"></i>
              <span>课程管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/order">
              <i class="el-icon-s-order"></i>
              <span>订单管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/coach">
              <i class="el-icon-user"></i>
              <span>教练管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/user">
              <i class="el-icon-user-solid"></i>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/equipment">
              <i class="el-icon-s-tools"></i>
              <span>器材管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/recipe">
              <i class="el-icon-food"></i>
              <span>菜谱管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/experience">
              <i class="el-icon-document"></i>
              <span>经验管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/notice">
              <i class="el-icon-bell"></i>
              <span>公告管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/health-profile">
              <i class="el-icon-s-custom"></i>
              <span>健康档案管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/body-assessment">
              <i class="el-icon-s-data"></i>
              <span>体测数据管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/profile">
              <i class="el-icon-user"></i>
              <span>管理员信息</span>
            </el-menu-item>
          </template>
          
          <!-- 教练菜单 -->
          <template v-if="userInfo.role === 'coach'">
            <el-menu-item index="/coach">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/coach/schedule">
              <i class="el-icon-date"></i>
              <span>个人排班</span>
            </el-menu-item>
            <el-menu-item index="/coach/course-schedule">
              <i class="el-icon-date"></i>
              <span>课程安排</span>
            </el-menu-item>
            <el-menu-item index="/coach/my-courses">
              <i class="el-icon-s-goods"></i>
              <span>我的授课</span>
            </el-menu-item>
            <el-menu-item index="/coach/health-fitness">
              <i class="el-icon-s-custom"></i>
              <span>健康体能</span>
            </el-menu-item>
            <el-menu-item index="/coach/appointment">
              <i class="el-icon-date"></i>
              <span>预约管理</span>
            </el-menu-item>
            <el-menu-item index="/coach/body-assessment">
              <i class="el-icon-s-data"></i>
              <span>体测数据管理</span>
            </el-menu-item>
            <el-menu-item index="/coach/content">
              <i class="el-icon-document"></i>
              <span>内容管理</span>
            </el-menu-item>
            <el-menu-item index="/coach/profile">
              <i class="el-icon-user"></i>
              <span>个人信息</span>
            </el-menu-item>
          </template>
          
          <!-- 用户菜单 -->
          <template v-if="userInfo.role === 'user'">
            <el-menu-item index="/user">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/user/course">
              <i class="el-icon-s-goods"></i>
              <span>课程中心</span>
            </el-menu-item>
            <el-menu-item index="/user/appointment">
              <i class="el-icon-date"></i>
              <span>我的预约</span>
            </el-menu-item>
            <el-menu-item index="/user/body-assessment">
              <i class="el-icon-s-data"></i>
              <span>体测与健康档案</span>
            </el-menu-item>
            <el-menu-item index="/user/training">
              <i class="el-icon-s-finance"></i>
              <span>训练计划与打卡</span>
            </el-menu-item>
            <el-menu-item index="/user/lessons">
              <i class="el-icon-s-order"></i>
              <span>私教课时管理</span>
            </el-menu-item>
            <el-menu-item index="/user/membership">
              <i class="el-icon-s-marketing"></i>
              <span>会员等级与权益</span>
            </el-menu-item>
            <el-menu-item index="/user/recharge">
              <i class="el-icon-wallet"></i>
              <span>账户充值</span>
            </el-menu-item>
            <el-menu-item index="/user/experience">
              <i class="el-icon-document"></i>
              <span>我的经验</span>
            </el-menu-item>
            <el-menu-item index="/user/profile">
              <i class="el-icon-user"></i>
              <span>个人信息</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapState } from 'vuex'
import { getAdminInfo } from '@/api'

export default {
  name: 'MainLayout',
  data() {
    return {
      avatarUrl: '',
      loading: false
    }
  },
  computed: {
    ...mapState(['userInfo']),
    activeMenu() {
      return this.$route.path
    }
  },
  mounted() {
    this.loadAvatar()
    // 将组件实例暴露到window对象，以便子组件可以调用其方法
    window.mainLayout = this
  },
  methods: {
    handleLogout() {
      this.$store.dispatch('logout')
      // 避免重复导航错误
      if (this.$router.currentRoute.path !== '/login') {
        this.$router.replace('/login').catch(err => {
          // 忽略重复导航错误
          if (err.name !== 'NavigationDuplicated') {
            console.error(err)
          }
        })
      }
    },
    async loadAvatar() {
      // 从localStorage获取用户信息，为所有角色加载头像
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo.avatar) {
        // 确保头像URL通过代理访问
        let avatar = userInfo.avatar
        if (!avatar.startsWith('http')) {
          if (!avatar.startsWith('/api')) {
            avatar = '/api' + avatar
          }
        }
        this.avatarUrl = avatar
      } else if (this.userInfo.role === 'admin') {
        // 对于admin角色，尝试从API获取头像
        this.loading = true
        try {
          const res = await getAdminInfo()
          if (res.data && res.data.avatar) {
            // 确保头像URL通过代理访问
            let avatar = res.data.avatar
            if (!avatar.startsWith('http')) {
              if (!avatar.startsWith('/api')) {
                avatar = '/api' + avatar
              }
            }
            this.avatarUrl = avatar
          }
        } catch (error) {
          console.error('加载头像失败:', error)
        } finally {
          this.loading = false
        }
      } else {
        // 如果没有头像信息，设置默认头像
        this.avatarUrl = ''
      }
    },
    handleAvatarClick() {
      // 根据不同角色跳转到对应的个人信息页面
      let profilePath = ''
      switch (this.userInfo.role) {
        case 'admin':
          profilePath = '/admin/profile'
          break
        case 'coach':
          profilePath = '/coach/profile'
          break
        case 'user':
          profilePath = '/user/profile'
          break
        default:
          return
      }
      
      if (this.$router.currentRoute.path !== profilePath) {
        this.$router.push(profilePath).catch(err => {
          // 忽略重复导航错误
          if (err.name !== 'NavigationDuplicated') {
            console.error(err)
          }
        })
      }
    },
    handleAvatarError(e) {
      // 图片加载失败时显示默认头像（使用base64编码的默认头像）
      e.target.src = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjNzc3IiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lam9pbj0icm91bmQiPjxwYXRoIGQ9Ik0xMiAyQzYuNDggMiAyIDYuNDggMiAxMnM0LjQ4IDEwIDEwIDEwIDEwLTQuNDggMTAtMTAtNC40OC0xMC0xMC0xMFptMCAxNmMtMi4yMSAwLTQtMS43OS00LTRzMS43OS00IDQtNCA0IDEuNzkgNCA0LTEuNzkgNC00IDR6bTAgLTljLTEuNiAwLTMtMS40LTMtM3MxLjQtMyAzLTMgMyAxLjQgMyAzLTEuNCAzLTMtMy0zem0wIDZoLTR2LTJoNHYyem0wIDR2LTRoLTJ2NGgyem0wIDR2LTRoLTJ2NGgyeiIvPjwvc3ZnPg=='
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background: #304156;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.avatar-container {
  position: relative;
  cursor: pointer;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  display: block;
  visibility: visible;
  opacity: 1;
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.avatar-loading {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading-icon {
  color: rgba(255, 255, 255, 0.8);
  font-size: 20px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .avatar {
    width: 36px;
    height: 36px;
  }
  
  .avatar-loading {
    width: 36px;
    height: 36px;
  }
}

@media (max-width: 480px) {
  .avatar {
    width: 32px;
    height: 32px;
  }
  
  .avatar-loading {
    width: 32px;
    height: 32px;
  }
  
  .loading-icon {
    font-size: 16px;
  }
}

.aside {
  background: #304156;
}

.menu {
  border: none;
  height: 100%;
}

.main {
  background: #f5f5f5;
  padding: 20px;
  border: none;
  outline: none;
  box-shadow: none;
  -webkit-box-shadow: none;
  -moz-box-shadow: none;
}

.el-main {
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  -webkit-box-shadow: none !important;
  -moz-box-shadow: none !important;
}
</style>