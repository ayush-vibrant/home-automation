package com.example.astuto.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_deviceId", columnList = "deviceId"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String deviceId;
    private Long timestamp; // unix epoch time
    private Double energyConsumed;
}
