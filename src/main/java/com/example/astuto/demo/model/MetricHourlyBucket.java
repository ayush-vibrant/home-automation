package com.example.astuto.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class MetricHourlyBucket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String deviceId;
    private Long bucketStartTimestamp; // Start of the time bucket
    private Long bucketEndTimestamp;   // End of the time bucket
    private BigDecimal totalEnergyConsumed;
    private Long dataPointCount;       // Number of data points aggregated

}
