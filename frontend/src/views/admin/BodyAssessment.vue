<template>
  <div class="body-assessment-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>体测数据管理</span>
        <el-button type="primary" @click="handleAdd">新增体测记录</el-button>
      </div>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div ref="bmiChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div ref="bodyCompositionChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div ref="circumferenceChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div ref="healthIndexChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-table :data="bodyAssessmentList" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="assessmentDate" label="体测日期" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.assessmentDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="height" label="身高" width="100">
          <template slot-scope="scope">
            {{ scope.row.height }} cm
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="体重" width="100">
          <template slot-scope="scope">
            {{ scope.row.weight }} kg
          </template>
        </el-table-column>
        <el-table-column prop="bmi" label="BMI" width="100">
          <template slot-scope="scope">
            <el-tag :type="getBMIType(scope.row.bmi)">
              {{ scope.row.bmi }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bodyFatRate" label="体脂率" width="100">
          <template slot-scope="scope">
            {{ scope.row.bodyFatRate }}%
          </template>
        </el-table-column>
        <el-table-column prop="muscleMass" label="肌肉量" width="100">
          <template slot-scope="scope">
            {{ scope.row.muscleMass }} kg
          </template>
        </el-table-column>
        <el-table-column prop="coachId" label="教练ID" width="100"></el-table-column>
        <el-table-column label="操作" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px">
      <el-form :model="bodyAssessmentForm" :rules="rules" ref="bodyAssessmentForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="bodyAssessmentForm.userId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体测日期" prop="assessmentDate">
              <el-date-picker v-model="bodyAssessmentForm.assessmentDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高" prop="height">
              <el-input-number v-model="bodyAssessmentForm.height" :min="50" :max="250" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重" prop="weight">
              <el-input-number v-model="bodyAssessmentForm.weight" :min="20" :max="200" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体脂率" prop="bodyFatRate">
              <el-input-number v-model="bodyAssessmentForm.bodyFatRate" :min="0" :max="50" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="肌肉量" prop="muscleMass">
              <el-input-number v-model="bodyAssessmentForm.muscleMass" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="骨密度" prop="boneDensity">
              <el-input-number v-model="bodyAssessmentForm.boneDensity" :min="0" :max="10" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基础代谢率" prop="basalMetabolicRate">
              <el-input-number v-model="bodyAssessmentForm.basalMetabolicRate" :min="1000" :max="3000" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="内脏脂肪等级" prop="visceralFatLevel">
              <el-input-number v-model="bodyAssessmentForm.visceralFatLevel" :min="1" :max="30" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身体水分率" prop="bodyWaterRate">
              <el-input-number v-model="bodyAssessmentForm.bodyWaterRate" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="蛋白质率" prop="proteinRate">
              <el-input-number v-model="bodyAssessmentForm.proteinRate" :min="0" :max="30" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="骨骼肌量" prop="skeletalMuscleMass">
              <el-input-number v-model="bodyAssessmentForm.skeletalMuscleMass" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="胸围" prop="chestCircumference">
              <el-input-number v-model="bodyAssessmentForm.chestCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="腰围" prop="waistCircumference">
              <el-input-number v-model="bodyAssessmentForm.waistCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="臀围" prop="hipCircumference">
              <el-input-number v-model="bodyAssessmentForm.hipCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="教练ID" prop="coachId">
          <el-input v-model="bodyAssessmentForm.coachId"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="体测数据详情" :visible.sync="viewDialogVisible" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ viewData.userId }}</el-descriptions-item>
        <el-descriptions-item label="体测日期">{{ formatDate(viewData.assessmentDate) }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ viewData.height }} cm</el-descriptions-item>
        <el-descriptions-item label="体重">{{ viewData.weight }} kg</el-descriptions-item>
        <el-descriptions-item label="BMI">
          <el-tag :type="getBMIType(viewData.bmi)">{{ viewData.bmi }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="体脂率">{{ viewData.bodyFatRate }}%</el-descriptions-item>
        <el-descriptions-item label="肌肉量">{{ viewData.muscleMass }} kg</el-descriptions-item>
        <el-descriptions-item label="骨密度">{{ viewData.boneDensity }}</el-descriptions-item>
        <el-descriptions-item label="基础代谢率">{{ viewData.basalMetabolicRate }} kcal</el-descriptions-item>
        <el-descriptions-item label="内脏脂肪等级">{{ viewData.visceralFatLevel }}</el-descriptions-item>
        <el-descriptions-item label="身体水分率">{{ viewData.bodyWaterRate }}%</el-descriptions-item>
        <el-descriptions-item label="蛋白质率">{{ viewData.proteinRate }}%</el-descriptions-item>
        <el-descriptions-item label="骨骼肌量">{{ viewData.skeletalMuscleMass }} kg</el-descriptions-item>
        <el-descriptions-item label="胸围">{{ viewData.chestCircumference }} cm</el-descriptions-item>
        <el-descriptions-item label="腰围">{{ viewData.waistCircumference }} cm</el-descriptions-item>
        <el-descriptions-item label="臀围">{{ viewData.hipCircumference }} cm</el-descriptions-item>
        <el-descriptions-item label="教练ID">{{ viewData.coachId }}</el-descriptions-item>
        <el-descriptions-item label="评估结果" :span="2">{{ viewData.assessmentResult }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getBodyAssessmentList, saveBodyAssessment, deleteBodyAssessment } from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'BodyAssessment',
  data() {
    return {
      bodyAssessmentList: [],
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '新增体测记录',
      bodyAssessmentForm: {
        id: null,
        userId: null,
        assessmentDate: new Date(),
        height: null,
        weight: null,
        bodyFatRate: null,
        muscleMass: null,
        boneDensity: null,
        basalMetabolicRate: null,
        bmi: null,
        visceralFatLevel: null,
        bodyWaterRate: null,
        proteinRate: null,
        skeletalMuscleMass: null,
        chestCircumference: null,
        waistCircumference: null,
        hipCircumference: null,
        coachId: null
      },
      viewData: {},
      rules: {
        userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
        assessmentDate: [{ required: true, message: '请选择体测日期', trigger: 'change' }],
        height: [{ required: true, message: '请输入身高', trigger: 'blur' }],
        weight: [{ required: true, message: '请输入体重', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadBodyAssessmentList()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  watch: {
    bodyAssessmentList: {
      handler() {
        this.$nextTick(() => {
          this.updateCharts()
        })
      },
      deep: true
    }
  },
  beforeDestroy() {
    if (this.bmiChart) {
      this.bmiChart.dispose()
    }
    if (this.bodyCompositionChart) {
      this.bodyCompositionChart.dispose()
    }
    if (this.circumferenceChart) {
      this.circumferenceChart.dispose()
    }
    if (this.healthIndexChart) {
      this.healthIndexChart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initCharts() {
      this.bmiChart = echarts.init(this.$refs.bmiChart)
      this.bodyCompositionChart = echarts.init(this.$refs.bodyCompositionChart)
      this.circumferenceChart = echarts.init(this.$refs.circumferenceChart)
      this.healthIndexChart = echarts.init(this.$refs.healthIndexChart)
      
      this.updateCharts()
      
      window.addEventListener('resize', this.handleResize)
    },
    updateCharts() {
      this.updateBMIChart()
      this.updateBodyCompositionChart()
      this.updateCircumferenceChart()
      this.updateHealthIndexChart()
    },
    updateBMIChart() {
      if (!this.bmiChart) return
      
      const bmiGroups = {
        '偏瘦': 0,
        '正常': 0,
        '超重': 0,
        '肥胖': 0
      }
      
      this.bodyAssessmentList.forEach(item => {
        if (item.bmi) {
          if (item.bmi < 18.5) {
            bmiGroups['偏瘦']++
          } else if (item.bmi < 24) {
            bmiGroups['正常']++
          } else if (item.bmi < 28) {
            bmiGroups['超重']++
          } else {
            bmiGroups['肥胖']++
          }
        }
      })
      
      const option = {
        title: {
          text: 'BMI分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'BMI',
            type: 'pie',
            radius: '50%',
            data: [
              { value: bmiGroups['偏瘦'], name: '偏瘦', itemStyle: { color: '#F56C6C' } },
              { value: bmiGroups['正常'], name: '正常', itemStyle: { color: '#67C23A' } },
              { value: bmiGroups['超重'], name: '超重', itemStyle: { color: '#E6A23C' } },
              { value: bmiGroups['肥胖'], name: '肥胖', itemStyle: { color: '#909399' } }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      
      this.bmiChart.setOption(option)
    },
    updateBodyCompositionChart() {
      if (!this.bodyCompositionChart) return
      
      const avgBodyFat = this.bodyAssessmentList.reduce((sum, item) => sum + (item.bodyFatRate || 0), 0) / this.bodyAssessmentList.length || 0
      const avgMuscleMass = this.bodyAssessmentList.reduce((sum, item) => sum + (item.muscleMass || 0), 0) / this.bodyAssessmentList.length || 0
      const avgBodyWater = this.bodyAssessmentList.reduce((sum, item) => sum + (item.bodyWaterRate || 0), 0) / this.bodyAssessmentList.length || 0
      const avgProtein = this.bodyAssessmentList.reduce((sum, item) => sum + (item.proteinRate || 0), 0) / this.bodyAssessmentList.length || 0
      
      const option = {
        title: {
          text: '平均身体成分',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['体脂率', '肌肉量', '身体水分率', '蛋白质率']
        },
        yAxis: {
          type: 'value',
          name: '数值'
        },
        series: [
          {
            name: '平均值',
            type: 'bar',
            data: [avgBodyFat.toFixed(1), avgMuscleMass.toFixed(1), avgBodyWater.toFixed(1), avgProtein.toFixed(1)],
            itemStyle: {
              color: function(params) {
                const colors = ['#F56C6C', '#409EFF', '#67C23A', '#E6A23C']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      
      this.bodyCompositionChart.setOption(option)
    },
    updateCircumferenceChart() {
      if (!this.circumferenceChart) return
      
      const avgChest = this.bodyAssessmentList.reduce((sum, item) => sum + (item.chestCircumference || 0), 0) / this.bodyAssessmentList.length || 0
      const avgWaist = this.bodyAssessmentList.reduce((sum, item) => sum + (item.waistCircumference || 0), 0) / this.bodyAssessmentList.length || 0
      const avgHip = this.bodyAssessmentList.reduce((sum, item) => sum + (item.hipCircumference || 0), 0) / this.bodyAssessmentList.length || 0
      
      const option = {
        title: {
          text: '平均围度数据',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['胸围', '腰围', '臀围']
        },
        yAxis: {
          type: 'value',
          name: 'cm'
        },
        series: [
          {
            name: '平均围度',
            type: 'bar',
            data: [avgChest.toFixed(1), avgWaist.toFixed(1), avgHip.toFixed(1)],
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.circumferenceChart.setOption(option)
    },
    updateHealthIndexChart() {
      if (!this.healthIndexChart) return
      
      const avgBoneDensity = this.bodyAssessmentList.reduce((sum, item) => sum + (item.boneDensity || 0), 0) / this.bodyAssessmentList.length || 0
      const avgBasalMetabolicRate = this.bodyAssessmentList.reduce((sum, item) => sum + (item.basalMetabolicRate || 0), 0) / this.bodyAssessmentList.length || 0
      const avgVisceralFat = this.bodyAssessmentList.reduce((sum, item) => sum + (item.visceralFatLevel || 0), 0) / this.bodyAssessmentList.length || 0
      const avgSkeletalMuscle = this.bodyAssessmentList.reduce((sum, item) => sum + (item.skeletalMuscleMass || 0), 0) / this.bodyAssessmentList.length || 0
      
      const option = {
        title: {
          text: '健康指标统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['骨密度', '基础代谢率', '内脏脂肪等级', '骨骼肌量']
        },
        yAxis: {
          type: 'value',
          name: '数值'
        },
        series: [
          {
            name: '平均值',
            type: 'bar',
            data: [avgBoneDensity.toFixed(2), avgBasalMetabolicRate.toFixed(0), avgVisceralFat.toFixed(1), avgSkeletalMuscle.toFixed(1)],
            itemStyle: {
              color: function(params) {
                const colors = ['#67C23A', '#409EFF', '#E6A23C', '#909399']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      
      this.healthIndexChart.setOption(option)
    },
    handleResize() {
      if (this.bmiChart) {
        this.bmiChart.resize()
      }
      if (this.bodyCompositionChart) {
        this.bodyCompositionChart.resize()
      }
      if (this.circumferenceChart) {
        this.circumferenceChart.resize()
      }
      if (this.healthIndexChart) {
        this.healthIndexChart.resize()
      }
    },
    loadBodyAssessmentList() {
      getBodyAssessmentList().then(res => {
        this.bodyAssessmentList = res.data
      })
    },
    handleAdd() {
      this.dialogTitle = '新增体测记录'
      this.bodyAssessmentForm = {
        id: null,
        userId: null,
        assessmentDate: new Date(),
        height: null,
        weight: null,
        bodyFatRate: null,
        muscleMass: null,
        boneDensity: null,
        basalMetabolicRate: null,
        bmi: null,
        visceralFatLevel: null,
        bodyWaterRate: null,
        proteinRate: null,
        skeletalMuscleMass: null,
        chestCircumference: null,
        waistCircumference: null,
        hipCircumference: null,
        coachId: null
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑体测记录'
      this.bodyAssessmentForm = { ...row, assessmentDate: new Date(row.assessmentDate) }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },
    handleSave() {
      this.$refs.bodyAssessmentForm.validate((valid) => {
        if (valid) {
          saveBodyAssessment(this.bodyAssessmentForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadBodyAssessmentList()
          }).catch(error => {
            console.error('保存失败:', error)
            this.$message.error(error.response?.data?.message || '保存失败，请稍后重试')
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteBodyAssessment(id).then(() => {
          this.$message.success('删除成功')
          this.loadBodyAssessmentList()
        })
      })
    },
    getBMIType(bmi) {
      if (!bmi) return 'info'
      if (bmi < 18.5) return 'warning'
      if (bmi < 24) return 'success'
      if (bmi < 28) return 'warning'
      return 'danger'
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.body-assessment-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
