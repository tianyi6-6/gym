<template>
  <div class="training-container">
    <el-card class="card">
      <template slot="header">
        <div class="card-header">
          <h2>训练计划与打卡</h2>
        </div>
      </template>
      
      <!-- 训练计划展示 -->
      <el-tabs v-model="activeTab" class="training-tabs">
        <el-tab-pane label="训练计划" name="plan">
          <div class="plan-controls">
            <el-radio-group v-model="planType" @change="loadPlan">
              <el-radio label="week">周计划</el-radio>
              <el-radio label="month">月计划</el-radio>
            </el-radio-group>
          </div>
          
          <div class="plan-content">
            <el-card v-for="(day, index) in planData" :key="index" class="day-card">
              <template slot="header">
                <div class="day-header">
                  <span class="day-title">{{ day.date }} - {{ day.day }}</span>
                  <span class="day-status" :class="{ 'completed': day.status === 'completed' }">
                    {{ day.status === 'completed' ? '已完成' : '未完成' }}
                  </span>
                </div>
              </template>
              <div class="exercise-list">
                <div v-for="(exercise, exIndex) in day.exercises" :key="exIndex" class="exercise-item">
                  <div class="exercise-info">
                    <h4>{{ exercise.name }}</h4>
                    <p>{{ exercise.sets }} 组 × {{ exercise.reps }} 次</p>
                  </div>
                  <div class="exercise-status">
                    <el-checkbox v-model="exercise.completed" @change="updateExerciseStatus(day, exercise)">
                      完成
                    </el-checkbox>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="训练打卡" name="checkin">
          <div class="checkin-form">
            <el-form :model="checkinForm" :rules="checkinRules" ref="checkinForm">
              <el-form-item label="训练日期" prop="date">
                <el-date-picker v-model="checkinForm.date" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
              </el-form-item>
              <el-form-item label="训练感受" prop="feeling">
                <el-input type="textarea" v-model="checkinForm.feeling" placeholder="请输入训练感受（10-200字）" :rows="4"></el-input>
              </el-form-item>
              <el-form-item label="训练成果照片">
                <el-upload
                  class="upload-demo"
                  action="/api/training/upload"
                  :on-success="handleImageUploadSuccess"
                  :on-error="handleImageUploadError"
                  :before-upload="beforeImageUpload"
                  :limit="3"
                  :file-list="checkinForm.photos"
                >
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div class="el-upload__tip" slot="tip">
                    最多上传3张照片，单张不超过5MB
                  </div>
                </el-upload>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitCheckin">提交打卡</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="打卡记录" name="records">
          <el-table :data="checkinRecords" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="date" label="打卡日期"></el-table-column>
            <el-table-column prop="feeling" label="训练感受" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="photosCount" label="照片数量"></el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button type="primary" size="small" @click="viewCheckinDetail(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 打卡详情对话框 -->
    <el-dialog title="打卡详情" :visible.sync="checkinDetailVisible" width="600px">
      <div v-if="selectedCheckin">
        <div class="detail-item">
          <span class="detail-label">打卡日期：</span>
          <span class="detail-value">{{ selectedCheckin.date }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">训练感受：</span>
          <p class="detail-value">{{ selectedCheckin.feeling }}</p>
        </div>
        <div class="detail-item">
          <span class="detail-label">训练照片：</span>
          <div class="photo-grid">
            <div v-for="(photo, index) in selectedCheckin.photos" :key="index" class="photo-item">
              <img :src="photo.url" :alt="'照片' + (index + 1)" class="photo">
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserTraining',
  data() {
    return {
      activeTab: 'plan',
      planType: 'week',
      checkinDetailVisible: false,
      selectedCheckin: null,
      planData: [
        {
          date: '2024-03-18',
          day: '周一',
          status: 'completed',
          exercises: [
            { name: '卧推', sets: 4, reps: 12, completed: true },
            { name: '哑铃飞鸟', sets: 3, reps: 15, completed: true },
            { name: '俯卧撑', sets: 3, reps: 20, completed: true }
          ]
        },
        {
          date: '2024-03-19',
          day: '周二',
          status: 'pending',
          exercises: [
            { name: '深蹲', sets: 4, reps: 10, completed: false },
            { name: '硬拉', sets: 3, reps: 8, completed: false },
            { name: '腿举', sets: 3, reps: 15, completed: false }
          ]
        },
        {
          date: '2024-03-20',
          day: '周三',
          status: 'pending',
          exercises: [
            { name: '引体向上', sets: 4, reps: 8, completed: false },
            { name: '划船', sets: 3, reps: 12, completed: false },
            { name: '二头弯举', sets: 3, reps: 15, completed: false }
          ]
        }
      ],
      checkinForm: {
        date: new Date(),
        feeling: '',
        photos: []
      },
      checkinRules: {
        date: [
          { required: true, message: '请选择训练日期', trigger: 'blur' }
        ],
        feeling: [
          { required: true, message: '请输入训练感受', trigger: 'blur' },
          { min: 10, max: 200, message: '训练感受长度在10-200字之间', trigger: 'blur' }
        ]
      },
      checkinRecords: [
        {
          id: 1,
          date: '2024-03-17',
          feeling: '今天的训练强度适中，感觉良好，完成了所有的训练项目。',
          photos: [
            { url: 'https://via.placeholder.com/150', name: 'photo1.jpg' },
            { url: 'https://via.placeholder.com/150', name: 'photo2.jpg' }
          ],
          photosCount: 2
        },
        {
          id: 2,
          date: '2024-03-15',
          feeling: '训练很有挑战性，特别是深蹲部分，但是坚持下来了，感觉很有成就感。',
          photos: [
            { url: 'https://via.placeholder.com/150', name: 'photo1.jpg' }
          ],
          photosCount: 1
        }
      ]
    }
  },
  methods: {
    loadPlan() {
      // 模拟加载训练计划
      if (this.planType === 'week') {
        this.planData = [
          {
            date: '2024-03-18',
            day: '周一',
            status: 'completed',
            exercises: [
              { name: '卧推', sets: 4, reps: 12, completed: true },
              { name: '哑铃飞鸟', sets: 3, reps: 15, completed: true },
              { name: '俯卧撑', sets: 3, reps: 20, completed: true }
            ]
          },
          {
            date: '2024-03-19',
            day: '周二',
            status: 'pending',
            exercises: [
              { name: '深蹲', sets: 4, reps: 10, completed: false },
              { name: '硬拉', sets: 3, reps: 8, completed: false },
              { name: '腿举', sets: 3, reps: 15, completed: false }
            ]
          },
          {
            date: '2024-03-20',
            day: '周三',
            status: 'pending',
            exercises: [
              { name: '引体向上', sets: 4, reps: 8, completed: false },
              { name: '划船', sets: 3, reps: 12, completed: false },
              { name: '二头弯举', sets: 3, reps: 15, completed: false }
            ]
          },
          {
            date: '2024-03-21',
            day: '周四',
            status: 'pending',
            exercises: [
              { name: '肩推', sets: 4, reps: 12, completed: false },
              { name: '侧平举', sets: 3, reps: 15, completed: false },
              { name: '俯身飞鸟', sets: 3, reps: 15, completed: false }
            ]
          },
          {
            date: '2024-03-22',
            day: '周五',
            status: 'pending',
            exercises: [
              { name: '深蹲', sets: 4, reps: 10, completed: false },
              { name: '箭步蹲', sets: 3, reps: 12, completed: false },
              { name: '小腿提踵', sets: 4, reps: 20, completed: false }
            ]
          },
          {
            date: '2024-03-23',
            day: '周六',
            status: 'pending',
            exercises: [
              { name: '引体向上', sets: 4, reps: 8, completed: false },
              { name: '高位下拉', sets: 3, reps: 12, completed: false },
              { name: '硬拉', sets: 3, reps: 8, completed: false }
            ]
          },
          {
            date: '2024-03-24',
            day: '周日',
            status: 'pending',
            exercises: [
              { name: '有氧训练', sets: 1, reps: 45, completed: false }
            ]
          }
        ]
      } else {
        // 月计划
        this.planData = [
          {
            date: '第1周',
            day: '胸部训练',
            status: 'completed',
            exercises: [
              { name: '卧推', sets: 4, reps: 12, completed: true },
              { name: '哑铃飞鸟', sets: 3, reps: 15, completed: true },
              { name: '俯卧撑', sets: 3, reps: 20, completed: true }
            ]
          },
          {
            date: '第1周',
            day: '腿部训练',
            status: 'completed',
            exercises: [
              { name: '深蹲', sets: 4, reps: 10, completed: true },
              { name: '硬拉', sets: 3, reps: 8, completed: true },
              { name: '腿举', sets: 3, reps: 15, completed: true }
            ]
          },
          {
            date: '第2周',
            day: '背部训练',
            status: 'pending',
            exercises: [
              { name: '引体向上', sets: 4, reps: 8, completed: false },
              { name: '划船', sets: 3, reps: 12, completed: false },
              { name: '二头弯举', sets: 3, reps: 15, completed: false }
            ]
          },
          {
            date: '第2周',
            day: '肩部训练',
            status: 'pending',
            exercises: [
              { name: '肩推', sets: 4, reps: 12, completed: false },
              { name: '侧平举', sets: 3, reps: 15, completed: false },
              { name: '俯身飞鸟', sets: 3, reps: 15, completed: false }
            ]
          }
        ]
      }
    },
    updateExerciseStatus(day, exercise) {
      // 检查当天所有训练是否完成
      const allCompleted = day.exercises.every(ex => ex.completed)
      if (allCompleted) {
        day.status = 'completed'
        this.$message.success(`${day.date} ${day.day}的训练已完成！`)
      } else {
        day.status = 'pending'
      }
    },
    beforeImageUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt5M = file.size / 1024 / 1024 < 5
      
      if (!isImage) {
        this.$message.error('只能上传 JPG、PNG 格式的图片')
        return false
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过 5MB')
        return false
      }
      return true
    },
    handleImageUploadSuccess(response, file, fileList) {
      this.checkinForm.photos = fileList
    },
    handleImageUploadError(error, file, fileList) {
      this.$message.error('图片上传失败')
    },
    submitCheckin() {
      this.$refs.checkinForm.validate((valid) => {
        if (valid) {
          // 模拟提交打卡
          this.$message.success('打卡成功')
          // 重置表单
          this.checkinForm = {
            date: new Date(),
            feeling: '',
            photos: []
          }
          // 刷新打卡记录
          this.loadCheckinRecords()
        }
      })
    },
    loadCheckinRecords() {
      // 模拟加载打卡记录
      this.checkinRecords = [
        {
          id: 1,
          date: '2024-03-18',
          feeling: '今天完成了胸部训练，感觉很棒！',
          photos: [
            { url: 'https://via.placeholder.com/150', name: 'photo1.jpg' },
            { url: 'https://via.placeholder.com/150', name: 'photo2.jpg' }
          ],
          photosCount: 2
        },
        {
          id: 2,
          date: '2024-03-17',
          feeling: '今天的训练强度适中，感觉良好，完成了所有的训练项目。',
          photos: [
            { url: 'https://via.placeholder.com/150', name: 'photo1.jpg' },
            { url: 'https://via.placeholder.com/150', name: 'photo2.jpg' }
          ],
          photosCount: 2
        },
        {
          id: 3,
          date: '2024-03-15',
          feeling: '训练很有挑战性，特别是深蹲部分，但是坚持下来了，感觉很有成就感。',
          photos: [
            { url: 'https://via.placeholder.com/150', name: 'photo1.jpg' }
          ],
          photosCount: 1
        }
      ]
    },
    viewCheckinDetail(checkin) {
      this.selectedCheckin = checkin
      this.checkinDetailVisible = true
    }
  }
}
</script>

<style scoped>
.training-container {
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

.training-tabs {
  margin-top: 20px;
}

.plan-controls {
  margin-bottom: 20px;
}

.day-card {
  margin-bottom: 20px;
}

.day-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.day-title {
  font-weight: bold;
}

.day-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  background: #f0f0f0;
}

.day-status.completed {
  background: #67C23A;
  color: white;
}

.exercise-list {
  margin-top: 10px;
}

.exercise-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.exercise-info h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.exercise-info p {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

.checkin-form {
  margin-top: 20px;
}

.photo-grid {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.photo-item {
  width: 100px;
  height: 100px;
  overflow: hidden;
  border-radius: 4px;
}

.photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-item {
  margin-bottom: 15px;
}

.detail-label {
  font-weight: bold;
  margin-right: 10px;
}

.detail-value {
  color: #606266;
}
</style>