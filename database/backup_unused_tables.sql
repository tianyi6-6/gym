-- 备份未使用的数据表
-- 执行时间: `date +%Y-%m-%d %H:%M:%S`
-- 备份教练预约表
CREATE TABLE IF NOT EXISTS `coach_appointment_backup` LIKE `coach_appointment`;
INSERT INTO `coach_appointment_backup`
SELECT *
FROM `coach_appointment`;
-- 备份器材预约表
CREATE TABLE IF NOT EXISTS `equipment_appointment_backup` LIKE `equipment_appointment`;
INSERT INTO `equipment_appointment_backup`
SELECT *
FROM `equipment_appointment`;
-- 显示备份结果
SELECT '教练预约表备份结果:' AS `info`;
SELECT COUNT(*) AS `原表记录数`
FROM `coach_appointment`;
SELECT COUNT(*) AS `备份表记录数`
FROM `coach_appointment_backup`;
SELECT '器材预约表备份结果:' AS `info`;
SELECT COUNT(*) AS `原表记录数`
FROM `equipment_appointment`;
SELECT COUNT(*) AS `备份表记录数`
FROM `equipment_appointment_backup`;