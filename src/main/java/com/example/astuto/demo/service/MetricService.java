package com.example.astuto.demo.service;

import com.example.astuto.demo.dto.MetricDTO;
import com.example.astuto.demo.model.Metric;
import com.example.astuto.demo.model.MetricHourlyBucket;
import com.example.astuto.demo.repository.MetricHourlyBucketRepository;
import com.example.astuto.demo.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetricService {

    private static final int MAX_DATA_POINTS = 200;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private MetricHourlyBucketRepository metricHourlyBucketRepository;

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

    public void aggregateDataIntoHourlyBuckets(String deviceId, Long start, Long end) {
        // Assuming 'start' and 'end' are aligned to hour boundaries
        for (long current = start; current < end; current += 3600) {
            long bucketEnd = current + 3600;
            List<Metric> metricsInBucket = metricRepository.findByDeviceIdAndTimestampBetween(deviceId, current, bucketEnd);

            if (!metricsInBucket.isEmpty()) {
                MetricHourlyBucket bucket = createHourlyBucket(metricsInBucket, deviceId, current);
                metricHourlyBucketRepository.save(bucket);
            }
        }
    }

    private MetricHourlyBucket createHourlyBucket(List<Metric> metrics, String deviceId, Long startTimestamp) {
        MetricHourlyBucket bucket = new MetricHourlyBucket();
        bucket.setDeviceId(deviceId);
        bucket.setBucketStartTimestamp(startTimestamp);
        bucket.setBucketEndTimestamp(startTimestamp + 3600); // 3600 seconds for one hour

        BigDecimal totalEnergy = metrics.stream()
                .filter(metric -> metric.getTimestamp() >= startTimestamp && metric.getTimestamp() < bucket.getBucketEndTimestamp())
                .map(metric -> BigDecimal.valueOf(metric.getEnergyConsumed()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        bucket.setTotalEnergyConsumed(totalEnergy);
        bucket.setDataPointCount((long) metrics.size());

        return bucket;
    }

    public Metric saveMetric(Metric metric) {
        return metricRepository.save(metric);
    }
}

