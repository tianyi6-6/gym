<template>
  <div class="course-page">
    <el-card>
      <div slot="header">课程中心</div>
      <el-row :gutter="20">
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
                <el-button type="primary" @click="handleBuy(course)">购买课程</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
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
import { getCoursePage, getCourseCount } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'UserCourse',
  data() {
    return {
      courseList: [],
      currentPage: 1,
      pageSize: 9,
      total: 0
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadCourseList()
  },
  methods: {
    loadCourseList() {
      getCoursePage(this.currentPage, this.pageSize).then(res => {
        this.courseList = res.data.filter(c => c.status === 1)
      })
      getCourseCount().then(res => {
        this.total = res.data
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.loadCourseList()
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.loadCourseList()
    },
    handleBuy(course) {
      this.$confirm(`确定要购买课程"${course.name}"吗？价格：¥${course.price}`, '确认购买', {
        type: 'warning'
      }).then(() => {
        // 这里应该调用购买API
        this.$message.success('购买成功')
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
  display: box;
  line-clamp: 2;
  box-orient: vertical;
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
}
</style>

