package com.omnitrace.controller;

import com.omnitrace.api.APIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Value("${leakcheck.api.key:}")
    private String key;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> check(@RequestParam String email) {
        APIClient.BreachResult r = new APIClient(key).checkEmail(email);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("email",    r.email());
        m.put("found",    r.found());
        m.put("breached", r.isBreached());
        m.put("highRisk", r.isHighRisk());
        m.put("sources",  r.sources());
        return ResponseEntity.ok(m);
    }
}
