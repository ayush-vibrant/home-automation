package com.example.astuto.demo.repository;

import com.example.astuto.demo.model.MetricHourlyBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricHourlyBucketRepository extends JpaRepository<MetricHourlyBucket, Long> {
    // Custom query methods if needed
}
