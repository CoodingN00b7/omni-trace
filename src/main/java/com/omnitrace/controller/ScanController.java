package com.omnitrace.controller;

import com.omnitrace.api.APIClient;
import com.omnitrace.model.FindingRecord;
import com.omnitrace.repository.FindingRepository;
import com.omnitrace.scanner.ScannerService;
import com.omnitrace.util.Anonymizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScannerService    scanner = new ScannerService();
    private final FindingRepository repo;

    @Value("${leakcheck.api.key:}")
    private String leakCheckKey;

    public ScanController(FindingRepository repo) { this.repo = repo; }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> res = new LinkedHashMap<>();
        try {
            Path tmp = Files.createTempFile("ot-", "-" + file.getOriginalFilename());
            file.transferTo(tmp);
            List<FindingRecord> findings = scanner.scanFile(tmp);
            Files.deleteIfExists(tmp);
            return respond(res, findings, file.getOriginalFilename());
        } catch (Exception e) {
            res.put("success", false); res.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(res);
        }
    }

    @PostMapping("/text")
    public ResponseEntity<Map<String, Object>> text(@RequestBody Map<String, String> body) {
        Map<String, Object> res = new LinkedHashMap<>();
        try {
            List<FindingRecord> findings = scanner.scanText(
                body.getOrDefault("text", ""), body.getOrDefault("label", "web-paste"));
            return respond(res, findings, body.getOrDefault("label", "web-paste"));
        } catch (Exception e) {
            res.put("success", false); res.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(res);
        }
    }

    private ResponseEntity<Map<String, Object>> respond(
            Map<String, Object> res, List<FindingRecord> findings, String source) {

        // Breach check for emails
        List<Map<String, Object>> breachResults = new ArrayList<>();
        findings.stream()
            .filter(r -> r.getDataType() == FindingRecord.DataType.EMAIL)
            .forEach(r -> {
                APIClient.BreachResult br = new APIClient(leakCheckKey).checkEmail(r.getRawValue());
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("email",    br.email());
                m.put("found",    br.found());
                m.put("breached", br.isBreached());
                m.put("highRisk", br.isHighRisk());
                m.put("sources",  br.sources());
                breachResults.add(m);
            });

        repo.saveAll(findings);

        Map<String, Long> breakdown = findings.stream()
            .collect(Collectors.groupingBy(r -> r.getDataType().name(), Collectors.counting()));

        res.put("success",       true);
        res.put("totalFindings", findings.size());
        res.put("flagged",       findings.stream().filter(FindingRecord::isFlagged).count());
        res.put("breakdown",     breakdown);
        res.put("findings",      findings);
        res.put("breachResults", breachResults);
        return ResponseEntity.ok(res);
    }
}
