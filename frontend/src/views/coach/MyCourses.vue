<template>
  <div class="my-courses">
    <div class="page-header">
      <h2>我的授课</h2>
      <el-button type="primary" @click="showAddDialog">新增授课</el-button>
    </div>

    <el-table :data="sortedCourseList" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="100" sortable :sort-orders="['ascending']">
        <template slot="header">
          <span>ID</span>
          <i class="el-icon-sort-up" style="margin-left: 5px; color: #409EFF"></i>
        </template>
      </el-table-column>
      <el-table-column prop="courseName" label="课程名称" width="180"></el-table-column>
      <el-table-column prop="courseType" label="课程类型" width="120"></el-table-column>
      <el-table-column prop="price" label="价格" width="100">
        <template slot-scope="scope">
          ¥{{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="duration" label="时长(分钟)" width="120"></el-table-column>
      <el-table-column prop="difficultyLevel" label="难度等级" width="100"></el-table-column>
      <el-table-column prop="studentCount" label="学员数量" width="100"></el-table-column>
      <el-table-column prop="totalSessions" label="总课时" width="100"></el-table-column>
      <el-table-column prop="rating" label="评分" width="100">
        <template slot-scope="scope">
          <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '进行中' : '已结束' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称"></el-input>
        </el-form-item>
        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="form.courseType" placeholder="请选择课程类型" style="width: 100%">
            <el-option label="力量训练" value="力量训练"></el-option>
            <el-option label="有氧运动" value="有氧运动"></el-option>
            <el-option label="瑜伽" value="瑜伽"></el-option>
            <el-option label="普拉提" value="普拉提"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="10"></el-input-number>
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="form.duration" :min="15" :max="180" :step="15"></el-input-number>
        </el-form-item>
        <el-form-item label="难度等级" prop="difficultyLevel">
          <el-select v-model="form.difficultyLevel" placeholder="请选择难度等级" style="width: 100%">
            <el-option label="初级" value="初级"></el-option>
            <el-option label="中级" value="中级"></el-option>
            <el-option label="高级" value="高级"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总课时" prop="totalSessions">
          <el-input-number v-model="form.totalSessions" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="3" placeholder="请输入课程描述"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">进行中</el-radio>
            <el-radio :label="0">已结束</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MyCourses',
  data() {
    return {
      courseList: [],
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增授课',
      form: {
        id: null,
        coachId: null,
        courseId: null,
        courseName: '',
        courseType: '',
        description: '',
        price: 0,
        duration: 60,
        difficultyLevel: '初级',
        studentCount: 0,
        totalSessions: 10,
        rating: 0,
        status: 1
      },
      rules: {
        courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        courseType: [{ required: true, message: '请选择课程类型', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        duration: [{ required: true, message: '请输入时长', trigger: 'blur' }],
        difficultyLevel: [{ required: true, message: '请选择难度等级', trigger: 'change' }],
        totalSessions: [{ required: true, message: '请输入总课时', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState(['userInfo']),
    sortedCourseList() {
      return [...this.courseList].sort((a, b) => a.id - b.id)
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      if (!this.userInfo.id) return
      
      this.loading = true
      this.$axios.get(`/coach-my-courses/coach/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200) {
            this.courseList = res.data || []
            this.$store.dispatch('updateCoachMyCourses', this.courseList)
          } else {
            this.$message.error(res.message || '获取课程列表失败')
          }
        })
        .catch(error => {
          console.error('获取课程列表失败:', error)
          this.$message.error('获取课程列表失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    showAddDialog() {
          this.dialogTitle = '新增授课'
          this.form = {
            id: null,
            coachId: this.userInfo.id,
            courseId: null,
            courseName: '',
            courseType: '',
            description: '',
            price: 0,
            duration: 60,
            difficultyLevel: '初级',
            studentCount: 0,
            totalSessions: 10,
            rating: 0,
            status: 1
          }
          this.dialogVisible = true
        },
    handleEdit(row) {
      this.dialogTitle = '编辑授课'
      this.form = {
        id: row.id,
        coachId: row.coachId,
        courseId: row.courseId,
        courseName: row.courseName,
        courseType: row.courseType,
        description: row.description,
        price: row.price,
        duration: row.duration,
        difficultyLevel: row.difficultyLevel,
        studentCount: row.studentCount,
        totalSessions: row.totalSessions,
        rating: row.rating,
        status: row.status
      }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios.post('/coach-my-courses/save', this.form)
            .then(res => {
              if (res.code === 200) {
                this.$message.success('保存成功')
                this.dialogVisible = false
                this.loadData()
                
                if (this.form.id) {
                  this.$store.dispatch('updateMyCoursesItem', this.form)
                }
              } else {
                this.$message.error(res.message || '保存失败')
              }
            })
            .catch(error => {
              console.error('保存失败:', error)
              this.$message.error('保存失败')
            })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该课程吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/coach-my-courses/${row.id}`)
          .then(res => {
            if (res.code === 200) {
              this.$message.success('删除成功')
              this.loadData()
            } else {
              this.$message.error(res.message || '删除失败')
            }
          })
          .catch(error => {
            console.error('删除失败:', error)
            this.$message.error('删除失败')
          })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.my-courses {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}
</style>
