<template>
  <div class="coach-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>教练管理</span>
        <el-button type="primary" @click="handleAdd">新增教练</el-button>
      </div>
      
      <el-table :data="coachList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="specialty" label="专长"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
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
      <el-form :model="coachForm" :rules="rules" ref="coachForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="coachForm.username" :disabled="coachForm.id != null"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!coachForm.id">
          <el-input v-model="coachForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="coachForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="coachForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-input v-model="coachForm.specialty"></el-input>
        </el-form-item>
        <el-form-item label="经验介绍" prop="experience">
          <el-input type="textarea" v-model="coachForm.experience" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="coachForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
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
import { getCoachList, saveCoach, deleteCoach } from '@/api'

export default {
  name: 'AdminCoach',
  data() {
    return {
      coachList: [],
      dialogVisible: false,
      dialogTitle: '新增教练',
      coachForm: {
        id: null,
        username: '',
        password: '',
        name: '',
        phone: '',
        specialty: '',
        experience: '',
        status: 1
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCoachList()
  },
  methods: {
    loadCoachList() {
      getCoachList().then(res => {
        this.coachList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增教练'
      this.coachForm = {
        id: null,
        username: '',
        password: '',
        name: '',
        phone: '',
        specialty: '',
        experience: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑教练'
      this.coachForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.coachForm.validate((valid) => {
        if (valid) {
          saveCoach(this.coachForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadCoachList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteCoach(id).then(() => {
          this.$message.success('删除成功')
          this.loadCoachList()
        })
      })
    }
  }
}
</script>

<style scoped>
.coach-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

