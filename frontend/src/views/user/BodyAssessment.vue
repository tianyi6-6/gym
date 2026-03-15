<template>
  <div class="body-assessment-container">
    <el-card class="card">
      <template slot="header">
        <div class="card-header">
          <h2>体测与健康档案</h2>
          <el-button type="primary" @click="handleUploadReport">上传体测报告</el-button>
        </div>
      </template>
      
      <!-- 体测数据展示 -->
      <el-tabs v-model="activeTab" class="data-tabs">
        <el-tab-pane label="最新体测数据" name="latest">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="data-card">
                <div class="data-item">
                  <span class="data-label">体重</span>
                  <span class="data-value">{{ latestData.weight || '--' }} kg</span>
                </div>
                <div class="data-item">
                  <span class="data-label">体脂率</span>
                  <span class="data-value">{{ latestData.bodyFatRate || '--' }}%</span>
                </div>
                <div class="data-item">
                  <span class="data-label">BMI</span>
                  <span class="data-value">{{ latestData.bmi || '--' }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="data-card">
                <div class="data-item">
                  <span class="data-label">胸围</span>
                  <span class="data-value">{{ latestData.chest || '--' }} cm</span>
                </div>
                <div class="data-item">
                  <span class="data-label">腰围</span>
                  <span class="data-value">{{ latestData.waist || '--' }} cm</span>
                </div>
                <div class="data-item">
                  <span class="data-label">臀围</span>
                  <span class="data-value">{{ latestData.hip || '--' }} cm</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="data-card">
                <div class="data-item">
                  <span class="data-label">肌肉量</span>
                  <span class="data-value">{{ latestData.muscleMass || '--' }} kg</span>
                </div>
                <div class="data-item">
                  <span class="data-label">基础代谢</span>
                  <span class="data-value">{{ latestData.bmr || '--' }} kcal</span>
                </div>
                <div class="data-item">
                  <span class="data-label">体水分率</span>
                  <span class="data-value">{{ latestData.waterRate || '--' }}%</span>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
        
        <el-tab-pane label="历史数据趋势" name="trend">
          <div class="trend-controls">
            <el-radio-group v-model="trendPeriod" @change="updateChart">
              <el-radio label="week">周</el-radio>
              <el-radio label="month">月</el-radio>
              <el-radio label="quarter">季度</el-radio>
            </el-radio-group>
          </div>
          <div class="chart-container">
            <div ref="weightChart" class="chart"></div>
            <div ref="bodyFatChart" class="chart"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="体测报告" name="reports">
          <el-table :data="reports" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="reportName" label="报告名称"></el-table-column>
            <el-table-column prop="uploadDate" label="上传日期"></el-table-column>
            <el-table-column prop="fileType" label="文件类型"></el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <el-button type="primary" size="small" @click="downloadReport(scope.row)">下载</el-button>
                <el-button type="danger" size="small" @click="deleteReport(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 上传体测报告对话框 -->
    <el-dialog title="上传体测报告" :visible.sync="uploadDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action="/api/body-assessment/upload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        multiple
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          支持 PDF、JPG、PNG 格式，单个文件不超过 10MB
        </div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'UserBodyAssessment',
  data() {
    return {
      activeTab: 'latest',
      trendPeriod: 'month',
      uploadDialogVisible: false,
      latestData: {
        weight: 65.5,
        bodyFatRate: 20.5,
        bmi: 22.5,
        chest: 95,
        waist: 75,
        hip: 90,
        muscleMass: 45.2,
        bmr: 1600,
        waterRate: 60
      },
      reports: [
        {
          id: 1,
          reportName: '2024年3月体测报告',
          uploadDate: '2024-03-15',
          fileType: 'PDF'
        },
        {
          id: 2,
          reportName: '2024年2月体测报告',
          uploadDate: '2024-02-20',
          fileType: 'JPG'
        }
      ],
      weightChart: null,
      bodyFatChart: null
    }
  },
  mounted() {
    this.initCharts()
  },
  beforeDestroy() {
    if (this.weightChart) {
      this.weightChart.dispose()
    }
    if (this.bodyFatChart) {
      this.bodyFatChart.dispose()
    }
  },
  methods: {
    handleUploadReport() {
      this.uploadDialogVisible = true
    },
    handleUploadSuccess(response, file, fileList) {
      this.$message.success('上传成功')
      this.uploadDialogVisible = false
      // 刷新报告列表
      this.loadReports()
    },
    handleUploadError(error, file, fileList) {
      this.$message.error('上传失败')
    },
    beforeUpload(file) {
      const isPDF = file.type === 'application/pdf'
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt10M = file.size / 1024 / 1024 < 10
      
      if (!isPDF && !isImage) {
        this.$message.error('只能上传 PDF、JPG、PNG 格式的文件')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过 10MB')
        return false
      }
      return true
    },
    downloadReport(report) {
      // 模拟下载
      this.$message.success('开始下载报告')
    },
    deleteReport(id) {
      this.$confirm('确定要删除这个报告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟删除
        this.reports = this.reports.filter(item => item.id !== id)
        this.$message.success('删除成功')
      }).catch(() => {
        // 取消删除
      })
    },
    loadReports() {
      // 模拟加载报告列表
      this.reports = [
        {
          id: 1,
          reportName: '2024年3月体测报告',
          uploadDate: '2024-03-15',
          fileType: 'PDF'
        },
        {
          id: 2,
          reportName: '2024年2月体测报告',
          uploadDate: '2024-02-20',
          fileType: 'JPG'
        },
        {
          id: 3,
          reportName: '2024年1月体测报告',
          uploadDate: '2024-01-10',
          fileType: 'PDF'
        }
      ]
    },
    initCharts() {
      this.$nextTick(() => {
        this.initWeightChart()
        this.initBodyFatChart()
      })
    },
    initWeightChart() {
      this.weightChart = echarts.init(this.$refs.weightChart)
      this.updateWeightChart()
    },
    initBodyFatChart() {
      this.bodyFatChart = echarts.init(this.$refs.bodyFatChart)
      this.updateBodyFatChart()
    },
    updateChart() {
      this.updateWeightChart()
      this.updateBodyFatChart()
    },
    updateWeightChart() {
      const option = {
        title: {
          text: '体重变化趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.getDateLabels()
        },
        yAxis: {
          type: 'value',
          name: '体重 (kg)'
        },
        series: [{
          data: this.getWeightData(),
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.weightChart.setOption(option)
    },
    updateBodyFatChart() {
      const option = {
        title: {
          text: '体脂率变化趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.getDateLabels()
        },
        yAxis: {
          type: 'value',
          name: '体脂率 (%)'
        },
        series: [{
          data: this.getBodyFatData(),
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#67C23A'
          }
        }]
      }
      this.bodyFatChart.setOption(option)
    },
    getDateLabels() {
      if (this.trendPeriod === 'week') {
        return ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      } else if (this.trendPeriod === 'month') {
        return ['第1周', '第2周', '第3周', '第4周']
      } else {
        return ['第1月', '第2月', '第3月']
      }
    },
    getWeightData() {
      if (this.trendPeriod === 'week') {
        return [65.5, 65.3, 65.0, 64.8, 64.5, 64.3, 64.0]
      } else if (this.trendPeriod === 'month') {
        return [66.0, 65.5, 65.0, 64.5]
      } else {
        return [67.0, 65.5, 64.5]
      }
    },
    getBodyFatData() {
      if (this.trendPeriod === 'week') {
        return [20.5, 20.4, 20.2, 20.0, 19.8, 19.6, 19.5]
      } else if (this.trendPeriod === 'month') {
        return [21.0, 20.5, 20.0, 19.5]
      } else {
        return [21.5, 20.5, 19.5]
      }
    }
  }
}
</script>

<style scoped>
.body-assessment-container {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.data-tabs {
  margin-top: 20px;
}

.data-card {
  margin-bottom: 20px;
}

.data-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.data-label {
  font-size: 14px;
  color: #606266;
}

.data-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.trend-controls {
  margin-bottom: 20px;
}

.chart-container {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.chart {
  flex: 1;
  height: 300px;
  background: #f9f9f9;
  border-radius: 4px;
  padding: 10px;
}

@media (max-width: 768px) {
  .chart-container {
    flex-direction: column;
  }
  
  .chart {
    height: 250px;
  }
}
</style>