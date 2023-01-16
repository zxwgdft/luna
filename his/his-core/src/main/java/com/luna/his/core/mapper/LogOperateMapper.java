package com.luna.his.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.luna.his.core.log.FixedTypeConverter;


public interface LogOperateMapper {
    List<FixedTypeConverter.IdName> findEmployeeName(@Param("ids") Set<Long> ids);

    List<FixedTypeConverter.IdName> findHospitalName(@Param("ids") Set<Long> ids);

    List<FixedTypeConverter.IdName> findPatientName(@Param("ids") Set<Long> ids);

    List<FixedTypeConverter.IdName> findPatientSource(@Param("ids") List<Integer> ids);

    @Select("SELECT name FROM patient WHERE id = #{id}")
    String getPatientName(@Param("id") Long id);

    @Select("SELECT name FROM org_employee WHERE id = #{id}")
    String getEmployeeName(@Param("id") Long id);

}