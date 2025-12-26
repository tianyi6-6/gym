<template>
  <div class="experience-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>我的经验</span>
        <el-button type="primary" @click="handleAdd">发布经验</el-button>
      </div>
      
      <el-table :data="experienceList" border>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="viewCount" label="浏览次数" width="120"></el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form :model="experienceForm" :rules="rules" ref="experienceForm" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="experienceForm.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="experienceForm.content" :rows="10"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="experienceForm.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
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
import { getExperienceList, saveExperience, deleteExperience } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'CoachExperience',
  data() {
    return {
      experienceList: [],
      dialogVisible: false,
      dialogTitle: '发布经验',
      experienceForm: {
        id: null,
        title: '',
        content: '',
        status: 1,
        coachId: null
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadExperienceList()
  },
  methods: {
    loadExperienceList() {
      if (this.userInfo.id) {
        getExperienceList().then(res => {
          this.experienceList = res.data.filter(e => e.coachId === this.userInfo.id)
        })
      }
    },
    handleAdd() {
      this.dialogTitle = '发布经验'
      this.experienceForm = {
        id: null,
        title: '',
        content: '',
        status: 1,
        coachId: this.userInfo.id
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑经验'
      this.experienceForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.experienceForm.validate((valid) => {
        if (valid) {
          saveExperience(this.experienceForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadExperienceList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteExperience(id).then(() => {
          this.$message.success('删除成功')
          this.loadExperienceList()
        })
      })
    }
  }
}
</script>

<style scoped>
.experience-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

