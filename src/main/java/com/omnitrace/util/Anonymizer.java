package com.omnitrace.util;

import com.omnitrace.model.FindingRecord;

public class Anonymizer {
    public static void mask(FindingRecord r) {
        r.setMaskedValue(switch (r.getDataType()) {
            case EMAIL   -> maskEmail(r.getRawValue());
            case PHONE   -> maskPhone(r.getRawValue());
            case AADHAAR -> maskAadhaar(r.getRawValue());
            case PAN     -> maskPan(r.getRawValue());
            case IPV4    -> maskIpv4(r.getRawValue());
            case IPV6    -> maskIpv6(r.getRawValue());
            case URL     -> maskUrl(r.getRawValue());
        });
    }

    private static String maskEmail(String e) {
        int at = e.indexOf('@');
        return at <= 0 ? "***@***" : e.charAt(0) + "***" + e.substring(at);
    }

    private static String maskPhone(String p) {
        String d = p.replaceAll("[^\\d]", "");
        return d.length() < 7 ? "XXXXXXXXXX" : "+XX-XXXXX-" + d.substring(d.length() - 5);
    }

    private static String maskAadhaar(String a) {
        String d = a.replaceAll("\\s", "");
        return d.length() != 12 ? "XXXX XXXX XXXX" : "XXXX XXXX " + d.substring(8);
    }

    private static String maskPan(String p) {
        return p.length() != 10 ? "XXXXXXXXXX" : "XXXXX" + p.substring(5, 9) + "X";
    }

    private static String maskIpv4(String ip) {
        String[] p = ip.split("\\.");
        return p.length != 4 ? "X.X.X.X" : p[0] + "." + p[1] + ".X.X";
    }

    private static String maskIpv6(String ip) {
        String[] p = ip.split(":");
        if (p.length < 4) return ip;
        p[2] = "XXXX"; p[3] = "XXXX";
        return String.join(":", p);
    }

    private static String maskUrl(String url) {
        try {
            int ss = url.indexOf("//") + 2;
            int de = url.indexOf("/", ss);
            if (de < 0) de = url.length();
            String scheme = url.substring(0, ss);
            String domain = url.substring(ss, de);
            String rest   = url.substring(de);
            String masked = domain.length() > 7
                ? domain.substring(0, 3) + "***" + domain.substring(domain.length() - 4)
                : "***";
            return scheme + masked + rest;
        } catch (Exception e) { return "***"; }
    }
}
