package com.omnitrace.scanner;

import com.omnitrace.model.FindingRecord;
import com.omnitrace.model.FindingRecord.DataType;
import com.omnitrace.util.Anonymizer;
import com.omnitrace.util.RiskScorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;

public class ScannerService {

    private static final Pattern EMAIL   = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE   = Pattern.compile("(\\+91[\\-\\s]?)?0?(91)?[789]\\d{9}");
    private static final Pattern URL     = Pattern.compile("https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+");
    private static final Pattern IPV4    = Pattern.compile("\\b((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b");
    private static final Pattern IPV6    = Pattern.compile("([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|::([0-9a-fA-F]{1,4}:){1,6}[0-9a-fA-F]{1,4}");
    private static final Pattern AADHAAR = Pattern.compile("\\b[2-9]\\d{3}\\s\\d{4}\\s\\d{4}\\b");
    private static final Pattern PAN     = Pattern.compile("\\b[A-Z]{5}[0-9]{4}[A-Z]\\b");

    private record PE(Pattern pattern, DataType type) {}

    private static final List<PE> PATTERNS = List.of(
        new PE(AADHAAR, DataType.AADHAAR),
        new PE(PAN,     DataType.PAN),
        new PE(EMAIL,   DataType.EMAIL),
        new PE(PHONE,   DataType.PHONE),
        new PE(IPV6,    DataType.IPV6),
        new PE(IPV4,    DataType.IPV4),
        new PE(URL,     DataType.URL)
    );

    public List<FindingRecord> scanFile(Path filePath) throws IOException {
        return scanText(Files.readString(filePath), filePath.toString());
    }

    public List<FindingRecord> scanText(String text, String label) {
        List<FindingRecord> findings = new ArrayList<>();
        for (PE p : PATTERNS) {
            Matcher m = p.pattern().matcher(text);
            while (m.find()) {
                FindingRecord r = new FindingRecord(
                    p.type(), m.group(), null, label, RiskScorer.score(p.type()));
                Anonymizer.mask(r);
                findings.add(r);
            }
        }
        return findings;
    }
}
