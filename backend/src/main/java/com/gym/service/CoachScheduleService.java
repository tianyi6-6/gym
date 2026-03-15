package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.CoachSchedule;
import com.gym.mapper.CoachScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CoachScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(CoachScheduleService.class);

    @Autowired
    private CoachScheduleMapper scheduleMapper;

    public Result<CoachSchedule> getById(Integer id) {
        try {
            CoachSchedule schedule = scheduleMapper.findById(id);
            if (schedule != null) {
                return Result.success(schedule);
            } else {
                return Result.error("排班不存在");
            }
        } catch (Exception e) {
            logger.error("获取排班失败", e);
            return Result.error("获取排班失败");
        }
    }

    public Result<List<CoachSchedule>> getByCoachId(Integer coachId) {
        try {
            List<CoachSchedule> schedules = scheduleMapper.findByCoachId(coachId);
            return Result.success(schedules);
        } catch (Exception e) {
            logger.error("获取教练排班失败", e);
            return Result.error("获取教练排班失败");
        }
    }

    public Result<List<CoachSchedule>> getByDateRange(Integer coachId, Date startDate, Date endDate) {
        try {
            List<CoachSchedule> schedules = scheduleMapper.findByCoachIdAndDateRange(coachId, startDate, endDate);
            return Result.success(schedules);
        } catch (Exception e) {
            logger.error("获取排班日期范围失败", e);
            return Result.error("获取排班日期范围失败");
        }
    }

    public Result<Void> create(CoachSchedule schedule) {
        try {
            // 检查时间冲突
            int conflictCount = scheduleMapper.countTimeConflict(
                    schedule.getCoachId(),
                    schedule.getScheduleDate(),
                    schedule.getStartTime(),
                    schedule.getEndTime(),
                    null
            );
            if (conflictCount > 0) {
                return Result.error("时间冲突，请选择其他时间");
            }

            scheduleMapper.insert(schedule);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("创建排班失败", e);
            return Result.error("创建排班失败");
        }
    }

    public Result<Void> update(CoachSchedule schedule) {
        try {
            // 检查时间冲突
            int conflictCount = scheduleMapper.countTimeConflict(
                    schedule.getCoachId(),
                    schedule.getScheduleDate(),
                    schedule.getStartTime(),
                    schedule.getEndTime(),
                    schedule.getId()
            );
            if (conflictCount > 0) {
                return Result.error("时间冲突，请选择其他时间");
            }

            scheduleMapper.update(schedule);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("更新排班失败", e);
            return Result.error("更新排班失败");
        }
    }

    public Result<Void> delete(Integer id) {
        try {
            scheduleMapper.delete(id);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("删除排班失败", e);
            return Result.error("删除排班失败");
        }
    }

    public Result<Void> batchCreate(List<CoachSchedule> schedules) {
        try {
            for (CoachSchedule schedule : schedules) {
                // 检查时间冲突
                int conflictCount = scheduleMapper.countTimeConflict(
                        schedule.getCoachId(),
                        schedule.getScheduleDate(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        null
                );
                if (conflictCount > 0) {
                    return Result.error("时间冲突，请选择其他时间");
                }
                scheduleMapper.insert(schedule);
            }
            return Result.success(null);
        } catch (Exception e) {
            logger.error("批量创建排班失败", e);
            return Result.error("批量创建排班失败");
        }
    }
}
