<template>
  <div class="recipe-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>菜谱管理</span>
        <el-button type="primary" @click="handleAdd">新增菜谱</el-button>
      </div>
      
      <el-table :data="recipeList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="category" label="分类" width="120"></el-table-column>
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
      <el-form :model="recipeForm" :rules="rules" ref="recipeForm" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="recipeForm.title"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="recipeForm.category"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="recipeForm.content" :rows="10"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="recipeForm.status">
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
import { getRecipeList, saveRecipe, deleteRecipe } from '@/api'

export default {
  name: 'AdminRecipe',
  data() {
    return {
      recipeList: [],
      dialogVisible: false,
      dialogTitle: '新增菜谱',
      recipeForm: {
        id: null,
        title: '',
        category: '',
        content: '',
        status: 1
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadRecipeList()
  },
  methods: {
    loadRecipeList() {
      getRecipeList().then(res => {
        this.recipeList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增菜谱'
      this.recipeForm = {
        id: null,
        title: '',
        category: '',
        content: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑菜谱'
      this.recipeForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.recipeForm.validate((valid) => {
        if (valid) {
          saveRecipe(this.recipeForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadRecipeList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteRecipe(id).then(() => {
          this.$message.success('删除成功')
          this.loadRecipeList()
        })
      })
    }
  }
}
</script>

<style scoped>
.recipe-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

