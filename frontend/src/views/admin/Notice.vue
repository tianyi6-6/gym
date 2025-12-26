<template>
  <div class="notice-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>公告管理</span>
        <el-button type="primary" @click="handleAdd">新增公告</el-button>
      </div>
      
      <el-table :data="noticeList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.type === 2 ? 'danger' : 'info'">
              {{ scope.row.type === 2 ? '重要' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
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
      <el-form :model="noticeForm" :rules="rules" ref="noticeForm" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="noticeForm.title"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="noticeForm.type">
            <el-radio :label="1">普通</el-radio>
            <el-radio :label="2">重要</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="noticeForm.content" :rows="10"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="noticeForm.status">
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
import { getNoticeList, saveNotice, deleteNotice } from '@/api'

export default {
  name: 'AdminNotice',
  data() {
    return {
      noticeList: [],
      dialogVisible: false,
      dialogTitle: '新增公告',
      noticeForm: {
        id: null,
        title: '',
        content: '',
        type: 1,
        status: 1
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadNoticeList()
  },
  methods: {
    loadNoticeList() {
      getNoticeList().then(res => {
        this.noticeList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增公告'
      this.noticeForm = {
        id: null,
        title: '',
        content: '',
        type: 1,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑公告'
      this.noticeForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.noticeForm.validate((valid) => {
        if (valid) {
          saveNotice(this.noticeForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadNoticeList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteNotice(id).then(() => {
          this.$message.success('删除成功')
          this.loadNoticeList()
        })
      })
    }
  }
}
</script>

<style scoped>
.notice-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

