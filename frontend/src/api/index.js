import axios from 'axios'

// 登录
export const login = (data) => {
  return axios.post('/auth/login', data)
}

// 课程相关
export const getCourseList = () => {
  return axios.get('/course/list')
}

export const getCoursePage = (page, size) => {
  return axios.get(`/course/page?page=${page}&size=${size}`)
}

export const getCourseCount = () => {
  return axios.get('/course/count')
}

export const getCourseById = (id) => {
  return axios.get(`/course/${id}`)
}

export const saveCourse = (data) => {
  return axios.post('/course/save', data)
}

export const deleteCourse = (id) => {
  return axios.delete(`/course/${id}`)
}

// 教练相关
export const getCoachList = () => {
  return axios.get('/coach/list')
}

export const saveCoach = (data) => {
  return axios.post('/coach/save', data)
}

export const deleteCoach = (id) => {
  return axios.delete(`/coach/${id}`)
}

export const registerCoach = (data) => {
  return axios.post('/coach/register', data)
}

// 用户相关
export const getUserList = () => {
  return axios.get('/user/list')
}

export const getUserById = (id) => {
  return axios.get(`/user/${id}`)
}

export const saveUser = (data) => {
  return axios.post('/user/save', data)
}

export const deleteUser = (id) => {
  return axios.delete(`/user/${id}`)
}

export const registerUser = (data) => {
  return axios.post('/user/register', data)
}

// 订单相关
export const getOrderList = () => {
  return axios.get('/order/list')
}

export const getOrderById = (id) => {
  return axios.get(`/order/${id}`)
}

export const getUserOrderList = (userId) => {
  return axios.get(`/order/user/${userId}`)
}

export const createOrder = (data) => {
  return axios.post('/order/create', data)
}

export const updateOrder = (data) => {
  return axios.post('/order/update', data)
}

export const deleteOrder = (id) => {
  return axios.delete(`/order/${id}`)
}

export const cancelOrder = (id) => {
  return axios.post(`/order/cancel/${id}`)
}

export const payOrder = (id) => {
  return axios.post(`/order/pay/${id}`)
}

// 器材相关
export const getEquipmentList = () => {
  return axios.get('/equipment/list')
}

export const saveEquipment = (data) => {
  return axios.post('/equipment/save', data)
}

export const deleteEquipment = (id) => {
  return axios.delete(`/equipment/${id}`)
}

// 菜谱相关
export const getRecipeList = () => {
  return axios.get('/recipe/list')
}

export const getRecipeById = (id) => {
  return axios.get(`/recipe/${id}`)
}

export const saveRecipe = (data) => {
  return axios.post('/recipe/save', data)
}

export const deleteRecipe = (id) => {
  return axios.delete(`/recipe/${id}`)
}

// 经验相关
export const getExperienceList = () => {
  return axios.get('/experience/list')
}

export const getExperienceById = (id) => {
  return axios.get(`/experience/${id}`)
}

export const saveExperience = (data) => {
  return axios.post('/experience/save', data)
}

export const deleteExperience = (id) => {
  return axios.delete(`/experience/${id}`)
}

// 公告相关
export const getNoticeList = () => {
  return axios.get('/notice/list')
}

export const saveNotice = (data) => {
  return axios.post('/notice/save', data)
}

export const deleteNotice = (id) => {
  return axios.delete(`/notice/${id}`)
}

// 充值相关
export const recharge = (data) => {
  return axios.post('/recharge', data)
}

export const getRechargeList = (userId) => {
  return axios.get(`/recharge/user/${userId}`)
}

// 健康档案相关
export const getHealthProfileByUserId = (userId) => {
  return axios.get(`/health-profile/user/${userId}`)
}

export const getHealthProfileById = (id) => {
  return axios.get(`/health-profile/${id}`)
}

export const getHealthProfileList = () => {
  return axios.get('/health-profile/list')
}

export const getHealthProfileByRiskLevel = (riskLevel) => {
  return axios.get(`/health-profile/risk/${riskLevel}`)
}

export const saveHealthProfile = (data) => {
  return axios.post('/health-profile/save', data)
}

export const deleteHealthProfile = (id) => {
  return axios.delete(`/health-profile/${id}`)
}

export const deleteHealthProfileByUserId = (userId) => {
  return axios.delete(`/health-profile/user/${userId}`)
}

