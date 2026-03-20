<template>
  <div class="course-page">
    <el-card>
      <div slot="header">课程中心</div>
      <!-- 筛选条件 -->
      <div class="filter-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="filter.coachId" placeholder="选择教练" clearable @change="handleFilterChange">
              <el-option
                v-for="coach in coachList"
                :key="coach.id"
                :label="coach.name"
                :value="coach.id"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filter.priceRange" placeholder="价格范围" clearable @change="handleFilterChange">
              <el-option label="全部" value="" />
              <el-option label="0-200" value="0-200" />
              <el-option label="200-500" value="200-500" />
              <el-option label="500-1000" value="500-1000" />
              <el-option label="1000以上" value="1000+" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="filter.keyword"
              placeholder="搜索课程名称"
              clearable
              @keyup.enter="handleFilterChange"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleFilterChange" />
            </el-input>
          </el-col>
          <el-col :span="6" style="text-align: right;">
            <el-button type="info" @click="resetFilter">重置筛选</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 课程列表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8" v-for="course in courseList" :key="course.id" style="margin-bottom: 20px">
          <el-card :body-style="{ padding: '0px' }">
            <div class="course-image" :style="{ backgroundImage: 'url(' + (course.image || '/default.jpg') + ')' }"></div>
            <div style="padding: 14px;">
              <h3>{{ course.name }}</h3>
              <p class="course-desc">{{ course.description }}</p>
              <div class="course-info">
                <span>教练：{{ course.coachName }}</span>
                <span class="price">¥{{ course.price }}</span>
              </div>
              <div class="course-actions">
                <el-button type="primary" @click="handleBuy(course)" style="margin-right: 10px;">购买课程</el-button>
                <el-button type="success" @click="handleAppointment(course)">预约课程</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 无数据提示 -->
      <div v-if="courseList.length === 0" class="no-data">
        <el-empty description="暂无课程数据" />
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
import { getCoursePage, getCourseCount, getCoachList, createOrder } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'UserCourse',
  data() {
    return {
      courseList: [],
      coachList: [],
      currentPage: 1,
      pageSize: 9,
      total: 0,
      filter: {
        coachId: '',
        priceRange: '',
        keyword: ''
      }
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadCoachList()
    this.loadCourseList()
  },
  methods: {
    // 加载教练列表
    loadCoachList() {
      getCoachList().then(res => {
        this.coachList = res.data
      })
    },
    // 加载课程列表
    loadCourseList() {
      getCoursePage(this.currentPage, this.pageSize).then(res => {
        let courses = res.data.filter(c => c.status === 1)
        // 应用筛选条件
        courses = this.applyFilter(courses)
        this.courseList = courses
      })
      getCourseCount().then(res => {
        this.total = res.data
      })
    },
    // 应用筛选条件
    applyFilter(courses) {
      return courses.filter(course => {
        // 教练筛选
        if (this.filter.coachId && course.coachId !== this.filter.coachId) {
          return false
        }
        // 价格范围筛选
        if (this.filter.priceRange) {
          const [min, max] = this.filter.priceRange.split('-')
          if (max === '1000+') {
            if (course.price < 1000) {
              return false
            }
          } else if (course.price < min || course.price > max) {
            return false
          }
        }
        // 关键词筛选
        if (this.filter.keyword && !course.name.includes(this.filter.keyword)) {
          return false
        }
        return true
      })
    },
    // 处理筛选条件变化
    handleFilterChange() {
      this.currentPage = 1 // 重置到第一页
      this.loadCourseList()
    },
    // 重置筛选条件
    resetFilter() {
      this.filter = {
        coachId: '',
        priceRange: '',
        keyword: ''
      }
      this.currentPage = 1
      this.loadCourseList()
    },
    // 处理页码大小变化
    handleSizeChange(size) {
      this.pageSize = size
      this.loadCourseList()
    },
    // 处理页码变化
    handleCurrentChange(current) {
      this.currentPage = current
      this.loadCourseList()
    },
    // 处理购买课程
    handleBuy(course) {
      if (!this.userInfo) {
        this.$message.warning('请先登录')
        return
      }
      // 检查用户余额
      if (!this.userInfo.balance || this.userInfo.balance < course.price) {
        this.$message.error('账户余额不足，请先充值')
        return
      }
      // 检查课程状态
      if (course.status !== 1) {
        this.$message.error('课程状态异常，无法购买')
        return
      }
      this.$confirm(`确定要购买课程"${course.name}"吗？价格：¥${course.price}`, '确认购买', {
        type: 'warning'
      }).then(() => {
        // 创建订单
        const orderData = {
          userId: this.userInfo.id,
          courseId: course.id,
          price: course.price,
          status: 'Paid',
          paymentStatus: '已支付',
          orderNumber: 'ORD' + Date.now() + Math.floor(Math.random() * 1000)
        }
        this.$message({ type: 'success', message: '购买成功', duration: 3000 })
        // 模拟更新用户余额
        const updatedUserInfo = { ...this.userInfo }
        updatedUserInfo.balance -= course.price
        this.$store.commit('SET_USER_INFO', updatedUserInfo)
        // 实际项目中，这里应该调用后端API来创建订单和更新余额
        createOrder(orderData).then(res => {
          if (res.code === 200) {
            this.$message.success('订单创建成功')
          } else {
            this.$message.error('订单创建失败：' + res.message)
          }
        }).catch(err => {
          this.$message.error('订单创建失败：' + err.message)
        })
      })
    },
    // 处理预约课程
    handleAppointment(course) {
      if (!this.userInfo) {
        this.$message.warning('请先登录')
        return
      }
      this.$prompt('请输入预约时间（格式：YYYY-MM-DD HH:MM）', '预约课程', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '2023-01-01 10:00'
      }).then(({ value }) => {
        // 这里应该调用预约API
        this.$message.success('预约成功，预约时间：' + value)
      }).catch(() => {
        // 取消预约
      })
    }
  }
}
</script>

<style scoped>
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.filter-container {
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 10px;
}

.no-data {
  padding: 40px 0;
  text-align: center;
}
</style>

<style scoped>
.course-page {
  padding: 20px;
}

.course-image {
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
}

.course-desc {
  color: #666;
  font-size: 14px;
  margin: 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.course-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.course-actions {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}
</style>

