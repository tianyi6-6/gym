<template>
  <div class="course-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>课程管理</span>
        <el-button type="primary" @click="handleAdd">新增课程</el-button>
      </div>
      
      <!-- 课程数据图表 -->
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>课程价格分布</span>
            </div>
            <BarChart 
              :chart-data="priceChartData" 
              :show-data-label="true"
              height="300px"
              @chart-click="handleChartClick"
            />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>课程时长分布</span>
            </div>
            <BarChart 
              :chart-data="durationChartData" 
              :show-data-label="true"
              height="300px"
              @chart-click="handleChartClick"
            />
          </el-card>
        </el-col>
      </el-row>
      
      <el-table :data="courseList" border>
        <el-table-column prop="id" label="ID" width="80" sortable :sort-orders="['ascending', 'descending']" :default-sort="{prop: 'id', order: 'ascending'}"></el-table-column>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="coachName" label="教练"></el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
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
      <el-form :model="courseForm" :rules="rules" ref="courseForm" label-width="100px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="courseForm.name"></el-input>
        </el-form-item>
        <el-form-item label="教练" prop="coachId">
          <el-select v-model="courseForm.coachId" placeholder="请选择教练" style="width: 100%">
            <el-option
              v-for="coach in coachList"
              :key="coach.id"
              :label="coach.name"
              :value="coach.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="courseForm.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="courseForm.duration" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="courseForm.description" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="courseForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程图片">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :on-change="handleImageChange"
            :before-upload="beforeUpload"
            accept=".jpg,.jpeg,.png,.gif"
          >
            <img v-if="courseForm.image" :src="courseForm.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div v-if="imagePreview" class="image-preview">
            <h4>图片预览</h4>
            <img :src="imagePreview" class="preview-image">
            <el-button type="primary" size="small" @click="confirmImage">确认使用</el-button>
            <el-button size="small" @click="cancelImage">取消</el-button>
          </div>
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
import { getCourseList, saveCourse, deleteCourse, getCoachList } from '@/api'
import BarChart from '@/components/BarChart.vue'

export default {
  name: 'AdminCourse',
  components: {
    BarChart
  },
  data() {
    return {
      courseList: [],
      coachList: [],
      dialogVisible: false,
      dialogTitle: '新增课程',
      courseForm: {
        id: null,
        name: '',
        coachId: null,
        price: 0,
        duration: 60,
        description: '',
        status: 1,
        image: ''
      },
      imagePreview: '',
      rules: {
        name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        coachId: [{ required: true, message: '请选择教练', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
      },
      priceChartData: {
        xAxis: [],
        series: []
      },
      durationChartData: {
        xAxis: [],
        series: []
      }
    }
  },
  mounted() {
    this.loadCourseList()
    this.loadCoachList()
  },
  methods: {
    loadCourseList() {
      getCourseList().then(res => {
        this.courseList = res.data.sort((a, b) => a.id - b.id)
        this.updateCharts()
      })
    },
    loadCoachList() {
      getCoachList().then(res => {
        this.coachList = res.data
      })
    },
    handleAdd() {
      this.dialogTitle = '新增课程'
      this.courseForm = {
        id: null,
        name: '',
        coachId: null,
        price: 0,
        duration: 60,
        description: '',
        status: 1,
        image: ''
      }
      this.imagePreview = ''
      this.dialogVisible = true
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isGIF = file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG && !isGIF) {
        this.$message.error('只能上传 JPG、PNG 或 GIF 格式的图片!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }

      // 验证图片尺寸
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = (e) => {
        const img = new Image()
        img.src = e.target.result
        img.onload = () => {
          const { width, height } = img
          if (width < 300 || height < 200) {
            this.$message.error('图片尺寸不能小于 300x200 像素!')
            return false
          }
        }
      }

      return true
    },
    handleImageChange(file) {
      const reader = new FileReader()
      reader.readAsDataURL(file.raw)
      reader.onload = (e) => {
        this.imagePreview = e.target.result
      }
    },
    confirmImage() {
      this.courseForm.image = this.imagePreview
      this.imagePreview = ''
      this.$message.success('图片添加成功')
    },
    cancelImage() {
      this.imagePreview = ''
    },
    handleEdit(row) {
      this.dialogTitle = '编辑课程'
      this.courseForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.courseForm.validate((valid) => {
        if (valid) {
          saveCourse(this.courseForm).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.loadCourseList()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定要删除吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteCourse(id).then(() => {
          this.$message.success('删除成功')
          this.loadCourseList()
        })
      })
    },
    updateCharts() {
      this.updatePriceChart()
      this.updateDurationChart()
    },
    updatePriceChart() {
      // 按价格区间分组
      const priceRanges = {
        '0-100': 0,
        '101-200': 0,
        '201-300': 0,
        '301-400': 0,
        '400+': 0
      }
      
      this.courseList.forEach(course => {
        const price = course.price
        if (price <= 100) {
          priceRanges['0-100']++
        } else if (price <= 200) {
          priceRanges['101-200']++
        } else if (price <= 300) {
          priceRanges['201-300']++
        } else if (price <= 400) {
          priceRanges['301-400']++
        } else {
          priceRanges['400+']++
        }
      })
      
      this.priceChartData = {
        xAxis: Object.keys(priceRanges),
        series: [{
          name: '课程数量',
          data: Object.values(priceRanges),
          color: '#409EFF'
        }]
      }
    },
    updateDurationChart() {
      // 按时长区间分组
      const durationRanges = {
        '30分钟以下': 0,
        '30-60分钟': 0,
        '61-90分钟': 0,
        '90分钟以上': 0
      }
      
      this.courseList.forEach(course => {
        const duration = course.duration
        if (duration < 30) {
          durationRanges['30分钟以下']++
        } else if (duration <= 60) {
          durationRanges['30-60分钟']++
        } else if (duration <= 90) {
          durationRanges['61-90分钟']++
        } else {
          durationRanges['90分钟以上']++
        }
      })
      
      this.durationChartData = {
        xAxis: Object.keys(durationRanges),
        series: [{
          name: '课程数量',
          data: Object.values(durationRanges),
          color: '#67C23A'
        }]
      }
    },
    handleChartClick(params) {
      const category = params.name
      const count = params.value
      
      this.$message({
        message: `类别: ${category}, 数量: ${count}`,
        type: 'info',
        duration: 2000
      })
    }
  }
}
</script>

<style scoped>
.course-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color .3s;
  width: 150px;
  height: 150px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.avatar {
  width: 150px;
  height: 150px;
  display: block;
}

.image-preview {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #f5f7fa;
}

.image-preview h4 {
  margin-bottom: 10px;
  color: #303133;
}

.preview-image {
  max-width: 100%;
  max-height: 300px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.image-preview .el-button {
  margin-right: 10px;
}
</style>

