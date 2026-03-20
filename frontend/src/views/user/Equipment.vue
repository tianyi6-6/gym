<template>
  <div class="equipment-page">
    <el-card>
      <div slot="header">器材预约</div>
      
      <!-- 器材列表 -->
      <el-row :gutter="20">
        <el-col :span="8" v-for="equipment in equipmentList" :key="equipment.id" style="margin-bottom: 20px">
          <el-card 
            :body-style="{ padding: '0px' }"
            :class="{ 'equipment-disabled': equipment.status !== 1 }"
          >
            <div class="equipment-image" :style="{ backgroundImage: 'url(' + (equipment.image || '/default.jpg') + ')' }"></div>
            <div style="padding: 14px;">
              <h3>{{ equipment.name }}</h3>
              <p class="equipment-desc">{{ equipment.description }}</p>
              <div class="equipment-info">
                <span>类型：{{ equipment.type }}</span>
                <span>可用数：{{ equipment.availableCount }}/{{ equipment.totalCount }}</span>
              </div>
              <div class="equipment-status">
                <el-tag :type="getStatusType(equipment.status)">
                  {{ getStatusText(equipment.status) }}
                </el-tag>
              </div>
              <div class="equipment-actions">
                <el-button 
                  type="primary" 
                  @click="handleReserve(equipment)"
                  :disabled="equipment.status !== 1"
                >
                  预约
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 预约对话框 -->
    <el-dialog title="预约器材" :visible.sync="reserveDialogVisible" width="400px">
      <el-form :model="reserveForm" :rules="rules" ref="reserveForm" label-width="100px">
        <el-form-item label="器材名称">
          {{ reserveForm.equipmentName }}
        </el-form-item>
        <el-form-item label="预约时间" prop="reserveTime">
          <el-date-picker
            v-model="reserveForm.reserveTime"
            type="datetime"
            placeholder="选择预约时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="预约时长" prop="duration">
          <el-input-number v-model="reserveForm.duration" :min="30" :max="180" :step="30" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="reserveForm.remark" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reserveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReserveSubmit">提交预约</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getEquipmentList } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'UserEquipment',
  data() {
    return {
      equipmentList: [],
      reserveDialogVisible: false,
      reserveForm: {
        equipmentId: null,
        equipmentName: '',
        reserveTime: new Date(),
        duration: 60,
        remark: ''
      },
      rules: {
        reserveTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
        duration: [{ required: true, message: '请输入预约时长', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadEquipmentList()
  },
  methods: {
    // 加载器材列表
    loadEquipmentList() {
      getEquipmentList().then(res => {
        this.equipmentList = res.data
      })
    },
    // 处理预约
    handleReserve(equipment) {
      if (!this.userInfo) {
        this.$message.warning('请先登录')
        return
      }
      this.reserveForm = {
        equipmentId: equipment.id,
        equipmentName: equipment.name,
        reserveTime: new Date(),
        duration: 60,
        remark: ''
      }
      this.reserveDialogVisible = true
    },
    // 提交预约
    handleReserveSubmit() {
      this.$refs.reserveForm.validate((valid) => {
        if (valid) {
          // 模拟预约操作
          // 实际项目中应该调用API保存预约
          setTimeout(() => {
            this.$message.success('预约成功')
            this.reserveDialogVisible = false
            this.loadEquipmentList()
          }, 500)
        }
      })
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

.equipment-image {
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
}

.equipment-desc {
  color: #666;
  font-size: 14px;
  margin: 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.equipment-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
  font-size: 14px;
}

.equipment-status {
  margin: 10px 0;
}

.equipment-actions {
  margin-top: 10px;
}

.equipment-disabled {
  opacity: 0.6;
  filter: grayscale(50%);
}

.dialog-footer {
  text-align: right;
}
</style>