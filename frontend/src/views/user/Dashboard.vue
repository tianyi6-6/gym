<template>
  <div class="user-dashboard">
    <el-alert
      v-if="error"
      :title="error"
      type="error"
      show-icon
      :closable="true"
      @close="error = null"
      style="margin-bottom: 20px">
    </el-alert>
    
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6" v-for="(item, index) in statItems" :key="index">
        <el-card shadow="hover" v-loading="loading">
          <div class="stat-item">
            <div class="stat-icon" :style="{ background: item.iconBg }">
              <i :class="item.icon" :style="{ color: item.iconColor }"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
              <div class="stat-trend" v-if="item.showTrend">
                <el-tag :type="item.trendType" size="mini">{{ item.trend }}</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="balance-card">
            <h3>账户余额</h3>
            <div class="balance-amount">¥{{ userInfo.balance || 0 }}</div>
            <el-button type="primary" @click="$router.push('/user/recharge')">充值</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的订单</h3>
            <div class="stat-number">{{ orderCount }}</div>
            <el-button type="text" @click="$router.push('/user/course')">查看课程</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的预约</h3>
            <div class="stat-number">{{ appointmentCount }}</div>
            <el-button type="text" @click="$router.push('/user/appointment')">查看预约</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span>订单趋势</span>
            <el-radio-group v-model="orderTrendPeriod" size="small" style="float: right">
              <el-radio-button label="week">近7天</el-radio-button>
              <el-radio-button label="month">近30天</el-radio-button>
              <el-radio-button label="quarter">近90天</el-radio-button>
            </el-radio-group>
          </div>
          <LineChart 
            :chart-data="orderTrendData" 
            :smooth="true"
            :show-area="true"
            @chart-click="handleOrderTrendClick"
          />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">课程类型分布</div>
          <PieChart 
            :chart-data="courseTypeData" 
            :radius="['40%', '70%']"
            @chart-click="handleCourseTypeClick"
          />
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">月度消费统计</div>
          <BarChart 
            :chart-data="monthlySpendingData" 
            :show-data-label="true"
            @chart-click="handleSpendingClick"
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">消费类别占比</div>
          <PieChart 
            :chart-data="spendingCategoryData" 
            :rose-type="'radius'"
            @chart-click="handleCategoryClick"
          />
        </el-card>
      </el-col>
    </el-row>
    
    <el-card style="margin-top: 20px">
      <div slot="header">系统公告</div>
      <el-table :data="noticeList" border>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 2 ? 'danger' : 'info'">
              {{ scope.row.type === 2 ? '重要' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getNoticeList, getUserOrderList, getUserStats, getCourseList } from '@/api'
import { mapState } from 'vuex'
import LineChart from '@/components/LineChart.vue'
import BarChart from '@/components/BarChart.vue'
import PieChart from '@/components/PieChart.vue'

export default {
  name: 'UserDashboard',
  components: {
    LineChart,
    BarChart,
    PieChart
  },
  data() {
    return {
      loading: false,
      error: null,
      autoRefreshInterval: null,
      statItems: [
        {
          label: '课程总数',
          value: 0,
          icon: 'el-icon-document',
          iconBg: 'rgba(64, 158, 255, 0.1)',
          iconColor: '#409EFF',
          trend: '0%',
          trendType: 'success',
          showTrend: false
        },
        {
          label: '订单总数',
          value: 0,
          icon: 'el-icon-shopping-cart-2',
          iconBg: 'rgba(103, 194, 58, 0.1)',
          iconColor: '#67c23a',
          trend: '0%',
          trendType: 'success',
          showTrend: false
        },
        {
          label: '用户总数',
          value: 0,
          icon: 'el-icon-user',
          iconBg: 'rgba(230, 162, 60, 0.1)',
          iconColor: '#e6a23c',
          trend: '0%',
          trendType: 'success',
          showTrend: false
        },
        {
          label: '教练总数',
          value: 0,
          icon: 'el-icon-s-custom',
          iconBg: 'rgba(245, 108, 108, 0.1)',
          iconColor: '#f56c6c',
          trend: '0%',
          trendType: 'success',
          showTrend: false
        }
      ],
      noticeList: [],
      orderCount: 0,
      appointmentCount: 0,
      orderTrendPeriod: 'week',
      orderTrendData: {
        xAxis: [],
        series: []
      },
      courseTypeData: [],
      monthlySpendingData: {
        xAxis: [],
        series: []
      },
      spendingCategoryData: []
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  watch: {
    orderTrendPeriod() {
      this.loadOrderTrendData()
    }
  },
  mounted() {
    this.loadStats()
    this.loadNoticeList()
    this.loadUserStats()
    this.loadOrderTrendData()
    this.loadCourseTypeData()
    this.loadMonthlySpendingData()
    this.loadSpendingCategoryData()
    this.startAutoRefresh()
  },
  beforeDestroy() {
    this.stopAutoRefresh()
  },
  methods: {
    loadStats() {
      this.loading = true
      this.error = null
      
      this.$axios.get('/dashboard/stats').then(res => {
        if (res.code === 200) {
          this.statItems[0].value = res.data.courseCount
          this.statItems[0].trend = this.calculateTrend(res.data.courseTrend, res.data.courseCount)
          this.statItems[0].showTrend = this.shouldShowTrend(res.data.courseCount)
          
          this.statItems[1].value = res.data.orderCount
          this.statItems[1].trend = this.calculateTrend(res.data.orderTrend, res.data.orderCount)
          this.statItems[1].showTrend = this.shouldShowTrend(res.data.orderCount)
          
          this.statItems[2].value = res.data.userCount
          this.statItems[2].trend = this.calculateTrend(res.data.userTrend, res.data.userCount)
          this.statItems[2].showTrend = this.shouldShowTrend(res.data.userCount)
          
          this.statItems[3].value = res.data.coachCount
          this.statItems[3].trend = this.calculateTrend(res.data.coachTrend, res.data.coachCount)
          this.statItems[3].showTrend = this.shouldShowTrend(res.data.coachCount)
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        this.error = '获取统计数据失败，请稍后重试'
        this.$message.error('获取统计数据失败: ' + (error.message || '未知错误'))
      }).finally(() => {
        this.loading = false
      })
    },
    
    calculateTrend(trendStr, count) {
      if (!trendStr || count === 0) return '0%'
      return trendStr
    },
    
    shouldShowTrend(count) {
      return count > 0
    },
    
    loadNoticeList() {
      getNoticeList().then(res => {
        this.noticeList = res.data.filter(n => n.status === 1).slice(0, 5)
      })
    },
    
    loadUserStats() {
      if (!this.userInfo.id) return
      getUserStats(this.userInfo.id).then(res => {
        this.orderCount = res.data.orderCount || 0
        this.appointmentCount = res.data.appointmentCount || 0
      }).catch(() => {
        getUserOrderList(this.userInfo.id).then(res => {
          this.orderCount = res.data.length || 0
        })
      })
    },
    
    loadOrderTrendData() {
      const daysMap = {
        week: 7,
        month: 30,
        quarter: 90
      }
      const days = daysMap[this.orderTrendPeriod]
      
      if (!this.userInfo.id) return
      
      getUserStats(this.userInfo.id).then(res => {
        const trendData = res.data.orderTrend || []
        const xAxis = trendData.map(item => item.date)
        const series = [{
          name: '订单数量',
          data: trendData.map(item => item.count)
        }]
        
        this.orderTrendData = { xAxis, series }
      }).catch(() => {
        this.orderTrendData = {
          xAxis: this.generateDateLabels(days),
          series: [{
            name: '订单数量',
            data: Array(days).fill(0)
          }]
        }
      })
    },
    
    loadCourseTypeData() {
      // 从课程列表获取真实的课程类型分布
      getCourseList().then(res => {
        const courseList = res.data.filter(c => c.status === 1)
        const courseTypeMap = {
          '力量训练': 0,
          '有氧运动': 0,
          '瑜伽': 0,
          '游泳': 0,
          '其他': 0
        }
        
        courseList.forEach(course => {
          const courseName = course.name
          if (courseName.includes('力量')) {
            courseTypeMap['力量训练']++
          } else if (courseName.includes('有氧')) {
            courseTypeMap['有氧运动']++
          } else if (courseName.includes('瑜伽')) {
            courseTypeMap['瑜伽']++
          } else if (courseName.includes('游泳')) {
            courseTypeMap['游泳']++
          } else {
            courseTypeMap['其他']++
          }
        })
        
        this.courseTypeData = Object.entries(courseTypeMap).map(([name, value]) => ({ name, value }))
      }).catch(() => {
        this.courseTypeData = [
          { name: '力量训练', value: 0 },
          { name: '有氧运动', value: 0 },
          { name: '瑜伽', value: 0 },
          { name: '游泳', value: 0 },
          { name: '其他', value: 0 }
        ]
      })
    },
    
    loadMonthlySpendingData() {
      if (!this.userInfo.id) return
      
      getUserStats(this.userInfo.id).then(res => {
        const monthlyData = res.data.monthlySpending || []
        const xAxis = monthlyData.map(item => item.month)
        const series = [{
          name: '消费金额',
          data: monthlyData.map(item => item.amount)
        }]
        
        this.monthlySpendingData = { xAxis, series }
      }).catch(() => {
        this.monthlySpendingData = {
          xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
          series: [{
            name: '消费金额',
            data: [0, 0, 0, 0, 0, 0]
          }]
        }
      })
    },
    
    loadSpendingCategoryData() {
      if (!this.userInfo.id) return
      
      getUserStats(this.userInfo.id).then(res => {
        this.spendingCategoryData = res.data.spendingCategory || [
          { name: '课程购买', value: 0 },
          { name: '私教课程', value: 0 },
          { name: '器材租赁', value: 0 },
          { name: '其他', value: 0 }
        ]
      }).catch(() => {
        this.spendingCategoryData = [
          { name: '课程购买', value: 0 },
          { name: '私教课程', value: 0 },
          { name: '器材租赁', value: 0 },
          { name: '其他', value: 0 }
        ]
      })
    },
    
    generateDateLabels(days) {
      const labels = []
      const today = new Date()
      for (let i = days - 1; i >= 0; i--) {
        const date = new Date(today)
        date.setDate(date.getDate() - i)
        labels.push(`${date.getMonth() + 1}/${date.getDate()}`)
      }
      return labels
    },
    
    handleOrderTrendClick(params) {
      const date = params.name
      const value = params.value
      
      this.$message({
        message: `点击了日期: ${date}, 订单数: ${value}`,
        type: 'info',
        duration: 2000
      })
      
      if (value > 0) {
        this.$confirm(`是否查看 ${date} 的订单详情?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/user/course')
        }).catch(() => {})
      }
    },
    
    handleCourseTypeClick(params) {
      const type = params.name
      const percent = params.percent
      
      this.$message({
        message: `课程类型: ${type}, 占比: ${percent}%`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${type} 的课程?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push({ path: '/user/course', query: { type } })
      }).catch(() => {})
    },
    
    handleSpendingClick(params) {
      const month = params.name
      const amount = params.value
      
      this.$message({
        message: `月份: ${month}, 消费金额: ¥${amount}`,
        type: 'info',
        duration: 2000
      })
      
      if (amount > 0) {
        this.$confirm(`是否查看 ${month} 的消费记录?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/user/course')
        }).catch(() => {})
      }
    },
    
    handleCategoryClick(params) {
      const category = params.name
      const amount = params.value
      
      this.$message({
        message: `消费类别: ${category}, 金额: ¥${amount}`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${category} 的消费详情?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push('/user/course')
      }).catch(() => {})
    },
    
    refreshData() {
      this.loadStats()
      this.loadNoticeList()
      this.loadUserStats()
      this.loadOrderTrendData()
      this.loadCourseTypeData()
      this.loadMonthlySpendingData()
      this.loadSpendingCategoryData()
    },
    
    startAutoRefresh() {
      this.autoRefreshInterval = setInterval(() => {
        this.refreshData()
      }, 60000)
    },
    
    stopAutoRefresh() {
      if (this.autoRefreshInterval) {
        clearInterval(this.autoRefreshInterval)
        this.autoRefreshInterval = null
      }
    }
  }
}
</script>

<style scoped>
.user-dashboard {
  padding: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon i {
  font-size: 28px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
}

.balance-card, .stat-card {
  text-align: center;
  padding: 20px;
}

.balance-amount {
  font-size: 36px;
  color: #409EFF;
  font-weight: bold;
  margin: 20px 0;
}

.stat-number {
  font-size: 32px;
  color: #67c23a;
  font-weight: bold;
  margin: 20px 0;
}

h3 {
  color: #666;
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  .user-dashboard {
    padding: 10px;
  }
  
  .balance-amount, .stat-number {
    font-size: 24px;
  }
  
  .stat-value {
    font-size: 22px;
  }
  
  .stat-icon {
    width: 50px;
    height: 50px;
  }
  
  .stat-icon i {
    font-size: 24px;
  }
}
</style>

