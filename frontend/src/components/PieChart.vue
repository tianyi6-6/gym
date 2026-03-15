<template>
  <div ref="chartContainer" class="pie-chart-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'PieChart',
  props: {
    chartData: {
      type: Array,
      required: true
    },
    height: {
      type: String,
      default: '350px'
    },
    radius: {
      type: [String, Array],
      default: '60%'
    },
    roseType: {
      type: String,
      default: ''
    },
    showDataLabel: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      handler(newVal) {
        if (this.chart && newVal) {
          this.updateChart(newVal)
        }
      },
      deep: true
    }
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    initChart() {
      const container = this.$refs.chartContainer
      if (!container) return

      container.style.height = this.height
      this.chart = echarts.init(container)
      this.updateChart(this.chartData)

      this.chart.on('click', (params) => {
        this.$emit('chart-click', params)
      })

      this.chart.on('pieselectchanged', (params) => {
        this.$emit('pie-change', params)
      })
    },
    updateChart(data) {
      if (!this.chart || !data) return

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255, 255, 255, 0.98)',
          borderColor: '#e4e7ed',
          borderWidth: 1,
          padding: [12, 16],
          textStyle: {
            color: '#303133',
            fontSize: 14
          },
          formatter: (params) => {
            if (!params) return ''
            const marker = `<span style="display:inline-block;margin-right:8px;border-radius:10px;width:12px;height:12px;background-color:${params.color};"></span>`
            return `<div style="margin-bottom: 6px; font-weight: bold; color: #409EFF; font-size: 15px">${params.name}</div>
                    <div style="margin: 4px 0">${marker}数值: <span style="font-weight: bold; color: #303133">${params.value}</span></div>
                    <div style="margin: 4px 0">${marker}占比: <span style="font-weight: bold; color: #67c23a">${params.percent}%</span></div>`
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          textStyle: {
            color: '#606266',
            fontSize: 13
          },
          selectedMode: true,
          icon: 'circle'
        },
        series: [
          {
            name: '数据分布',
            type: 'pie',
            radius: this.radius,
            roseType: this.roseType,
            data: data,
            label: this.showDataLabel ? {
              show: true,
              formatter: '{b}: {d}%',
              color: '#606266',
              fontSize: 12,
              fontWeight: 'normal'
            } : {
              show: false
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 20,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.4)'
              },
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold',
                color: '#303133'
              },
              scale: true,
              scaleSize: 10
            },
            labelLine: {
              smooth: 0.3,
              length: 15,
              length2: 25,
              lineStyle: {
                color: '#dcdfe6'
              }
            },
            itemStyle: {
              borderRadius: 4,
              borderColor: '#fff',
              borderWidth: 2
            },
            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: (idx) => Math.random() * 200
          }
        ]
      }

      this.chart.setOption(option, true)
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },
    resize() {
      this.handleResize()
    }
  }
}
</script>

<style scoped>
.pie-chart-container {
  width: 100%;
}
</style>
