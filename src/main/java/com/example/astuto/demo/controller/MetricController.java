package com.example.astuto.demo.controller;

import com.example.astuto.demo.model.Metric;
import com.example.astuto.demo.repository.MetricRepository;
import com.example.astuto.demo.request.MetricsRequest;
import com.example.astuto.demo.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @Autowired
    private MetricService metricService;

    @Autowired
    private MetricRepository metricRepository;

    @PostMapping
    public ResponseEntity<Metric> createMetric(@RequestBody Metric metric) {
        Metric savedMetric = metricService.saveMetric(metric);
        return ResponseEntity.ok(savedMetric);
    }

    @GetMapping
    public ResponseEntity<List<Metric>> getMetrics(@RequestBody MetricsRequest request) {
        List<Metric> metrics = metricService.getDownsampledMetrics(request.getDeviceId(), request.getFrom(), request.getTo());
        return ResponseEntity.ok(metrics);
    }

}
