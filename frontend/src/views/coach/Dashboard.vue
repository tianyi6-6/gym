<template>
  <div class="coach-dashboard">
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>月度课程统计</span>
            <el-tag type="info" size="small" style="float: right">本月数据</el-tag>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="monthly-stat-card">
                <div class="stat-icon-wrapper" style="background: rgba(64, 158, 255, 0.1)">
                  <i class="el-icon-document" style="color: #409EFF; font-size: 32px"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-label">课程总数</div>
                  <div class="stat-value">{{ monthlyStats.totalCourses }}</div>
                  <div class="stat-desc">本月累计课程</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monthly-stat-card">
                <div class="stat-icon-wrapper" style="background: rgba(103, 194, 58, 0.1)">
                  <i class="el-icon-user" style="color: #67C23A; font-size: 32px"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-label">学员出勤率</div>
                  <div class="stat-value">{{ monthlyStats.attendanceRate.toFixed(1) }}%</div>
                  <div class="stat-desc">平均出勤率</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monthly-stat-card">
                <div class="stat-icon-wrapper" style="background: rgba(230, 162, 60, 0.1)">
                  <i class="el-icon-star-on" style="color: #E6A23C; font-size: 32px"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-label">课程满意度</div>
                  <div class="stat-value">{{ monthlyStats.satisfactionScore.toFixed(1) }}</div>
                  <div class="stat-desc">1-5分制评分</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monthly-stat-card">
                <div class="stat-icon-wrapper" style="background: rgba(245, 108, 108, 0.1)">
                  <i class="el-icon-time" style="color: #F56C6C; font-size: 32px"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-label">训练时长</div>
                  <div class="stat-value">{{ monthlyStats.totalHours.toFixed(1) }}h</div>
                  <div class="stat-desc">本月累计时长</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">月度课程统计</div>
          <BarChart 
            :chart-data="monthlyCourseData" 
            :show-data-label="true"
            @chart-click="handleMonthlyCourseClick"
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">课程类型分布</div>
          <PieChart 
            :chart-data="courseTypeData" 
            :rose-type="'radius'"
            @chart-click="handleCourseTypeClick"
          />
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>教练健康体能状况</span>
            <el-tag type="success" size="small" style="float: right">实时数据</el-tag>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(64, 158, 255, 0.1)">
                  <i class="el-icon-s-custom" style="color: #409EFF; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">体重</div>
                  <div class="health-stat-value">{{ healthFitness.weight.toFixed(1) }} kg</div>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(103, 194, 58, 0.1)">
                  <i class="el-icon-odometer" style="color: #67C23A; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">体脂率</div>
                  <div class="health-stat-value">{{ healthFitness.bodyFatRate.toFixed(1) }}%</div>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(230, 162, 60, 0.1)">
                  <i class="el-icon-monitor" style="color: #E6A23C; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">心率</div>
                  <div class="health-stat-value">{{ healthFitness.heartRate }} bpm</div>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(245, 108, 108, 0.1)">
                  <i class="el-icon-timer" style="color: #F56C6C; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">训练时长</div>
                  <div class="health-stat-value">{{ healthFitness.trainingHours.toFixed(1) }} h</div>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(103, 194, 58, 0.1)">
                  <i class="el-icon-fire" style="color: #67C23A; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">卡路里消耗</div>
                  <div class="health-stat-value">{{ healthFitness.caloriesBurned }} kcal</div>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="health-stat-card">
                <div class="health-stat-icon" style="background: rgba(64, 158, 255, 0.1)">
                  <i class="el-icon-medal" style="color: #409EFF; font-size: 28px"></i>
                </div>
                <div class="health-stat-content">
                  <div class="health-stat-label">BMI</div>
                  <div class="health-stat-value">{{ healthFitness.bmi.toFixed(1) }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">综合能力评估</div>
          <RadarChart 
            :chart-data="{ abilityScores: healthFitness.abilityScores }"
            height="400px"
            @chart-click="handleRadarClick"
          />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header">心率监测</div>
          <GaugeChart 
            :value="healthFitness.heartRate" 
            title="心率"
            unit=" bpm"
            :min="40"
            :max="180"
            height="300px"
            :color="['#67C23A', '#E6A23C', '#F56C6C']"
          />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header">体脂率</div>
          <GaugeChart 
            :value="healthFitness.bodyFatRate" 
            title="体脂率"
            unit="%"
            :min="5"
            :max="35"
            height="300px"
            :color="['#67C23A', '#E6A23C', '#F56C6C']"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { getCoachStats } from '@/api'
import LineChart from '@/components/LineChart.vue'
import BarChart from '@/components/BarChart.vue'
import PieChart from '@/components/PieChart.vue'
import RadarChart from '@/components/RadarChart.vue'
import GaugeChart from '@/components/GaugeChart.vue'

export default {
  name: 'CoachDashboard',
  components: {
    LineChart,
    BarChart,
    PieChart,
    RadarChart,
    GaugeChart
  },
  data() {
    return {
      loading: false,
      error: null,
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
      courseTrendPeriod: 'week',
      courseTrendData: {
        xAxis: [],
        series: []
      },
      studentLevelData: [],
      monthlyCourseData: {
        xAxis: [],
        series: []
      },
      courseTypeData: [],
      monthlyStats: {
        totalCourses: 0,
        attendanceRate: 0.0,
        satisfactionScore: 0.0,
        totalHours: 0.0,
        totalCalories: 0
      },
      healthFitness: {
        weight: 0.0,
        bodyFatRate: 0.0,
        heartRate: 0,
        trainingHours: 0.0,
        caloriesBurned: 0,
        muscleMass: 0.0,
        bmi: 0.0,
        abilityScores: {
          strength: 0,
          endurance: 0,
          flexibility: 0,
          speed: 0,
          coordination: 0
        },
        history: {
          dates: [],
          weights: [],
          bodyFatRates: [],
          heartRates: []
        }
      }
    }
  },
  computed: {
    ...mapState(['userInfo', 'coachData']),
    myCoursesCount() {
      return this.coachData.myCourses ? this.coachData.myCourses.length : 0
    }
  },
  watch: {
    courseTrendPeriod() {
      this.loadCourseTrendData()
    },
    'coachData.healthFitness': {
      handler(newVal) {
        if (newVal && newVal.weight !== undefined) {
          this.healthFitness = {
            weight: newVal.weight || 0.0,
            bodyFatRate: newVal.bodyFatRate || 0.0,
            heartRate: newVal.heartRate || 0,
            trainingHours: newVal.trainingHours || 0.0,
            caloriesBurned: newVal.caloriesBurned || 0,
            muscleMass: newVal.muscleMass || 0.0,
            bmi: newVal.bmi || 0.0,
            abilityScores: {
              strength: newVal.strengthScore || 0,
              endurance: newVal.enduranceScore || 0,
              flexibility: newVal.flexibilityScore || 0,
              speed: newVal.speedScore || 0,
              coordination: newVal.coordinationScore || 0
            },
            history: {
              dates: newVal.dates || [],
              weights: newVal.weights || [],
              bodyFatRates: newVal.bodyFatRates || [],
              heartRates: newVal.heartRates || []
            }
          };
        }
      },
      immediate: true
    },
    'coachData.myCourses': {
      handler(newVal) {
        if (newVal) {
          this.loadCourseTypeData()
          this.monthlyStats.totalCourses = newVal.length
        }
      },
      deep: true
    }
  },
  mounted() {
    this.loadStats()
    this.loadData()
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
    
    loadData() {
      this.loadCourseTrendData()
      this.loadStudentLevelData()
      this.loadMonthlyCourseData()
      this.loadCourseTypeData()
      this.loadMonthlyStats()
      this.loadHealthFitness()
    },
    
    loadCourseTrendData() {
      if (!this.userInfo.id) return
      
      this.$axios.get(`/coach-dashboard/course-trend/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200) {
            const data = res.data;
            this.courseTrendData = {
              xAxis: data.months,
              series: [
                {
                  name: '课程数量',
                  data: data.courseCounts,
                  type: 'line',
                  smooth: true,
                  showSymbol: true,
                  symbolSize: 8,
                  lineStyle: {
                    width: 3
                  },
                  itemStyle: {
                    color: '#409EFF'
                  }
                },
                {
                  name: '学员数量',
                  data: data.studentCounts,
                  type: 'bar',
                  barWidth: '30%',
                  itemStyle: {
                    color: '#67C23A'
                  }
                }
              ]
            };
          }
        })
        .catch(error => {
          console.error('获取课程趋势数据失败:', error);
          this.error = '获取课程趋势数据失败，请稍后重试';
          this.$message.error('获取课程趋势数据失败: ' + (error.message || '未知错误'));
        });
    },
    
    loadStudentLevelData() {
      if (!this.userInfo.id) return
      
      getCoachStats(this.userInfo.id).then(res => {
        this.studentLevelData = res.data.studentLevelDistribution || [
          { name: '初级', value: 0 },
          { name: '中级', value: 0 },
          { name: '高级', value: 0 }
        ]
      }).catch(() => {
        this.studentLevelData = [
          { name: '初级', value: 0 },
          { name: '中级', value: 0 },
          { name: '高级', value: 0 }
        ]
      })
    },
    
    loadMonthlyCourseData() {
      if (!this.userInfo.id) return
      
      getCoachStats(this.userInfo.id).then(res => {
        const monthlyData = res.data.monthlyCourses || []
        const xAxis = monthlyData.map(item => item.month)
        const series = [{
          name: '课程数量',
          data: monthlyData.map(item => item.count)
        }]
        
        this.monthlyCourseData = { xAxis, series }
      }).catch(() => {
        this.monthlyCourseData = {
          xAxis: ['1月', '2月', '3月', '4月', '5月', '6月'],
          series: [{
            name: '课程数量',
            data: [0, 0, 0, 0, 0, 0]
          }]
        }
      })
    },
    
    loadCourseTypeData() {
      const myCourses = this.coachData.myCourses || []
      
      if (myCourses.length === 0) {
        this.courseTypeData = [
          { name: '力量训练', value: 0 },
          { name: '有氧运动', value: 0 },
          { name: '瑜伽', value: 0 },
          { name: '普拉提', value: 0 },
          { name: '其他', value: 0 }
        ]
        return
      }
      
      const typeCount = {}
      myCourses.forEach(course => {
        const type = course.courseType || '其他'
        typeCount[type] = (typeCount[type] || 0) + 1
      })
      
      const allTypes = ['力量训练', '有氧运动', '瑜伽', '普拉提', '其他']
      this.courseTypeData = allTypes.map(type => ({
        name: type,
        value: typeCount[type] || 0
      }))
    },
    
    loadMonthlyStats() {
      if (!this.userInfo.id) return
      
      this.$axios.get(`/coach-dashboard/monthly-stats/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200) {
            const data = res.data;
            this.monthlyStats = {
              totalCourses: this.myCoursesCount,
              attendanceRate: data.attendanceRate || 0.0,
              satisfactionScore: data.satisfactionScore || 0.0,
              totalHours: data.totalHours || 0.0,
              totalCalories: data.totalCalories || 0
            };
          }
        })
        .catch(error => {
          console.error('获取月度统计数据失败:', error);
          this.$message.error('获取月度统计数据失败: ' + (error.message || '未知错误'));
        });
    },
    
    loadHealthFitness() {
      if (!this.userInfo.id) return
      
      this.$axios.get(`/coach-dashboard/health-fitness/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200) {
            const data = res.data;
            this.healthFitness = {
              weight: data.weight || 0.0,
              bodyFatRate: data.bodyFatRate || 0.0,
              heartRate: data.heartRate || 0,
              trainingHours: data.trainingHours || 0.0,
              caloriesBurned: data.caloriesBurned || 0,
              muscleMass: data.muscleMass || 0.0,
              bmi: data.bmi || 0.0,
              abilityScores: data.abilityScores || {
                strength: 0,
                endurance: 0,
                flexibility: 0,
                speed: 0,
                coordination: 0
              },
              history: {
                dates: data.dates || [],
                weights: data.weights || [],
                bodyFatRates: data.bodyFatRates || [],
                heartRates: data.heartRates || []
              }
            };
            
            this.$store.dispatch('updateCoachHealthFitness', this.healthFitness);
          }
        })
        .catch(error => {
          console.error('获取健康体能数据失败:', error);
          this.$message.error('获取健康体能数据失败: ' + (error.message || '未知错误'));
        });
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
    
    handleCourseTrendClick(params) {
      const date = params.name
      const value = params.value
      
      this.$message({
        message: `点击了日期: ${date}, 课程数: ${value}`,
        type: 'info',
        duration: 2000
      })
      
      if (value > 0) {
        this.$confirm(`是否查看 ${date} 的课程安排详情?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/coach/appointment')
        }).catch(() => {})
      }
    },
    
    handleStudentLevelClick(params) {
      const level = params.name
      const count = params.value
      const percent = params.percent
      
      this.$message({
        message: `学员等级: ${level}, 人数: ${count}, 占比: ${percent}%`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${level} 的学员列表?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push({ path: '/coach/appointment', query: { level } })
      }).catch(() => {})
    },
    
    handleMonthlyCourseClick(params) {
      const month = params.name
      const count = params.value
      
      this.$message({
        message: `月份: ${month}, 课程数: ${count}`,
        type: 'info',
        duration: 2000
      })
      
      if (count > 0) {
        this.$confirm(`是否查看 ${month} 的课程统计详情?`, '查看详情', {
          confirmButtonText: '查看',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$router.push('/coach/appointment')
        }).catch(() => {})
      }
    },
    
    handleCourseTypeClick(params) {
      const type = params.name
      const count = params.value
      const percent = params.percent
      
      this.$message({
        message: `课程类型: ${type}, 数量: ${count}, 占比: ${percent}%`,
        type: 'info',
        duration: 2000
      })
      
      this.$confirm(`是否查看 ${type} 的课程?`, '查看详情', {
        confirmButtonText: '查看',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$router.push({ path: '/coach/appointment', query: { type } })
      }).catch(() => {})
    },
    
    handleRadarClick(params) {
      const abilityName = params.name
      const abilityValue = params.value
      
      this.$message({
        message: `能力维度: ${abilityName}, 评分: ${abilityValue}`,
        type: 'info',
        duration: 2000
      })
    },
    
    refreshData() {
      this.loadStats()
      this.loadCourseTrendData()
      this.loadStudentLevelData()
      this.loadMonthlyCourseData()
      this.loadCourseTypeData()
      this.loadMonthlyStats()
      this.loadHealthFitness()
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
.coach-dashboard {
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

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  color: #409EFF;
  font-weight: bold;
  margin: 20px 0;
}

h3 {
  color: #666;
  margin-bottom: 10px;
}

.monthly-stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.monthly-stat-card:hover {
  background: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon-wrapper {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-info .stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-info .stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-info .stat-desc {
  font-size: 12px;
  color: #C0C4CC;
}

.health-stat-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.health-stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.health-stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.health-stat-content {
  flex: 1;
}

.health-stat-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 6px;
}

.health-stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

@media (max-width: 768px) {
  .coach-dashboard {
    padding: 10px;
  }
  
  .stat-number {
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

