## Example API Usage with cURL

### 1. Add a Metric

#### Request
```bash
curl -X POST http://localhost:8080/metrics \
     -H 'Content-Type: application/json' \
     -d '{
           "deviceId": "device123",
           "energyConsumed": 10.5,
           "timestamp": 1633036800
         }'
```

#### Response
```bash 
{
    "id": 1,
    "deviceId": "device123",
    "energyConsumed": 10.5,
    "timestamp": 1633036800
}
```

### 2. Get Metrics

#### Request
```bash
curl -X GET 'http://localhost:8080/metrics?deviceId=device123&startTimestamp=1633036800&endTimestamp=1633123200'
```

#### Response
```bash
[
    {
        "id": 1,
        "deviceId": "device123",
        "energyConsumed": 10.5,
        "timestamp": 1633036800
    },
    {
        "id": 2,
        "deviceId": "device123",
        "energyConsumed": 12.0,
        "timestamp": 1633040400
    }
    // ... more metrics ...
]
```
