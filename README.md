# OmniTrace — Digital Footprint Analyzer

<div align="center">

![OmniTrace Banner](https://img.shields.io/badge/OmniTrace-v2.0-6366f1?style=for-the-badge&logo=shield&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Railway](https://img.shields.io/badge/Deployed_on-Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white)

**A Java-based security tool that identifies and logs potential PII leaks from local text-based sources and validates online exposure for emails using LeakCheck.io**

[Live Demo](#) • [Features](#features) • [Setup](#setup) • [API](#api-endpoints)

</div>

---

## Screenshots

| Email Threat Analyzer | Dashboard | Records |
|---|---|---|
| Hero page with breach checking | Stats, charts, recent findings | Filter, view, delete records |

---

## Features

- **Email Threat Analyzer** — Check if an email has appeared in known data breaches via LeakCheck.io (inspired by HaveIBeenPwned)
- **PII Scanner** — Upload a file or paste text to detect sensitive data using regex patterns
- **Live Dashboard** — Stats overview with animated counters and breakdown charts
- **Database Records** — View, filter by type, and delete stored findings
- **Risk Scoring** — Auto-flags high-risk records (Aadhaar/PAN score 10, auto-flagged)
- **PII Masking** — Masks sensitive data before storing in the database
- **50 Sample Records** — Auto-seeded on first startup for immediate demo

---

## PII Types Detected

| Type | Pattern | Risk Score | Auto-Flagged |
|------|---------|------------|--------------|
| Aadhaar | `XXXX XXXX XXXX` (12-digit) | 10 | Yes |
| PAN | `AAAAA9999A` | 10 | Yes |
| Email | Standard email format | 7 | No |
| Phone | Indian/Global mobile | 6 | No |
| IPv4 | Dotted decimal | 4 | No |
| IPv6 | Full/compressed | 4 | No |
| URL | HTTP/HTTPS links | 2 | No |

---

## Tech Stack

- **Backend** — Java 21, Spring Boot 3.2, Spring Data JPA
- **Frontend** — Thymeleaf, Vanilla JS, Custom CSS (no frameworks)
- **Database** — MySQL 8.0
- **API** — LeakCheck.io for breach detection
- **Deployment** — Docker, Railway

---

## Setup

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8.0+

### 1. Clone the repository
```bash
git clone https://github.com/CoodingN00b7/omni-trace.git
cd omni-trace
```

### 2. Create the database
```bash
mysql -u root -p
```
```sql
CREATE DATABASE IF NOT EXISTS omnitrace;
EXIT;
```

### 3. Configure environment variables
Copy the example config:
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/omnitrace?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
leakcheck.api.key=        # leave blank for free tier (50 checks/day)
app.seed.data=true        # set false after first run
```

### 4. Build and run
```bash
mvn clean package -DskipTests
java -jar target/omni-trace-2.0.0.jar
```

### 5. Open in browser
```
http://localhost:8080
```

---

## Pages

| Route | Description |
|-------|-------------|
| `/` | Email Threat Analyzer — breach check hero page |
| `/dashboard` | Stats, breakdown charts, recent high-risk findings |
| `/scanner` | Upload file or paste text to scan for PII |
| `/records` | View, filter by type, delete database records |

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/email/check?email=` | Check email against LeakCheck breach database |
| `POST` | `/api/scan/upload` | Upload file for PII scan |
| `POST` | `/api/scan/text` | Scan plain text for PII |
| `GET` | `/api/records` | Get all stored findings |
| `GET` | `/api/records/stats` | Get summary statistics |
| `DELETE` | `/api/records/{id}` | Delete a single record |
| `DELETE` | `/api/records` | Delete all records |

---

## Docker

```bash
docker build -t omni-trace .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host:3306/omnitrace \
  -e DB_USER=root \
  -e DB_PASS=yourpassword \
  omni-trace
```

---

## Deployment on Railway

1. Fork this repository
2. Create a new project on [Railway](https://railway.app)
3. Add a **MySQL** database service
4. Deploy from GitHub and set these environment variables on the app service:

```
DB_URL    =  jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USER   =  ${{MySQL.MYSQLUSER}}
DB_PASS   =  ${{MySQL.MYSQLPASSWORD}}
```

---

## Project Structure

```
omni-trace/
├── src/main/java/com/omnitrace/
│   ├── OmniTraceApplication.java      # Spring Boot entry point
│   ├── api/
│   │   └── APIClient.java             # LeakCheck.io integration
│   ├── controller/
│   │   ├── WebController.java         # Thymeleaf page routes
│   │   ├── ScanController.java        # /api/scan REST endpoints
│   │   ├── RecordsController.java     # /api/records REST endpoints
│   │   └── EmailController.java       # /api/email REST endpoints
│   ├── model/
│   │   └── FindingRecord.java         # JPA entity
│   ├── repository/
│   │   └── FindingRepository.java     # Spring Data JPA
│   ├── scanner/
│   │   └── ScannerService.java        # Regex PII detection engine
│   └── util/
│       ├── RiskScorer.java            # Risk score lookup
│       ├── Anonymizer.java            # PII masking logic
│       └── DataSeeder.java            # 50 sample records on startup
├── src/main/resources/
│   ├── application.properties         # Config (gitignored)
│   ├── application.properties.example # Safe template for GitHub
│   ├── static/
│   │   ├── css/style.css              # Full custom dark UI
│   │   └── js/app.js                  # Interactive JS
│   └── templates/
│       ├── email.html                 # Threat Analyzer hero page
│       ├── dashboard.html             # Stats dashboard
│       ├── scanner.html               # PII scanner
│       └── records.html               # Database records viewer
├── Dockerfile
└── pom.xml
```

---

## Security Notes

- `application.properties` is gitignored — never committed to GitHub
- Raw PII values are masked before database storage
- High-risk records (score ≥ 8) are automatically flagged
- All database inserts use JPA `PreparedStatement` (SQL injection safe)

---

## License

MIT License — feel free to use and modify.

---

<div align="center">
Built with Java 21 + Spring Boot by Fardeen Akmal
</div>
