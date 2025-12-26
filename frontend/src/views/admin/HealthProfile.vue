<template>
  <div class="health-profile-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>健康档案管理</span>
        <div>
          <el-select v-model="riskLevelFilter" placeholder="风险等级" clearable @change="loadHealthProfileList" style="margin-right: 10px; width: 150px">
            <el-option label="低风险" :value="1"></el-option>
            <el-option label="中风险" :value="2"></el-option>
            <el-option label="高风险" :value="3"></el-option>
          </el-select>
          <el-button type="primary" @click="handleAdd">新增健康档案</el-button>
        </div>
      </div>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div ref="riskLevelChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div ref="genderChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div ref="ageChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div ref="lifestyleChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-table :data="healthProfileList" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template slot-scope="scope">
            {{ scope.row.gender === 1 ? '男' : '女' }}
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
        <el-table-column prop="riskLevel" label="风险等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form :model="healthProfileForm" :rules="rules" ref="healthProfileForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="healthProfileForm.userId" :disabled="healthProfileForm.id != null"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="healthProfileForm.age" :min="1" :max="120" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="healthProfileForm.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职业" prop="occupation">
              <el-input v-model="healthProfileForm.occupation"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高" prop="height">
              <el-input-number v-model="healthProfileForm.height" :min="50" :max="250" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重" prop="weight">
              <el-input-number v-model="healthProfileForm.weight" :min="20" :max="200" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="既往病史" prop="medicalHistory">
          <el-input v-model="healthProfileForm.medicalHistory" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="过敏史" prop="allergyHistory">
          <el-input v-model="healthProfileForm.allergyHistory" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="手术史" prop="surgeryHistory">
          <el-input v-model="healthProfileForm.surgeryHistory" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="运动经历" prop="exerciseHistory">
          <el-input v-model="healthProfileForm.exerciseHistory" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="运动习惯" prop="exerciseHabits">
          <el-input v-model="healthProfileForm.exerciseHabits" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="运动偏好" prop="exercisePreference">
          <el-input v-model="healthProfileForm.exercisePreference" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="作息时间" prop="sleepHabits">
              <el-input v-model="healthProfileForm.sleepHabits"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="饮食习惯" prop="dietHabits">
              <el-input v-model="healthProfileForm.dietHabits"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="吸烟状况" prop="smokingStatus">
              <el-select v-model="healthProfileForm.smokingStatus" style="width: 100%">
                <el-option label="不吸烟" :value="0"></el-option>
                <el-option label="偶尔吸烟" :value="1"></el-option>
                <el-option label="经常吸烟" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="饮酒状况" prop="drinkingStatus">
              <el-select v-model="healthProfileForm.drinkingStatus" style="width: 100%">
                <el-option label="不饮酒" :value="0"></el-option>
                <el-option label="偶尔饮酒" :value="1"></el-option>
                <el-option label="经常饮酒" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="健康档案详情" :visible.sync="viewDialogVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ viewData.userId }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ viewData.age }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ viewData.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ viewData.occupation }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ viewData.height }} cm</el-descriptions-item>
        <el-descriptions-item label="体重">{{ viewData.weight }} kg</el-descriptions-item>
        <el-descriptions-item label="既往病史" :span="2">{{ viewData.medicalHistory }}</el-descriptions-item>
        <el-descriptions-item label="过敏史" :span="2">{{ viewData.allergyHistory }}</el-descriptions-item>
        <el-descriptions-item label="手术史" :span="2">{{ viewData.surgeryHistory }}</el-descriptions-item>
        <el-descriptions-item label="运动经历" :span="2">{{ viewData.exerciseHistory }}</el-descriptions-item>
        <el-descriptions-item label="运动习惯" :span="2">{{ viewData.exerciseHabits }}</el-descriptions-item>
        <el-descriptions-item label="运动偏好" :span="2">{{ viewData.exercisePreference }}</el-descriptions-item>
        <el-descriptions-item label="作息时间">{{ viewData.sleepHabits }}</el-descriptions-item>
        <el-descriptions-item label="饮食习惯">{{ viewData.dietHabits }}</el-descriptions-item>
        <el-descriptions-item label="吸烟状况">{{ getSmokingStatusText(viewData.smokingStatus) }}</el-descriptions-item>
        <el-descriptions-item label="饮酒状况">{{ getDrinkingStatusText(viewData.drinkingStatus) }}</el-descriptions-item>
        <el-descriptions-item label="风险等级" :span="2">
          <el-tag :type="getRiskLevelType(viewData.riskLevel)">
            {{ getRiskLevelText(viewData.riskLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险因素" :span="2">{{ viewData.riskFactors }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getHealthProfileList, getHealthProfileByRiskLevel, saveHealthProfile, deleteHealthProfile } from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'HealthProfile',
  data() {
    return {
      healthProfileList: [],
      riskLevelFilter: null,
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '新增健康档案',
      healthProfileForm: {
        id: null,
        userId: null,
        age: null,
        gender: 1,
        height: null,
        weight: null,
        occupation: '',
        medicalHistory: '',
        allergyHistory: '',
        surgeryHistory: '',
        exerciseHistory: '',
        exerciseHabits: '',
        exercisePreference: '',
        sleepHabits: '',
        dietHabits: '',
        smokingStatus: 0,
        drinkingStatus: 0
      },
      viewData: {},
      rules: {
        userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadHealthProfileList()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  watch: {
    healthProfileList: {
      handler() {
        this.$nextTick(() => {
          this.updateCharts()
        })
      },
      deep: true
    }
  },
  beforeDestroy() {
    if (this.riskLevelChart) {
      this.riskLevelChart.dispose()
    }
    if (this.genderChart) {
      this.genderChart.dispose()
    }
    if (this.ageChart) {
      this.ageChart.dispose()
    }
    if (this.lifestyleChart) {
      this.lifestyleChart.dispose()
    }
  },
  methods: {
    initCharts() {
      this.riskLevelChart = echarts.init(this.$refs.riskLevelChart)
      this.genderChart = echarts.init(this.$refs.genderChart)
      this.ageChart = echarts.init(this.$refs.ageChart)
      this.lifestyleChart = echarts.init(this.$refs.lifestyleChart)
      
      this.updateCharts()
      
      window.addEventListener('resize', this.handleResize)
    },
    updateCharts() {
      this.updateRiskLevelChart()
      this.updateGenderChart()
      this.updateAgeChart()
      this.updateLifestyleChart()
    },
    updateRiskLevelChart() {
      if (!this.riskLevelChart) return
      
      const riskLevelData = [
        { value: this.healthProfileList.filter(item => item.riskLevel === 1).length, name: '低风险' },
        { value: this.healthProfileList.filter(item => item.riskLevel === 2).length, name: '中风险' },
        { value: this.healthProfileList.filter(item => item.riskLevel === 3).length, name: '高风险' }
      ]
      
      const option = {
        title: {
          text: '风险等级分布',
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
            name: '风险等级',
            type: 'pie',
            radius: '50%',
            data: riskLevelData,
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
      
      this.riskLevelChart.setOption(option)
    },
    updateGenderChart() {
      if (!this.genderChart) return
      
      const genderData = [
        { value: this.healthProfileList.filter(item => item.gender === 1).length, name: '男' },
        { value: this.healthProfileList.filter(item => item.gender === 2).length, name: '女' }
      ]
      
      const option = {
        title: {
          text: '性别分布',
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
            name: '性别',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: genderData
          }
        ]
      }
      
      this.genderChart.setOption(option)
    },
    updateAgeChart() {
      if (!this.ageChart) return
      
      const ageGroups = {
        '18-25': 0,
        '26-35': 0,
        '36-45': 0,
        '46-55': 0,
        '56+': 0
      }
      
      this.healthProfileList.forEach(item => {
        if (item.age >= 18 && item.age <= 25) {
          ageGroups['18-25']++
        } else if (item.age >= 26 && item.age <= 35) {
          ageGroups['26-35']++
        } else if (item.age >= 36 && item.age <= 45) {
          ageGroups['36-45']++
        } else if (item.age >= 46 && item.age <= 55) {
          ageGroups['46-55']++
        } else if (item.age >= 56) {
          ageGroups['56+']++
        }
      })
      
      const option = {
        title: {
          text: '年龄分布',
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
          data: Object.keys(ageGroups)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '人数',
            type: 'bar',
            data: Object.values(ageGroups),
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.ageChart.setOption(option)
    },
    updateLifestyleChart() {
      if (!this.lifestyleChart) return
      
      const smokingData = [
        { value: this.healthProfileList.filter(item => item.smokingStatus === 0).length, name: '不吸烟' },
        { value: this.healthProfileList.filter(item => item.smokingStatus === 1).length, name: '偶尔吸烟' },
        { value: this.healthProfileList.filter(item => item.smokingStatus === 2).length, name: '经常吸烟' }
      ]
      
      const drinkingData = [
        { value: this.healthProfileList.filter(item => item.drinkingStatus === 0).length, name: '不饮酒' },
        { value: this.healthProfileList.filter(item => item.drinkingStatus === 1).length, name: '偶尔饮酒' },
        { value: this.healthProfileList.filter(item => item.drinkingStatus === 2).length, name: '经常饮酒' }
      ]
      
      const option = {
        title: {
          text: '生活方式统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['吸烟状况', '饮酒状况'],
          top: 30
        },
        xAxis: {
          type: 'category',
          data: ['不', '偶尔', '经常']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '吸烟状况',
            type: 'bar',
            data: [smokingData[0].value, smokingData[1].value, smokingData[2].value],
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '饮酒状况',
            type: 'bar',
            data: [drinkingData[0].value, drinkingData[1].value, drinkingData[2].value],
            itemStyle: {
              color: '#E6A23C'
            }
          }
        ]
      }
      
      this.lifestyleChart.setOption(option)
    },
    handleResize() {
      if (this.riskLevelChart) {
        this.riskLevelChart.resize()
      }
      if (this.genderChart) {
        this.genderChart.resize()
      }
      if (this.ageChart) {
        this.ageChart.resize()
      }
      if (this.lifestyleChart) {
        this.lifestyleChart.resize()
      }
    },
    loadHealthProfileList() {
      if (this.riskLevelFilter) {
        getHealthProfileByRiskLevel(this.riskLevelFilter).then(res => {
          this.healthProfileList = res.data
        })
      } else {
        getHealthProfileList().then(res => {
          this.healthProfileList = res.data
        })
      }
    },
    handleAdd() {
      this.dialogTitle = '新增健康档案'
      this.healthProfileForm = {
        id: null,
        userId: null,
        age: null,
        gender: 1,
        height: null,
        weight: null,
        occupation: '',
        medicalHistory: '',
        allergyHistory: '',
        surgeryHistory: '',
        exerciseHistory: '',
        exerciseHabits: '',
        exercisePreference: '',
        sleepHabits: '',
        dietHabits: '',
        smokingStatus: 0,
        drinkingStatus: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑健康档案'
      this.healthProfileForm = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },
    handleSave() {
      this.$refs.healthProfileForm.validate((valid) => {
        if (valid) {
          saveHealthProfile(this.healthProfileForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadHealthProfileList()
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
        deleteHealthProfile(id).then(() => {
          this.$message.success('删除成功')
          this.loadHealthProfileList()
        })
      })
    },
    getRiskLevelType(level) {
      const map = { 1: 'success', 2: 'warning', 3: 'danger' }
      return map[level] || 'info'
    },
    getRiskLevelText(level) {
      const map = { 1: '低风险', 2: '中风险', 3: '高风险' }
      return map[level] || '未知'
    },
    getSmokingStatusText(status) {
      const map = { 0: '不吸烟', 1: '偶尔吸烟', 2: '经常吸烟' }
      return map[status] || '未知'
    },
    getDrinkingStatusText(status) {
      const map = { 0: '不饮酒', 1: '偶尔饮酒', 2: '经常饮酒' }
      return map[status] || '未知'
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.health-profile-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
