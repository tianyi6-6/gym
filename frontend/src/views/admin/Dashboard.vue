<template>
  <div class="dashboard-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>数据概览</span>
        <el-select v-model="timeRange" @change="handleTimeRangeChange" size="small" style="width: 120px;">
          <el-option label="本周" value="week"></el-option>
          <el-option label="本月" value="month"></el-option>
          <el-option label="本年" value="year"></el-option>
        </el-select>
      </div>

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
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>课程销售与会员增长</span>
              <el-radio-group v-model="chartTimeRange" size="small" style="float: right">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
                <el-radio-button label="year">近12个月</el-radio-button>
              </el-radio-group>
            </div>
            <LineChart 
              :chart-data="mainChartData" 
              :smooth="true"
              :show-area="false"
              height="350px"
              @chart-click="handleMainChartClick"
            />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>会员等级分布</span>
            </div>
            <PieChart 
              :chart-data="memberLevelData" 
              :radius="['40%', '70%']"
              height="350px"
              @chart-click="handleMemberLevelClick"
            />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>课程类型分布</span>
            </div>
            <BarChart 
              :chart-data="courseTypeChartData" 
              :show-data-label="true"
              height="350px"
              @chart-click="handleCourseTypeClick"
            />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>教练综合能力</span>
            </div>
            <div ref="radarChart" style="height: 350px"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <div slot="header">
              <span>快捷操作</span>
            </div>
            <div class="quick-actions">
              <el-button type="primary" icon="el-icon-plus" @click="handleAddUser">
                新增用户
              </el-button>
              <el-button type="success" icon="el-icon-user" @click="handleAddCoach">
                新增教练
              </el-button>
              <el-button type="warning" icon="el-icon-bell" @click="handlePublish">
                发布公告
              </el-button>
              <el-button type="info" icon="el-icon-sold-out" @click="handleQuickOrder">
                快速开单
              </el-button>
              <el-button type="danger" icon="el-icon-refresh" @click="refreshData">
                刷新数据
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import LineChart from '@/components/LineChart.vue'
import BarChart from '@/components/BarChart.vue'
import PieChart from '@/components/PieChart.vue'

