package com.omnitrace.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "footprint_results")
public class FindingRecord {

    public enum DataType { EMAIL, PHONE, URL, IPV4, IPV6, AADHAAR, PAN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @Column(name = "raw_value", columnDefinition = "TEXT")
    private String rawValue;

    @Column(name = "masked_value", columnDefinition = "TEXT")
    private String maskedValue;

    @Column(name = "source_file")
    private String sourceFile;

    @Column(name = "risk_score")
    private int riskScore;

    @Column(name = "flagged")
    private boolean flagged;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public FindingRecord() {}

    public FindingRecord(DataType dataType, String rawValue, String maskedValue,
                         String sourceFile, int riskScore) {
        this.dataType    = dataType;
        this.rawValue    = rawValue;
        this.maskedValue = maskedValue;
        this.sourceFile  = sourceFile;
        this.riskScore   = riskScore;
        this.flagged     = riskScore >= 8;
        this.timestamp   = LocalDateTime.now();
    }

    public int getId()                   { return id; }
    public DataType getDataType()        { return dataType; }
    public String getRawValue()          { return rawValue; }
    public String getMaskedValue()       { return maskedValue; }
    public void setMaskedValue(String v) { this.maskedValue = v; }
    public String getSourceFile()        { return sourceFile; }
    public int getRiskScore()            { return riskScore; }
    public boolean isFlagged()           { return flagged; }
    public LocalDateTime getTimestamp()  { return timestamp; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
}
