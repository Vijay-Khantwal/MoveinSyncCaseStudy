# Intelligent Alert Escalation & Resolution System

## Overview

This project implements a backend-only **Intelligent Alert Escalation & Resolution System** that centralizes alert ingestion, dynamically evaluates escalation rules, automatically closes alerts, and exposes analytics APIs for dashboard visualization.

The system simulates a fleet monitoring platform where modules like Safety, Compliance, and Feedback generate alerts that must be managed intelligently without constant manual intervention.

This implementation focuses on:

- Centralized alert ingestion
- Configurable rule engine
- Auto-close background processing
- Dashboard analytics APIs
- Alert lifecycle tracking
- Manual resolution support

---

## Problem Context

Fleet-monitoring modules generate alerts such as:

- Overspeeding
- Compliance document expiry
- Negative driver feedback

Previously, alerts required manual review. This system automates:

- Escalation
- Auto-closure
- Lifecycle tracking
- Trend analytics

---

## Core Requirements Implemented

### 1. Centralized Alert Management

Alerts from multiple sources are ingested via a unified REST API and stored in a normalized structure:

```
{ alertId, sourceType, severity, timestamp, status, metadata }
```

Alert lifecycle states:

```
OPEN → ESCALATED → AUTO-CLOSED → RESOLVED
```

All alerts and lifecycle events are persisted in the database.

---

### 2. Lightweight Rule Engine

Rules are stored in an external JSON configuration file and evaluated dynamically at runtime.

Example rule DSL:

```json
{
  "overspeed": { "escalate_if_count": 3, "window_mins": 60 },
  "feedback_negative": { "escalate_if_count": 2, "window_mins": 1440 },
  "compliance": { "auto_close_if": "document_valid" }
}
```

Features:

- Rules are not hardcoded
- Config-driven evaluation
- Easily extendable
- Decoupled from alert persistence

---

### 3. Auto-Close Background Job

A scheduled worker periodically scans alerts to:

- Apply expiration rules
- Auto-close eligible alerts
- Record lifecycle events

The job is idempotent and safe to re-run.

---

### 4. Dashboard Analytics

The system exposes REST APIs that power dashboard views such as:

- Severity summaries
- Top offending drivers
- Auto-closed alert transparency
- Trend analytics
- Alert drill-down

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- H2 in-memory database
- Jackson JSON
- Lombok

---

## Project Setup

### Prerequisites

- JDK 21
- Maven or Gradle
- Postman or curl for API testing

### Run Application

```bash
./mvnw spring-boot:run
```

### H2 Database Console

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:mem:alertdb
```

---

## API Documentation

### 1. Create Alert

**POST**

```
/alerts
```

Creates a new alert and triggers rule evaluation.

#### Sample Request

```json
{
  "sourceType": "overspeed",
  "severity": "WARNING",
  "metadata": {
    "driverId": "D123",
    "vehicleId": "V001"
  }
}
```

---

### 2. Manual Alert Resolution

**POST**

```
/alerts/{alertId}/resolve
```

Manually marks an alert as RESOLVED and logs a lifecycle event.

---

### 3. Alert Drill-Down

**GET**

```
/alerts/{alertId}
```

Returns:

- Alert details
- Full lifecycle event history

---

### 4. Recent Auto-Closed Alerts

**GET**

```
/alerts/auto-closed?hours=24
```

Returns recently auto-closed alerts.

---

### 5. Daily Trend Analytics

**GET**

```
/analytics/daily-trends
```

Returns aggregated daily counts of:

- Escalations
- Auto-closures
- Resolutions

Example response:

```json
[
  {
    "date": "2026-02-22",
    "ESCALATED": 2,
    "AUTO_CLOSED": 1,
    "RESOLVED": 1
  }
]
```

---

### 6. Compliance Document Renewal

**POST**

```
/compliance/{alertId}/renewed
```

Marks a compliance document as renewed and auto-closes the alert.

---

### 7. Rule Configuration View

**GET**

```
/config/rules
```

Returns active rule configuration.

---

### 8. Dashboard Summary

**GET**

```
/dashboard/summary
```

Provides aggregated dashboard statistics including:

- Severity counts
- Top offenders
- Recent lifecycle events

---

## Demo Test Scenarios

### Rule-Based Escalation Demo

Send 3 overspeed alerts for the same driver within 1 hour:

```json
{
  "sourceType": "overspeed",
  "severity": "WARNING",
  "metadata": {
    "driverId": "D001"
  }
}
```

Expected result:

→ Alert escalates to CRITICAL.

---

### Compliance Auto-Close Demo

Create compliance alert:

```json
{
  "sourceType": "compliance",
  "severity": "INFO",
  "metadata": {
    "driverId": "D002"
  }
}
```

Then call:

```
POST /compliance/{alertId}/renewed
```

Expected result:

→ Alert auto-closes.

---

### Manual Resolution Demo

```
POST /alerts/{alertId}/resolve
```

Expected result:

→ Alert status becomes RESOLVED.

---

## Functional Guarantees

- Rules are dynamically configurable
- Background jobs are idempotent
- Escalation triggers only once per condition
- Manual resolution is supported
- Alerts expire after configurable time
- Lifecycle events are fully tracked

---

## Alert Lifecycle Events

Each alert records:

- CREATED
- ESCALATED
- AUTO_CLOSED
- RESOLVED

This enables transparency and auditing.

---

## Future Improvements

- Web dashboard UI
- Kafka-based ingestion
- Persistent production database
- Authentication and RBAC
- Advanced rule DSL

---

## Conclusion

This system demonstrates a scalable backend architecture for intelligent alert management with:

- Automated escalation
- Dynamic rule evaluation
- Transparent analytics
- Full lifecycle tracking

It provides a strong foundation for real-world operational alert systems.
