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
            int courseCount = courseService.findAll().getData().size();
            int orderCount = orderService.findAll().getData().size();
            int userCount = userService.findAll().getData().size();
            int coachCount = coachService.findAll().getData().size();

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

            if ("month".equals(timeRange)) {
                xAxis = new String[] { "第1周", "第2周", "第3周", "第4周" };
                courseSales = new int[] { 45, 52, 38, 65 };
                memberGrowth = new int[] { 28, 35, 42, 50 };
                activity = new int[] { 120, 145, 130, 160 };
                revenue = new int[] { 150, 180, 165, 200 };
            } else if ("year".equals(timeRange)) {
                xAxis = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
                courseSales = new int[] { 180, 220, 195, 240, 280, 320, 350, 380, 340, 310, 290, 260 };
                memberGrowth = new int[] { 120, 145, 160, 185, 210, 235, 260, 285, 270, 255, 240, 225 };
                activity = new int[] { 480, 520, 490, 560, 620, 680, 720, 760, 700, 660, 620, 580 };
                revenue = new int[] { 600, 720, 680, 780, 860, 940, 1020, 1100, 980, 920, 860, 800 };
            } else {
                xAxis = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
                courseSales = new int[] { 5, 20, 36, 10, 10, 20, 15 };
                memberGrowth = new int[] { 8, 15, 25, 18, 22, 30, 28 };
                activity = new int[] { 30, 45, 35, 50, 40, 55, 45 };
                revenue = new int[] { 20, 35, 45, 30, 40, 50, 55 };
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

            Map<String, Object> item1 = new HashMap<>();
            item1.put("value", 120);
            item1.put("name", "普通会员");
            data.add(item1);

            Map<String, Object> item2 = new HashMap<>();
            item2.put("value", 85);
            item2.put("name", "银卡会员");
            data.add(item2);

            Map<String, Object> item3 = new HashMap<>();
            item3.put("value", 45);
            item3.put("name", "金卡会员");
            data.add(item3);

            Map<String, Object> item4 = new HashMap<>();
            item4.put("value", 20);
            item4.put("name", "钻石会员");
            data.add(item4);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取会员等级分布数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/course-type-distribution")
    public Result<Map<String, Object>> getCourseTypeDistribution() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("names", new String[] { "瑜伽", "健身", "游泳", "舞蹈", "搏击", "其他" });
            data.put("counts", new int[] { 35, 45, 25, 20, 15, 10 });

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
