<template>
  <div class="body-assessment-page">
    <el-card class="body-assessment-card">
      <template slot="header">
        <div class="card-header">
          <span>会员体测数据管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <i class="el-icon-plus"></i> 新增体测记录
          </el-button>
        </div>
      </template>
      
      <!-- 搜索和筛选 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="会员姓名">
            <el-input v-model="filterForm.userName" placeholder="输入会员姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="会员编号">
            <el-input v-model="filterForm.userId" placeholder="输入会员编号" clearable></el-input>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchAssessments">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 会员选择 -->
      <div class="member-selection" v-if="!selectedMember">
        <el-card class="member-card">
          <div slot="header">选择会员</div>
          <el-table
            :data="memberList"
            style="width: 100%"
            @row-click="selectMember"
          >
            <el-table-column prop="id" label="会员编号" width="120"></el-table-column>
            <el-table-column prop="name" label="姓名" width="120"></el-table-column>
            <el-table-column prop="phone" label="电话" width="150"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="membershipLevel" label="会员等级" width="120"></el-table-column>
          </el-table>
        </el-card>
      </div>
      
      <!-- 会员基本信息 -->
      <div class="member-info" v-if="selectedMember">
        <el-card class="info-card">
          <div slot="header">会员基本信息</div>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="会员编号">{{ selectedMember.id }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ selectedMember.name }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ selectedMember.gender }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ selectedMember.age }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ selectedMember.phone }}</el-descriptions-item>
            <el-descriptions-item label="会员等级">{{ selectedMember.membershipLevel }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" size="small" style="margin-top: 10px" @click="handleAdd">
            <i class="el-icon-plus"></i> 新增体测记录
          </el-button>
        </el-card>
      </div>
      
      <!-- 数据可视化 -->
      <el-row :gutter="20" style="margin-bottom: 20px" v-if="selectedMember">
        <el-col :span="12">
          <el-card>
            <div slot="header" class="chart-header">
              <span>BMI趋势</span>
            </div>
            <div ref="bmiChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header" class="chart-header">
              <span>身体成分变化</span>
            </div>
            <div ref="bodyCompositionChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 体测记录列表 -->
      <el-table
        :data="assessments"
        style="width: 100%"
        stripe
        border
        v-if="selectedMember"
      >
        <el-table-column prop="id" label="记录ID" width="100"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column label="体测日期" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.assessmentDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="height" label="身高(cm)" width="100"></el-table-column>
        <el-table-column prop="weight" label="体重(kg)" width="100"></el-table-column>
        <el-table-column prop="bmi" label="BMI" width="100">
          <template slot-scope="scope">
            <el-tag :type="getBMIType(scope.row.bmi)">
              {{ scope.row.bmi }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bodyFatRate" label="体脂率(%)" width="100"></el-table-column>
        <el-table-column prop="muscleMass" label="肌肉量(kg)" width="100"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination" v-if="selectedMember">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px">
      <el-form :model="assessmentForm" :rules="rules" ref="assessmentForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="assessmentForm.userId" placeholder="请输入用户ID"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体测日期" prop="assessmentDate">
              <el-date-picker v-model="assessmentForm.assessmentDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高" prop="height">
              <el-input-number v-model="assessmentForm.height" :min="50" :max="250" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重" prop="weight">
              <el-input-number v-model="assessmentForm.weight" :min="20" :max="200" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体脂率" prop="bodyFatRate">
              <el-input-number v-model="assessmentForm.bodyFatRate" :min="0" :max="50" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="肌肉量" prop="muscleMass">
              <el-input-number v-model="assessmentForm.muscleMass" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="骨密度" prop="boneDensity">
              <el-input-number v-model="assessmentForm.boneDensity" :min="0" :max="10" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基础代谢率" prop="basalMetabolicRate">
              <el-input-number v-model="assessmentForm.basalMetabolicRate" :min="1000" :max="3000" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="内脏脂肪等级" prop="visceralFatLevel">
              <el-input-number v-model="assessmentForm.visceralFatLevel" :min="1" :max="30" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身体水分率" prop="bodyWaterRate">
              <el-input-number v-model="assessmentForm.bodyWaterRate" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="蛋白质率" prop="proteinRate">
              <el-input-number v-model="assessmentForm.proteinRate" :min="0" :max="30" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="骨骼肌量" prop="skeletalMuscleMass">
              <el-input-number v-model="assessmentForm.skeletalMuscleMass" :min="0" :max="100" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="胸围" prop="chestCircumference">
              <el-input-number v-model="assessmentForm.chestCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="腰围" prop="waistCircumference">
              <el-input-number v-model="assessmentForm.waistCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="臀围" prop="hipCircumference">
              <el-input-number v-model="assessmentForm.hipCircumference" :min="50" :max="150" :precision="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="健康目标" prop="healthGoal">
              <el-input
                v-model="assessmentForm.healthGoal"
                type="textarea"
                :rows="4"
                placeholder="请输入健康目标（50-500字）"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="评估建议" prop="assessmentResult">
              <el-input
                v-model="assessmentForm.assessmentResult"
                type="textarea"
                :rows="3"
                placeholder="请输入评估建议"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </span>
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
        <el-descriptions-item label="评估结果" :span="2">{{ viewData.assessmentResult }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="exportData">导出数据</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CoachBodyAssessment',
  data() {
    return {
      assessments: [],
      memberList: [],
      selectedMember: null,
      filterForm: {
        userId: '',
        userName: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '新增体测记录',
      assessmentForm: {
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
        healthGoal: '',
        assessmentResult: '',
        coachId: null
      },
      viewData: {},
      rules: {
        userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
        assessmentDate: [{ required: true, message: '请选择体测日期', trigger: 'change' }],
        height: [{ required: true, message: '请输入身高', trigger: 'blur' }],
        weight: [{ required: true, message: '请输入体重', trigger: 'blur' }],
        bodyFatRate: [{ 
          required: true, 
          message: '请输入体脂率', 
          trigger: 'blur' 
        }, {
          type: 'number',
          min: 0,
          max: 50,
          message: '体脂率应在0-50%之间',
          trigger: 'blur'
        }],
        healthGoal: [{ 
          required: true, 
          message: '请输入健康目标', 
          trigger: 'blur' 
        }, {
          min: 50,
          max: 500,
          message: '健康目标应在50-500字之间',
          trigger: 'blur'
        }]
      },
      bmiChart: null,
      bodyCompositionChart: null
    }
  },
  mounted() {
    this.loadMembers()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    if (this.bmiChart) {
      this.bmiChart.dispose()
    }
    if (this.bodyCompositionChart) {
      this.bodyCompositionChart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initCharts() {
      this.bmiChart = this.$echarts.init(this.$refs.bmiChart)
      this.bodyCompositionChart = this.$echarts.init(this.$refs.bodyCompositionChart)
      this.updateCharts()
      window.addEventListener('resize', this.handleResize)
    },
    updateCharts() {
      this.updateBMIChart()
      this.updateBodyCompositionChart()
    },
    updateBMIChart() {
      if (!this.bmiChart) return
      
      const data = this.assessments
        .filter(item => item.bmi && item.assessmentDate)
        .sort((a, b) => new Date(a.assessmentDate) - new Date(b.assessmentDate))
      
      const dates = data.map(item => this.formatDate(item.assessmentDate, 'short'))
      const bmiValues = data.map(item => item.bmi)
      
      const option = {
        title: {
          text: 'BMI变化趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: {
          type: 'value',
          name: 'BMI'
        },
        series: [
          {
            name: 'BMI',
            type: 'line',
            data: bmiValues,
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.bmiChart.setOption(option)
    },
    updateBodyCompositionChart() {
      if (!this.bodyCompositionChart) return
      
      const data = this.assessments
        .filter(item => item.bodyFatRate && item.muscleMass && item.assessmentDate)
        .sort((a, b) => new Date(a.assessmentDate) - new Date(b.assessmentDate))
      
      const dates = data.map(item => this.formatDate(item.assessmentDate, 'short'))
      const bodyFatValues = data.map(item => item.bodyFatRate)
      const muscleValues = data.map(item => item.muscleMass)
      
      const option = {
        title: {
          text: '身体成分变化',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['体脂率', '肌肉量'],
          top: 30
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: [
          {
            type: 'value',
            name: '体脂率(%)',
            position: 'left'
          },
          {
            type: 'value',
            name: '肌肉量(kg)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '体脂率',
            type: 'line',
            data: bodyFatValues,
            smooth: true,
            itemStyle: {
              color: '#F56C6C'
            }
          },
          {
            name: '肌肉量',
            type: 'line',
            yAxisIndex: 1,
            data: muscleValues,
            smooth: true,
            itemStyle: {
              color: '#67C23A'
            }
          }
        ]
      }
      
      this.bodyCompositionChart.setOption(option)
    },
    handleResize() {
      if (this.bmiChart) {
        this.bmiChart.resize()
      }
      if (this.bodyCompositionChart) {
        this.bodyCompositionChart.resize()
      }
    },
    // 加载会员列表
    loadMembers() {
      // 模拟加载会员数据
      // 实际项目中应该调用API获取数据
      const mockMembers = [
        {
          id: 1,
          name: '张三',
          gender: '男',
          age: 25,
          phone: '13800138000',
          email: 'zhangsan@example.com',
          membershipLevel: '黄金会员'
        },
        {
          id: 2,
          name: '李四',
          gender: '女',
          age: 30,
          phone: '13900139000',
          email: 'lisi@example.com',
          membershipLevel: '白金会员'
        },
        {
          id: 3,
          name: '王五',
          gender: '男',
          age: 28,
          phone: '13700137000',
          email: 'wangwu@example.com',
          membershipLevel: '钻石会员'
        }
      ]
      this.memberList = mockMembers
    },
    
    // 加载体测记录
    loadAssessments() {
      if (!this.selectedMember) return
      
      // 模拟加载数据
      // 实际项目中应该调用API获取数据
      const mockData = [
        {
          id: 1,
          userId: this.selectedMember.id,
          assessmentDate: new Date('2024-11-01'),
          height: 175,
          weight: 70,
          bodyFatRate: 15,
          muscleMass: 45,
          boneDensity: 1.2,
          basalMetabolicRate: 1800,
          bmi: 22.86,
          visceralFatLevel: 5,
          bodyWaterRate: 60,
          proteinRate: 18,
          skeletalMuscleMass: 35,
          chestCircumference: 95,
          waistCircumference: 80,
          hipCircumference: 95,
          healthGoal: '希望在3个月内减少体脂率到12%，增加肌肉量5kg',
          assessmentResult: 'BMI正常;体脂率正常;建议增加力量训练，控制饮食',
          coachId: 1
        },
        {
          id: 2,
          userId: this.selectedMember.id,
          assessmentDate: new Date('2024-12-01'),
          height: 175,
          weight: 68,
          bodyFatRate: 13,
          muscleMass: 46,
          boneDensity: 1.21,
          basalMetabolicRate: 1850,
          bmi: 22.20,
          visceralFatLevel: 4,
          bodyWaterRate: 62,
          proteinRate: 19,
          skeletalMuscleMass: 36,
          chestCircumference: 96,
          waistCircumference: 78,
          hipCircumference: 94,
          healthGoal: '希望在3个月内减少体脂率到12%，增加肌肉量5kg',
          assessmentResult: 'BMI正常;体脂率正常;继续保持训练强度，注意营养补充',
          coachId: 1
        }
      ]
      this.assessments = mockData
      this.pagination.total = mockData.length
      this.updateCharts()
    },
    
    // 选择会员
    selectMember(row) {
      this.selectedMember = row
      this.loadAssessments()
    },
    searchAssessments() {
      // 模拟搜索功能
      this.$message.info('搜索功能已触发')
    },
    resetFilter() {
      this.filterForm = {
        userId: '',
        dateRange: []
      }
    },
    handleAdd() {
      if (!this.selectedMember) {
        this.$message.warning('请先选择会员')
        return
      }
      
      this.dialogTitle = '新增体测记录'
      this.assessmentForm = {
        id: null,
        userId: this.selectedMember.id,
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
        healthGoal: '',
        assessmentResult: '',
        coachId: 1 // 假设当前教练ID为1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑体测记录'
      this.assessmentForm = { ...row, assessmentDate: new Date(row.assessmentDate) }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },
    handleSave() {
      this.$refs.assessmentForm.validate((valid) => {
        if (valid) {
          // 计算BMI
          if (this.assessmentForm.height && this.assessmentForm.weight) {
            const heightInMeters = this.assessmentForm.height / 100
            this.assessmentForm.bmi = parseFloat((this.assessmentForm.weight / (heightInMeters * heightInMeters)).toFixed(2))
          }
          
          // 模拟保存操作
          // 实际项目中应该调用API保存数据
          setTimeout(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadAssessments()
          }, 500)
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除此体测记录吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟删除操作
        this.$message.success('删除成功')
        this.loadAssessments()
      }).catch(() => {
        // 取消删除
      })
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadAssessments()
    },
    handleCurrentChange(current) {
      this.pagination.currentPage = current
      this.loadAssessments()
    },
    exportData() {
      // 模拟导出功能
      this.$message.success('数据导出成功')
    },
    getBMIType(bmi) {
      if (!bmi) return 'info'
      if (bmi < 18.5) return 'warning'
      if (bmi < 24) return 'success'
      if (bmi < 28) return 'warning'
      return 'danger'
    },
    formatDate(date, format = 'full') {
      if (!date) return ''
      const d = new Date(date)
      if (format === 'short') {
        return `${d.getMonth() + 1}/${d.getDate()}`
      }
      return d.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.body-assessment-page {
  padding: 20px;
}

.body-assessment-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-header {
  font-weight: bold;
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.filter-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}
</style>
