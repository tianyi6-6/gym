<template>
  <div class="user-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>用户管理</span>
        <el-button type="primary" @click="handleAdd">新增用户</el-button>
      </div>
      
      <el-table :data="userList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column label="密码" width="120">
          <template slot-scope="scope">
            <span>••••••</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="balance" label="余额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.balance }}
          </template>
        </el-table-column>
        <el-table-column prop="memberLevel" label="会员等级" width="120">
          <template slot-scope="scope">
            {{ scope.row.memberLevel || '普通会员' }}
          </template>
        </el-table-column>
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
      <el-form :model="userForm" :rules="rules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="userForm.id != null"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="余额" prop="balance">
          <el-input-number v-model="userForm.balance" :min="0" :max="99999999.99" :precision="2" :step="100" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="会员等级" prop="memberLevel">
          <el-select v-model="userForm.memberLevel" placeholder="请选择会员等级" style="width: 100%">
            <el-option label="普通会员" value="普通会员"></el-option>
            <el-option label="银卡会员" value="银卡会员"></el-option>
            <el-option label="金卡会员" value="金卡会员"></el-option>
            <el-option label="钻石会员" value="钻石会员"></el-option>
          </el-select>
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
import { getUserList, saveUser, deleteUser } from '@/api'

export default {
  name: 'AdminUser',
  data() {
    return {
      userList: [],
      dialogVisible: false,
      dialogTitle: '新增用户',
      userForm: {
        id: null,
        username: '',
        password: '',
        name: '',
        phone: '',
        balance: 0,
        status: 1,
        memberLevel: '普通会员'
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的11位手机号码', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadUserList()
  },
  methods: {
    loadUserList() {
      getUserList().then(res => {
        this.userList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.userForm = {
        id: null,
        username: '',
        password: '',
        name: '',
        phone: '',
        balance: 0,
        status: 1,
        memberLevel: '普通会员'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.userForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          saveUser(this.userForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadUserList()
          }).catch(error => {
            console.error('保存失败:', error)
            this.$message.error(error.response?.data?.message || '保存失败，请稍后重试')
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteUser(id).then(() => {
          this.$message.success('删除成功')
          this.loadUserList()
        })
      })
    }
  }
}
</script>

<style scoped>
.user-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

