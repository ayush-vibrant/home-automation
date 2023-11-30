package com.example.astuto.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO {
    private String deviceId;
    private Double averageEnergyConsumed;
    private Long averageTimestamp;
}
