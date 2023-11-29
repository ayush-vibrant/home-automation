package com.example.astuto.demo.service;

import com.example.astuto.demo.model.Metric;
import com.example.astuto.demo.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetricService {

    private static final int MAX_DATA_POINTS = 200;

    @Autowired
    private MetricRepository metricRepository;

    public List<Metric> getDownsampledMetrics(String deviceId, Long startTimestamp, Long endTimestamp) {
        Long count = metricRepository.countByDeviceIdAndTimestampBetween(deviceId, startTimestamp, endTimestamp);

        if (count <= MAX_DATA_POINTS) {
            return metricRepository.findByDeviceIdAndTimestampBetween(deviceId, startTimestamp, endTimestamp);
        }

        int factor = (int) Math.ceil((double) count / MAX_DATA_POINTS);
        return downsampleMetrics(deviceId, startTimestamp, endTimestamp, factor);
    }

    private List<Metric> downsampleMetrics(String deviceId, Long start, Long end, int factor) {
        List<Metric> downsampled = new ArrayList<>();
        Long currentStart = start;

        while (currentStart < end) {
            Long currentEnd = (Long) Math.min(currentStart + factor - 1, end);
            List<Metric> batch = metricRepository.findByDeviceIdAndTimestampBetween(deviceId, currentStart, currentEnd);

            if (!batch.isEmpty()) {
                downsampled.add(averageMetrics(batch, deviceId));
            }

            currentStart += factor;
        }

        return downsampled;
    }

    private Metric averageMetrics(List<Metric> metrics, String deviceId) {
        double totalEnergy = 0;
        long totalTimestamp = 0;

        for (Metric metric : metrics) {
            totalEnergy += metric.getEnergyConsumed();
            totalTimestamp += metric.getTimestamp();
        }

        Metric averageMetric = new Metric();
        averageMetric.setEnergyConsumed(Double.valueOf(totalEnergy / metrics.size()));
        averageMetric.setTimestamp(Long.valueOf(totalTimestamp / metrics.size())); // Average timestamp
        averageMetric.setDeviceId(deviceId); // deviceId is same for all metrics in the batch

        return averageMetric;
    }

    public Metric saveMetric(Metric metric) {
        return metricRepository.save(metric);
    }
}

