package com.gym.controller;

import com.gym.entity.CoachAppointment;
import com.gym.service.CoachAppointmentService;
import com.gym.common.Result;
import com.gym.common.RequireRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coach/appointment")
public class CoachAppointmentController {

    @Autowired
    private CoachAppointmentService appointmentService;

    @PostMapping("/create")
    @RequireRole({ "coach" })
    public Result<String> createAppointment(@RequestBody CoachAppointment appointment) {
        try {
            boolean hasConflict = appointmentService.checkTimeConflict(
                    appointment.getCoachId(),
                    appointment.getAppointmentTime().toString(),
                    appointment.getDuration(),
                    null);
            if (hasConflict) {
                return Result.error("时间冲突，请选择其他时间");
            }
            appointmentService.createAppointment(appointment);
            return Result.success("预约创建成功");
        } catch (Exception e) {
            return Result.error("预约创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    @RequireRole({ "coach" })
    public Result<String> updateAppointment(@RequestBody CoachAppointment appointment) {
        try {
            boolean hasConflict = appointmentService.checkTimeConflict(
                    appointment.getCoachId(),
                    appointment.getAppointmentTime().toString(),
                    appointment.getDuration(),
                    appointment.getId());
            if (hasConflict) {
                return Result.error("时间冲突，请选择其他时间");
            }
            appointmentService.updateAppointment(appointment);
            return Result.success("预约更新成功");
        } catch (Exception e) {
            return Result.error("预约更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @RequireRole({ "coach" })
    public Result<String> deleteAppointment(@PathVariable Integer id) {
        try {
            appointmentService.deleteAppointment(id);
            return Result.success("预约删除成功");
        } catch (Exception e) {
            return Result.error("预约删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    @RequireRole({ "coach" })
    public Result<CoachAppointment> getAppointmentById(@PathVariable Integer id) {
        try {
            CoachAppointment appointment = appointmentService.getAppointmentById(id);
            return Result.success(appointment);
        } catch (Exception e) {
            return Result.error("获取预约失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-coach/{coachId}")
    @RequireRole({ "coach" })
    public Result<List<CoachAppointment>> getAppointmentsByCoachId(@PathVariable Integer coachId) {
        try {
            List<CoachAppointment> appointments = appointmentService.getAppointmentsByCoachId(coachId);
            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error("获取预约列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-status/{coachId}/{status}")
    @RequireRole({ "coach" })
    public Result<List<CoachAppointment>> getAppointmentsByStatus(@PathVariable Integer coachId,
            @PathVariable Integer status) {
        try {
            List<CoachAppointment> appointments = appointmentService.getAppointmentsByCoachIdAndStatus(coachId, status);
            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error("获取预约列表失败：" + e.getMessage());
        }
    }

    @PostMapping("/search")
    @RequireRole({ "coach" })
    public Result<List<CoachAppointment>> searchAppointments(@RequestBody Map<String, Object> conditions) {
        try {
            List<CoachAppointment> appointments = appointmentService.getAppointmentsByConditions(conditions);
            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error("搜索预约失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-update")
    @RequireRole({ "coach" })
    public Result<String> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        try {
            Object idsObj = params.get("ids");
            List<Integer> ids = new java.util.ArrayList<>();
            if (idsObj instanceof List<?>) {
                for (Object item : (List<?>) idsObj) {
                    if (item instanceof Integer) {
                        ids.add((Integer) item);
                    }
                }
            }
            Integer status = (Integer) params.get("status");
            appointmentService.batchUpdateStatus(ids, status);
            return Result.success("批量更新成功");
        } catch (Exception e) {
            return Result.error("批量更新失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-date-range")
    @RequireRole({ "coach" })
    public Result<List<CoachAppointment>> getAppointmentsByDateRange(
            @RequestParam Integer coachId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<CoachAppointment> appointments = appointmentService.getAppointmentsByDateRange(coachId, startDate,
                    endDate);
            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error("获取预约列表失败：" + e.getMessage());
        }
    }
}
