-- OmniTrace v2 Database Setup
-- Run once before starting the application

CREATE DATABASE IF NOT EXISTS omnitrace;
USE omnitrace;

CREATE TABLE IF NOT EXISTS footprint_results (
  id           INT AUTO_INCREMENT PRIMARY KEY,
  data_type    VARCHAR(20)  NOT NULL,
  raw_value    TEXT         NOT NULL,
  masked_value TEXT,
  source_file  VARCHAR(512),
  risk_score   INT          DEFAULT 0,
  flagged      TINYINT(1)   DEFAULT 0,
  timestamp    DATETIME     NOT NULL
);
