<template>
  <div class="lessons-container">
    <el-card class="card">
      <template slot="header">
        <div class="card-header">
          <h2>私教课时管理</h2>
        </div>
      </template>
      
      <!-- 课时概览 -->
      <div class="lessons-overview">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="overview-card">
              <div class="overview-item">
                <span class="overview-label">剩余课时</span>
                <span class="overview-value">{{ remainingLessons }}</span>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="overview-card">
              <div class="overview-item">
                <span class="overview-label">有效期至</span>
                <span class="overview-value">{{ validityPeriod }}</span>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="overview-card">
              <div class="overview-item">
                <span class="overview-label">下次上课</span>
                <span class="overview-value">{{ nextLessonTime }}</span>
              </div>
              <div class="countdown" v-if="nextLessonCountdown">
                {{ nextLessonCountdown }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 消课记录 -->
      <div class="lessons-records">
        <h3>已消课记录</h3>
        <el-table :data="lessonRecords" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="date" label="课程日期"></el-table-column>
          <el-table-column prop="duration" label="时长(分钟)"></el-table-column>
          <el-table-column prop="coachName" label="教练姓名"></el-table-column>
          <el-table-column prop="content" label="训练内容" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'completed' ? 'success' : 'warning'">
                {{ scope.row.status === 'completed' ? '已完成' : '进行中' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 上课签到 -->
      <div class="checkin-section">
        <h3>上课签到</h3>
        <el-card class="checkin-card">
          <div class="checkin-methods">
            <div class="method-item">
              <h4>扫码签到</h4>
              <div class="qrcode-container">
                <img src="https://via.placeholder.com/200x200?text=QR+Code" alt="签到二维码" class="qrcode">
                <p>请使用手机扫描二维码进行签到</p>
              </div>
            </div>
            <div class="method-item">
              <h4>手动签到</h4>
              <el-form :model="checkinForm" :rules="checkinRules" ref="checkinForm">
                <el-form-item label="课程ID" prop="lessonId">
                  <el-input v-model="checkinForm.lessonId" placeholder="请输入课程ID"></el-input>
                </el-form-item>
                <el-form-item label="验证码" prop="verificationCode">
                  <el-input v-model="checkinForm.verificationCode" placeholder="请输入教练提供的验证码"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitCheckin">确认签到</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserLessons',
  data() {
    return {
      remainingLessons: 12,
      validityPeriod: '2024-12-31',
      nextLessonTime: '2024-03-20 19:00',
      nextLessonCountdown: '3天 4小时 30分钟',
      lessonRecords: [
        {
          id: 1,
          date: '2024-03-15',
          duration: 60,
          coachName: '张教练',
          content: '胸部训练：卧推、哑铃飞鸟、俯卧撑',
          status: 'completed'
        },
        {
          id: 2,
          date: '2024-03-12',
          duration: 60,
          coachName: '张教练',
          content: '腿部训练：深蹲、硬拉、腿举',
          status: 'completed'
        },
        {
          id: 3,
          date: '2024-03-09',
          duration: 60,
          coachName: '张教练',
          content: '背部训练：引体向上、划船、二头弯举',
          status: 'completed'
        }
      ],
      checkinForm: {
        lessonId: '',
        verificationCode: ''
      },
      checkinRules: {
        lessonId: [
          { required: true, message: '请输入课程ID', trigger: 'blur' }
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.updateCountdown()
    setInterval(() => {
      this.updateCountdown()
    }, 60000) // 每分钟更新一次倒计时
  },
  methods: {
    updateCountdown() {
      // 模拟倒计时更新
      const now = new Date()
      const nextLesson = new Date('2024-03-20 19:00:00')
      const diff = nextLesson - now
      
      if (diff > 0) {
        const days = Math.floor(diff / (1000 * 60 * 60 * 24))
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
        this.nextLessonCountdown = `${days}天 ${hours}小时 ${minutes}分钟`
      } else {
        this.nextLessonCountdown = '课程已开始'
      }
    },
    submitCheckin() {
      this.$refs.checkinForm.validate((valid) => {
        if (valid) {
          // 模拟提交签到
          this.$message.success('签到成功')
          // 重置表单
          this.checkinForm = {
            lessonId: '',
            verificationCode: ''
          }
          // 刷新课时信息
          this.updateLessonInfo()
        }
      })
    },
    updateLessonInfo() {
      // 模拟更新课时信息
      this.remainingLessons = 11 // 减少一节课
      // 刷新消课记录
      this.loadLessonRecords()
    },
    loadLessonRecords() {
      // 模拟加载消课记录
      this.lessonRecords = [
        {
          id: 4,
          date: new Date().toISOString().split('T')[0],
          duration: 60,
          coachName: '张教练',
          content: '肩部训练：肩推、侧平举、俯身飞鸟',
          status: 'completed'
        },
        {
          id: 1,
          date: '2024-03-15',
          duration: 60,
          coachName: '张教练',
          content: '胸部训练：卧推、哑铃飞鸟、俯卧撑',
          status: 'completed'
        },
        {
          id: 2,
          date: '2024-03-12',
          duration: 60,
          coachName: '张教练',
          content: '腿部训练：深蹲、硬拉、腿举',
          status: 'completed'
        },
        {
          id: 3,
          date: '2024-03-09',
          duration: 60,
          coachName: '张教练',
          content: '背部训练：引体向上、划船、二头弯举',
          status: 'completed'
        }
      ]
    }
  }
}
</script>

<style scoped>
.lessons-container {
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

.lessons-overview {
  margin-bottom: 30px;
}

.overview-card {
  text-align: center;
  padding: 20px 0;
}

.overview-item {
  margin-bottom: 10px;
}

.overview-label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.overview-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.countdown {
  font-size: 14px;
  color: #409EFF;
  margin-top: 10px;
}

.lessons-records {
  margin-bottom: 30px;
}

.lessons-records h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.checkin-section {
  margin-bottom: 20px;
}

.checkin-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.checkin-methods {
  display: flex;
  gap: 40px;
}

.method-item {
  flex: 1;
}

.method-item h4 {
  margin-bottom: 15px;
  font-size: 14px;
  font-weight: bold;
}

.qrcode-container {
  text-align: center;
}

.qrcode {
  margin-bottom: 10px;
  border: 1px solid #f0f0f0;
  padding: 10px;
}

.qrcode-container p {
  font-size: 12px;
  color: #606266;
  margin: 0;
}

@media (max-width: 768px) {
  .checkin-methods {
    flex-direction: column;
    gap: 20px;
  }
  
  .overview-value {
    font-size: 20px;
  }
}
</style>