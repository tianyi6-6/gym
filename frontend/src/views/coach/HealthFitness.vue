<template>
  <div class="health-fitness">
    <div class="page-header">
      <h2>健康体能状况</h2>
      <el-button type="primary" @click="showEditDialog">编辑数据</el-button>
    </div>

    <div class="health-stats">
      <div class="stat-card">
        <div class="stat-icon" style="background: #409EFF;">
          <i class="el-icon-s-custom"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">体重</div>
          <div class="stat-value">{{ healthData.weight || 0 }} kg</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #67C23A;">
          <i class="el-icon-s-data"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">体脂率</div>
          <div class="stat-value">{{ healthData.bodyFatRate || 0 }}%</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #E6A23C;">
          <i class="el-icon-s-flag"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">心率</div>
          <div class="stat-value">{{ healthData.heartRate || 0 }} bpm</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #F56C6C;">
          <i class="el-icon-s-opportunity"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">训练时长</div>
          <div class="stat-value">{{ healthData.trainingHours || 0 }} 小时</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #909399;">
          <i class="el-icon-s-marketing"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">卡路里消耗</div>
          <div class="stat-value">{{ healthData.caloriesBurned || 0 }} kcal</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #606266;">
          <i class="el-icon-s-check"></i>
        </div>
        <div class="stat-content">
          <div class="stat-label">BMI</div>
          <div class="stat-value">{{ healthData.bmi || 0 }}</div>
        </div>
      </div>
    </div>

    <div class="charts-container">
      <div class="chart-wrapper">
        <div class="chart-title">综合体能雷达图</div>
        <div ref="radarChart" class="chart"></div>
      </div>
      <div class="chart-wrapper">
        <div class="chart-title">心率监测</div>
        <div ref="heartRateChart" class="chart"></div>
      </div>
      <div class="chart-wrapper">
        <div class="chart-title">体脂率监测</div>
        <div ref="bodyFatChart" class="chart"></div>
      </div>
    </div>

    <el-dialog title="编辑健康数据" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="体重(kg)" prop="weight">
          <el-input-number v-model="form.weight" :min="30" :max="200" :precision="1"></el-input-number>
        </el-form-item>
        <el-form-item label="体脂率(%)" prop="bodyFatRate">
          <el-input-number v-model="form.bodyFatRate" :min="5" :max="40" :precision="1"></el-input-number>
        </el-form-item>
        <el-form-item label="静息心率(bpm)" prop="heartRate">
          <el-input-number v-model="form.heartRate" :min="40" :max="120"></el-input-number>
        </el-form-item>
        <el-form-item label="训练时长(小时)" prop="trainingHours">
          <el-input-number v-model="form.trainingHours" :min="0" :max="10000" :precision="1"></el-input-number>
        </el-form-item>
        <el-form-item label="卡路里消耗(kcal)" prop="caloriesBurned">
          <el-input-number v-model="form.caloriesBurned" :min="0" :max="1000000"></el-input-number>
        </el-form-item>
        <el-form-item label="肌肉力量" prop="muscleStrength">
          <el-input-number v-model="form.muscleStrength" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="耐力" prop="endurance">
          <el-input-number v-model="form.endurance" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="柔韧性" prop="flexibility">
          <el-input-number v-model="form.flexibility" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="爆发力" prop="explosivePower">
          <el-input-number v-model="form.explosivePower" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="平衡能力" prop="balance">
          <el-input-number v-model="form.balance" :min="0" :max="100"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'HealthFitness',
  data() {
    return {
      healthData: {},
      loading: false,
      dialogVisible: false,
      form: {
        id: null,
        coachId: null,
        weight: 70,
        bodyFatRate: 15,
        heartRate: 70,
        trainingHours: 100,
        caloriesBurned: 50000,
        muscleStrength: 80,
        endurance: 75,
        flexibility: 70,
        explosivePower: 85,
        balance: 75
      },
      rules: {
        weight: [{ required: true, message: '请输入体重', trigger: 'blur' }],
        bodyFatRate: [{ required: true, message: '请输入体脂率', trigger: 'blur' }],
        heartRate: [{ required: true, message: '请输入心率', trigger: 'blur' }],
        trainingHours: [{ required: true, message: '请输入训练时长', trigger: 'blur' }],
        caloriesBurned: [{ required: true, message: '请输入卡路里消耗', trigger: 'blur' }]
      },
      radarChart: null,
      heartRateChart: null,
      bodyFatChart: null
    }
  },
  computed: {
    ...mapState(['userInfo', 'coachData']),
    bmi() {
      if (this.form.weight && this.form.height) {
        const heightInMeters = this.form.height / 100
        return (this.form.weight / (heightInMeters * heightInMeters)).toFixed(1)
      }
      return 0
    }
  },
  created() {
    this.loadData()
    
    this.$watch(
      () => this.coachData.healthFitness,
      (newVal) => {
        if (newVal) {
          this.healthData = newVal
          this.form = { ...newVal }
          this.updateCharts()
        }
      },
      { immediate: true }
    )
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    if (this.radarChart) {
      this.radarChart.dispose()
    }
    if (this.heartRateChart) {
      this.heartRateChart.dispose()
    }
    if (this.bodyFatChart) {
      this.bodyFatChart.dispose()
    }
  },
  methods: {
    loadData() {
      if (!this.userInfo.id) return
      
      this.loading = true
      this.$axios.get(`/coach-health-fitness/coach/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200 && res.data && res.data.length > 0) {
            this.healthData = res.data[0]
            this.form = { ...this.healthData }
            this.$store.dispatch('updateCoachHealthFitness', this.healthData)
          } else {
            this.healthData = {}
          }
        })
        .catch(error => {
          console.error('获取健康数据失败:', error)
          this.$message.error('获取健康数据失败')
        })
        .finally(() => {
          this.loading = false
          this.updateCharts()
        })
    },
    showEditDialog() {
      this.form = {
        id: this.healthData.id || null,
        coachId: this.userInfo.id,
        weight: this.healthData.weight || 70,
        bodyFatRate: this.healthData.bodyFatRate || 15,
        heartRate: this.healthData.heartRate || 70,
        trainingHours: this.healthData.trainingHours || 100,
        caloriesBurned: this.healthData.caloriesBurned || 50000,
        muscleStrength: this.healthData.muscleStrength || 80,
        endurance: this.healthData.endurance || 75,
        flexibility: this.healthData.flexibility || 70,
        explosivePower: this.healthData.explosivePower || 85,
        balance: this.healthData.balance || 75
      }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios.post('/coach-health-fitness/save', this.form)
            .then(res => {
              if (res.code === 200) {
                this.$message.success('保存成功')
                this.dialogVisible = false
                this.healthData = res.data
                this.$store.dispatch('updateHealthFitness', res.data)
              } else {
                this.$message.error(res.message || '保存失败')
              }
            })
            .catch(error => {
              console.error('保存失败:', error)
              this.$message.error('保存失败')
            })
        }
      })
    },
    initCharts() {
      this.radarChart = echarts.init(this.$refs.radarChart)
      this.heartRateChart = echarts.init(this.$refs.heartRateChart)
      this.bodyFatChart = echarts.init(this.$refs.bodyFatChart)
      
      this.updateCharts()
      
      window.addEventListener('resize', this.handleResize)
    },
    updateCharts() {
      if (!this.radarChart || !this.heartRateChart || !this.bodyFatChart) return
      
      const data = this.healthData || {}
      
      const radarOption = {
        tooltip: {
          trigger: 'item'
        },
        radar: {
          indicator: [
            { name: '肌肉力量', max: 100 },
            { name: '耐力', max: 100 },
            { name: '柔韧性', max: 100 },
            { name: '爆发力', max: 100 },
            { name: '平衡能力', max: 100 }
          ],
          radius: '65%',
          axisName: {
            color: '#666'
          }
        },
        series: [{
          name: '综合体能',
          type: 'radar',
          data: [{
            value: [
              data.muscleStrength || 80,
              data.endurance || 75,
              data.flexibility || 70,
              data.explosivePower || 85,
              data.balance || 75
            ],
            name: '当前体能',
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            },
            lineStyle: {
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF'
            }
          }]
        }]
      }
      
      const heartRateOption = {
        series: [{
          type: 'gauge',
          startAngle: 180,
          endAngle: 0,
          min: 40,
          max: 120,
          splitNumber: 8,
          axisLine: {
            lineStyle: {
              width: 6,
              color: [
                [0.25, '#FF6E76'],
                [0.5, '#FDDD60'],
                [0.75, '#58D9F9'],
                [1, '#7CFFB2']
              ]
            }
          },
          pointer: {
            icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
            length: '12%',
            width: 20,
            offsetCenter: [0, '-60%'],
            itemStyle: {
              color: 'auto'
            }
          },
          axisTick: {
            length: 12,
            lineStyle: {
              color: 'auto',
              width: 2
            }
          },
          splitLine: {
            length: 20,
            lineStyle: {
              color: 'auto',
              width: 5
            }
          },
          axisLabel: {
            color: '#464646',
            fontSize: 20,
            distance: -60
          },
          title: {
            offsetCenter: [0, '-20%'],
            fontSize: 20
          },
          detail: {
            fontSize: 30,
            offsetCenter: [0, '0%'],
            valueAnimation: true,
            formatter: function (value) {
              return Math.round(value) + ' bpm'
            },
            color: 'auto'
          },
          data: [{
            value: data.heartRate || 70,
            name: '心率'
          }]
        }]
      }
      
      const bodyFatOption = {
        series: [{
          type: 'gauge',
          startAngle: 180,
          endAngle: 0,
          min: 5,
          max: 40,
          splitNumber: 7,
          axisLine: {
            lineStyle: {
              width: 6,
              color: [
                [0.25, '#7CFFB2'],
                [0.5, '#58D9F9'],
                [0.75, '#FDDD60'],
                [1, '#FF6E76']
              ]
            }
          },
          pointer: {
            icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
            length: '12%',
            width: 20,
            offsetCenter: [0, '-60%'],
            itemStyle: {
              color: 'auto'
            }
          },
          axisTick: {
            length: 12,
            lineStyle: {
              color: 'auto',
              width: 2
            }
          },
          splitLine: {
            length: 20,
            lineStyle: {
              color: 'auto',
              width: 5
            }
          },
          axisLabel: {
            color: '#464646',
            fontSize: 20,
            distance: -60
          },
          title: {
            offsetCenter: [0, '-20%'],
            fontSize: 20
          },
          detail: {
            fontSize: 30,
            offsetCenter: [0, '0%'],
            valueAnimation: true,
            formatter: function (value) {
              return Math.round(value) + '%'
            },
            color: 'auto'
          },
          data: [{
            value: data.bodyFatRate || 15,
            name: '体脂率'
          }]
        }]
      }
      
      this.radarChart.setOption(radarOption)
      this.heartRateChart.setOption(heartRateOption)
      this.bodyFatChart.setOption(bodyFatOption)
    },
    handleResize() {
      if (this.radarChart) {
        this.radarChart.resize()
      }
      if (this.heartRateChart) {
        this.heartRateChart.resize()
      }
      if (this.bodyFatChart) {
        this.bodyFatChart.resize()
      }
    }
  }
}
</script>

<style scoped>
.health-fitness {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.health-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.chart-wrapper {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 15px;
  text-align: center;
}

.chart {
  width: 100%;
  height: 350px;
}
</style>
