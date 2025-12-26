<template>
  <div class="appointment-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>我的预约</span>
        <el-button type="primary" @click="handleAdd">新增预约</el-button>
      </div>
      
      <el-table :data="appointmentList" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="coachName" label="教练"></el-table-column>
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
            <el-button size="mini" type="danger" @click="handleCancel(scope.row)" v-if="scope.row.status === 0">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserAppointment',
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
    handleAdd() {
      this.$message.info('预约功能开发中')
    },
    handleCancel(row) {
      this.$confirm('确定要取消预约吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$message.success('取消成功')
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