// 体测数据相关
export const getBodyAssessmentById = (id) => {
  return axios.get(`/body-assessment/${id}`)
}

export const getBodyAssessmentByUserId = (userId) => {
  return axios.get(`/body-assessment/user/${userId}`)
}

export const getLatestBodyAssessmentByUserId = (userId) => {
  return axios.get(`/body-assessment/user/${userId}/latest`)
}

export const getBodyAssessmentByCoachId = (coachId) => {
  return axios.get(`/body-assessment/coach/${coachId}`)
}

export const getBodyAssessmentList = () => {
  return axios.get('/body-assessment/list')
}

export const saveBodyAssessment = (data) => {
  return axios.post('/body-assessment/save', data)
}

export const deleteBodyAssessment = (id) => {
  return axios.delete(`/body-assessment/${id}`)
}

export const deleteBodyAssessmentByUserId = (userId) => {
  return axios.delete(`/body-assessment/user/${userId}`)
}

// 健康目标相关
export const getHealthGoalById = (id) => {
  return axios.get(`/health-goal/${id}`)
}

export const getHealthGoalByUserId = (userId) => {
  return axios.get(`/health-goal/user/${userId}`)
}

export const getHealthGoalByUserIdAndStatus = (userId, status) => {
  return axios.get(`/health-goal/user/${userId}/status/${status}`)
}

export const getHealthGoalList = () => {
  return axios.get('/health-goal/list')
}

export const saveHealthGoal = (data) => {
  return axios.post('/health-goal/save', data)
}

export const updateHealthGoalProgress = (id, progress) => {
  return axios.put(`/health-goal/${id}/progress`, null, {
    params: { progress }
  })
}

export const updateHealthGoalStatus = (id, status) => {
  return axios.put(`/health-goal/${id}/status`, null, {
    params: { status }
  })
}

export const deleteHealthGoal = (id) => {
  return axios.delete(`/health-goal/${id}`)
}

export const deleteHealthGoalByUserId = (userId) => {
  return axios.delete(`/health-goal/user/${userId}`)
}

// 健康风险预警相关
export const getHealthRiskWarningById = (id) => {
  return axios.get(`/health-risk-warning/${id}`)
}

export const getHealthRiskWarningByUserId = (userId) => {
  return axios.get(`/health-risk-warning/user/${userId}`)
}

export const getHealthRiskWarningByUserIdAndStatus = (userId, status) => {
  return axios.get(`/health-risk-warning/user/${userId}/status/${status}`)
}

export const getHealthRiskWarningByStatus = (status) => {
  return axios.get(`/health-risk-warning/status/${status}`)
}

export const getHealthRiskWarningList = () => {
  return axios.get('/health-risk-warning/list')
}

export const saveHealthRiskWarning = (data) => {
  return axios.post('/health-risk-warning/save', data)
}

export const updateHealthRiskWarningStatus = (id, status) => {
  return axios.put(`/health-risk-warning/${id}/status`, null, {
    params: { status }
  })
}

export const updateHealthRiskWarningHandleStatus = (id, handleStatus) => {
  return axios.put(`/health-risk-warning/${id}/handle-status`, null, {
    params: { handleStatus }
  })
}

export const deleteHealthRiskWarning = (id) => {
  return axios.delete(`/health-risk-warning/${id}`)
}

export const deleteHealthRiskWarningByUserId = (userId) => {
  return axios.delete(`/health-risk-warning/user/${userId}`)
}

// Dashboard相关
export const getDashboardStats = () => {
  return axios.get('/dashboard/stats')
}

export const getCourseSalesData = (days) => {
  return axios.get('/dashboard/course-sales', {
    params: { days }
  })
}

export const getMemberGrowthData = (days) => {
  return axios.get('/dashboard/member-growth', {
    params: { days }
  })
}

export const getMemberLevelDistribution = () => {
  return axios.get('/dashboard/member-level-distribution')
}

export const getCourseTypeDistribution = () => {
  return axios.get('/dashboard/course-type-distribution')
}

export const getCoachRadarData = () => {
  return axios.get('/dashboard/coach-radar')
}

export const getUserStats = (userId) => {
  return axios.get(`/dashboard/user-stats/${userId}`)
}

export const getCoachStats = (coachId) => {
  return axios.get(`/dashboard/coach-stats/${coachId}`)
}

