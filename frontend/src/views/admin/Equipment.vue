<template>
  <div class="equipment-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>器材管理</span>
        <el-button type="primary" @click="handleAdd">新增器材</el-button>
      </div>
      
      <el-table :data="equipmentList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="name" label="器材名称"></el-table-column>
        <el-table-column prop="type" label="类型"></el-table-column>
        <el-table-column prop="totalCount" label="总数" width="100"></el-table-column>
        <el-table-column prop="availableCount" label="可用数" width="100"></el-table-column>
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
      <el-form :model="equipmentForm" :rules="rules" ref="equipmentForm" label-width="100px">
        <el-form-item label="器材名称" prop="name">
          <el-input v-model="equipmentForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input v-model="equipmentForm.type"></el-input>
        </el-form-item>
        <el-form-item label="总数" prop="totalCount">
          <el-input-number v-model="equipmentForm.totalCount" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="可用数" prop="availableCount">
          <el-input-number v-model="equipmentForm.availableCount" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="equipmentForm.description" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="equipmentForm.status">
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
import { getEquipmentList, saveEquipment, deleteEquipment } from '@/api'

export default {
  name: 'AdminEquipment',
  data() {
    return {
      equipmentList: [],
      dialogVisible: false,
      dialogTitle: '新增器材',
      equipmentForm: {
        id: null,
        name: '',
        type: '',
        totalCount: 1,
        availableCount: 1,
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入器材名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadEquipmentList()
  },
  methods: {
    loadEquipmentList() {
      getEquipmentList().then(res => {
        this.equipmentList = res.data.sort((a, b) => a.id - b.id)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增器材'
      this.equipmentForm = {
        id: null,
        name: '',
        type: '',
        totalCount: 1,
        availableCount: 1,
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑器材'
      this.equipmentForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.equipmentForm.validate((valid) => {
        if (valid) {
          saveEquipment(this.equipmentForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadEquipmentList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteEquipment(id).then(() => {
          this.$message.success('删除成功')
          this.loadEquipmentList()
        })
      })
    }
  }
}
</script>

<style scoped>
.equipment-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

