<template>
  <div class="user-dashboard">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="balance-card">
            <h3>账户余额</h3>
            <div class="balance-amount">¥{{ userInfo.balance || 0 }}</div>
            <el-button type="primary" @click="$router.push('/user/recharge')">充值</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的订单</h3>
            <div class="stat-number">{{ orderCount }}</div>
            <el-button type="text" @click="$router.push('/user/course')">查看课程</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-card">
            <h3>我的预约</h3>
            <div class="stat-number">{{ appointmentCount }}</div>
            <el-button type="text" @click="$router.push('/user/appointment')">查看预约</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card style="margin-top: 20px">
      <div slot="header">系统公告</div>
      <el-table :data="noticeList" border>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 2 ? 'danger' : 'info'">
              {{ scope.row.type === 2 ? '重要' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getNoticeList } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'UserDashboard',
  data() {
    return {
      noticeList: [],
      orderCount: 0,
      appointmentCount: 0
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadNoticeList()
  },
  methods: {
    loadNoticeList() {
      getNoticeList().then(res => {
        this.noticeList = res.data.filter(n => n.status === 1).slice(0, 5)
      })
    }
  }
}
</script>

<style scoped>
.user-dashboard {
  padding: 20px;
}

.balance-card, .stat-card {
  text-align: center;
  padding: 20px;
}

.balance-amount {
  font-size: 36px;
  color: #409EFF;
  font-weight: bold;
  margin: 20px 0;
}

.stat-number {
  font-size: 32px;
  color: #67c23a;
  font-weight: bold;
  margin: 20px 0;
}

h3 {
  color: #666;
  margin-bottom: 10px;
}
</style>

