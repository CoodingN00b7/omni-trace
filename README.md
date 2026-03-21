# 🚀 OmniTrace — Digital Footprint Analyzer

<div align="center">

🔴 **Live Application**
👉 https://omni-trace-production-c4ab.up.railway.app/

<br>

![OmniTrace Banner](https://img.shields.io/badge/OmniTrace-v2.0-6366f1?style=for-the-badge\&logo=shield\&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge\&logo=mysql\&logoColor=white)
![Railway](https://img.shields.io/badge/Deployed_on-Railway-0B0D0E?style=for-the-badge\&logo=railway\&logoColor=white)

<br>

**🔐 Detect • Analyze • Protect**
Production-ready cybersecurity system for detecting PII leaks and breach exposure.

[🌐 Live Demo](https://omni-trace-production-c4ab.up.railway.app/) • [⚙️ Setup](#setup) • [📡 API](#api)

</div>

---

## 🧠 Overview

OmniTrace is a **real-world inspired cybersecurity platform** that detects sensitive data exposure and evaluates risk.

It simulates how **enterprise security systems**:

* Scan data for leaks
* Analyze breach exposure
* Score risk levels
* Store data securely

---

## 🎯 Problem Statement

Sensitive user data like **Aadhaar, PAN, emails, and phone numbers** are often:

* Stored insecurely
* Leaked unknowingly
* Exposed in breaches

👉 OmniTrace solves this by **automating detection + risk analysis**.

---

## ⚡ Key Features

* 🔎 Email breach detection via LeakCheck API
* 🧾 PII scanner (text + file upload)
* 📊 Real-time analytics dashboard
* ⚠️ Risk scoring engine
* 🔐 PII masking before DB storage
* 🗂️ Record management system

---

## 🧬 PII Detection Engine

| Type    | Risk  | Description          |
| ------- | ----- | -------------------- |
| Aadhaar | 🔴 10 | Highly sensitive     |
| PAN     | 🔴 10 | Financial identity   |
| Email   | 🟠 7  | Breach-prone         |
| Phone   | 🟡 6  | Moderately sensitive |
| IP      | 🟢 4  | Low sensitivity      |
| URL     | 🔵 2  | Informational        |

---

## 🏗️ System Architecture

```
User Input (Text/File)
        ↓
Scanner Service (Regex Engine)
        ↓
Risk Scorer → High/Medium/Low
        ↓
Data Masking Layer
        ↓
Database (MySQL)
        ↓
Dashboard (Analytics UI)
```

---

## 🧠 System Design Highlights

* **Modular Architecture** (Controller → Service → Repository)
* **Stateless Backend** (REST APIs)
* **Secure Data Handling**
* **Scalable Deployment via Docker + Railway**
* **External API Integration (LeakCheck)**

---

## 🛠️ Tech Stack

* Backend: Java 21, Spring Boot

* Frontend: Thymeleaf, JS

* Database: MySQL

* Deployment: Railway + Docker

---

## ⚙️ Setup

### Clone

* git clone https://github.com/CoodingN00b7/omni-trace.git

* cd omni-trace


### Database

* CREATE DATABASE omnitrace;


### Config

* spring.datasource.url=jdbc:mysql://localhost:3306/omnitrace
* spring.datasource.username=root
* spring.datasource.password=YOUR_PASSWORD

### Run

* mvn clean package -DskipTests

* java -jar target/omni-trace-2.0.0.jar

### Access

* http://localhost:8080

---

## 📡 API

* GET /api/email/check?email=

* POST /api/scan/upload

* POST /api/scan/text

* GET /api/records

* DELETE /api/records/{id}

---

## 🔐 Security

* ✔ Masked PII storage
* ✔ SQL injection safe
* ✔ Risk-based alerting

---

## 📈 Future Enhancements

* AI-based anomaly detection
* Dark web monitoring
* Authentication system
* Real-time alerts

---

## 👨‍💻 Author

Fardeen Akmal
Cybersecurity + Full Stack Developer

---

## ⭐ Support

If you like this project:

* ⭐ Star the repo
* 🍴 Fork it
* 📢 Share it

---

## 📜 License

MIT License
