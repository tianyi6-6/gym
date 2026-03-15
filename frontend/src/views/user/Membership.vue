<template>
  <div class="membership-container">
    <el-card class="card">
      <template slot="header">
        <div class="card-header">
          <h2>会员等级与权益</h2>
        </div>
      </template>
      
      <!-- 会员等级展示 -->
      <div class="membership-level">
        <el-card class="level-card">
          <div class="level-info">
            <div class="level-icon" :class="currentLevel.levelClass">
              <i class="el-icon-medal"></i>
            </div>
            <div class="level-details">
              <h3>{{ currentLevel.name }}</h3>
              <p>会员有效期至：{{ validityPeriod }}</p>
            </div>
          </div>
          
          <!-- 等级进度条 -->
          <div class="level-progress">
            <div class="progress-info">
              <span>当前进度</span>
              <span>{{ currentLevel.progress }}%</span>
            </div>
            <el-progress :percentage="currentLevel.progress" :color="currentLevel.color"></el-progress>
            <div class="progress-text">
              距离升级还需：{{ upgradeRequirements }} 积分
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 权益列表 -->
      <div class="benefits-section">
        <h3>会员权益</h3>
        <el-tabs v-model="activeTab" class="benefits-tabs">
          <el-tab-pane label="当前等级权益" name="current">
            <el-card v-for="(benefit, index) in currentLevel.benefits" :key="index" class="benefit-card">
              <div class="benefit-item">
                <div class="benefit-icon" :class="currentLevel.levelClass">
                  <i :class="benefit.icon"></i>
                </div>
                <div class="benefit-content">
                  <h4>{{ benefit.title }}</h4>
                  <p>{{ benefit.description }}</p>
                </div>
              </div>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="所有等级权益" name="all">
            <el-card v-for="(level, index) in membershipLevels" :key="index" class="level-benefit-card" :class="level.levelClass">
              <template slot="header">
                <div class="level-header">
                  <div class="level-badge" :class="level.levelClass">
                    <i class="el-icon-medal"></i>
                    <span>{{ level.name }}</span>
                  </div>
                </div>
              </template>
              <div class="benefits-list">
                <div v-for="(benefit, benefitIndex) in level.benefits" :key="benefitIndex" class="benefit-item">
                  <div class="benefit-icon" :class="level.levelClass">
                    <i :class="benefit.icon"></i>
                  </div>
                  <div class="benefit-content">
                    <h4>{{ benefit.title }}</h4>
                    <p>{{ benefit.description }}</p>
                  </div>
                </div>
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
      
      <!-- 权益说明文档 -->
      <div class="document-section">
        <h3>权益说明文档</h3>
        <el-card class="document-card">
          <div class="document-list">
            <div v-for="(document, index) in documents" :key="index" class="document-item">
              <div class="document-info">
                <h4>{{ document.title }}</h4>
                <p>{{ document.description }}</p>
              </div>
              <el-button type="primary" size="small" @click="downloadDocument(document)">
                下载
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserMembership',
  data() {
    return {
      activeTab: 'current',
      validityPeriod: '2024-12-31',
      upgradeRequirements: 500,
      membershipLevels: [
        {
          id: 1,
          name: '青铜会员',
          levelClass: 'bronze',
          color: '#CD7F32',
          progress: 0,
          benefits: [
            {
              icon: 'el-icon-time',
              title: '免费停车',
              description: '每次可免费停车2小时'
            },
            {
              icon: 'el-icon-ticket',
              title: '课程预约',
              description: '可预约7天内课程'
            },
            {
              icon: 'el-icon-present',
              title: '生日礼品',
              description: '生日当月赠送小礼品'
            }
          ]
        },
        {
          id: 2,
          name: '黄金会员',
          levelClass: 'gold',
          color: '#FFD700',
          progress: 60,
          benefits: [
            {
              icon: 'el-icon-time',
              title: '免费停车',
              description: '每次可免费停车4小时'
            },
            {
              icon: 'el-icon-ticket',
              title: '优先预约',
              description: '可预约14天内课程，享受优先预约权'
            },
            {
              icon: 'el-icon-present',
              title: '生日礼品',
              description: '生日当月赠送精美礼品'
            },
            {
              icon: 'el-icon-news',
              title: '专属活动',
              description: '可参与专属会员活动'
            }
          ]
        },
        {
          id: 3,
          name: '钻石会员',
          levelClass: 'diamond',
          color: '#B9F2FF',
          progress: 100,
          benefits: [
            {
              icon: 'el-icon-time',
              title: '免费停车',
              description: '每次可免费停车6小时'
            },
            {
              icon: 'el-icon-ticket',
              title: '优先预约',
              description: '可预约30天内课程，享受优先预约权'
            },
            {
              icon: 'el-icon-present',
              title: '生日礼品',
              description: '生日当月赠送豪华礼品'
            },
            {
              icon: 'el-icon-news',
              title: '专属活动',
              description: '可参与所有专属会员活动'
            },
            {
              icon: 'el-icon-user',
              title: '专属教练',
              description: '配备专属私人教练'
            }
          ]
        }
      ],
      documents: [
        {
          id: 1,
          title: '会员权益说明',
          description: '详细说明各等级会员的权益内容和使用规则'
        },
        {
          id: 2,
          title: '会员升级规则',
          description: '说明会员等级升级的条件和流程'
        },
        {
          id: 3,
          title: '会员服务条款',
          description: '会员服务的相关条款和注意事项'
        }
      ]
    }
  },
  computed: {
    currentLevel() {
      // 假设当前是黄金会员
      return this.membershipLevels[1]
    }
  },
  methods: {
    downloadDocument(document) {
      // 模拟下载文档
      this.$message.success(`开始下载：${document.title}`)
    }
  }
}
</script>

