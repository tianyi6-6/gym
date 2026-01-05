<template>
  <div class="course-schedule">
    <div class="page-header">
      <h2>课程安排</h2>
      <el-button type="primary" @click="showAddDialog">新增课程安排</el-button>
    </div>

    <el-table :data="scheduleList" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="courseName" label="课程名称" width="180"></el-table-column>
      <el-table-column prop="scheduleDate" label="日期" width="150">
        <template slot-scope="scope">
          {{ formatDate(scope.row.scheduleDate) }}
        </template>
      </el-table-column>
      <el-table-column label="时间段" width="200">
        <template slot-scope="scope">
          {{ formatTime(scope.row.startTime) }} - {{ formatTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="地点" width="120"></el-table-column>
      <el-table-column prop="maxStudents" label="最大人数" width="100"></el-table-column>
      <el-table-column prop="currentStudents" label="当前人数" width="100"></el-table-column>
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
        <el-form-item label="日期" prop="scheduleDate">
          <el-date-picker
            v-model="form.scheduleDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            style="width: 100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="form.startTime"
            placeholder="选择开始时间"
            value-format="HH:mm:ss"
            style="width: 100%">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="form.endTime"
            placeholder="选择结束时间"
            value-format="HH:mm:ss"
            style="width: 100%">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入地点"></el-input>
        </el-form-item>
        <el-form-item label="最大人数" prop="maxStudents">
          <el-input-number v-model="form.maxStudents" :min="1" :max="50"></el-input-number>
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
  name: 'CourseSchedule',
  data() {
    return {
      scheduleList: [],
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增课程安排',
      form: {
        id: null,
        coachId: null,
        courseId: null,
        courseName: '',
        scheduleDate: '',
        startTime: '',
        endTime: '',
        location: '',
        maxStudents: 10,
        currentStudents: 0,
        status: 1
      },
      rules: {
        courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        scheduleDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        location: [{ required: true, message: '请输入地点', trigger: 'blur' }],
        maxStudents: [{ required: true, message: '请输入最大人数', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      if (!this.userInfo.id) return
      
      this.loading = true
      this.$axios.get(`/coach-course-schedule/coach/${this.userInfo.id}`)
        .then(res => {
          if (res.code === 200) {
            this.scheduleList = res.data || []
            this.$store.dispatch('updateCoachCourseSchedule', this.scheduleList)
          } else {
            this.$message.error(res.message || '获取课程安排失败')
          }
        })
        .catch(error => {
          console.error('获取课程安排失败:', error)
          this.$message.error('获取课程安排失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    showAddDialog() {
      this.dialogTitle = '新增课程安排'
      this.form = {
        id: null,
        coachId: this.userInfo.id,
        courseId: null,
        courseName: '',
        scheduleDate: '',
        startTime: '',
        endTime: '',
        location: '',
        maxStudents: 10,
        currentStudents: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑课程安排'
      this.form = {
        id: row.id,
        coachId: row.coachId,
        courseId: row.courseId,
        courseName: row.courseName,
        scheduleDate: this.formatDate(row.scheduleDate),
        startTime: this.formatTime(row.startTime),
        endTime: this.formatTime(row.endTime),
        location: row.location,
        maxStudents: row.maxStudents,
        currentStudents: row.currentStudents,
        status: row.status
      }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios.post('/coach-course-schedule/save', this.form)
            .then(res => {
              if (res.code === 200) {
                this.$message.success('保存成功')
                this.dialogVisible = false
                this.loadData()
                
                if (this.form.id) {
                  this.$store.dispatch('updateCourseScheduleItem', this.form)
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
      this.$confirm('确定要删除该课程安排吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/coach-course-schedule/${row.id}`)
          .then(res => {
            if (res.code === 200) {
              this.$message.success('删除成功')
              this.$store.dispatch('deleteCourseScheduleItem', row.id)
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
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toISOString().split('T')[0]
    },
    formatTime(time) {
      if (!time) return ''
      const t = new Date(time)
      return t.toTimeString().split(' ')[0].substring(0, 5)
    }
  }
}
</script>

<style scoped>
.course-schedule {
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
