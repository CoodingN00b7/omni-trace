package com.omnitrace.util;

import com.omnitrace.model.FindingRecord.DataType;

public class RiskScorer {
    public static int score(DataType type) {
        return switch (type) {
            case AADHAAR -> 10;
            case PAN     -> 10;
            case EMAIL   ->  7;
            case PHONE   ->  6;
            case IPV4    ->  4;
            case IPV6    ->  4;
            case URL     ->  2;
        };
    }
}
