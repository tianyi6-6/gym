<template>
  <div ref="chartContainer" class="line-chart-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'LineChart',
  props: {
    chartData: {
      type: Object,
      required: true
    },
    height: {
      type: String,
      default: '350px'
    },
    smooth: {
      type: Boolean,
      default: true
    },
    showArea: {
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

      this.chart.on('dataZoom', (params) => {
        this.$emit('data-zoom', params)
      })
    },
    updateChart(data) {
      if (!this.chart || !data) return

      const series = data.series.map((item, index) => {
        // 为不同的数据线设置差异化样式
        let lineStyle, itemStyle, symbol, symbolSize, lineWidth;
        
        if (index === 0) {
          // 课程销售数据线
          lineStyle = {
            type: 'solid', // 实线
            width: 3 // 较粗的线条
          };
          itemStyle = {
            color: '#409EFF' // 蓝色
          };
          symbol = 'circle'; // 圆形标记点
          symbolSize = 8;
          lineWidth = 3;
        } else if (index === 1) {
          // 会员增长数据线
          lineStyle = {
            type: 'dashed', // 虚线
            width: 2 // 较细的线条
          };
          itemStyle = {
            color: '#67C23A' // 绿色
          };
          symbol = 'triangle'; // 三角形标记点
          symbolSize = 10;
          lineWidth = 2;
        } else {
          // 默认样式
          lineStyle = {
            type: 'solid',
            width: 2
          };
          itemStyle = {
            color: item.color || '#409EFF'
          };
          symbol = 'circle';
          symbolSize = 6;
          lineWidth = 2;
        }
        
        return {
          name: item.name,
          type: 'line',
          smooth: this.smooth,
          data: item.data,
          itemStyle: itemStyle,
          lineStyle: lineStyle,
          symbol: symbol,
          symbolSize: symbolSize,
          lineWidth: lineWidth,
          areaStyle: this.showArea ? {
            opacity: 0.3
          } : undefined,
          emphasis: {
            focus: 'series'
          }
        };
      });

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
            type: 'cross',
            crossStyle: {
              color: '#999'
            },
            label: {
              backgroundColor: '#6a7985',
              color: '#fff',
              padding: [4, 8],
              fontSize: 12
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
        xAxis: {
          type: 'category',
          boundaryGap: false,
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
        yAxis: {
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
.line-chart-container {
  width: 100%;
}
</style>
