<template>
  <div class="my-orders-page">
    <el-card>
      <div slot="header">我的订单</div>
      <!-- 筛选和搜索 -->
      <div class="filter-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filter.status" placeholder="订单状态" clearable @change="handleFilterChange">
              <el-option label="全部" value="" />
              <el-option label="已支付" value="Paid" />
              <el-option label="待支付" value="Pending" />
              <el-option label="已退款" value="Refunded" />
              <el-option label="已取消" value="Cancelled" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filter.dateRange" placeholder="日期范围" clearable @change="handleFilterChange">
              <el-option label="最近7天" value="7" />
              <el-option label="最近30天" value="30" />
              <el-option label="本月" value="month" />
              <el-option label="上月" value="lastMonth" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-input
              v-model="filter.keyword"
              placeholder="搜索课程名称或订单号"
              clearable
              @keyup.enter="handleFilterChange"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleFilterChange" />
            </el-input>
          </el-col>
          <el-col :span="4" style="text-align: right;">
            <el-button type="info" @click="resetFilter">重置筛选</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 订单列表 -->
      <el-table v-loading="loading" :data="orderList" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="orderNumber" label="订单号" width="180" />
        <el-table-column label="课程信息" width="280">
          <template slot-scope="scope">
            <div class="course-info">
              <img :src="scope.row.courseImage || '/default.jpg'" alt="课程图片" class="course-image" />
              <div class="course-details">
                <div class="course-name">{{ scope.row.courseName }}</div>
                <div class="course-coach">教练：{{ scope.row.coachName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="购买时间" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="订单金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="120" />
        <el-table-column prop="status" label="订单状态" width="120" />
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="viewOrderDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 无数据提示 -->
      <div v-if="!loading && orderList.length === 0" class="no-data">
        <el-empty description="暂无订单数据" />
      </div>
      <!-- 分页 -->
      <div class="pagination" v-if="total > 0">
        <el-pagination
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { getUserOrderList } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'MyOrders',
  data() {
    return {
      orderList: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filter: {
        status: '',
        dateRange: '',
        keyword: ''
      }
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadOrderList()
  },
  methods: {
    // 加载订单列表
    loadOrderList() {
      if (!this.userInfo || !this.userInfo.id) {
        this.$message.warning('请先登录')
        return
      }
      this.loading = true
      getUserOrderList(this.userInfo.id).then(res => {
        let orders = res.data
        // 应用筛选条件
        orders = this.applyFilter(orders)
        this.orderList = orders
        this.total = orders.length
        this.loading = false
      }).catch(err => {
        this.$message.error('加载订单失败：' + err.message)
        this.loading = false
      })
    },
    // 应用筛选条件
    applyFilter(orders) {
      return orders.filter(order => {
        // 状态筛选
        if (this.filter.status && order.status !== this.filter.status) {
          return false
        }
        // 关键词筛选
        if (this.filter.keyword) {
          const keyword = this.filter.keyword.toLowerCase()
          if (!order.courseName.toLowerCase().includes(keyword) && 
              !order.orderNumber.toLowerCase().includes(keyword)) {
            return false
          }
        }
        // 日期范围筛选
        if (this.filter.dateRange) {
          const orderDate = new Date(order.createTime)
          const now = new Date()
          let startDate
          
          switch (this.filter.dateRange) {
            case '7':
              startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
              break
            case '30':
              startDate = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
              break
            case 'month':
              startDate = new Date(now.getFullYear(), now.getMonth(), 1)
              break
            case 'lastMonth':
              startDate = new Date(now.getFullYear(), now.getMonth() - 1, 1)
              const endDate = new Date(now.getFullYear(), now.getMonth(), 0)
              if (orderDate > endDate) {
                return false
              }
              break
          }
          
          if (startDate && orderDate < startDate) {
            return false
          }
        }
        return true
      })
    },
    // 处理筛选条件变化
    handleFilterChange() {
      this.currentPage = 1 // 重置到第一页
      this.loadOrderList()
    },
    // 重置筛选条件
    resetFilter() {
      this.filter = {
        status: '',
        dateRange: '',
        keyword: ''
      }
      this.currentPage = 1
      this.loadOrderList()
    },
    // 处理页码大小变化
    handleSizeChange(size) {
      this.pageSize = size
      this.loadOrderList()
    },
    // 处理页码变化
    handleCurrentChange(current) {
      this.currentPage = current
      this.loadOrderList()
    },
    // 查看订单详情
    viewOrderDetail(order) {
      this.$alert(`订单详情：\n订单号：${order.orderNumber}\n课程：${order.courseName}\n教练：${order.coachName}\n金额：¥${order.price}\n购买时间：${this.formatDate(order.createTime)}\n支付状态：${order.paymentStatus}\n订单状态：${order.status}`, '订单详情', {
        confirmButtonText: '确定'
      })
    },
    // 格式化日期
    formatDate(dateString) {
      const date = new Date(dateString)
      return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.no-data {
  padding: 40px 0;
  text-align: center;
}

.course-info {
  display: flex;
  align-items: center;
}

.course-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  margin-right: 10px;
  object-fit: cover;
}

.course-details {
  flex: 1;
}

.course-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.course-coach {
  font-size: 12px;
  color: #666;
}
</style>

<style scoped>
.my-orders-page {
  padding: 20px;
}
</style>