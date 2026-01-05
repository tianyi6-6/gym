<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="logo">私人健身房管理系统</div>
      <div class="user-info">
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
          </template>
          
          <!-- 教练菜单 -->
          <template v-if="userInfo.role === 'coach'">
            <el-menu-item index="/coach">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
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
            <el-menu-item index="/user/recharge">
              <i class="el-icon-wallet"></i>
              <span>账户充值</span>
            </el-menu-item>
            <el-menu-item index="/user/experience">
              <i class="el-icon-document"></i>
              <span>我的经验</span>
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

export default {
  name: 'MainLayout',
  computed: {
    ...mapState(['userInfo']),
    activeMenu() {
      return this.$route.path
    }
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

