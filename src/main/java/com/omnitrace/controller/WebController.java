package com.omnitrace.controller;

import com.omnitrace.model.FindingRecord;
import com.omnitrace.repository.FindingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final FindingRepository repo;

    public WebController(FindingRepository repo) { this.repo = repo; }

    @GetMapping("/")
    public String email(Model model) {
        model.addAttribute("activePage", "email");
        return "email";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<FindingRecord> all = repo.findAll();
        long total   = all.size();
        long flagged = all.stream().filter(FindingRecord::isFlagged).count();

        Map<String, Long> breakdown = all.stream()
            .collect(Collectors.groupingBy(r -> r.getDataType().name(), Collectors.counting()));

        List<FindingRecord> recentFlagged = all.stream()
            .filter(FindingRecord::isFlagged)
            .sorted(Comparator.comparing(FindingRecord::getTimestamp).reversed())
            .limit(6)
            .collect(Collectors.toList());

        // Last 7 days trend (count per day)
        Map<String, Long> trend = all.stream()
            .filter(r -> r.getTimestamp().isAfter(java.time.LocalDateTime.now().minusDays(7)))
            .collect(Collectors.groupingBy(
                r -> r.getTimestamp().toLocalDate().toString(), Collectors.counting()));

        model.addAttribute("total",        total);
        model.addAttribute("flagged",       flagged);
        model.addAttribute("breakdown",     breakdown);
        model.addAttribute("recentFlagged", recentFlagged);
        model.addAttribute("trend",         trend);
        model.addAttribute("activePage",    "dashboard");
        return "dashboard";
    }

    @GetMapping("/scanner")
    public String scanner(Model model) {
        model.addAttribute("activePage", "scanner");
        return "scanner";
    }

    @GetMapping("/records")
    public String records(Model model) {
        List<FindingRecord> all = repo.findAll()
            .stream()
            .sorted(Comparator.comparing(FindingRecord::getTimestamp).reversed())
            .collect(Collectors.toList());

        model.addAttribute("records",    all);
        model.addAttribute("total",      all.size());
        model.addAttribute("flagged",    all.stream().filter(FindingRecord::isFlagged).count());
        model.addAttribute("activePage", "records");
        return "records";
    }
}
