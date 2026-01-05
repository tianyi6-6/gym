<template>
  <div ref="chartContainer" class="radar-chart-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'RadarChart',
  props: {
    chartData: {
      type: Object,
      required: true
    },
    height: {
      type: String,
      default: '400px'
    },
    indicator: {
      type: Array,
      default: () => [
        { name: '力量', max: 100 },
        { name: '耐力', max: 100 },
        { name: '柔韧性', max: 100 },
        { name: '速度', max: 100 },
        { name: '协调性', max: 100 }
      ]
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
    },
    updateChart(data) {
      if (!this.chart || !data) return

      const seriesData = data.series || []
      const abilityScores = data.abilityScores || {}

      if (seriesData.length === 0 && Object.keys(abilityScores).length > 0) {
        seriesData.push({
          name: '综合能力',
          value: [
            abilityScores.strength || 0,
            abilityScores.endurance || 0,
            abilityScores.flexibility || 0,
            abilityScores.speed || 0,
            abilityScores.coordination || 0
          ]
        })
      }

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
            const indicator = this.indicator
            let result = `<div style="font-weight: bold; color: #409EFF; font-size: 15px; margin-bottom: 8px">${params.name}</div>`
            params.value.forEach((val, idx) => {
              result += `<div style="margin: 4px 0; color: #606266">${indicator[idx].name}: <span style="font-weight: bold; color: #303133">${val}</span></div>`
            })
            return result
          }
        },
        legend: {
          data: seriesData.map(item => item.name),
          top: 10,
          textStyle: {
            color: '#606266',
            fontSize: 13
          }
        },
        radar: {
          indicator: this.indicator,
          center: ['50%', '55%'],
          radius: '65%',
          splitNumber: 5,
          shape: 'polygon',
          name: {
            textStyle: {
              color: '#606266',
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          splitArea: {
            areaStyle: {
              color: ['rgba(64, 158, 255, 0.05)', 'rgba(64, 158, 255, 0.1)', 'rgba(64, 158, 255, 0.15)', 'rgba(64, 158, 255, 0.2)', 'rgba(64, 158, 255, 0.25)']
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(64, 158, 255, 0.3)',
              width: 1
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(64, 158, 255, 0.3)',
              width: 1
            }
          }
        },
        series: [
          {
            name: '能力评估',
            type: 'radar',
            data: seriesData,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            },
            emphasis: {
              lineStyle: {
                width: 5,
                color: '#66b1ff'
              },
              areaStyle: {
                color: 'rgba(64, 158, 255, 0.5)'
              }
            }
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
.radar-chart-container {
  width: 100%;
}
</style>
