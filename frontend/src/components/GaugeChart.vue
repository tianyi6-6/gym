<template>
  <div ref="chartContainer" class="gauge-chart-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'GaugeChart',
  props: {
    value: {
      type: Number,
      required: true
    },
    title: {
      type: String,
      default: '数值'
    },
    unit: {
      type: String,
      default: ''
    },
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Number,
      default: 100
    },
    height: {
      type: String,
      default: '300px'
    },
    color: {
      type: Array,
      default: () => ['#67C23A', '#E6A23C', '#F56C6C']
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    value: {
      handler(newVal) {
        if (this.chart) {
          this.updateChart(newVal)
        }
      }
    },
    title: {
      handler() {
        if (this.chart) {
          this.updateChart(this.value)
        }
      }
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
      this.updateChart(this.value)

      this.chart.on('click', (params) => {
        this.$emit('chart-click', params)
      })
    },
    updateChart(val) {
      if (!this.chart) return

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
            return `<div style="font-weight: bold; color: #409EFF; font-size: 15px; margin-bottom: 8px">${this.title}</div>
                    <div style="color: #606266">当前数值: <span style="font-weight: bold; color: #303133">${val}${this.unit}</span></div>`
          }
        },
        series: [
          {
            type: 'gauge',
            min: this.min,
            max: this.max,
            startAngle: 210,
            endAngle: -30,
            radius: '90%',
            center: ['50%', '60%'],
            itemStyle: {
              color: '#409EFF',
              shadowColor: 'rgba(0, 0, 0, 0.3)',
              shadowBlur: 10,
              shadowOffsetX: 2,
              shadowOffsetY: 2
            },
            progress: {
              show: true,
              roundCap: true,
              width: 18,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: this.color[0] },
                  { offset: 0.5, color: this.color[1] },
                  { offset: 1, color: this.color[2] }
                ])
              }
            },
            pointer: {
              show: true,
              length: '60%',
              width: 8,
              itemStyle: {
                color: 'auto',
                shadowColor: 'rgba(0, 0, 0, 0.3)',
                shadowBlur: 5,
                shadowOffsetX: 1,
                shadowOffsetY: 1
              }
            },
            axisLine: {
              roundCap: true,
              lineStyle: {
                width: 18,
                color: [
                  [0.33, this.color[0]],
                  [0.66, this.color[1]],
                  [1, this.color[2]]
                ]
              }
            },
            axisTick: {
              distance: -28,
              splitNumber: 5,
              lineStyle: {
                width: 2,
                color: '#999'
              }
            },
            splitLine: {
              distance: -32,
              length: 14,
              lineStyle: {
                width: 3,
                color: '#999'
              }
            },
            axisLabel: {
              distance: -10,
              color: '#606266',
              fontSize: 12,
              fontWeight: 'bold'
            },
            anchor: {
              show: true,
              showAbove: true,
              size: 20,
              itemStyle: {
                borderWidth: 10,
                borderColor: '#409EFF'
              }
            },
            title: {
              show: true,
              offsetCenter: [0, '30%'],
              fontSize: 16,
              color: '#303133',
              fontWeight: 'bold'
            },
            detail: {
              valueAnimation: true,
              fontSize: 32,
              offsetCenter: [0, '-15%'],
              formatter: (value) => {
                return `${value}${this.unit}`
              },
              color: '#409EFF',
              fontWeight: 'bold'
            },
            data: [
              {
                value: val,
                name: this.title
              }
            ]
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
.gauge-chart-container {
  width: 100%;
}
</style>
