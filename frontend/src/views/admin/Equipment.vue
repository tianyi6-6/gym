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
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleStatusChange(scope.row)">状态修改</el-button>
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
          <el-select v-model="equipmentForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="正常空闲" :value="1"></el-option>
            <el-option label="维修保养中" :value="2"></el-option>
            <el-option label="使用中" :value="3"></el-option>
            <el-option label="禁用" :value="0"></el-option>
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
    },
    // 处理状态修改
    handleStatusChange(row) {
      this.dialogTitle = '修改器材状态'
      this.equipmentForm = { ...row }
      this.dialogVisible = true
    },
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        0: '禁用',
        1: '正常空闲',
        2: '维修保养中',
        3: '使用中'
      }
      return statusMap[status] || '未知'
    },
    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        0: 'danger',
        1: 'success',
        2: 'warning',
        3: 'info'
      }
      return typeMap[status] || 'info'
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

