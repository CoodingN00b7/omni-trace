package com.omnitrace.util;

import com.omnitrace.model.FindingRecord;
import com.omnitrace.repository.FindingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FindingRepository repository;

    @Value("${app.seed.data:false}")
    private boolean seedData;

    public DataSeeder(FindingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (!seedData || repository.count() > 0) return;

        List<FindingRecord> records = new ArrayList<>();
        LocalDateTime base = LocalDateTime.now().minusDays(30);

        // ── EMAIL records ───────────────────────────────────────────────────
        String[][] emails = {
            {"rahul.sharma@gmail.com",      "r***@gmail.com",      "app-server.log"},
            {"priya.verma@yahoo.com",        "p***@yahoo.com",      "db-dump-2024.sql"},
            {"amit.singh@hotmail.com",       "a***@hotmail.com",    "user-export.csv"},
            {"neha.gupta@outlook.com",       "n***@outlook.com",    "auth-logs.txt"},
            {"vikram.patel@rediffmail.com",  "v***@rediffmail.com", "backup-jan.sql"},
            {"sunita.rao@gmail.com",         "s***@gmail.com",      "error-dump.log"},
            {"karan.mehta@company.in",       "k***@company.in",     "employee-data.csv"},
            {"divya.nair@gmail.com",         "d***@gmail.com",      "registration.log"},
            {"arjun.kapoor@icloud.com",      "a***@icloud.com",     "config-backup.txt"},
            {"pooja.joshi@gmail.com",        "p***@gmail.com",      "access-log.txt"},
        };
        for (int i = 0; i < emails.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.EMAIL, emails[i][0], emails[i][1], emails[i][2], 7);
            r.setTimestamp(base.plusDays(i * 3).plusHours(i));
            records.add(r);
        }

        // ── AADHAAR records ─────────────────────────────────────────────────
        String[][] aadhaar = {
            {"2345 6789 0123", "XXXX XXXX 0123", "kyc-records.sql"},
            {"3456 7890 1234", "XXXX XXXX 1234", "user-kyc-dump.csv"},
            {"4567 8901 2345", "XXXX XXXX 2345", "onboarding-data.txt"},
            {"5678 9012 3456", "XXXX XXXX 3456", "identity-logs.log"},
            {"6789 0123 4567", "XXXX XXXX 4567", "db-backup-feb.sql"},
            {"7890 1234 5678", "XXXX XXXX 5678", "audit-trail.txt"},
            {"8901 2345 6789", "XXXX XXXX 6789", "compliance-export.csv"},
            {"9012 3456 7890", "XXXX XXXX 7890", "app-server.log"},
        };
        for (int i = 0; i < aadhaar.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.AADHAAR, aadhaar[i][0], aadhaar[i][1], aadhaar[i][2], 10);
            r.setTimestamp(base.plusDays(i * 2).plusHours(i + 2));
            records.add(r);
        }

        // ── PAN records ─────────────────────────────────────────────────────
        String[][] pan = {
            {"ABCDE1234F", "XXXXX1234X", "tax-records.sql"},
            {"FGHIJ5678K", "XXXXX5678X", "finance-dump.csv"},
            {"KLMNO9012P", "XXXXX9012X", "payroll-export.txt"},
            {"PQRST3456U", "XXXXX3456X", "hr-data.log"},
            {"UVWXY7890Z", "XXXXX7890X", "audit-logs.sql"},
            {"MNOPQ1357R", "XXXXX1357X", "user-profile.csv"},
            {"STUVW2468X", "XXXXX2468X", "compliance.txt"},
        };
        for (int i = 0; i < pan.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.PAN, pan[i][0], pan[i][1], pan[i][2], 10);
            r.setTimestamp(base.plusDays(i * 2 + 1).plusHours(i + 1));
            records.add(r);
        }

        // ── PHONE records ───────────────────────────────────────────────────
        String[][] phones = {
            {"+919876543210", "+XX-XXXXX-43210", "contacts-export.csv"},
            {"+918765432109", "+XX-XXXXX-32109", "user-signup.log"},
            {"+917654321098", "+XX-XXXXX-21098", "crm-backup.sql"},
            {"+916543210987", "+XX-XXXXX-10987", "app-events.log"},
            {"+919988776655", "+XX-XXXXX-76655", "auth-trace.txt"},
            {"+918877665544", "+XX-XXXXX-65544", "db-dump-mar.sql"},
            {"+917766554433", "+XX-XXXXX-54433", "registration.log"},
        };
        for (int i = 0; i < phones.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.PHONE, phones[i][0], phones[i][1], phones[i][2], 6);
            r.setTimestamp(base.plusDays(i * 2).plusHours(i + 4));
            records.add(r);
        }

        // ── IPV4 records ────────────────────────────────────────────────────
        String[][] ipv4 = {
            {"192.168.1.105", "192.168.X.X", "network-trace.log"},
            {"10.0.0.25",     "10.0.X.X",    "firewall-log.txt"},
            {"172.16.0.50",   "172.16.X.X",  "access-log.log"},
            {"192.168.2.200", "192.168.X.X", "server-events.txt"},
            {"10.10.1.15",    "10.10.X.X",   "nginx-access.log"},
            {"203.0.113.42",  "203.0.X.X",   "api-trace.log"},
        };
        for (int i = 0; i < ipv4.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.IPV4, ipv4[i][0], ipv4[i][1], ipv4[i][2], 4);
            r.setTimestamp(base.plusDays(i * 3 + 1).plusHours(i));
            records.add(r);
        }

        // ── URL records ─────────────────────────────────────────────────────
        String[][] urls = {
            {"https://api.internal.company.com/v1/users",        "https://api***.com/v1/users",        "config-backup.txt"},
            {"https://admin.portal.in/dashboard",                "https://adm***.in/dashboard",        "error-dump.log"},
            {"https://db.private.network/query",                 "https://db.***.net/query",           "app-server.log"},
            {"https://payments.gateway.com/process",             "https://pay***.com/process",         "transaction.log"},
            {"https://internal.hrms.company.in/employee/list",   "https://int***.in/employee/list",    "hr-dump.sql"},
            {"https://s3.bucket.private.aws.com/backup/2024",    "https://s3.***.com/backup/2024",     "infra-log.txt"},
        };
        for (int i = 0; i < urls.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.URL, urls[i][0], urls[i][1], urls[i][2], 2);
            r.setTimestamp(base.plusDays(i * 2 + 2).plusHours(i + 3));
            records.add(r);
        }

        // ── IPV6 records ────────────────────────────────────────────────────
        String[][] ipv6 = {
            {"2001:db8:85a3:0000:0000:8a2e:0370:7334", "2001:db8:XXXX:XXXX:0000:8a2e:0370:7334", "ipv6-trace.log"},
            {"fe80:0000:0000:0000:0202:b3ff:fe1e:8329", "fe80:0000:XXXX:XXXX:0202:b3ff:fe1e:8329", "network-dump.txt"},
        };
        for (int i = 0; i < ipv6.length; i++) {
            FindingRecord r = new FindingRecord(
                FindingRecord.DataType.IPV6, ipv6[i][0], ipv6[i][1], ipv6[i][2], 4);
            r.setTimestamp(base.plusDays(i * 5 + 3).plusHours(i + 1));
            records.add(r);
        }

        repository.saveAll(records);
        System.out.printf("[Seeder] Inserted %d sample records into footprint_results%n", records.size());
    }
}
