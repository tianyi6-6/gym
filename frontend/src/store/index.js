import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    coachData: {
      courseSchedule: [],
      myCourses: [],
      healthFitness: {
        weight: 0.0,
        bodyFatRate: 0.0,
        heartRate: 0,
        trainingHours: 0.0,
        caloriesBurned: 0,
        muscleMass: 0.0,
        bmi: 0.0,
        strengthScore: 0,
        enduranceScore: 0,
        flexibilityScore: 0,
        speedScore: 0,
        coordinationScore: 0
      },
      lastUpdated: null
    }
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    CLEAR_AUTH(state) {
      state.token = ''
      state.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },
    SET_COACH_COURSE_SCHEDULE(state, schedule) {
      state.coachData.courseSchedule = schedule
      state.coachData.lastUpdated = new Date().toISOString()
    },
    SET_COACH_MY_COURSES(state, courses) {
      state.coachData.myCourses = courses
      state.coachData.lastUpdated = new Date().toISOString()
    },
    SET_COACH_HEALTH_FITNESS(state, healthData) {
      state.coachData.healthFitness = healthData
      state.coachData.lastUpdated = new Date().toISOString()
    },
    UPDATE_COACH_COURSE_SCHEDULE_ITEM(state, item) {
      const index = state.coachData.courseSchedule.findIndex(s => s.id === item.id)
      if (index !== -1) {
        state.coachData.courseSchedule.splice(index, 1, item)
      } else {
        state.coachData.courseSchedule.push(item)
      }
      state.coachData.lastUpdated = new Date().toISOString()
    },
    DELETE_COACH_COURSE_SCHEDULE_ITEM(state, id) {
      state.coachData.courseSchedule = state.coachData.courseSchedule.filter(s => s.id !== id)
      state.coachData.lastUpdated = new Date().toISOString()
    },
    UPDATE_COACH_MY_COURSES_ITEM(state, item) {
      const index = state.coachData.myCourses.findIndex(c => c.id === item.id)
      if (index !== -1) {
        state.coachData.myCourses.splice(index, 1, item)
      } else {
        state.coachData.myCourses.push(item)
      }
      state.coachData.lastUpdated = new Date().toISOString()
    },
    DELETE_COACH_MY_COURSES_ITEM(state, id) {
      state.coachData.myCourses = state.coachData.myCourses.filter(c => c.id !== id)
      state.coachData.lastUpdated = new Date().toISOString()
    },
    UPDATE_COACH_HEALTH_FITNESS(state, healthData) {
      state.coachData.healthFitness = healthData
      state.coachData.lastUpdated = new Date().toISOString()
    }
  },
  actions: {
    login({ commit }, { token, userInfo }) {
      commit('SET_TOKEN', token)
      commit('SET_USER_INFO', userInfo)
    },
    logout({ commit }) {
      commit('CLEAR_AUTH')
    },
    updateCoachCourseSchedule({ commit }, schedule) {
      commit('SET_COACH_COURSE_SCHEDULE', schedule)
    },
    updateCoachMyCourses({ commit }, courses) {
      commit('SET_COACH_MY_COURSES', courses)
    },
    updateCoachHealthFitness({ commit }, healthData) {
      commit('SET_COACH_HEALTH_FITNESS', healthData)
    },
    updateCourseScheduleItem({ commit }, item) {
      commit('UPDATE_COACH_COURSE_SCHEDULE_ITEM', item)
    },
    deleteCourseScheduleItem({ commit }, id) {
      commit('DELETE_COACH_COURSE_SCHEDULE_ITEM', id)
    },
    updateMyCoursesItem({ commit }, item) {
      commit('UPDATE_COACH_MY_COURSES_ITEM', item)
    },
    deleteMyCoursesItem({ commit }, id) {
      commit('DELETE_COACH_MY_COURSES_ITEM', id)
    },
    updateHealthFitness({ commit }, healthData) {
      commit('UPDATE_COACH_HEALTH_FITNESS', healthData)
    }
  }
})

