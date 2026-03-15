<template>
  <div class="experience-page">
    <el-card>
      <div slot="header">经验管理</div>
      
      <el-table :data="experienceList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="userName" label="用户" width="120"></el-table-column>
        <el-table-column prop="coachName" label="教练" width="120"></el-table-column>
        <el-table-column prop="viewCount" label="浏览次数" width="120"></el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getExperienceList, deleteExperience } from '@/api'

export default {
  name: 'AdminExperience',
  data() {
    return {
      experienceList: []
    }
  },
  mounted() {
    this.loadExperienceList()
  },
  methods: {
    loadExperienceList() {
      getExperienceList().then(res => {
        this.experienceList = res.data.sort((a, b) => a.id - b.id)
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
</style>

