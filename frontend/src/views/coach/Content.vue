<template>
  <div class="content-container">
    <el-card class="content-card">
      <template slot="header">
        <div class="card-header">
          <span>专业健身内容管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <i class="el-icon-plus"></i> 新增内容
          </el-button>
        </div>
      </template>
      
      <!-- 搜索和筛选 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="选择状态" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option label="草稿" value="0"></el-option>
              <el-option label="已发布" value="1"></el-option>
              <el-option label="已下架" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关键词">
            <el-input v-model="filterForm.keyword" placeholder="输入标题或摘要" clearable></el-input>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchContents">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 内容列表 -->
      <el-table
        :data="contents"
        style="width: 100%"
        stripe
        border
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="authorName" label="作者" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="统计" width="200">
          <template slot-scope="scope">
            <div class="stats">
              <span>浏览: {{ scope.row.viewCount }}</span>
              <span>点赞: {{ scope.row.likeCount }}</span>
              <span>收藏: {{ scope.row.collectCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px">
      <el-form :model="contentForm" :rules="rules" ref="contentForm" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="contentForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
            v-model="contentForm.summary"
            type="textarea"
            rows="3"
            placeholder="请输入摘要"
          ></el-input>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverImage">
          <el-upload
            class="upload-demo"
            action="/api/upload"
            :on-success="handleImageUpload"
            :before-upload="beforeUpload"
            :limit="1"
            :file-list="fileList"
          >
            <el-button size="small" type="primary">上传图片</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <!-- 这里使用富文本编辑器，实际项目中需要集成第三方编辑器 -->
            <el-input
              v-model="contentForm.content"
              type="textarea"
              rows="10"
              placeholder="请输入内容"
            ></el-input>
          </div>
        </el-form-item>
        <el-form-item label="标签" prop="tagIds">
          <el-select
            v-model="contentForm.tagIds"
            multiple
            placeholder="选择标签"
            style="width: 100%"
          >
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="contentForm.status" style="width: 100%">
            <el-option label="草稿" value="0"></el-option>
            <el-option label="已发布" value="1"></el-option>
            <el-option label="已下架" value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
        <el-button type="success" @click="handlePreview" v-if="contentForm.id">预览</el-button>
      </span>
    </el-dialog>
    
    <!-- 查看对话框 -->
    <el-dialog title="内容详情" :visible.sync="viewDialogVisible" width="900px">
      <div class="content-detail">
        <h2 class="content-title">{{ viewData.title }}</h2>
        <div class="content-meta">
          <span>作者：{{ viewData.authorName }}</span>
          <span>发布时间：{{ formatDate(viewData.publishTime) }}</span>
          <span>浏览：{{ viewData.viewCount }}</span>
          <span>点赞：{{ viewData.likeCount }}</span>
        </div>
        <div class="content-tags">
          <el-tag v-for="tag in viewTags" :key="tag.id" size="small" class="tag-item">
            {{ tag.name }}
          </el-tag>
        </div>
        <div class="content-body" v-html="viewData.content"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    
    <!-- 预览对话框 -->
    <el-dialog title="内容预览" :visible.sync="previewDialogVisible" width="900px">
      <div class="content-detail">
        <h2 class="content-title">{{ contentForm.title }}</h2>
        <div class="content-meta">
          <span>作者：{{ contentForm.authorName }}</span>
          <span>预览模式</span>
        </div>
        <div class="content-tags">
          <el-tag v-for="tagId in contentForm.tagIds" :key="tagId" size="small" class="tag-item">
            {{ getTagName(tagId) }}
          </el-tag>
        </div>
        <div class="content-body" v-html="contentForm.content"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CoachContent',
  data() {
    return {
      contents: [],
      tags: [],
      filterForm: {
        status: '',
        keyword: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      viewDialogVisible: false,
      previewDialogVisible: false,
      dialogTitle: '新增内容',
      contentForm: {
        id: null,
        title: '',
        content: '',
        summary: '',
        coverImage: '',
        authorId: 1, // 假设当前教练ID为1
        authorName: '教练', // 假设当前教练名称
        status: 0,
        tagIds: []
      },
      viewData: {},
      viewTags: [],
      fileList: [],
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        summary: [{ required: true, message: '请输入摘要', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadContents()
    this.loadTags()
  },
  methods: {
    loadContents() {
      // 模拟加载数据
      const mockData = [
        {
          id: 1,
          title: '健身新手入门指南',
          content: '<p>这是一篇关于健身新手入门的详细指南，包括如何开始健身计划、选择适合的器材、制定合理的饮食计划等内容。</p>',
          summary: '健身新手入门的全面指南，帮助你快速开始健身之旅',
          coverImage: '',
          authorId: 1,
          authorName: '教练',
          status: 1,
          viewCount: 120,
          likeCount: 25,
          collectCount: 15,
          shareCount: 8,
          createTime: new Date('2024-11-01 10:00:00'),
          publishTime: new Date('2024-11-01 10:00:00')
        },
        {
          id: 2,
          title: '如何正确进行深蹲训练',
          content: '<p>深蹲是一种非常有效的下肢训练动作，但很多人在做深蹲时存在姿势不正确的问题。本文将详细介绍正确的深蹲姿势和常见错误。</p>',
          summary: '深蹲训练的正确姿势和技巧指南',
          coverImage: '',
          authorId: 1,
          authorName: '教练',
          status: 1,
          viewCount: 89,
          likeCount: 18,
          collectCount: 12,
          shareCount: 5,
          createTime: new Date('2024-11-05 14:30:00'),
          publishTime: new Date('2024-11-05 14:30:00')
        },
        {
          id: 3,
          title: '健身饮食计划制定',
          content: '<p>合理的饮食计划对于健身效果至关重要。本文将教你如何根据自己的健身目标制定适合的饮食计划。</p>',
          summary: '健身饮食计划的制定方法和原则',
          coverImage: '',
          authorId: 1,
          authorName: '教练',
          status: 0,
          viewCount: 0,
          likeCount: 0,
          collectCount: 0,
          shareCount: 0,
          createTime: new Date('2024-11-10 09:15:00')
        }
      ]
      this.contents = mockData
      this.pagination.total = mockData.length
    },
    loadTags() {
      // 模拟加载标签数据
      const mockTags = [
        { id: 1, name: '新手入门' },
        { id: 2, name: '力量训练' },
        { id: 3, name: '有氧运动' },
        { id: 4, name: '饮食营养' },
        { id: 5, name: '康复训练' }
      ]
      this.tags = mockTags
    },
    searchContents() {
      // 模拟搜索功能
      this.$message.info('搜索功能已触发')
    },
    resetFilter() {
      this.filterForm = {
        status: '',
        keyword: '',
        dateRange: []
      }
    },
    handleAdd() {
      this.dialogTitle = '新增内容'
      this.contentForm = {
        id: null,
        title: '',
        content: '',
        summary: '',
        coverImage: '',
        authorId: 1,
        authorName: '教练',
        status: 0,
        tagIds: []
      }
      this.fileList = []
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑内容'
      this.contentForm = { ...row, tagIds: [1, 2] } // 模拟标签ID
      this.fileList = []
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewTags = this.tags.filter(tag => [1, 2].includes(tag.id)) // 模拟标签
      this.viewDialogVisible = true
    },
    handleSave() {
      this.$refs.contentForm.validate((valid) => {
        if (valid) {
          // 模拟保存操作
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadContents()
        }
      })
    },
    handlePreview() {
      this.previewDialogVisible = true
    },
    handleDelete(id) {
      this.$confirm('确定要删除此内容吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟删除操作
        this.$message.success('删除成功')
        this.loadContents()
      }).catch(() => {
        // 取消删除
      })
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadContents()
    },
    handleCurrentChange(current) {
      this.pagination.currentPage = current
      this.loadContents()
    },
    handleImageUpload(response, file, fileList) {
      // 模拟图片上传成功
      this.contentForm.coverImage = 'mock-image-url'
      this.$message.success('图片上传成功')
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('只能上传JPG/PNG文件')
      }
      if (!isLt2M) {
        this.$message.error('文件大小不能超过2MB')
      }
      return isJPG && isLt2M
    },
    getStatusText(status) {
      const statusMap = {
        0: '草稿',
        1: '已发布',
        2: '已下架'
      }
      return statusMap[status] || '未知'
    },
    getStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'success',
        2: 'warning'
      }
      return typeMap[status] || 'info'
    },
    getTagName(tagId) {
      const tag = this.tags.find(t => t.id === tagId)
      return tag ? tag.name : ''
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.content-container {
  padding: 20px;
}

.content-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.filter-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.stats {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stats span {
  font-size: 12px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  min-height: 300px;
}

.content-detail {
  padding: 20px;
}

.content-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.content-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #606266;
}

.content-tags {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.tag-item {
  margin-right: 10px;
}

.content-body {
  line-height: 1.6;
  font-size: 16px;
}
</style>
