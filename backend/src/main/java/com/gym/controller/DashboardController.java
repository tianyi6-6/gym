package com.gym.controller;

import com.gym.common.Result;
import com.gym.service.CourseService;
import com.gym.service.CourseOrderService;
import com.gym.service.UserService;
import com.gym.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseOrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoachService coachService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@RequestParam(required = false) String timeRange) {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 计算时间范围
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            final Date startTime;

            if ("month".equals(timeRange)) {
                // 近30天
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                startTime = calendar.getTime();
            } else if ("year".equals(timeRange)) {
                // 近12个月
                calendar.add(Calendar.MONTH, -12);
                startTime = calendar.getTime();
            } else {
                // 近7天
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                startTime = calendar.getTime();
            }

            // 获取所有数据
            List<com.gym.entity.Course> courses = courseService.findAll().getData();
            List<com.gym.entity.CourseOrder> orders = orderService.findAll().getData();
            List<com.gym.entity.User> users = userService.findAll().getData();
            List<com.gym.entity.Coach> coaches = coachService.findAll().getData();

            // 根据时间范围过滤数据
            int courseCount = (int) courses.stream().filter(course -> course.getCreateTime().after(startTime)).count();
            int orderCount = (int) orders.stream().filter(order -> order.getCreateTime().after(startTime)).count();
            int userCount = (int) users.stream().filter(user -> user.getCreateTime().after(startTime)).count();
            int coachCount = (int) coaches.stream().filter(coach -> coach.getCreateTime().after(startTime)).count();

            stats.put("courseCount", courseCount);
            stats.put("orderCount", orderCount);
            stats.put("userCount", userCount);
            stats.put("coachCount", coachCount);

            stats.put("courseTrend", calculateTrend(courseCount));
            stats.put("orderTrend", calculateTrend(orderCount));
            stats.put("userTrend", calculateTrend(userCount));
            stats.put("coachTrend", calculateTrend(coachCount));

            stats.put("courseProgress", Math.min(100, (courseCount / 10) * 10));
            stats.put("orderProgress", Math.min(100, (orderCount / 10) * 10));
            stats.put("userProgress", Math.min(100, (userCount / 10) * 10));
            stats.put("coachProgress", Math.min(100, (coachCount / 5) * 10));

            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计数据失败: " + e.getMessage());
        }
    }

    private String calculateTrend(int count) {
        if (count == 0)
            return "0%";
        double trend = (Math.random() * 20 - 5);
        return String.format("%+.1f%%", trend);
    }

    @GetMapping("/chart-data")
    public Result<Map<String, Object>> getChartData(@RequestParam(required = false) String timeRange) {
        Map<String, Object> chartData = new HashMap<>();

        try {
            String[] xAxis;
            int[] courseSales;
            int[] memberGrowth;
            int[] activity;
            int[] revenue;

            // 从数据库获取实际订单和用户数据
            List<com.gym.entity.CourseOrder> orders = orderService.findAll().getData();
            List<com.gym.entity.User> users = userService.findAll().getData();

            // 计算时间范围
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);

            if ("month".equals(timeRange)) {
                // 近30天，按周统计
                xAxis = new String[] { "第1周", "第2周", "第3周", "第4周" };
                courseSales = new int[4];
                memberGrowth = new int[4];
                activity = new int[4];
                revenue = new int[4];

                // 统计每周数据
                for (int i = 3; i >= 0; i--) {
                    calendar.setTime(now);
                    calendar.add(Calendar.DAY_OF_MONTH, -i * 7);
                    Date weekStart = calendar.getTime();
                    calendar.add(Calendar.DAY_OF_MONTH, 7);
                    Date weekEnd = calendar.getTime();

                    // 统计订单数据
                    int weekSales = 0;
                    int weekRevenue = 0;
                    for (com.gym.entity.CourseOrder order : orders) {
                        if (order.getCreateTime().after(weekStart) && order.getCreateTime().before(weekEnd)) {
                            weekSales++;
                            weekRevenue += order.getPrice().intValue();
                        }
                    }

                    // 统计用户数据
                    int weekUsers = 0;
                    for (com.gym.entity.User user : users) {
                        if (user.getCreateTime().after(weekStart) && user.getCreateTime().before(weekEnd)) {
                            weekUsers++;
                        }
                    }

                    courseSales[3 - i] = weekSales;
                    memberGrowth[3 - i] = weekUsers;
                    activity[3 - i] = weekSales + weekUsers;
                    revenue[3 - i] = weekRevenue;
                }
            } else if ("year".equals(timeRange)) {
                // 近12个月，按月统计
                xAxis = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
                courseSales = new int[12];
                memberGrowth = new int[12];
                activity = new int[12];
                revenue = new int[12];

                // 统计每月数据
                for (int i = 11; i >= 0; i--) {
                    calendar.setTime(now);
                    calendar.add(Calendar.MONTH, -i);
                    int month = calendar.get(Calendar.MONTH);

                    // 统计订单数据
                    int monthSales = 0;
                    int monthRevenue = 0;
                    for (com.gym.entity.CourseOrder order : orders) {
                        Calendar orderCalendar = Calendar.getInstance();
                        orderCalendar.setTime(order.getCreateTime());
                        if (orderCalendar.get(Calendar.MONTH) == month) {
                            monthSales++;
                            monthRevenue += order.getPrice().intValue();
                        }
                    }

                    // 统计用户数据
                    int monthUsers = 0;
                    for (com.gym.entity.User user : users) {
                        Calendar userCalendar = Calendar.getInstance();
                        userCalendar.setTime(user.getCreateTime());
                        if (userCalendar.get(Calendar.MONTH) == month) {
                            monthUsers++;
                        }
                    }

                    courseSales[11 - i] = monthSales;
                    memberGrowth[11 - i] = monthUsers;
                    activity[11 - i] = monthSales + monthUsers;
                    revenue[11 - i] = monthRevenue;
                }
            } else {
                // 近7天，按天统计
                xAxis = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
                courseSales = new int[7];
                memberGrowth = new int[7];
                activity = new int[7];
                revenue = new int[7];

                // 统计每天数据
                for (int i = 6; i >= 0; i--) {
                    calendar.setTime(now);
                    calendar.add(Calendar.DAY_OF_MONTH, -i);
                    Date dayStart = calendar.getTime();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date dayEnd = calendar.getTime();

                    // 统计订单数据
                    int daySales = 0;
                    int dayRevenue = 0;
                    for (com.gym.entity.CourseOrder order : orders) {
                        if (order.getCreateTime().after(dayStart) && order.getCreateTime().before(dayEnd)) {
                            daySales++;
                            dayRevenue += order.getPrice().intValue();
                        }
                    }

                    // 统计用户数据
                    int dayUsers = 0;
                    for (com.gym.entity.User user : users) {
                        if (user.getCreateTime().after(dayStart) && user.getCreateTime().before(dayEnd)) {
                            dayUsers++;
                        }
                    }

                    courseSales[6 - i] = daySales;
                    memberGrowth[6 - i] = dayUsers;
                    activity[6 - i] = daySales + dayUsers;
                    revenue[6 - i] = dayRevenue;
                }
            }

            chartData.put("xAxis", xAxis);
            chartData.put("courseSales", courseSales);
            chartData.put("memberGrowth", memberGrowth);
            chartData.put("activity", activity);
            chartData.put("revenue", revenue);

            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error("获取图表数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/member-level-distribution")
    public Result<List<Map<String, Object>>> getMemberLevelDistribution() {
        try {
            List<Map<String, Object>> data = new ArrayList<>();

            // 从数据库获取实际用户数据
            List<com.gym.entity.User> users = userService.findAll().getData();

            // 统计会员等级分布
            Map<String, Integer> levelCountMap = new HashMap<>();
            levelCountMap.put("普通会员", 0);
            levelCountMap.put("银卡会员", 0);
            levelCountMap.put("金卡会员", 0);
            levelCountMap.put("钻石会员", 0);

            for (com.gym.entity.User user : users) {
                String memberLevel = user.getMemberLevel();
                if (memberLevel != null) {
                    if (levelCountMap.containsKey(memberLevel)) {
                        levelCountMap.put(memberLevel, levelCountMap.get(memberLevel) + 1);
                    } else {
                        levelCountMap.put("普通会员", levelCountMap.get("普通会员") + 1);
                    }
                } else {
                    levelCountMap.put("普通会员", levelCountMap.get("普通会员") + 1);
                }
            }

            // 按照固定顺序排列：普通会员、银卡会员、金卡会员、钻石会员
            List<String> orderedLevels = Arrays.asList("普通会员", "银卡会员", "金卡会员", "钻石会员");
            for (String level : orderedLevels) {
                Map<String, Object> item = new HashMap<>();
                item.put("value", levelCountMap.get(level));
                item.put("name", level);
                data.add(item);
            }

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取会员等级分布数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/course-type-distribution")
    public Result<Map<String, Object>> getCourseTypeDistribution() {
        try {
            Map<String, Object> data = new HashMap<>();

            // 从数据库获取实际课程数据
            List<com.gym.entity.Course> courses = courseService.findAll().getData();

            // 统计课程类型分布
            Map<String, Integer> typeCountMap = new HashMap<>();
            typeCountMap.put("游泳", 0);
            typeCountMap.put("瑜伽", 0);
            typeCountMap.put("有氧运动", 0);
            typeCountMap.put("力量训练", 0);
            typeCountMap.put("其他", 0);

            for (com.gym.entity.Course course : courses) {
                String courseName = course.getName();
                if (courseName.contains("游泳")) {
                    typeCountMap.put("游泳", typeCountMap.get("游泳") + 1);
                } else if (courseName.contains("瑜伽")) {
                    typeCountMap.put("瑜伽", typeCountMap.get("瑜伽") + 1);
                } else if (courseName.contains("有氧")) {
                    typeCountMap.put("有氧运动", typeCountMap.get("有氧运动") + 1);
                } else if (courseName.contains("力量")) {
                    typeCountMap.put("力量训练", typeCountMap.get("力量训练") + 1);
                } else {
                    typeCountMap.put("其他", typeCountMap.get("其他") + 1);
                }
            }

            // 按照固定顺序排列：游泳、瑜伽、有氧运动、力量训练、其他
            List<String> orderedNames = Arrays.asList("游泳", "瑜伽", "有氧运动", "力量训练", "其他");
            List<Integer> orderedCounts = new ArrayList<>();
            for (String name : orderedNames) {
                orderedCounts.add(typeCountMap.get(name));
            }

            // 转换为前端需要的格式
            data.put("names", orderedNames.toArray(new String[0]));
            data.put("counts", orderedCounts.stream().mapToInt(Integer::intValue).toArray());

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取课程类型分布数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/coach-radar")
    public Result<Map<String, Object>> getCoachRadarData() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("coachNames", new String[] { "张教练", "李教练", "王教练" });

            List<Map<String, Object>> coachData = new ArrayList<>();

            Map<String, Object> coach1 = new HashMap<>();
            coach1.put("value", new int[] { 85, 90, 75, 80, 88 });
            coach1.put("name", "张教练");
            coachData.add(coach1);

            Map<String, Object> coach2 = new HashMap<>();
            coach2.put("value", new int[] { 78, 85, 90, 85, 80 });
            coach2.put("name", "李教练");
            coachData.add(coach2);

            Map<String, Object> coach3 = new HashMap<>();
            coach3.put("value", new int[] { 92, 80, 85, 78, 85 });
            coach3.put("name", "王教练");
            coachData.add(coach3);

            data.put("coachData", coachData);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取教练雷达图数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/user-stats/{userId}")
    public Result<Map<String, Object>> getUserStats(@PathVariable Integer userId) {
        try {
            Map<String, Object> stats = new HashMap<>();

            stats.put("totalOrders", 12);
            stats.put("totalSpent", 5800);
            stats.put("totalCourses", 8);
            stats.put("totalHours", 48);

            stats.put("monthlyOrders", new int[] { 2, 3, 1, 4, 2 });
            stats.put("monthlySpent", new int[] { 800, 1200, 400, 1600, 800 });
            stats.put("monthlyHours", new int[] { 8, 12, 4, 16, 8 });

            Map<String, Object> courseType1 = new HashMap<>();
            courseType1.put("name", "瑜伽");
            courseType1.put("value", 3);
            Map<String, Object> courseType2 = new HashMap<>();
            courseType2.put("name", "健身");
            courseType2.put("value", 2);
            Map<String, Object> courseType3 = new HashMap<>();
            courseType3.put("name", "游泳");
            courseType3.put("value", 2);
            Map<String, Object> courseType4 = new HashMap<>();
            courseType4.put("name", "舞蹈");
            courseType4.put("value", 1);
            stats.put("courseTypeDistribution", new Map[] { courseType1, courseType2, courseType3, courseType4 });

            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取用户统计数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/coach-stats/{coachId}")
    public Result<Map<String, Object>> getCoachStats(@PathVariable Integer coachId) {
        try {
            Map<String, Object> stats = new HashMap<>();

            stats.put("totalStudents", 45);
            stats.put("totalHours", 320);
            stats.put("totalRevenue", 25600);
            stats.put("avgRating", 4.8);

            stats.put("weeklyStudents", new int[] { 8, 10, 12, 9, 11, 15, 13 });
            stats.put("weeklyHours", new int[] { 45, 52, 60, 48, 55, 68, 62 });
            stats.put("weeklyRevenue", new int[] { 3200, 3800, 4400, 3600, 4200, 5200, 4800 });

            Map<String, Object> progress1 = new HashMap<>();
            progress1.put("name", "已完成");
            progress1.put("value", 35);
            Map<String, Object> progress2 = new HashMap<>();
            progress2.put("name", "进行中");
            progress2.put("value", 8);
            Map<String, Object> progress3 = new HashMap<>();
            progress3.put("name", "未开始");
            progress3.put("value", 2);
            stats.put("studentProgress", new Map[] { progress1, progress2, progress3 });

            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取教练统计数据失败: " + e.getMessage());
        }
    }
}
