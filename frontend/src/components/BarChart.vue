<template>
  <div ref="chartContainer" class="bar-chart-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'BarChart',
  props: {
    chartData: {
      type: Object,
      required: true
    },
    height: {
      type: String,
      default: '350px'
    },
    horizontal: {
      type: Boolean,
      default: false
    },
    showDataLabel: {
      type: Boolean,
      default: false
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

      this.chart.on('legendselectchanged', (params) => {
        this.$emit('legend-change', params)
      })
    },
    updateChart(data) {
      if (!this.chart || !data) return

      const series = data.series.map(item => ({
        name: item.name,
        type: 'bar',
        data: item.data,
        itemStyle: {
          color: item.color || '#409EFF',
          borderRadius: [4, 4, 0, 0]
        },
        label: this.showDataLabel ? {
          show: true,
          position: 'top',
          formatter: '{c}'
        } : undefined,
        emphasis: {
          focus: 'series',
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }))

      const option = {
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.98)',
          borderColor: '#e4e7ed',
          borderWidth: 1,
          padding: [10, 15],
          textStyle: {
            color: '#303133',
            fontSize: 14
          },
          axisPointer: {
            type: 'shadow',
            shadowStyle: {
              color: 'rgba(150, 150, 150, 0.1)'
            }
          },
          formatter: (params) => {
            if (!params || params.length === 0) return ''
            let result = `<div style="margin-bottom: 8px; font-weight: bold; color: #409EFF">${params[0].axisValue}</div>`
            params.forEach(param => {
              const marker = `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${param.color};"></span>`
              result += `<div style="margin: 4px 0">${marker}${param.seriesName}: <span style="font-weight: bold; color: #303133">${param.value}</span></div>`
            })
            return result
          }
        },
        legend: {
          data: data.series.map(s => s.name),
          top: 10,
          textStyle: {
            color: '#606266',
            fontSize: 13
          },
          selectedMode: true
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: this.horizontal ? {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12
          },
          splitLine: {
            lineStyle: {
              color: '#f0f2f5',
              type: 'dashed'
            }
          }
        } : {
          type: 'category',
          data: data.xAxis,
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12
          },
          axisPointer: {
            type: 'shadow'
          }
        },
        yAxis: this.horizontal ? {
          type: 'category',
          data: data.xAxis,
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12
          }
        } : {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12
          },
          splitLine: {
            lineStyle: {
              color: '#f0f2f5',
              type: 'dashed'
            }
          }
        },
        series: series
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
.bar-chart-container {
  width: 100%;
}
</style>
