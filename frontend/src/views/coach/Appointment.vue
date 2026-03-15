<template>
  <div class="appointment-container">
    <el-card class="appointment-card">
      <template slot="header">
        <div class="card-header">
          <span>预约管理</span>
          <el-button type="primary" size="small" @click="refreshAppointments">
            <i class="el-icon-refresh"></i> 刷新
          </el-button>
        </div>
      </template>
      
      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="选择状态" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option label="待处理" value="0"></el-option>
              <el-option label="已确认" value="1"></el-option>
              <el-option label="已拒绝" value="2"></el-option>
              <el-option label="已完成" value="3"></el-option>
              <el-option label="已取消" value="4"></el-option>
            </el-select>
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
          <el-form-item label="用户姓名">
            <el-input v-model="filterForm.userName" placeholder="输入用户姓名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchAppointments">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 批量操作 -->
      <div class="batch-section" v-if="selectedAppointments.length > 0">
        <el-button type="primary" @click="handleBatchConfirm">批量确认</el-button>
        <el-button type="danger" @click="handleBatchReject">批量拒绝</el-button>
        <el-button @click="handleBatchDelete">批量删除</el-button>
      </div>
      
      <!-- 预约列表 -->
      <el-table
        :data="appointments"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="预约ID" width="100"></el-table-column>
        <el-table-column prop="userName" label="用户姓名" width="120"></el-table-column>
        <el-table-column label="预约时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.appointmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="100"></el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="handleView(scope.row)" v-if="scope.row.status === 0">查看</el-button>
            <el-button size="small" type="success" @click="handleConfirm(scope.row)" v-if="scope.row.status === 0">确认</el-button>
            <el-button size="small" type="danger" @click="handleReject(scope.row)" v-if="scope.row.status === 0">拒绝</el-button>
            <el-button size="small" type="info" @click="handleView(scope.row)" v-else>查看</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)" v-if="scope.row.status !== 3">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
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
    
    <!-- 查看预约对话框 -->
    <el-dialog
      title="预约详情"
      :visible.sync="viewDialogVisible"
      width="500px"
    >
      <el-form :model="currentAppointment" label-width="100px">
        <el-form-item label="用户姓名">
          {{ currentAppointment.userName }}
        </el-form-item>
        <el-form-item label="预约时间">
          {{ formatDateTime(currentAppointment.appointmentTime) }}
        </el-form-item>
        <el-form-item label="时长">
          {{ currentAppointment.duration }} 分钟
        </el-form-item>
        <el-form-item label="备注">
          {{ currentAppointment.remark || '无' }}
        </el-form-item>
        <el-form-item label="状态">
          <el-tag :type="getStatusType(currentAppointment.status)">
            {{ getStatusText(currentAppointment.status) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="创建时间">
          {{ formatDateTime(currentAppointment.createTime) }}
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    
    <!-- 拒绝预约对话框 -->
    <el-dialog
      title="拒绝预约"
      :visible.sync="rejectDialogVisible"
      width="400px"
    >
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            rows="3"
            placeholder="请输入拒绝原因"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CoachAppointment',
  data() {
    return {
      appointments: [],
      selectedAppointments: [],
      filterForm: {
        status: '',
        dateRange: [],
        userName: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      viewDialogVisible: false,
      rejectDialogVisible: false,
      currentAppointment: {},
      rejectForm: {
        reason: ''
      }
    }
  },
  mounted() {
    this.loadAppointments()
  },
  methods: {
    loadAppointments() {
      // 模拟加载数据
      // 实际项目中应该调用API获取数据
      const mockData = [
        {
          id: 1,
          userId: 1,
          coachId: 1,
          userName: '张三',
          appointmentTime: new Date('2024-12-01 10:00:00'),
          duration: 60,
          remark: '想了解健身计划',
          status: 0,
          createTime: new Date('2024-11-30 15:00:00')
        },
        {
          id: 2,
          userId: 2,
          coachId: 1,
          userName: '李四',
          appointmentTime: new Date('2024-12-01 14:00:00'),
          duration: 90,
          remark: '需要减脂指导',
          status: 1,
          createTime: new Date('2024-11-30 16:00:00')
        },
        {
          id: 3,
          userId: 3,
          coachId: 1,
          userName: '王五',
          appointmentTime: new Date('2024-12-02 09:00:00'),
          duration: 60,
          remark: '',
          status: 2,
          createTime: new Date('2024-11-30 17:00:00')
        }
      ]
      this.appointments = mockData
      this.pagination.total = mockData.length
    },
    refreshAppointments() {
      this.loadAppointments()
      this.$message.success('刷新成功')
    },
    searchAppointments() {
      // 模拟搜索功能
      this.$message.info('搜索功能已触发')
    },
    resetFilter() {
      this.filterForm = {
        status: '',
        dateRange: [],
        userName: ''
      }
    },
    handleSelectionChange(selection) {
      this.selectedAppointments = selection
    },
    handleBatchConfirm() {
      if (this.selectedAppointments.length === 0) {
        this.$message.warning('请选择要确认的预约')
        return
      }
      const ids = this.selectedAppointments.map(item => item.id)
      // 模拟批量确认
      this.$message.success('批量确认成功')
      this.loadAppointments()
    },
    handleBatchReject() {
      if (this.selectedAppointments.length === 0) {
        this.$message.warning('请选择要拒绝的预约')
        return
      }
      const ids = this.selectedAppointments.map(item => item.id)
      // 模拟批量拒绝
      this.$message.success('批量拒绝成功')
      this.loadAppointments()
    },
    handleBatchDelete() {
      if (this.selectedAppointments.length === 0) {
        this.$message.warning('请选择要删除的预约')
        return
      }
      this.$confirm('确定要删除选中的预约吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟批量删除
        this.$message.success('批量删除成功')
        this.loadAppointments()
      }).catch(() => {
        // 取消删除
      })
    },
    handleView(appointment) {
      this.currentAppointment = { ...appointment }
      this.viewDialogVisible = true
    },
    handleConfirm(appointment) {
      this.$confirm('确定要确认此预约吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        // 模拟确认操作
        this.$message.success('预约确认成功')
        this.loadAppointments()
      }).catch(() => {
        // 取消确认
      })
    },
    handleReject(appointment) {
      this.currentAppointment = { ...appointment }
      this.rejectForm.reason = ''
      this.rejectDialogVisible = true
    },
    confirmReject() {
      if (!this.rejectForm.reason) {
        this.$message.warning('请输入拒绝原因')
        return
      }
      // 模拟拒绝操作
      this.$message.success('预约拒绝成功')
      this.rejectDialogVisible = false
      this.loadAppointments()
    },
    handleDelete(appointment) {
      this.$confirm('确定要删除此预约吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟删除操作
        this.$message.success('预约删除成功')
        this.loadAppointments()
      }).catch(() => {
        // 取消删除
      })
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadAppointments()
    },
    handleCurrentChange(current) {
      this.pagination.currentPage = current
      this.loadAppointments()
    },
    formatDateTime(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    getStatusText(status) {
      const statusMap = {
        0: '待处理',
        1: '已确认',
        2: '已拒绝',
        3: '已完成',
        4: '已取消'
      }
      return statusMap[status] || '未知'
    },
    getStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'success',
        2: 'danger',
        3: 'warning',
        4: 'gray'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.appointment-container {
  padding: 20px;
}

.appointment-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.batch-section {
  margin-bottom: 20px;
  display: flex;
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
