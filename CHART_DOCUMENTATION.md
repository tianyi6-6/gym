# 数据可视化图表使用说明文档

## 一、概述

本文档详细介绍了健身房管理系统中数据可视化图表功能的实现、使用方法和优化策略。该功能为管理员、用户和教练三个角色提供了丰富的数据展示和交互体验。

## 二、数据来源说明

### 2.1 管理员Dashboard数据

**数据接口：** `/api/dashboard/*`

| 接口路径 | 说明 | 返回数据 |
|---------|------|---------|
| `/api/dashboard/stats` | 统计数据概览 | 课程数、订单数、用户数、教练数及趋势 |
| `/api/dashboard/chart-data` | 主图表数据 | 课程销售和会员增长趋势 |
| `/api/dashboard/member-level-distribution` | 会员等级分布 | 各等级会员人数 |
| `/api/dashboard/course-type-distribution` | 课程类型分布 | 各类型课程数量 |
| `/api/dashboard/coach-radar` | 教练综合能力 | 教练能力雷达图数据 |

### 2.2 用户Dashboard数据

**数据接口：** `/api/dashboard/user-stats/{userId}`

返回数据包含：
- `orderCount`: 订单总数
- `appointmentCount`: 预约总数
- `orderTrend`: 订单趋势数据（日期、数量）
- `courseTypeDistribution`: 课程类型分布
- `monthlySpending`: 月度消费统计
- `spendingCategory`: 消费类别分布

### 2.3 教练Dashboard数据

**数据接口：** `/api/dashboard/coach-stats/{coachId}`

返回数据包含：
- `courseCount`: 课程总数
- `appointmentCount`: 预约总数
- `experienceCount`: 经验值
- `courseTrend`: 课程趋势数据
- `studentLevelDistribution`: 学员等级分布
- `monthlyCourses`: 月度课程统计
- `courseTypeDistribution`: 课程类型分布

## 三、数据处理逻辑

### 3.1 后端数据处理

**文件位置：** `backend/src/main/java/com/gym/controller/DashboardController.java`

**核心处理逻辑：**

1. **统计数据计算**
   - 根据时间范围（周/月/年）聚合数据
   - 计算环比增长率
   - 实时统计各维度数据

2. **趋势数据处理**
   - 按日期分组统计
   - 生成连续的时间序列
   - 填充缺失日期数据

3. **分布数据处理**
   - 按类别分组统计
   - 计算百分比占比
   - 排序和过滤

### 3.2 前端数据处理

**文件位置：** `frontend/src/views/{role}/Dashboard.vue`

**数据转换示例：**

```javascript
loadMainChartData() {
  const daysMap = {
    week: 7,
    month: 30,
    quarter: 90
  }
  const days = daysMap[this.chartTimeRange]
  
  this.$axios.get('/api/dashboard/chart-data', { 
    params: { timeRange: this.chartTimeRange } 
  }).then(res => {
    this.mainChartData = {
      xAxis: res.data.xAxis,
      series: [
        {
          name: '课程销售',
          data: res.data.courseSales
        },
        {
          name: '会员增长',
          data: res.data.memberGrowth
        }
      ]
    }
  })
}
```

## 四、图表组件使用方法

### 4.1 LineChart（折线图）

**组件路径：** `frontend/src/components/LineChart.vue`

**Props参数：**

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| chartData | Object | 必填 | 图表数据对象 |
| height | String | '350px' | 图表高度 |
| smooth | Boolean | true | 是否平滑曲线 |
| showArea | Boolean | false | 是否显示面积 |

**数据格式：**

```javascript
{
  xAxis: ['1/1', '1/2', '1/3', '1/4', '1/5'],
  series: [
    {
      name: '课程销售',
      data: [10, 20, 15, 30, 25],
      color: '#409EFF'
    },
    {
      name: '会员增长',
      data: [5, 10, 8, 15, 12],
      color: '#67C23A'
    }
  ]
}
```

**使用示例：**

```vue
<LineChart 
  :chart-data="mainChartData" 
  :smooth="true"
  :show-area="true"
  height="350px"
  @chart-click="handleChartClick"
/>
```

**事件：**

- `chart-click`: 点击图表数据点时触发，返回参数包含 name, value, seriesName

### 4.2 BarChart（柱状图）

**组件路径：** `frontend/src/components/BarChart.vue`

