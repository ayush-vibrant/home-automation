package com.example.astuto.demo.scheduler;

import com.example.astuto.demo.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataAggregationScheduler {

    @Autowired
    private MetricService metricService;

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void aggregateData() {
        // Need to Call metricService.aggregateDataIntoHourlyBuckets(...) with appropriate parameters
    }
}
