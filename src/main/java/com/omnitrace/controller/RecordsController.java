package com.omnitrace.controller;

import com.omnitrace.model.FindingRecord;
import com.omnitrace.repository.FindingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/records")
public class RecordsController {

    private final FindingRepository repo;
    public RecordsController(FindingRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<FindingRecord>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        List<FindingRecord> all = repo.findAll();
        Map<String, Long> bd = all.stream()
            .collect(Collectors.groupingBy(r -> r.getDataType().name(), Collectors.counting()));
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("total",     all.size());
        m.put("flagged",   repo.countByFlaggedTrue());
        m.put("breakdown", bd);
        return ResponseEntity.ok(m);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> del(@PathVariable int id) {
        repo.deleteById(id); return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delAll() {
        repo.deleteAll(); return ResponseEntity.noContent().build();
    }
}