**Props参数：**

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| chartData | Object | 必填 | 图表数据对象 |
| height | String | '350px' | 图表高度 |
| horizontal | Boolean | false | 是否水平显示 |
| showDataLabel | Boolean | false | 是否显示数据标签 |

**数据格式：**

```javascript
{
  xAxis: ['力量训练', '有氧运动', '瑜伽', '游泳', '其他'],
  series: [
    {
      name: '课程数量',
      data: [15, 20, 10, 8, 5],
      color: '#409EFF'
    }
  ]
}
```

**使用示例：**

```vue
<BarChart 
  :chart-data="courseTypeData" 
  :show-data-label="true"
  height="350px"
  @chart-click="handleBarClick"
/>
```

**事件：**

- `chart-click`: 点击柱状图时触发
- `legend-change`: 图例选择改变时触发

### 4.3 PieChart（饼图）

**组件路径：** `frontend/src/components/PieChart.vue`

**Props参数：**

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| chartData | Array | 必填 | 图表数据数组 |
| height | String | '350px' | 图表高度 |
| radius | String/Array | '60%' | 饼图半径 |
| roseType | String | '' | 玫瑰图类型（'radius'或''） |
| showDataLabel | Boolean | true | 是否显示数据标签 |

**数据格式：**

```javascript
[
  { name: '普通会员', value: 50, color: '#409EFF' },
  { name: '银卡会员', value: 30, color: '#67C23A' },
  { name: '金卡会员', value: 15, color: '#E6A23C' },
  { name: '钻石会员', value: 5, color: '#F56C6C' }
]
```

**使用示例：**

```vue
<PieChart 
  :chart-data="memberLevelData" 
  :radius="['40%', '70%']"
  height="350px"
  @chart-click="handlePieClick"
/>
```

**事件：**

- `chart-click`: 点击饼图扇区时触发
- `pie-change`: 饼图选择改变时触发

## 五、交互功能说明

### 5.1 悬停交互

所有图表组件都实现了丰富的悬停效果：

**折线图：**
- 鼠标悬停显示十字准星
- 显示详细数据提示框
- 高亮当前数据点

**柱状图：**
- 鼠标悬停显示阴影指示器
- 显示详细数据提示框
- 高亮当前柱子

**饼图：**
- 鼠标悬停扇区放大
- 显示详细数据提示框（名称、数值、占比）
- 高亮当前扇区

### 5.2 点击交互

点击图表元素会触发相应事件，并弹出确认对话框：

**管理员Dashboard：**
- 点击课程销售数据 → 跳转到订单管理页面
- 点击会员增长数据 → 跳转到用户管理页面
- 点击会员等级 → 跳转到对应等级的用户列表
- 点击课程类型 → 跳转到对应类型的课程列表

**用户Dashboard：**
- 点击订单趋势 → 跳转到课程订单页面
- 点击课程类型 → 跳转到对应类型的课程页面
- 点击消费数据 → 跳转到消费记录页面

**教练Dashboard：**
- 点击课程趋势 → 跳转到预约管理页面
- 点击学员等级 → 跳转到对应等级的学员列表
- 点击课程类型 → 跳转到对应类型的课程页面

### 5.3 图例筛选

所有图表都支持图例筛选功能：
- 点击图例可以显示/隐藏对应的数据系列
- 支持多选
- 实时更新图表显示

### 5.4 时间范围切换

折线图支持时间范围切换：
- 近7天
- 近30天
- 近90天

切换后自动重新加载对应时间范围的数据。

## 六、性能优化

### 6.1 图表加载优化

**1. 数据懒加载**
```javascript
watch: {
  chartTimeRange() {
    this.loadMainChartData()
  }
}
```

**2. 防抖处理**
```javascript
methods: {
  handleResize: _.debounce(function() {
    if (this.chart) {
      this.chart.resize()
    }
  }, 300)
}
```

**3. 自动刷新**
```javascript
startAutoRefresh() {
  this.refreshTimer = setInterval(() => {
    this.refreshData()
  }, 60000)
}
```

### 6.2 图表渲染优化

**1. 使用setOption的第二个参数**
```javascript
this.chart.setOption(option, true)
```

**2. 事件监听优化**
```javascript
beforeDestroy() {
  window.removeEventListener('resize', this.handleResize)
  if (this.chart) {
    this.chart.dispose()
  }
}
```

**3. 数据更新优化**
```javascript
watch: {
  chartData: {
    handler(newVal) {
      if (this.chart && newVal) {
        this.updateChart(newVal)
      }
    },
    deep: true
  }
}
```

