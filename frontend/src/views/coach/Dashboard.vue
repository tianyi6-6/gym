<template>
  <div class="coach-dashboard">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的课程</h3>
            <div class="stat-number">{{ courseCount }}</div>
            <el-button type="text">查看详情</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的预约</h3>
            <div class="stat-number">{{ appointmentCount }}</div>
            <el-button type="text" @click="$router.push('/coach/appointment')">查看预约</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的经验</h3>
            <div class="stat-number">{{ experienceCount }}</div>
            <el-button type="text" @click="$router.push('/coach/experience')">查看经验</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'CoachDashboard',
  data() {
    return {
      courseCount: 0,
      appointmentCount: 0,
      experienceCount: 0
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      // 这里应该调用API获取数据
      this.courseCount = 5
      this.appointmentCount = 10
      this.experienceCount = 3
    }
  }
}
</script>

<style scoped>
.coach-dashboard {
  padding: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  color: #409EFF;
  font-weight: bold;
  margin: 20px 0;
}

h3 {
  color: #666;
  margin-bottom: 10px;
}
</style>

