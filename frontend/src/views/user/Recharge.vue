<template>
  <div class="recharge-page">
    <el-card>
      <div slot="header">账户充值</div>
      <div class="balance-info">
        <h2>当前余额：¥{{ userInfo.balance || 0 }}</h2>
      </div>
      <el-form :model="rechargeForm" :rules="rules" ref="rechargeForm" label-width="100px" style="max-width: 500px; margin-top: 30px">
        <el-form-item label="充值金额" prop="amount">
          <el-input-number v-model="rechargeForm.amount" :min="1" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRecharge">充值</el-button>
        </el-form-item>
      </el-form>
      
      <el-divider></el-divider>
      
      <h3>充值记录</h3>
      <el-table :data="rechargeList" border style="margin-top: 20px">
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.type === 1 ? '#67c23a' : '#f56c6c' }">
              {{ scope.row.type === 1 ? '+' : '-' }}¥{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column prop="createTime" label="时间" width="180"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { recharge, getRechargeList } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'UserRecharge',
  data() {
    return {
      rechargeForm: {
        amount: 100
      },
      rules: {
        amount: [{ required: true, message: '请输入充值金额', trigger: 'blur' }]
      },
      rechargeList: []
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  mounted() {
    this.loadRechargeList()
  },
  methods: {
    loadRechargeList() {
      if (this.userInfo.id) {
        getRechargeList(this.userInfo.id).then(res => {
          this.rechargeList = res.data
        })
      }
    },
    handleRecharge() {
      this.$refs.rechargeForm.validate((valid) => {
        if (valid) {
          recharge({
            userId: this.userInfo.id,
            amount: this.rechargeForm.amount,
            type: 1,
            remark: '用户充值'
          }).then(() => {
            this.$message.success('充值成功')
            this.loadRechargeList()
            // 更新用户信息
            this.userInfo.balance = (this.userInfo.balance || 0) + this.rechargeForm.amount
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.recharge-page {
  padding: 20px;
}

.balance-info {
  text-align: center;
  padding: 30px;
  background: #f5f7fa;
  border-radius: 5px;
}

.balance-info h2 {
  color: #409EFF;
  font-size: 32px;
}
</style>

