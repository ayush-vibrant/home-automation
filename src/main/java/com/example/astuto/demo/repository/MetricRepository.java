package com.example.astuto.demo.repository;

import com.example.astuto.demo.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByDeviceIdAndTimestampBetween(String deviceId, Long startTimeStamp, Long endTimeStamp);

    Long countByDeviceIdAndTimestampBetween(String deviceId, Long startTimeStamp, Long endTimeStamp);
}
