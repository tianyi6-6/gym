import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/admin',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      // 管理员路由
      {
        path: '/admin',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/course',
        name: 'AdminCourse',
        component: () => import('@/views/admin/Course.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/order',
        name: 'AdminOrder',
        component: () => import('@/views/admin/Order.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/coach',
        name: 'AdminCoach',
        component: () => import('@/views/admin/Coach.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/user',
        name: 'AdminUser',
        component: () => import('@/views/admin/User.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/equipment',
        name: 'AdminEquipment',
        component: () => import('@/views/admin/Equipment.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/recipe',
        name: 'AdminRecipe',
        component: () => import('@/views/admin/Recipe.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/experience',
        name: 'AdminExperience',
        component: () => import('@/views/admin/Experience.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/notice',
        name: 'AdminNotice',
        component: () => import('@/views/admin/Notice.vue'),
        meta: { role: 'admin' }
      },
      {
        path: '/admin/health-profile',
        name: 'AdminHealthProfile',
        component: () => import('@/views/admin/HealthProfile.vue'),
        meta: { role: 'admin' }
      },
      { path: '/admin/body-assessment',
        name: 'AdminBodyAssessment',
        component: () => import('@/views/admin/BodyAssessment.vue'),
        meta: { role: 'admin' }
      },
      { path: '/admin/profile',
        name: 'AdminProfile',
        component: () => import('@/views/admin/AdminProfile.vue'),
        meta: { role: 'admin' }
      },
      // 教练路由
      {
        path: '/coach',
        name: 'CoachDashboard',
        component: () => import('@/views/coach/Dashboard.vue'),
        meta: { role: 'coach' }
      },
      {
        path: '/coach/course-schedule',
        name: 'CoachCourseSchedule',
        component: () => import('@/views/coach/CourseSchedule.vue'),
        meta: { role: 'coach' }
      },
      {
        path: '/coach/my-courses',
        name: 'CoachMyCourses',
        component: () => import('@/views/coach/MyCourses.vue'),
        meta: { role: 'coach' }
      },
      {
        path: '/coach/health-fitness',
        name: 'CoachHealthFitness',
        component: () => import('@/views/coach/HealthFitness.vue'),
        meta: { role: 'coach' }
      },
      // 用户路由
      {
        path: '/user',
        name: 'UserDashboard',
        component: () => import('@/views/user/Dashboard.vue'),
        meta: { role: 'user' }
      },
      {
        path: '/user/course',
        name: 'UserCourse',
        component: () => import('@/views/user/Course.vue'),
        meta: { role: 'user' }
      },
      {
        path: '/user/appointment',
        name: 'UserAppointment',
        component: () => import('@/views/user/Appointment.vue'),
        meta: { role: 'user' }
      },
      {
        path: '/user/recharge',
        name: 'UserRecharge',
        component: () => import('@/views/user/Recharge.vue'),
        meta: { role: 'user' }
      },
      {
        path: '/user/experience',
        name: 'UserExperience',
        component: () => import('@/views/user/Experience.vue'),
        meta: { role: 'user' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  
  if (to.meta.requiresAuth && !token) {
    // 如果已经在登录页，避免重复导航
    if (to.path !== '/login') {
      next('/login')
    } else {
      next()
    }
  } else if (to.meta.role && userInfo.role !== to.meta.role) {
    // 角色权限检查
    if (to.path !== '/login') {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router

