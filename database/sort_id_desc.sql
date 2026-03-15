-- 对健康档案管理系统和体测数据管理系统中的ID字段执行降序排序操作
-- 执行时间: `date +%Y-%m-%d %H:%M:%S`
-- 选择数据库
USE gym;
-- 1. 对body_assessment表执行降序排序
-- 创建临时表，保存按ID降序排列的数据
CREATE TABLE IF NOT EXISTS body_assessment_temp AS
SELECT *
FROM body_assessment
ORDER BY id DESC;
-- 删除原表数据
DELETE FROM body_assessment;
-- 重置自增计数器
ALTER TABLE body_assessment AUTO_INCREMENT = 1;
-- 重新插入数据，让自增ID按降序重新生成
INSERT INTO body_assessment (
        user_id,
        assessment_date,
        body_fat_rate,
        muscle_mass,
        bone_density,
        basal_metabolic_rate,
        bmi,
        visceral_fat_level,
        body_water_rate,
        protein_rate,
        skeletal_muscle_mass,
        weight,
        height,
        chest_circumference,
        waist_circumference,
        hip_circumference,
        assessment_result,
        coach_id
    )
SELECT user_id,
    assessment_date,
    body_fat_rate,
    muscle_mass,
    bone_density,
    basal_metabolic_rate,
    bmi,
    visceral_fat_level,
    body_water_rate,
    protein_rate,
    skeletal_muscle_mass,
    weight,
    height,
    chest_circumference,
    waist_circumference,
    hip_circumference,
    assessment_result,
    coach_id
FROM body_assessment_temp;
-- 删除临时表
DROP TABLE body_assessment_temp;
-- 2. 对health_profile表执行降序排序
-- 创建临时表，保存按ID降序排列的数据
CREATE TABLE IF NOT EXISTS health_profile_temp AS
SELECT *
FROM health_profile
ORDER BY id DESC;
-- 删除原表数据
DELETE FROM health_profile;
-- 重置自增计数器
ALTER TABLE health_profile AUTO_INCREMENT = 1;
-- 重新插入数据，让自增ID按降序重新生成
INSERT INTO health_profile (
        user_id,
        age,
        gender,
        height,
        weight,
        occupation,
        medical_history,
        allergy_history,
        surgery_history,
        exercise_history,
        exercise_habits,
        exercise_preference,
        sleep_habits,
        diet_habits,
        smoking_status,
        drinking_status,
        risk_level,
        risk_factors
    )
SELECT user_id,
    age,
    gender,
    height,
    weight,
    occupation,
    medical_history,
    allergy_history,
    surgery_history,
    exercise_history,
    exercise_habits,
    exercise_preference,
    sleep_habits,
    diet_habits,
    smoking_status,
    drinking_status,
    risk_level,
    risk_factors
FROM health_profile_temp;
-- 删除临时表
DROP TABLE health_profile_temp;
-- 3. 验证排序结果
-- 验证body_assessment表的排序结果
SELECT 'body_assessment表排序结果验证:' AS info;
SELECT id,
    user_id,
    assessment_date
FROM body_assessment
ORDER BY id DESC
LIMIT 10;
-- 验证health_profile表的排序结果
SELECT 'health_profile表排序结果验证:' AS info;
SELECT id,
    user_id,
    create_time
FROM health_profile
ORDER BY id DESC
LIMIT 10;
-- 显示操作完成信息
SELECT 'ID字段降序排序操作完成' AS info;