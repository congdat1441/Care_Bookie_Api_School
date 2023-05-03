package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.repository.projection.HospitalGetAllProjection;
import com.spring.carebookie.repository.projection.ServiceByHospitalIdProjection;
import com.spring.carebookie.repository.sql.HospitalSql;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    @Query(value = HospitalSql.GET_ALL_HOSPITAL, nativeQuery = true)
    List<HospitalGetAllProjection> getAllHospital();

    @Query(value = HospitalSql.GET_WARD_BY_HOSPITAL_ID, nativeQuery = true)
    List<String> getWardsByHospitalId(@Param("hospitalId") String hospitalId);

    @Query(value = HospitalSql.SEARCH_HOSPITAL_BY_KEY, nativeQuery = true)
    List<HospitalGetAllProjection> searchHospitalByKey(@Param("key") String key);

    @Query(value = HospitalSql.GET_ALL_SERVICE_BY_HOSPITAL_ID, nativeQuery = true)
    List<ServiceByHospitalIdProjection> getAllServiceByHospitalId(@Param("hospitalId") String hospitalId);

}
