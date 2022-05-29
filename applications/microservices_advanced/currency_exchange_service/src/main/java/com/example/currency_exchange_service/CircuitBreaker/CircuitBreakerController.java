package com.example.currency_exchange_service.CircuitBreaker;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample")
    @Retry(name="sample", fallbackMethod="hardcodedResponse") // default @Retry configuration
    public String sample() {
        this.logger.info("Sample API call received");
        ResponseEntity<String> res = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class); // This should fail
        return res.getBody();
    }

    public String hardcodedResponse(Exception ex) { // You can have different responses ruturned back for different kind of exceptions!
        return "fallback-qq";
    }
}
