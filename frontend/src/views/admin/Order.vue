<template>
  <div class="order-page">
    <el-card>
      <div slot="header">
        <span>订单管理</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">新增订单</el-button>
      </div>
      
      <el-table :data="orderList" border>
        <el-table-column prop="orderNo" label="订单号" width="200" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'orderNo', order: 'ascending'}"></el-table-column>
        <el-table-column prop="userName" label="用户" width="120"></el-table-column>
        <el-table-column prop="courseName" label="课程" width="150"></el-table-column>
        <el-table-column prop="price" label="金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="success" size="mini" @click="handlePay(scope.row)" v-if="scope.row.status === 0">支付</el-button>
            <el-button type="warning" size="mini" @click="handleCancel(scope.row)" v-if="scope.row.status === 0">取消</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="orderForm" :rules="rules" ref="orderForm" label-width="100px">
        <el-form-item label="用户" prop="userId">
          <el-select v-model="orderForm.userId" placeholder="请选择用户" style="width: 100%">
            <el-option v-for="user in userList" :key="user.id" :label="user.name" :value="user.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="orderForm.courseId" placeholder="请选择课程" style="width: 100%">
            <el-option v-for="course in courseList" :key="course.id" :label="course.name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="price">
          <el-input-number v-model="orderForm.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="isEdit">
          <el-select v-model="orderForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待支付" :value="0"></el-option>
            <el-option label="已支付" :value="1"></el-option>
            <el-option label="已取消" :value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrderList, getOrderById, createOrder, updateOrder, deleteOrder, cancelOrder, payOrder, getUserList, getCourseList } from '@/api'

export default {
  name: 'AdminOrder',
  data() {
    return {
      orderList: [],
      userList: [],
      courseList: [],
      dialogVisible: false,
      dialogTitle: '新增订单',
      isEdit: false,
      loading: false,
      orderForm: {
        id: null,
        userId: null,
        courseId: null,
        price: 0,
        status: 0
      },
      rules: {
        userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
        courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
        price: [{ required: true, message: '请输入金额', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadOrderList()
    this.loadUserList()
    this.loadCourseList()
  },
  methods: {
    loadOrderList() {
      getOrderList().then(res => {
        this.orderList = res.data.sort((a, b) => a.orderNo.localeCompare(b.orderNo))
      })
    },
    loadUserList() {
      getUserList().then(res => {
        this.userList = res.data
      })
    },
    loadCourseList() {
      getCourseList().then(res => {
        this.courseList = res.data
      })
    },
    handleAdd() {
      this.dialogTitle = '新增订单'
      this.isEdit = false
      this.orderForm = {
        id: null,
        userId: null,
        courseId: null,
        price: 0,
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑订单'
      this.isEdit = true
      getOrderById(row.id).then(res => {
        this.orderForm = res.data
        this.dialogVisible = true
      })
    },
    handleSubmit() {
      this.$refs.orderForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.isEdit) {
            updateOrder(this.orderForm).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadOrderList()
            }).finally(() => {
              this.loading = false
            })
          } else {
            createOrder(this.orderForm).then(() => {
              this.$message.success('创建成功')
              this.dialogVisible = false
              this.loadOrderList()
            }).finally(() => {
              this.loading = false
            })
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该订单吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteOrder(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadOrderList()
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认取消该订单吗？', '提示', {
        type: 'warning'
      }).then(() => {
        cancelOrder(row.id).then(() => {
          this.$message.success('取消成功')
          this.loadOrderList()
        })
      })
    },
    handlePay(row) {
      this.$confirm('确认支付该订单吗？', '提示', {
        type: 'warning'
      }).then(() => {
        payOrder(row.id).then(() => {
          this.$message.success('支付成功')
          this.loadOrderList()
        })
      })
    },
    getStatusType(status) {
      const map = { 0: 'warning', 1: 'success', 2: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 0: '待支付', 1: '已支付', 2: '已取消' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.order-page {
  padding: 20px;
}
</style>
