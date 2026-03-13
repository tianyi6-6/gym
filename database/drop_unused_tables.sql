-- 删除未使用的数据表
-- 执行时间: `date +%Y-%m-%d %H:%M:%S`
-- 操作人: 系统管理员
-- 删除器材预约表（先删除外键约束相关的表）
DROP TABLE IF EXISTS `equipment_appointment`;
-- 删除教练预约表
DROP TABLE IF EXISTS `coach_appointment`;
-- 显示删除结果
SELECT '未使用数据表删除完成' AS `info`;