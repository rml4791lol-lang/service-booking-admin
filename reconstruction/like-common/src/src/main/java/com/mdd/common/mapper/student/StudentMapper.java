/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdd.common.entity.student.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper
extends BaseMapper<Student> {

    /**
     * 券流水引用数（#23 停用规则）。
     *
     * <p>券实体在 T29.4 才接入，这里先用只读计数，避免有券记录的学员被物理删除。</p>
     */
    @Select(value={"SELECT COUNT(*) FROM la_coupon_flow WHERE student_id = #{studentId}"})
    long countCouponFlow(@Param("studentId") Integer studentId);

    /**
     * 券实例引用数（#23 停用规则）。
     */
    @Select(value={"SELECT COUNT(*) FROM la_coupon_instance WHERE student_id = #{studentId}"})
    long countCouponInstance(@Param("studentId") Integer studentId);
}

