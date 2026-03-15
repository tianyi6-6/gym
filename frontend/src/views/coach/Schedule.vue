<template>
  <div class="schedule-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>个人排班表管理</span>
        <div class="header-actions">
          <el-button type="primary" @click="handleAddSchedule">
            <i class="el-icon-plus"></i> 添加排班
          </el-button>
          <el-button @click="handleBatchAdd">
            <i class="el-icon-s-operation"></i> 批量设置
          </el-button>
        </div>
      </div>
      
      <!-- 视图切换 -->
      <div class="view-switch">
        <el-radio-group v-model="currentView" @change="handleViewChange">
          <el-radio-button label="day">日视图</el-radio-button>
          <el-radio-button label="week">周视图</el-radio-button>
          <el-radio-button label="month">月视图</el-radio-button>
        </el-radio-group>
        
        <!-- 日期选择器 -->
        <div class="date-picker">
          <el-date-picker
            v-model="currentDate"
            type="date"
            placeholder="选择日期"
            @change="handleDateChange"
          ></el-date-picker>
          <el-button @click="prevPeriod">
            <i class="el-icon-arrow-left"></i>
          </el-button>
          <el-button @click="nextPeriod">
            <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
      </div>
      
      <!-- 日视图 -->
      <div v-if="currentView === 'day'" class="day-view">
        <div class="time-grid">
          <div class="time-axis">
            <div v-for="hour in 24" :key="hour" class="time-slot">
              {{ hour.toString().padStart(2, '0') }}:00
            </div>
          </div>
          <div class="schedule-grid">
            <div v-for="hour in 24" :key="hour" class="hour-row">
              <div class="hour-cell"></div>
            </div>
          </div>
        </div>
        <div class="day-schedules">
          <el-card v-for="schedule in daySchedules" :key="schedule.id" class="schedule-card">
            <div class="schedule-time">{{ schedule.startTime }} - {{ schedule.endTime }}</div>
            <div class="schedule-type">{{ getScheduleTypeText(schedule.type) }}</div>
            <div class="schedule-status">{{ getScheduleStatusText(schedule.status) }}</div>
            <div class="schedule-desc">{{ schedule.description || '无描述' }}</div>
            <div class="schedule-actions">
              <el-button size="small" type="primary" @click="handleEditSchedule(schedule)">
                编辑
              </el-button>
              <el-button size="small" type="danger" @click="handleDeleteSchedule(schedule.id)">
                删除
              </el-button>
            </div>
          </el-card>
        </div>
      </div>
      
      <!-- 周视图 -->
      <div v-else-if="currentView === 'week'" class="week-view">
        <div class="week-header">
          <div v-for="(day, index) in weekDays" :key="index" class="week-day">
            <div class="day-name">{{ day.name }}</div>
            <div class="day-date">{{ day.date }}</div>
          </div>
        </div>
        <div class="week-grid">
          <div class="time-axis">
            <div v-for="hour in 12" :key="hour" class="time-slot">
              {{ (hour + 7).toString().padStart(2, '0') }}:00
            </div>
          </div>
          <div class="week-columns">
            <div v-for="(day, index) in weekDays" :key="index" class="week-column">
              <div v-for="hour in 12" :key="hour" class="week-cell"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 月视图 -->
      <div v-else-if="currentView === 'month'" class="month-view">
        <div class="month-header">
          <div class="month-title">{{ currentDate.getFullYear() }}年{{ currentDate.getMonth() + 1 }}月</div>
          <div class="weekday-header">
            <div v-for="day in ['日', '一', '二', '三', '四', '五', '六']" :key="day" class="weekday">{{ day }}</div>
          </div>
        </div>
        <div class="month-grid">
          <div v-for="(day, index) in monthDays" :key="index" class="month-day" :class="{ 'other-month': !day.isCurrentMonth }">
            <div class="day-number">{{ day.date }}</div>
            <div class="day-schedule-count" v-if="day.scheduleCount > 0">
              {{ day.scheduleCount }}个排班
            </div>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 添加/编辑排班对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
    >
      <el-form :model="scheduleForm" :rules="scheduleRules" ref="scheduleForm">
        <el-form-item label="日期" prop="scheduleDate">
          <el-date-picker
            v-model="scheduleForm.scheduleDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="scheduleForm.startTime"
            format="HH:mm"
            placeholder="选择开始时间"
            style="width: 100%"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="scheduleForm.endTime"
            format="HH:mm"
            placeholder="选择结束时间"
            style="width: 100%"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="scheduleForm.type" placeholder="选择类型" style="width: 100%">
            <el-option label="课程" value="课程"></el-option>
            <el-option label="咨询" value="咨询"></el-option>
            <el-option label="休息" value="休息"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="scheduleForm.status" placeholder="选择状态" style="width: 100%">
            <el-option label="不可预约" value="0"></el-option>
            <el-option label="可预约" value="1"></el-option>
            <el-option label="已预约" value="2"></el-option>
            <el-option label="已完成" value="3"></el-option>
            <el-option label="已取消" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="scheduleForm.description"
            type="textarea"
            placeholder="请输入描述"
            rows="3"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSchedule">保存</el-button>
      </span>
    </el-dialog>
    
    <!-- 批量设置对话框 -->
    <el-dialog
      title="批量设置排班"
      :visible.sync="batchDialogVisible"
      width="600px"
    >
      <el-form :model="batchForm" :rules="batchRules" ref="batchForm">
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="batchForm.startDate"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="batchForm.endDate"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="重复类型" prop="repeatType">
          <el-select v-model="batchForm.repeatType" placeholder="选择重复类型" style="width: 100%">
            <el-option label="每天" value="daily"></el-option>
            <el-option label="每周" value="weekly"></el-option>
            <el-option label="每月" value="monthly"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="batchForm.startTime"
            format="HH:mm"
            placeholder="选择开始时间"
            style="width: 100%"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="batchForm.endTime"
            format="HH:mm"
            placeholder="选择结束时间"
            style="width: 100%"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="batchForm.type" placeholder="选择类型" style="width: 100%">
            <el-option label="课程" value="课程"></el-option>
            <el-option label="咨询" value="咨询"></el-option>
            <el-option label="休息" value="休息"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="batchForm.status" placeholder="选择状态" style="width: 100%">
            <el-option label="不可预约" value="0"></el-option>
            <el-option label="可预约" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="batchForm.description"
            type="textarea"
            placeholder="请输入描述"
            rows="3"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchSave">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Schedule',
  data() {
    return {
      currentView: 'day',
      currentDate: new Date(),
      schedules: [],
      daySchedules: [],
      weekDays: [],
      monthDays: [],
      dialogVisible: false,
      batchDialogVisible: false,
      dialogTitle: '添加排班',
      currentScheduleId: null,
      scheduleForm: {
        coachId: 0,
        scheduleDate: new Date(),
        startTime: '',
        endTime: '',
        type: '课程',
        status: 1,
        description: ''
      },
      batchForm: {
        startDate: new Date(),
        endDate: new Date(),
        repeatType: 'daily',
        startTime: '',
        endTime: '',
        type: '课程',
        status: 1,
        description: ''
      },
      scheduleRules: {
        scheduleDate: [{ required: true, message: '请选择日期', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'blur' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类型', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
      },
      batchRules: {
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'blur' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'blur' }],
        repeatType: [{ required: true, message: '请选择重复类型', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'blur' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类型', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCoachId()
    this.loadSchedules()
    this.initWeekDays()
    this.initMonthDays()
  },
  methods: {
    loadCoachId() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      this.scheduleForm.coachId = userInfo.id || 1
    },
    loadSchedules() {
      // 模拟数据，实际应该调用API
      this.schedules = [
        {
          id: 1,
          coachId: 1,
          scheduleDate: new Date(),
          startTime: '09:00',
          endTime: '10:00',
          type: '课程',
          status: 1,
          description: '瑜伽课程'
        },
        {
          id: 2,
          coachId: 1,
          scheduleDate: new Date(),
          startTime: '14:00',
          endTime: '15:30',
          type: '课程',
          status: 1,
          description: '力量训练'
        }
      ]
      this.filterDaySchedules()
    },
    filterDaySchedules() {
      this.daySchedules = this.schedules.filter(item => {
        const scheduleDate = new Date(item.scheduleDate)
        return scheduleDate.getDate() === this.currentDate.getDate() &&
               scheduleDate.getMonth() === this.currentDate.getMonth() &&
               scheduleDate.getFullYear() === this.currentDate.getFullYear()
      })
    },
    initWeekDays() {
      const days = []
      const startOfWeek = new Date(this.currentDate)
      startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay())
      
      for (let i = 0; i < 7; i++) {
        const date = new Date(startOfWeek)
        date.setDate(startOfWeek.getDate() + i)
        days.push({
          name: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][i],
          date: date.getDate()
        })
      }
      this.weekDays = days
    },
    initMonthDays() {
      const days = []
      const year = this.currentDate.getFullYear()
      const month = this.currentDate.getMonth()
      
      const firstDay = new Date(year, month, 1)
      const lastDay = new Date(year, month + 1, 0)
      const startDate = new Date(firstDay)
      startDate.setDate(startDate.getDate() - firstDay.getDay())
      
      for (let i = 0; i < 42; i++) {
        const date = new Date(startDate)
        date.setDate(startDate.getDate() + i)
        days.push({
          date: date.getDate(),
          isCurrentMonth: date.getMonth() === month,
          scheduleCount: Math.floor(Math.random() * 3)
        })
      }
      this.monthDays = days
    },
    handleViewChange() {
      if (this.currentView === 'week') {
        this.initWeekDays()
      } else if (this.currentView === 'month') {
        this.initMonthDays()
      } else if (this.currentView === 'day') {
        this.filterDaySchedules()
      }
    },
    handleDateChange() {
      if (this.currentView === 'day') {
        this.filterDaySchedules()
      } else if (this.currentView === 'week') {
        this.initWeekDays()
      } else if (this.currentView === 'month') {
        this.initMonthDays()
      }
    },
    prevPeriod() {
      if (this.currentView === 'day') {
        this.currentDate.setDate(this.currentDate.getDate() - 1)
      } else if (this.currentView === 'week') {
        this.currentDate.setDate(this.currentDate.getDate() - 7)
        this.initWeekDays()
      } else if (this.currentView === 'month') {
        this.currentDate.setMonth(this.currentDate.getMonth() - 1)
        this.initMonthDays()
      }
      this.filterDaySchedules()
    },
    nextPeriod() {
      if (this.currentView === 'day') {
        this.currentDate.setDate(this.currentDate.getDate() + 1)
      } else if (this.currentView === 'week') {
        this.currentDate.setDate(this.currentDate.getDate() + 7)
        this.initWeekDays()
      } else if (this.currentView === 'month') {
        this.currentDate.setMonth(this.currentDate.getMonth() + 1)
        this.initMonthDays()
      }
      this.filterDaySchedules()
    },
    handleAddSchedule() {
      this.dialogTitle = '添加排班'
      this.currentScheduleId = null
      this.scheduleForm = {
        coachId: this.scheduleForm.coachId,
        scheduleDate: new Date(),
        startTime: '',
        endTime: '',
        type: '课程',
        status: 1,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEditSchedule(schedule) {
      this.dialogTitle = '编辑排班'
      this.currentScheduleId = schedule.id
      this.scheduleForm = {
        coachId: schedule.coachId,
        scheduleDate: new Date(schedule.scheduleDate),
        startTime: schedule.startTime,
        endTime: schedule.endTime,
        type: schedule.type,
        status: schedule.status,
        description: schedule.description
      }
      this.dialogVisible = true
    },
    handleDeleteSchedule(id) {
      this.$confirm('确定要删除这个排班吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实际应该调用API删除
        this.schedules = this.schedules.filter(item => item.id !== id)
        this.filterDaySchedules()
        this.$message.success('删除成功')
      }).catch(() => {
        // 取消删除
      })
    },
    handleSaveSchedule() {
      this.$refs.scheduleForm.validate((valid) => {
        if (valid) {
          if (this.currentScheduleId) {
            // 编辑现有排班
            const index = this.schedules.findIndex(item => item.id === this.currentScheduleId)
            if (index !== -1) {
              this.schedules[index] = {
                ...this.schedules[index],
                ...this.scheduleForm
              }
              this.$message.success('更新成功')
            }
          } else {
            // 添加新排班
            const newSchedule = {
              id: Date.now(),
              ...this.scheduleForm
            }
            this.schedules.push(newSchedule)
            this.$message.success('添加成功')
          }
          this.filterDaySchedules()
          this.dialogVisible = false
        }
      })
    },
    handleBatchAdd() {
      this.batchDialogVisible = true
    },
    handleBatchSave() {
      this.$refs.batchForm.validate((valid) => {
        if (valid) {
          // 实际应该调用API批量创建
          this.$message.success('批量设置成功')
          this.batchDialogVisible = false
        }
      })
    },
    getScheduleTypeText(type) {
      const typeMap = {
        '课程': '课程',
        '咨询': '咨询',
        '休息': '休息',
        '其他': '其他'
      }
      return typeMap[type] || type
    },
    getScheduleStatusText(status) {
      const statusMap = {
        0: '不可预约',
        1: '可预约',
        2: '已预约',
        3: '已完成',
        4: '已取消'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.schedule-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.view-switch {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0;
}

.date-picker {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 日视图 */
.day-view {
  margin-top: 20px;
}

.time-grid {
  display: flex;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
}

.time-axis {
  width: 80px;
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.time-slot {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  border-bottom: 1px solid #e4e7ed;
  font-size: 12px;
  color: #909399;
}

.schedule-grid {
  flex: 1;
}

.hour-row {
  height: 60px;
  border-bottom: 1px solid #e4e7ed;
}

.hour-cell {
  height: 100%;
  border-right: 1px solid #e4e7ed;
}

.day-schedules {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.schedule-card {
  border-left: 4px solid #409EFF;
}

.schedule-time {
  font-weight: bold;
  margin-bottom: 5px;
}

.schedule-type {
  color: #67C23A;
  margin-bottom: 5px;
}

.schedule-status {
  color: #E6A23C;
  margin-bottom: 5px;
}

.schedule-desc {
  color: #606266;
  margin-bottom: 10px;
}

.schedule-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

/* 周视图 */
.week-view {
  margin-top: 20px;
}

.week-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 10px;
}

.week-day {
  flex: 1;
  text-align: center;
  padding: 10px 0;
}

.day-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.day-date {
  color: #909399;
  font-size: 12px;
}

.week-grid {
  display: flex;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
}

.week-columns {
  flex: 1;
  display: flex;
}

.week-column {
  flex: 1;
  border-right: 1px solid #e4e7ed;
}

.week-cell {
  height: 60px;
  border-bottom: 1px solid #e4e7ed;
}

/* 月视图 */
.month-view {
  margin-top: 20px;
}

.month-header {
  margin-bottom: 10px;
}

.month-title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.weekday-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
}

.weekday {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  font-weight: bold;
}

.month-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 1px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
}

.month-day {
  min-height: 100px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  background-color: #f9f9f9;
}

.month-day.other-month {
  background-color: #f0f0f0;
  color: #c0c4cc;
}

.day-number {
  font-weight: bold;
  margin-bottom: 5px;
}

.day-schedule-count {
  font-size: 12px;
  color: #409EFF;
  margin-top: 5px;
}
</style>