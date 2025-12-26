<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.courseCount }}</div>
            <div class="stat-label">课程总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.orderCount }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.coachCount }}</div>
            <div class="stat-label">教练总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card style="margin-top: 20px">
      <div slot="header">数据统计</div>
      <div id="chart" style="width: 100%; height: 400px;"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'AdminDashboard',
  data() {
    return {
      stats: {
        courseCount: 0,
        orderCount: 0,
        userCount: 0,
        coachCount: 0
      }
    }
  },
  mounted() {
    this.loadStats()
    this.initChart()
  },
  methods: {
    loadStats() {
      // 这里应该调用API获取统计数据
      this.stats = {
        courseCount: 10,
        orderCount: 25,
        userCount: 50,
        coachCount: 5
      }
    },
    initChart() {
      const chart = echarts.init(document.getElementById('chart'))
      const option = {
        title: {
          text: '课程销售统计'
        },
        tooltip: {},
        xAxis: {
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20, 15]
        }]
      }
      chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}
</style>