export default {
  name: 'AdminDashboard',
  components: {
    LineChart,
    BarChart,
    PieChart
  },
  data() {
    return {
      timeRange: 'week',
      chartTimeRange: 'week',
      loading: false,
      error: null,
      autoRefreshInterval: null,
      statItems: [
        {
          label: '课程总数',
          value: 0,
          icon: 'el-icon-reading',
          iconBg: '#ecf5ff',
          iconColor: '#409EFF',
          trend: '+12.5%',
          trendType: 'success',
          showTrend: true
        },
        {
          label: '订单总数',
          value: 0,
          icon: 'el-icon-shopping-cart-2',
          iconBg: '#f0f9ff',
          iconColor: '#67C23A',
          trend: '+8.3%',
          trendType: 'success',
          showTrend: true
        },
        {
          label: '用户总数',
          value: 0,
          icon: 'el-icon-user',
          iconBg: '#f4f4f5',
          iconColor: '#909399',
          trend: '+15.7%',
          trendType: 'success',
          showTrend: true
        },
        {
          label: '教练总数',
          value: 0,
          icon: 'el-icon-medal',
          iconBg: '#fdf6ec',
          iconColor: '#E6A23C',
          trend: '+5.2%',
          trendType: 'success',
          showTrend: true
        }
      ],
      mainChartData: {
        xAxis: [],
        series: []
      },
      memberLevelData: [],
      courseTypeChartData: {
        xAxis: [],
        series: []
      },
      radarChart: null,
      refreshTimer: null
    }
  },
  watch: {
    chartTimeRange() {
      this.timeRange = this.chartTimeRange
      this.refreshData()
    },
    timeRange() {
      this.chartTimeRange = this.timeRange
      this.refreshData()
    }
  },
  mounted() {
    this.loadStats()
    this.loadMainChartData()
    this.loadMemberLevelData()
    this.loadCourseTypeData()
    this.initRadarChart()
    window.addEventListener('resize', this.handleResize)
    this.startAutoRefresh()
  },
  beforeDestroy() {
    this.stopAutoRefresh()
    window.removeEventListener('resize', this.handleResize)
    this.disposeCharts()
  },
  methods: {
    disposeCharts() {
      if (this.radarChart) {
        this.radarChart.dispose()
      }
    },
    handleTimeRangeChange() {
      this.refreshData()
    },
    loadStats() {
      this.loading = true
      this.error = null
      
      this.$axios.get('/dashboard/stats', { params: { timeRange: this.timeRange } }).then(res => {
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
    calculateTrend(trend, value) {
      if (value === 0) return '-'
      if (!trend) return '-'
      return trend
    },
    shouldShowTrend(value) {
      return value > 0
    },
    loadMainChartData() {
      const daysMap = {
        week: 7,
        month: 30,
        year: 365
      }
      const days = daysMap[this.chartTimeRange]
      
      this.$axios.get('/dashboard/chart-data', { params: { timeRange: this.chartTimeRange } }).then(res => {
        if (res.code === 200 && res.data) {
          this.mainChartData = {
            xAxis: res.data.xAxis,
            series: [
              {
                name: '课程销售',
                data: res.data.courseSales
              },
              {
                name: '会员增长',
                data: res.data.memberGrowth
              }
            ]
          }
        }
      }).catch(error => {
        console.error('获取图表数据失败:', error)
        this.mainChartData = {
          xAxis: this.generateDateLabels(days),
          series: [
            {
              name: '课程销售',
              data: Array(days).fill(0)
            },
            {
              name: '会员增长',
              data: Array(days).fill(0)
            }
          ]
        }
      })
    },
    loadMemberLevelData() {
      this.$axios.get('/dashboard/member-level-distribution').then(res => {
        if (res.code === 200 && res.data) {
          this.memberLevelData = res.data
        }
      }).catch(error => {
        console.error('获取会员等级分布数据失败:', error)
        this.memberLevelData = [
          { name: '普通会员', value: 0 },
          { name: '银卡会员', value: 0 },
          { name: '金卡会员', value: 0 },
          { name: '钻石会员', value: 0 }
        ]
      })
    },
    loadCourseTypeData() {
      this.$axios.get('/dashboard/course-type-distribution').then(res => {
        if (res.code === 200 && res.data) {
          this.courseTypeChartData = {
            xAxis: res.data.names,
            series: [{
              name: '课程数量',
              data: res.data.counts
            }]
          }
        }
      }).catch(error => {
        console.error('获取课程类型分布数据失败:', error)
        this.courseTypeChartData = {
          xAxis: ['力量训练', '有氧运动', '瑜伽', '游泳', '其他'],
          series: [{
            name: '课程数量',
            data: [0, 0, 0, 0, 0]
          }]
        }
      })
    },
    initRadarChart() {
      const chartDom = this.$refs.radarChart
      if (!chartDom) return
      
      this.radarChart = echarts.init(chartDom)
      
      this.$axios.get('/dashboard/coach-radar').then(res => {
        if (res.code === 200 && res.data) {
          const option = {
            tooltip: {},
            legend: {
              data: res.data.coachNames
            },
            radar: {
              indicator: [
                { name: '专业能力', max: 100 },
                { name: '教学经验', max: 100 },
                { name: '沟通能力', max: 100 },
                { name: '服务态度', max: 100 },
                { name: '业绩表现', max: 100 }
              ]
            },
            series: [
              {
                name: '教练能力',
                type: 'radar',
                data: res.data.coachData
              }
            ]
          }
          this.radarChart.setOption(option)
        }
      }).catch(error => {
        console.error('获取教练雷达图数据失败:', error)
      })
    },
    handleResize() {
      if (this.radarChart) {
        this.radarChart.resize()
      }
    },
    refreshData() {
      this.loadStats()
      this.loadMainChartData()
      this.loadMemberLevelData()
      this.loadCourseTypeData()
      this.initRadarChart()
    },
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.refreshData()
      }, 60000)
    },
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },
    handleAddUser() {
      this.$router.push({ path: '/register', query: { role: 'user' } })
    },
    handleAddCoach() {
      this.$router.push({ path: '/register', query: { role: 'coach' } })
    },
    handlePublish() {
      this.$router.push('/admin/notice')
    },
    handleQuickOrder() {
      this.$router.push('/admin/order')
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
    handleMainChartClick(params) {
      const date = params.name
      const seriesName = params.seriesName
      const value = params.value
      
      this.$message({
        message: `点击了日期: ${date}, ${seriesName}: ${value}`,
        type: 'info',
        duration: 2000
      })
      
      if (seriesName === '课程销售') {
        this.$confirm(`是否查看 ${date} 的课程销售详情?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/admin/order')
        }).catch(() => {})
      } else if (seriesName === '会员增长') {
        this.$confirm(`是否查看 ${date} 的新增会员详情?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/admin/user')
        }).catch(() => {})
      }
    },
    handleMemberLevelClick(params) {
      const level = params.name
      const count = params.value
      const percent = params.percent
      
      this.$message({
        message: `会员等级: ${level}, 人数: ${count}, 占比: ${percent}%`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${level} 的会员列表?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push({ path: '/admin/user', query: { level } })
      }).catch(() => {})
    },
    handleCourseTypeClick(params) {
      const type = params.name
      const count = params.value
      
      this.$message({
        message: `课程类型: ${type}, 数量: ${count}`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${type} 的课程列表?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push({ path: '/admin/course', query: { type } })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.dashboard-page {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
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
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  display: inline-block;
}

.quick-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
</style>
