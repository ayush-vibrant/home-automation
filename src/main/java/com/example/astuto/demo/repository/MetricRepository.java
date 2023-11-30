package com.example.astuto.demo.repository;

import com.example.astuto.demo.dto.MetricDTO;
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

    @Query(value = "SELECT new com.example.astuto.demo.dto.MetricDTO(" +
            "m.deviceId, " +
            "AVG(m.energyConsumed), " +
            "TRUNC(m.timestamp / 3600) * 3600) " +
            "FROM Metric m " +
            "WHERE m.timestamp BETWEEN :startTimestamp AND :endTimestamp " +
            "AND m.deviceId = :deviceId " +
            "GROUP BY m.deviceId, TRUNC(m.timestamp / 3600)",
            nativeQuery = true)
    List<MetricDTO> findDownsampledByDeviceIdAndTimestampBetween(String deviceId, Long startTimestamp, Long endTimestamp);

}
