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
    
    <!-- 支付方式选择对话框 -->
    <el-dialog title="选择支付方式" :visible.sync="payDialogVisible" width="400px">
      <div v-if="selectedOrder">
        <div class="order-info">
          <p>订单号：{{ selectedOrder.orderNo }}</p>
          <p>金额：¥{{ selectedOrder.price }}</p>
        </div>
        <div class="payment-methods">
          <el-radio-group v-model="paymentMethod">
            <el-radio label="支付宝">
              <div class="payment-option">
                <span class="payment-icon">支付宝</span>
                <span class="payment-desc">推荐使用支付宝APP扫码支付</span>
              </div>
            </el-radio>
            <el-radio label="微信支付">
              <div class="payment-option">
                <span class="payment-icon">微信支付</span>
                <span class="payment-desc">推荐使用微信APP扫码支付</span>
              </div>
            </el-radio>
            <el-radio label="余额支付">
              <div class="payment-option">
                <span class="payment-icon">余额支付</span>
                <div class="payment-desc">
                  <span>使用账户余额支付</span>
                  <span class="balance-info">可用余额: ¥{{ userBalance.toFixed(2) }}</span>
                </div>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePaymentConfirm">确认支付</el-button>
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
      },
      // 支付方式选择对话框
      payDialogVisible: false,
      selectedOrder: null,
      paymentMethod: '',
      userBalance: 0
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
      this.selectedOrder = row
      // 获取用户余额
      this.getUserBalance(row.userId)
      this.payDialogVisible = true
    },
    getUserBalance(userId) {
      // 这里应该调用后端API获取用户余额
      // 为了演示，我们使用模拟数据
      this.$axios.get(`/user/${userId}`).then(res => {
        if (res.code === 200) {
          this.userBalance = res.data.balance
        }
      }).catch(error => {
        console.error('获取用户余额失败:', error)
        this.userBalance = 0
      })
    },
    handlePaymentConfirm() {
      if (!this.paymentMethod) {
        this.$message.error('请选择支付方式')
        return
      }
      
      // 余额支付验证
      if (this.paymentMethod === '余额支付') {
        if (this.userBalance < this.selectedOrder.price) {
          this.$message.error('余额不足，请选择其他支付方式')
          return
        }
      }
      
      // 模拟支付跳转
      this.$message.info(`选择了${this.paymentMethod}支付，正在处理支付...`)
      
      // 模拟支付成功后的回调
      setTimeout(() => {
        payOrder(this.selectedOrder.id).then(() => {
          this.$message.success('支付成功')
          this.payDialogVisible = false
          this.loadOrderList()
        })
      }, 1000)
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

.order-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.payment-methods {
  margin: 20px 0;
}

.payment-option {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin: 5px 0;
  transition: all 0.3s;
}

.payment-option:hover {
  border-color: #409EFF;
  background-color: #ecf5ff;
}

.payment-icon {
  font-size: 16px;
  font-weight: bold;
  margin-right: 10px;
  color: #303133;
}

.payment-desc {
  font-size: 14px;
  color: #909399;
  display: flex;
  flex-direction: column;
}

.balance-info {
  font-size: 12px;
  color: #67C23A;
  margin-top: 5px;
  font-weight: bold;
}
</style>
