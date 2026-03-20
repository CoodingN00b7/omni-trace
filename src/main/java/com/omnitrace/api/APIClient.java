package com.omnitrace.api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;

public class APIClient {

    private static final String FREE_URL = "https://leakcheck.io/api/public";
    private static final String UA       = "OmniTrace/2.0 Security Scanner";

    private final HttpClient http;

    public APIClient(String key) {
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public BreachResult checkEmail(String email) {
        try {
            String url = FREE_URL + "?check=" + encode(email);

            HttpResponse<String> res = http.send(
                HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", UA)
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .build(),
                HttpResponse.BodyHandlers.ofString());

            System.out.println("[API] Status: " + res.statusCode());
            System.out.println("[API] Body: "   + res.body());

            return res.statusCode() == 200
                ? parse(email, res.body())
                : BreachResult.unknown(email);

        } catch (Exception e) {
            System.err.println("[API] Request failed: " + e.getMessage());
            return BreachResult.unknown(email);
        }
    }

    private BreachResult parse(String email, String json) {
        try {
            JSONObject root = new JSONObject(json);

            if (!root.optBoolean("success", false)) {
                System.err.println("[API] success=false: "
                        + root.optString("message", "no message"));
                return BreachResult.unknown(email);
            }

            int found = root.optInt("found", 0);
            List<String> sources = new ArrayList<>();

            JSONArray arr = root.optJSONArray("sources");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    Object item = arr.get(i);
                    if (item instanceof JSONObject src) {
                        String name = src.optString("name", "");
                        String date = src.optString("date", "");
                        if (!name.isEmpty()) {
                            sources.add(date.isEmpty() ? name : name + " (" + date + ")");
                        }
                    } else if (item instanceof String s) {
                        sources.add(s);
                    }
                }
            }

            return new BreachResult(email, found, sources);

        } catch (Exception e) {
            System.err.println("[API] Parse error: " + e.getMessage());
            return BreachResult.unknown(email);
        }
    }

    private static String encode(String v) {
        try {
            return java.net.URLEncoder.encode(
                    v, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return v;
        }
    }

    public record BreachResult(
            String       email,
            int          found,
            List<String> sources
    ) {
        public static BreachResult unknown(String email) {
            return new BreachResult(email, 0, List.of());
        }
        public boolean isBreached() { return found > 0; }
        public boolean isHighRisk() { return found >= 3; }
    }
}
