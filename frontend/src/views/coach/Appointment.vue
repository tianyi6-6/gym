<template>
  <div class="appointment-page">
    <el-card>
      <div slot="header">我的预约</div>
      
      <el-table :data="appointmentList" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="appointmentTime" label="预约时间" width="180"></el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="handleConfirm(scope.row)" v-if="scope.row.status === 0">确认</el-button>
            <el-button size="mini" type="danger" @click="handleCancel(scope.row)" v-if="scope.row.status === 0">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CoachAppointment',
  data() {
    return {
      appointmentList: []
    }
  },
  mounted() {
    this.loadAppointmentList()
  },
  methods: {
    loadAppointmentList() {
      // 这里应该调用API
      this.appointmentList = []
    },
    handleConfirm(row) {
      this.$confirm('确定要确认这个预约吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$message.success('确认成功')
        this.loadAppointmentList()
      })
    },
    handleCancel(row) {
      this.$confirm('确定要拒绝这个预约吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$message.success('已拒绝')
        this.loadAppointmentList()
      })
    },
    getStatusType(status) {
      const map = { 0: 'warning', 1: 'success', 2: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 0: '待确认', 1: '已确认', 2: '已取消' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.appointment-page {
  padding: 20px;
}
</style>