### 6.3 响应式设计

**1. 媒体查询**
```css
@media (max-width: 768px) {
  .dashboard-page {
    padding: 10px;
  }
  
  .stat-number {
    font-size: 24px;
  }
}
```

**2. 灵活布局**
```vue
<el-row :gutter="20">
  <el-col :span="16">
    <el-card>
      <LineChart height="350px" />
    </el-card>
  </el-col>
  <el-col :span="8">
    <el-card>
      <PieChart height="350px" />
    </el-card>
  </el-col>
</el-row>
```

**3. 自适应高度**
```javascript
mounted() {
  this.initChart()
  window.addEventListener('resize', this.handleResize)
}
```

## 七、视觉设计

### 7.1 配色方案

| 颜色 | 用途 | 十六进制 |
|------|------|---------|
| 主色 | 主要数据系列 | #409EFF |
| 成功 | 增长趋势 | #67C23A |
| 警告 | 注意数据 | #E6A23C |
| 危险 | 重要数据 | #F56C6C |
| 信息 | 辅助数据 | #909399 |

### 7.2 样式规范

**1. 提示框样式**
- 背景色：rgba(255, 255, 255, 0.98)
- 边框：#e4e7ed
- 内边距：[10, 15] 或 [12, 16]
- 字体颜色：#303133
- 字体大小：14px

**2. 图表标题**
- 字体大小：15px
- 字体粗细：bold
- 颜色：#409EFF

**3. 数据标签**
- 字体大小：12px
- 颜色：#606266
- 字体粗细：normal

**4. 强调效果**
- 阴影模糊：20px
- 阴影颜色：rgba(0, 0, 0, 0.4)
- 放大比例：10px

## 八、常见问题

### 8.1 图表不显示

**可能原因：**
1. 数据格式不正确
2. 容器高度未设置
3. ECharts未正确初始化

**解决方法：**
```javascript
mounted() {
  this.$nextTick(() => {
    this.initChart()
  })
}
```

### 8.2 数据更新不生效

**可能原因：**
1. watch未设置deep: true
2. 数据引用未改变
3. setOption未正确调用

**解决方法：**
```javascript
watch: {
  chartData: {
    handler(newVal) {
      if (this.chart && newVal) {
        this.chart.setOption(this.getOption(newVal), true)
      }
    },
    deep: true
  }
}
```

### 8.3 响应式不生效

**可能原因：**
1. resize事件未监听
2. 组件销毁时未移除监听
3. 父容器宽度未变化

**解决方法：**
```javascript
mounted() {
  window.addEventListener('resize', this.handleResize)
},
beforeDestroy() {
  window.removeEventListener('resize', this.handleResize)
}
```

## 九、扩展开发

### 9.1 添加新的图表类型

1. 在`frontend/src/components/`目录下创建新组件
2. 参考现有组件的结构和实现
3. 实现props、data、methods
4. 添加事件监听和emit

### 9.2 添加新的数据接口

1. 在`DashboardController.java`中添加新的接口方法
2. 实现数据查询和处理逻辑
3. 在`frontend/src/api/index.js`中添加对应的API函数
4. 在Dashboard组件中调用新接口

### 9.3 自定义图表样式

通过props传递自定义配置：

```javascript
props: {
  customOption: {
    type: Object,
    default: () => ({})
  }
}
```

在updateChart方法中合并配置：

```javascript
const option = {
  ...this.defaultOption,
  ...this.customOption
}
```

## 十、技术栈

- **前端框架：** Vue.js 2.x
- **UI组件库：** Element UI
- **图表库：** ECharts 5.x
- **后端框架：** Spring Boot
- **数据库：** MySQL
- **构建工具：** Webpack

## 十一、文件结构

```
gym/
├── backend/
│   └── src/main/java/com/gym/
│       └── controller/
│           └── DashboardController.java
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   │   └── index.js
│   │   ├── components/
│   │   │   ├── LineChart.vue
│   │   │   ├── BarChart.vue
│   │   │   └── PieChart.vue
│   │   └── views/
│   │       ├── admin/
│   │       │   └── Dashboard.vue
│   │       ├── user/
│   │       │   └── Dashboard.vue
│   │       └── coach/
│   │           └── Dashboard.vue
```

## 十二、版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2025-12-27 | 初始版本，实现基础图表功能 |

## 十三、联系方式

如有问题或建议，请联系开发团队。