<style scoped>
.membership-container {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.membership-level {
  margin-bottom: 30px;
}

.level-card {
  padding: 20px;
}

.level-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.level-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  margin-right: 20px;
}

.level-icon.bronze {
  background: #CD7F32;
  color: white;
}

.level-icon.gold {
  background: #FFD700;
  color: white;
}

.level-icon.diamond {
  background: #B9F2FF;
  color: #1890FF;
}

.level-details h3 {
  margin: 0 0 5px 0;
  font-size: 20px;
  font-weight: bold;
}

.level-details p {
  margin: 0;
  color: #606266;
}

.level-progress {
  margin-top: 20px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
  color: #606266;
}

.progress-text {
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
  text-align: right;
}

.benefits-section {
  margin-bottom: 30px;
}

.benefits-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.benefits-tabs {
  margin-top: 20px;
}

.benefit-card {
  margin-bottom: 15px;
}

.benefit-item {
  display: flex;
  align-items: flex-start;
}

.benefit-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  margin-right: 15px;
  flex-shrink: 0;
}

.benefit-icon.bronze {
  background: #CD7F32;
  color: white;
}

.benefit-icon.gold {
  background: #FFD700;
  color: white;
}

.benefit-icon.diamond {
  background: #B9F2FF;
  color: #1890FF;
}

.benefit-content h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
  font-weight: bold;
}

.benefit-content p {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

.level-benefit-card {
  margin-bottom: 20px;
}

.level-header {
  display: flex;
  align-items: center;
}

.level-badge {
  display: flex;
  align-items: center;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
}

.level-badge.bronze {
  background: #CD7F32;
  color: white;
}

.level-badge.gold {
  background: #FFD700;
  color: white;
}

.level-badge.diamond {
  background: #B9F2FF;
  color: #1890FF;
}

.level-badge i {
  margin-right: 5px;
}

.benefits-list {
  margin-top: 15px;
}

.document-section {
  margin-bottom: 20px;
}

.document-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.document-card {
  padding: 20px;
}

.document-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.document-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 4px;
}

.document-info h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
  font-weight: bold;
}

.document-info p {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

@media (max-width: 768px) {
  .level-info {
    flex-direction: column;
    text-align: center;
  }
  
  .level-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .document-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .document-item .el-button {
    align-self: flex-end;
  }
}
</style>