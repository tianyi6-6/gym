<template>
  <div class="course-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>课程管理</span>
        <el-button type="primary" @click="handleAdd">新增课程</el-button>
      </div>
      
      <el-table :data="courseList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="coachName" label="教练"></el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="courseForm" :rules="rules" ref="courseForm" label-width="100px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="courseForm.name"></el-input>
        </el-form-item>
        <el-form-item label="教练" prop="coachId">
          <el-select v-model="courseForm.coachId" placeholder="请选择教练" style="width: 100%">
            <el-option
              v-for="coach in coachList"
              :key="coach.id"
              :label="coach.name"
              :value="coach.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="courseForm.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="courseForm.duration" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="courseForm.description" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="courseForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCourseList, saveCourse, deleteCourse, getCoachList } from '@/api'

export default {
  name: 'AdminCourse',
  data() {
    return {
      courseList: [],
      coachList: [],
      dialogVisible: false,
      dialogTitle: '新增课程',
      courseForm: {
        id: null,
        name: '',
        coachId: null,
        price: 0,
        duration: 60,
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        coachId: [{ required: true, message: '请选择教练', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCourseList()
    this.loadCoachList()
  },
  methods: {
    loadCourseList() {
      getCourseList().then(res => {
        this.courseList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    loadCoachList() {
      getCoachList().then(res => {
        this.coachList = res.data
      })
    },
    handleAdd() {
      this.dialogTitle = '新增课程'
      this.courseForm = {
        id: null,
        name: '',
        coachId: null,
        price: 0,
        duration: 60,
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑课程'
      this.courseForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.courseForm.validate((valid) => {
        if (valid) {
          saveCourse(this.courseForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadCourseList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteCourse(id).then(() => {
          this.$message.success('删除成功')
          this.loadCourseList()
        })
      })
    }
  }
}
</script>

<style scoped>
.course-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

