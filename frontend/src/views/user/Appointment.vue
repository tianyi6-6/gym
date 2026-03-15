<template>
  <div class="appointment-container">
    <el-card class="main-card">
      <template slot="header">
        <div class="card-header">
          <h2>教练预约</h2>
          <el-button type="primary" @click="handleAddAppointment">新增预约</el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" class="appointment-tabs">
        <!-- 预约页面 -->
        <el-tab-pane label="预约教练" name="booking">
          <!-- 教练选择 -->
          <div class="section">
            <h3>选择教练</h3>
            <div class="coach-list">
              <el-card 
                v-for="coach in coaches" 
                :key="coach.id" 
                class="coach-card"
                @click="selectCoach(coach)"
                :class="{ 'selected': selectedCoach && selectedCoach.id === coach.id }"
              >
                <div class="coach-info">
                  <div class="coach-avatar">
                    <img :src="coach.avatar || 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA4MCA4MCI+PHBhdGggZmlsbD0iI2ZmZiIgZD0iTTQwIDc4QzQ5Ljk0MSA3OCA1OCA2OS45NDEgNTggNjBDNTggNTAuMDU5IDQ5Ljk0MSAyIDQwIDJDMzAuMDU5IDIgMjIgMTAuMDU5IDIyIDYwQzIyIDY5Ljk0MSAzMC4wNTkgNzggNDAgNzh6bTAgLTkwQzQ5Ljk0MSAtMTIgNTgtMC4wNTkgNTggOEM1OCAxNy45NDEgNDkuOTQxIDYwIDQwIDYwQzMwLjA1OSA2MCAyMiAxNy45NDEgMjIgOEMyMiAtMC4wNTkgMzAuMDU5IC0xMiA0MCAxMnpNIDQwIDEzOEM0OS45NDEgMTM4IDU4IDEyOS45NDEgNTggMTIwQzU4IDExMC4wNTkgNDkuOTQxIDYwIDQwIDYwQzMwLjA1OSA2MCAyMiAxMTAuMDU5IDIyIDEyMEMyMiAxMjkuOTQxIDMwLjA1OSAxMzggNDAgMTM4em0wIDgtMTAuMDAxLTU4LTkuOTktNTgtMS4wMDItMCAuOTktMy4wMDAxIDEtM2M3LjQ4NC0yLjk5OTkgMTcuNjY5LTEuNjY2NyAyNC4zMzQtMS42NjY3QzQ4LjM3MyA2My42NjY5IDU4IDczLjk1NzkgNTggODhDNzYgNjkuNDQwMSA2OS45NjQxIDYwIDYwIDYwYzEuMDUyOS04Ljk0NCAxMS4xMzc5LTkuOTkgMjAtOS45OXptMC01OWwtMTAuMDAxLTU4LTkuOTktNTgtMS4wMDItMCAuOTktMy4wMDAxIDEtM2M3LjQ4NC0yLjk5OTkgMTcuNjY5LTEuNjY2NyAyNC4zMzQtMS42NjY3QzQ4LjM3MyA0LjY2NjkgNTggMTQuOTU3OSA1OCAzMUM3NiAyNS40NDAxIDY5Ljk2NDEgMTYgNjAgMTZjMS4wNTI5LTguOTQ0IDExLjEzNzktOS45OSAyMC05Ljk5eiIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjNjY2IiBzdHJva2Utd2lkdGg9IjIiLz48L3N2Zz4='" :alt="coach.name" class="avatar">
                  </div>
                  <div class="coach-details">
                    <h4>{{ coach.name }}</h4>
                    <div class="coach-specialties">
                      <el-tag 
                        v-for="(specialty, index) in coach.specialties" 
                        :key="index"
                        size="small"
                        v-if="index < 3"
                      >
                        {{ specialty }}
                      </el-tag>
                      <el-tag size="small" v-if="coach.specialties.length > 3">...</el-tag>
                    </div>
                    <div class="coach-rating">
                      <el-rate v-model="coach.rating" disabled show-score :score-template="score"></el-rate>
                    </div>
                  </div>
                </div>
                <div class="coach-availability">
                  <h5>可预约日期</h5>
                  <div class="availability-dates">
                    <div 
                      v-for="date in coach.availableDates" 
                      :key="date.date"
                      class="date-item"
                      @click="selectDate(date)"
                      :class="{ 'selected': selectedDate && selectedDate.date === date.date }"
                    >
                      <div class="date">{{ date.date }}</div>
                      <div class="day">{{ date.day }}</div>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
          
          <!-- 时间选择 -->
          <div class="section" v-if="selectedCoach && selectedDate">
            <h3>选择时间</h3>
            <div class="time-selector">
              <div class="time-slots">
                <div 
                  v-for="slot in timeSlots" 
                  :key="slot.time"
                  class="time-slot"
                  :class="{ 'available': slot.available, 'selected': selectedTime === slot.time, 'unavailable': !slot.available }"
                  @click="selectTimeSlot(slot)"
                >
                  {{ slot.time }}
                </div>
              </div>
            </div>
            
            <!-- 时长选择 -->
            <div class="duration-selector" v-if="selectedTime">
              <h4>选择时长</h4>
              <el-radio-group v-model="selectedDuration">
                <el-radio-button label="0.5">0.5小时</el-radio-button>
                <el-radio-button label="1">1小时</el-radio-button>
                <el-radio-button label="1.5">1.5小时</el-radio-button>
                <el-radio-button label="2">2小时</el-radio-button>
              </el-radio-group>
            </div>
            
            <!-- 预约按钮 -->
            <div class="booking-actions" v-if="selectedTime && selectedDuration">
              <el-button type="primary" @click="submitBooking" :loading="loading">
                确认预约
              </el-button>
            </div>
          </div>
        </el-tab-pane>
        
        <!-- 预约记录 -->
        <el-tab-pane label="预约记录" name="records">
          <!-- 筛选条件 -->
          <div class="filter-section">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-select v-model="statusFilter" placeholder="选择状态">
                  <el-option label="全部" value=""></el-option>
                  <el-option label="待确认" value="0"></el-option>
                  <el-option label="已确认" value="1"></el-option>
                  <el-option label="已取消" value="2"></el-option>
                </el-select>
              </el-col>
              <el-col :span="16">
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-col>
            </el-row>
          </div>
          
          <!-- 预约记录列表 -->
          <el-table :data="filteredAppointments" style="width: 100%" border>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column label="教练" width="120">
              <template slot-scope="scope">
                <div class="table-coach-info">
                  <img :src="getCoachAvatar(scope.row.coachId)" :alt="scope.row.coachName" class="table-avatar">
                  <span>{{ scope.row.coachName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="appointmentTime" label="预约时间" width="180"></el-table-column>
            <el-table-column prop="duration" label="时长(小时)" width="100"></el-table-column>
            <el-table-column prop="location" label="场地" width="120"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template slot-scope="scope">
                <el-button 
                  size="mini" 
                  type="primary" 
                  @click="editAppointment(scope.row)"
                  v-if="scope.row.status === 0"
                >
                  修改
                </el-button>
                <el-button 
                  size="mini" 
                  type="danger" 
                  @click="cancelAppointment(scope.row)"
                  :disabled="!canCancel(scope.row)"
                  v-if="scope.row.status === 0"
                >
                  {{ canCancel(scope.row) ? '取消' : '无法取消' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 教练详情对话框 -->
    <el-dialog title="教练详情" :visible.sync="coachDetailVisible" width="600px">
      <div v-if="selectedCoach" class="coach-detail">
        <div class="coach-detail-header">
          <img :src="selectedCoach.avatar || 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDAgMTAwIj48cGF0aCBmaWxsPSIjZmZmIiBkPSJNNTAgOThDNjIuNDUyOCA5OCA3NSA4NS40NTI4IDc1IDcyQzc1IDU4LjU0NzIgNjIuNDUyOCA0NiA1MCA0NkM0Ny41NDcyIDQ2IDQ1LjE4ODMgNDYuNjA4MSA0My4wNTM0IDQ4LjY0MzJDMTguNzE4OCA3My4zNjQzIDE1IDc3IDUgNzVDNSA1OC4zMzMzIDE4LjM0MTYgNDYgNTAgNDZDMzEuNjU4NCA0NiAxNSA1OC4zMzMzIDE1IDc1QzE1IDgyLjc0OTMgMjMuNjU1NyA5MCAzMi41IDkwQzQxLjM0NDMgOTAgNDUgOTMgNDUgOTRjMC42NjIgMC40NDUgMS4yNTk3IDAuOTI1OCAxLjc2IDMuMTQ0NEM0Ni4zNjMgOTguODc1IDQ4LjE3MzMgMTAwIDUwIDEwMHpNNTAgNjBDNjMuODA3MSA2MCA3NSA0OC44MDcxIDc1IDM1Qzc1IDIxLjE5MjkgNjMuODA3MSAxMCA1MCAxMEMzNi4xOTI5IDEwIDI1IDIxLjE5MjkgMjUgMzVDMjUgNDguODA3MSAzNi4xOTI5IDYwIDUwIDYwem0wLTU3QzYxLjA0NTcgMyA3MCAxMi45NTQzIDcwIDI0QzcwIDM1LjA0NTcgNjEuMDQ1NyA0NSA1MCA0NUMzOC45NTQzIDQ1IDMwIDM1LjA0NTcgMzAgMjRDMTguOTU0MyAyNCAxMCAxNS4wNDU3IDEwIDUuMDAwM1YzLjAwMDFDMTAuMDAwMi03LjA0NTcgMTguOTU0My0xNS45OTkyIDMwLTE2IDQzLjAwMDEtMTYgNTYgLTE2IDc3LTE2Qzc3LjAwMDEtMTYgNzcuMDAwMS0xNS45OTkyIDc3LjAwMDEtMTVjMC4wMDAxIDUuMDQ1Ny00Ljk1NDMgMTAtMTAgMTBDNjcuMDAwMSA1IDY0LjY0MzEgNSA2Mi41IDVDMzguMTYwNSA1IDMwIDEzLjE2MDUgMzAgMzVDMzAgNDYuODQwMiAzOC4xNjA1IDU1IDYwIDU1QzYxLjUwMDcgNTUgNjMuMDI5NSA1NC4zMzEyIDY0LjA2ODQgNTMuMjk0OEw2My42NTEzIDQ1LjYwMzFjLTAuODkwMy0xLjY5OTktMS45NjA3LTMuMjc2OC0zLjE3NDgtNC42OTM1QzYwLjE1IDQwLjA4MiA1OS42OTQzIDQwIDU5LjY5NDMgNDBjLS4xOTU5IDAtLjM4OTIgMC0uNTc5OSAwbC0xLjEyMTctMS4yNDE1Yy0uOTU3OC0xLjA2MzgtMi4wNzUtMS45MzM2LTMuMzA3MS0yLjU3OTdDNTEuNjY4MSAzNC40MjA5IDUwLjc1MzMgMzQgNTAgMzRjLTEuNjUxOCAwLTMuMDQ5Mi43NDkyLTMuOTc3OSAxLjk1NTFDNDUuMzY4MiAzNi40NjkyIDQ1IDM4LjI1MTUgNDUgNDAuMjE4M2MwIDEuOTY2OC4zNjgyIDMuNzQ5MSAxLjA1MTUgNS4yODU0QzQ2LjI5IDQ3LjQwMzEgNDcuNzg4NSA0OCA1MCA0OHM1LjUyNS0xLjY0OTYgNS41MjUtNC45NDk3YzAtMy4zMDAxLTIuNjk5OS02LTYtNnptLTEwIDBjLTcuNzMyIDEuNzgwNS0xMi42MzIgOC4yMjA0LTEyLjY3MiAxNi4wNDczLTMuOTguMzg1Mi04LjAwMjQgOC4xMDQ5LTguMDQyNCAxNi4wNDczIDAgNy45NDI0IDQuMDU0MiAxNS42NTY5IDguMDQyNCAxNi4wNDczLjQwMDAyLjM4OTguNDQwMDIgMS42MTI2IDAgMi4wMTA0LS40NDAwMi4zOTQyLS45MTUzNS43NDU5LTEuNDQwMS4yMDQxLTYuNDQwMS01LjQ4OTgtMTEuNjQwMS0xMS45Mjk4LTExLjY0MDF6IiBmaWxsPSJub25lIiBzdHJva2U9IiM2NjYiIHN0cm9rZS13aWR0aD0iMiIvPjwvc3ZnPg=='" :alt="selectedCoach.name" class="detail-avatar">
          <div class="detail-info">
            <h3>{{ selectedCoach.name }}</h3>
            <div class="detail-specialties">
              <el-tag v-for="(specialty, index) in selectedCoach.specialties" :key="index" size="small">
                {{ specialty }}
              </el-tag>
            </div>
            <div class="detail-rating">
              <el-rate v-model="selectedCoach.rating" disabled show-score :score-template="score"></el-rate>
            </div>
          </div>
        </div>
        <div class="detail-description">
          <h4>个人介绍</h4>
          <p>{{ selectedCoach.description }}</p>
        </div>
        <div class="detail-reviews">
          <h4>用户评价</h4>
          <div v-for="(review, index) in selectedCoach.reviews" :key="index" class="review-item">
            <div class="review-header">
              <span class="review-user">{{ review.username }}</span>
              <el-rate v-model="review.rating" disabled :max="5" show-score :score-template="score"></el-rate>
            </div>
            <p class="review-content">{{ review.content }}</p>
            <span class="review-date">{{ review.date }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 预约修改对话框 -->
    <el-dialog title="修改预约" :visible.sync="editDialogVisible" width="600px">
      <el-form :model="editForm" :rules="editRules" ref="editForm">
        <el-form-item label="教练" prop="coachId">
          <el-select v-model="editForm.coachId" placeholder="选择教练" disabled>
            <el-option 
              v-for="coach in coaches" 
              :key="coach.id" 
              :label="coach.name" 
              :value="coach.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期" prop="date">
          <el-date-picker v-model="editForm.date" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="预约时间" prop="time">
          <el-select v-model="editForm.time" placeholder="选择时间" style="width: 100%">
            <el-option 
              v-for="slot in editTimeSlots" 
              :key="slot.time" 
              :label="slot.time" 
              :value="slot.time"
              :disabled="!slot.available"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时长" prop="duration">
          <el-radio-group v-model="editForm.duration">
            <el-radio-button label="0.5">0.5小时</el-radio-button>
            <el-radio-button label="1">1小时</el-radio-button>
            <el-radio-button label="1.5">1.5小时</el-radio-button>
            <el-radio-button label="2">2小时</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="editForm.remark" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <template slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit" :loading="loading">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getCoachList } from '@/api/index'

export default {
  name: 'UserAppointment',
  data() {
    return {
      activeTab: 'booking',
      loading: false,
      selectedCoach: null,
      selectedDate: null,
      selectedTime: null,
      selectedDuration: '1',
      coachDetailVisible: false,
      editDialogVisible: false,
      editForm: {},
      editTimeSlots: [],
      statusFilter: '',
      dateRange: null,
      score: '{value}',
      coaches: [],
      timeSlots: [
        { time: '09:00', available: true },
        { time: '09:30', available: true },
        { time: '10:00', available: false },
        { time: '10:30', available: true },
        { time: '11:00', available: true },
        { time: '11:30', available: false },
        { time: '12:00', available: false },
        { time: '12:30', available: false },
        { time: '13:00', available: true },
        { time: '13:30', available: true },
        { time: '14:00', available: true },
        { time: '14:30', available: false },
        { time: '15:00', available: true },
        { time: '15:30', available: true },
        { time: '16:00', available: true },
        { time: '16:30', available: false },
        { time: '17:00', available: true },
        { time: '17:30', available: true },
        { time: '18:00', available: true },
        { time: '18:30', available: false },
        { time: '19:00', available: true },
        { time: '19:30', available: true },
        { time: '20:00', available: true },
        { time: '20:30', available: true }
      ],
      appointments: [
        {
          id: 1,
          coachId: 1,
          coachName: '张教练',
          appointmentTime: '2024-03-20 19:00',
          duration: 1,
          location: '训练室A',
          status: 0,
          remark: '力量训练'
        },
        {
          id: 2,
          coachId: 2,
          coachName: '李教练',
          appointmentTime: '2024-03-18 10:00',
          duration: 1.5,
          location: '瑜伽室',
          status: 1,
          remark: '瑜伽课程'
        },
        {
          id: 3,
          coachId: 3,
          coachName: '王教练',
          appointmentTime: '2024-03-15 18:00',
          duration: 1,
          location: '训练室B',
          status: 2,
          remark: 'HIIT训练'
        }
      ],
      editRules: {
        date: [
          { required: true, message: '请选择预约日期', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请选择预约时间', trigger: 'blur' }
        ],
        duration: [
          { required: true, message: '请选择时长', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadCoaches()
  },
  methods: {
    loadCoaches() {
      getCoachList().then(response => {
        if (response.data.code === 200) {
          this.coaches = response.data.data.map(coach => ({
            ...coach,
            specialties: coach.specialty ? coach.specialty.split(',') : [],
            reviews: [],
            availableDates: [
              { date: '2024-03-18', day: '周一' },
              { date: '2024-03-19', day: '周二' },
              { date: '2024-03-20', day: '周三' },
              { date: '2024-03-21', day: '周四' },
              { date: '2024-03-22', day: '周五' },
              { date: '2024-03-23', day: '周六' },
              { date: '2024-03-24', day: '周日' }
            ]
          }))
        }
      }).catch(error => {
        console.error('Failed to load coaches:', error)
        // 如果API调用失败，使用模拟数据
        this.coaches = [
          {
            id: 1,
            name: '张教练',
            avatar: 'https://via.placeholder.com/80x80?text=Coach+1',
            specialty: '力量训练,有氧运动',
            specialties: ['力量训练', '有氧运动'],
            rating: 4.8,
            description: '拥有10年健身指导经验，擅长力量训练和有氧运动。',
            reviews: [
              {
                username: '用户A',
                rating: 5,
                content: '教练非常专业，训练计划制定合理，效果明显。',
                date: '2024-03-10'
              },
              { username: '用户B',
                rating: 4,
                content: '训练氛围很好，教练很有耐心。',
                date: '2024-03-05'
              }
            ],
            availableDates: [
              { date: '2024-03-18', day: '周一' },
              { date: '2024-03-19', day: '周二' },
              { date: '2024-03-20', day: '周三' },
              { date: '2024-03-21', day: '周四' },
              { date: '2024-03-22', day: '周五' },
              { date: '2024-03-23', day: '周六' },
              { date: '2024-03-24', day: '周日' }
            ]
          },
          {
            id: 2,
            name: '李教练',
            avatar: 'https://via.placeholder.com/80x80?text=Coach+2',
            specialty: '瑜伽,普拉提,康复训练',
            specialties: ['瑜伽', '普拉提', '康复训练'],
            rating: 4.9,
            description: '瑜伽教练，拥有国际瑜伽导师认证，擅长普拉提和康复训练。',
            reviews: [
              {
                username: '用户C',
                rating: 5,
                content: '瑜伽课程非常专业，教练指导细致。',
                date: '2024-03-12'
              }
            ],
            availableDates: [
              { date: '2024-03-18', day: '周一' },
              { date: '2024-03-20', day: '周三' },
              { date: '2024-03-22', day: '周五' },
              { date: '2024-03-24', day: '周日' }
            ]
          },
          {
            id: 3,
            name: '王教练',
            avatar: 'https://via.placeholder.com/80x80?text=Coach+3',
            specialty: '有氧训练,HIIT,功能性训练',
            specialties: ['有氧训练', 'HIIT', '功能性训练'],
            rating: 4.7,
            description: '专注于有氧训练和HIIT，帮助学员快速减脂。',
            reviews: [
              {
                username: '用户D',
                rating: 4,
                content: '训练强度适中，效果不错。',
                date: '2024-03-08'
              }
            ],
            availableDates: [
              { date: '2024-03-19', day: '周二' },
              { date: '2024-03-21', day: '周四' },
              { date: '2024-03-23', day: '周六' }
            ]
          }
        ]
      })
    },
    handleAddAppointment() {
      // 切换到预约标签页
      this.activeTab = 'booking'
      // 重置预约状态
      this.selectedCoach = null
      this.selectedDate = null
      this.selectedTime = null
      this.selectedDuration = '1'
      // 确保教练数据已加载
      if (this.coaches.length === 0) {
        this.loadCoaches()
      }
    },
    selectCoach(coach) {
      this.selectedCoach = coach
      this.selectedDate = null
      this.selectedTime = null
      this.selectedDuration = '1'
    },
    selectDate(date) {
      this.selectedDate = date
      this.selectedTime = null
      this.selectedDuration = '1'
      // 模拟加载该日期的可用时间
      this.loadTimeSlots()
    },
    selectTimeSlot(slot) {
      if (slot.available) {
        this.selectedTime = slot.time
      }
    },
    loadTimeSlots() {
      // 模拟加载时间槽
      this.timeSlots = [
        { time: '09:00', available: true },
        { time: '09:30', available: true },
        { time: '10:00', available: false },
        { time: '10:30', available: true },
        { time: '11:00', available: true },
        { time: '11:30', available: false },
        { time: '12:00', available: false },
        { time: '12:30', available: false },
        { time: '13:00', available: true },
        { time: '13:30', available: true },
        { time: '14:00', available: true },
        { time: '14:30', available: false },
        { time: '15:00', available: true },
        { time: '15:30', available: true },
        { time: '16:00', available: true },
        { time: '16:30', available: false },
        { time: '17:00', available: true },
        { time: '17:30', available: true },
        { time: '18:00', available: true },
        { time: '18:30', available: false },
        { time: '19:00', available: true },
        { time: '19:30', available: true },
        { time: '20:00', available: true },
        { time: '20:30', available: true }
      ]
    },
    submitBooking() {
      this.loading = true
      // 模拟提交预约
      setTimeout(() => {
        const newAppointment = {
          id: this.appointments.length + 1,
          coachId: this.selectedCoach.id,
          coachName: this.selectedCoach.name,
          appointmentTime: `${this.selectedDate.date} ${this.selectedTime}`,
          duration: parseFloat(this.selectedDuration),
          location: '训练室A',
          status: 0,
          remark: ''
        }
        this.appointments.unshift(newAppointment)
        this.$message.success('预约成功')
        this.activeTab = 'records'
        this.loading = false
      }, 1000)
    },
    getStatusType(status) {
      const map = { 0: 'warning', 1: 'success', 2: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 0: '待确认', 1: '已确认', 2: '已取消' }
      return map[status] || '未知'
    },
    getCoachAvatar(coachId) {
      const coach = this.coaches.find(c => c.id === coachId)
      return coach && coach.avatar ? coach.avatar : 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHBhdGggZmlsbD0iI2ZmZiIgZD0iTTIwIDM1QzI4LjI4NCAzNSAzNSAyOC4yODQgMzUgMjBDMzUgMTEuNzE2IDI4LjI4NCA1IDIwIDVDMTEuNzE2IDUgNSAxMS43MTYgNSAyMEM1IDI4LjI4NCAxMS43MTYgMzUgMjAgMzV6bTAgLTUwQzI4LjI4NCAwIDM1IDYuNzE2IDM1IDE1QzM1IDIzLjI4NCAyOC4yODQgMzAgMjAgMzBDMTEuNzE2IDMwIDUgMjMuMjg0IDUgMTVDNSA2LjcxNiAxMS43MTYgMCAyMCAwem0wIDU4QzI4LjI4NCA4IDM1IDE0LjcxNiAzNSAyM0MzNSAzMS4yODQgMjguMjg0IDM3IDIwIDM3QzExLjcxNiAzNyA1IDMxLjI4NCA1IDIzQzUgMTQuNzE2IDExLjcxNiA4IDIwOHoiIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzY2NiIgc3Ryb2tlLXdpZHRoPSIxIi8+PC9zdmc+'
    },
    canCancel(appointment) {
      const appointmentTime = new Date(appointment.appointmentTime)
      const now = new Date()
      const diffHours = (appointmentTime - now) / (1000 * 60 * 60)
      return diffHours >= 24
    },
    cancelAppointment(appointment) {
      if (!this.canCancel(appointment)) {
        this.$message.error('距离预约时间不足24小时，无法取消')
        return
      }
      
      this.$confirm('确定要取消预约吗？取消后将立即释放该时段。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        appointment.status = 2
        this.$message.success('取消成功')
      }).catch(() => {
        // 取消操作
      })
    },
    editAppointment(appointment) {
      this.editForm = {
        id: appointment.id,
        coachId: appointment.coachId,
        date: new Date(appointment.appointmentTime.split(' ')[0]),
        time: appointment.appointmentTime.split(' ')[1],
        duration: appointment.duration.toString(),
        remark: appointment.remark
      }
      // 模拟加载时间槽
      this.editTimeSlots = [
        { time: '09:00', available: true },
        { time: '09:30', available: true },
        { time: '10:00', available: false },
        { time: '10:30', available: true },
        { time: '11:00', available: true },
        { time: '11:30', available: false },
        { time: '12:00', available: false },
        { time: '12:30', available: false },
        { time: '13:00', available: true },
        { time: '13:30', available: true },
        { time: '14:00', available: true },
        { time: '14:30', available: false },
        { time: '15:00', available: true },
        { time: '15:30', available: true },
        { time: '16:00', available: true },
        { time: '16:30', available: false },
        { time: '17:00', available: true },
        { time: '17:30', available: true },
        { time: '18:00', available: true },
        { time: '18:30', available: false },
        { time: '19:00', available: true },
        { time: '19:30', available: true },
        { time: '20:00', available: true },
        { time: '20:30', available: true }
      ]
      this.editDialogVisible = true
    },
    submitEdit() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.loading = true
          // 模拟提交修改
          setTimeout(() => {
            const appointment = this.appointments.find(a => a.id === this.editForm.id)
            if (appointment) {
              appointment.appointmentTime = `${this.editForm.date.toISOString().split('T')[0]} ${this.editForm.time}`
              appointment.duration = parseFloat(this.editForm.duration)
              appointment.remark = this.editForm.remark
            }
            this.$message.success('修改成功')
            this.editDialogVisible = false
            this.loading = false
          }, 1000)
        }
      })
    }
  },
  computed: {
    filteredAppointments() {
      let result = [...this.appointments]
      
      // 按状态筛选
      if (this.statusFilter) {
        result = result.filter(item => item.status.toString() === this.statusFilter)
      }
      
      // 按日期范围筛选
      if (this.dateRange && this.dateRange.length === 2) {
        const startDate = new Date(this.dateRange[0])
        const endDate = new Date(this.dateRange[1])
        result = result.filter(item => {
          const appointmentDate = new Date(item.appointmentTime)
          return appointmentDate >= startDate && appointmentDate <= endDate
        })
      }
      
      // 按时间倒序排列
      result.sort((a, b) => {
        return new Date(b.appointmentTime) - new Date(a.appointmentTime)
      })
      
      return result
    }
  }
}
</script>

<style scoped>
.appointment-container {
  padding: 20px;
}

.main-card {
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

.appointment-tabs {
  margin-top: 20px;
}

.section {
  margin-bottom: 30px;
}

.section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.coach-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.coach-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.coach-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.coach-card.selected {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.coach-info {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.coach-avatar {
  margin-right: 15px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.coach-details h4 {
  margin: 0 0 10px 0;
  font-size: 16px;
  font-weight: bold;
}

.coach-specialties {
  margin-bottom: 10px;
}

.coach-rating {
  margin-top: 10px;
}

.coach-availability h5 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: bold;
}

.availability-dates {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.date-item {
  width: 60px;
  height: 60px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.date-item:hover {
  border-color: #409EFF;
}

.date-item.selected {
  background: #409EFF;
  color: white;
  border-color: #409EFF;
}

.date {
  font-size: 14px;
  font-weight: bold;
}

.day {
  font-size: 12px;
}

.time-slots {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
}

.time-slot {
  height: 40px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.time-slot.available:hover {
  border-color: #409EFF;
  color: #409EFF;
}

.time-slot.selected {
  background: #409EFF;
  color: white;
  border-color: #409EFF;
}

.time-slot.unavailable {
  background: #f5f7fa;
  color: #c0c4cc;
  cursor: not-allowed;
}

.duration-selector {
  margin: 20px 0;
}

.duration-selector h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: bold;
}

.booking-actions {
  margin-top: 20px;
}

.filter-section {
  margin-bottom: 20px;
}

.table-coach-info {
  display: flex;
  align-items: center;
}

.table-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
}

.coach-detail {
  padding: 20px;
}

.coach-detail-header {
  display: flex;
  margin-bottom: 20px;
}

.detail-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-right: 20px;
  object-fit: cover;
}

.detail-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: bold;
}

.detail-specialties {
  margin-bottom: 10px;
}

.detail-description {
  margin-bottom: 20px;
}

.detail-description h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: bold;
}

.detail-reviews h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: bold;
}

.review-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 10px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.review-user {
  font-weight: bold;
}

.review-content {
  margin: 0 0 10px 0;
}

.review-date {
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .coach-list {
    grid-template-columns: 1fr;
  }
  
  .time-slots {
    grid-template-columns: repeat(auto-fill, minmax(70px, 1fr));
  }
  
  .availability-dates {
    justify-content: center;
  }
  
  .coach-detail-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .detail-avatar {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style>