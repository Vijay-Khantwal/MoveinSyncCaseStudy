# Intelligent Alert Escalation & Resolution System

## Overview

This system centralizes alerts from multiple fleet modules and applies
dynamic rule-based escalation and automated closure.

It demonstrates:

- Central alert ingestion
- Lightweight rule engine
- Background auto-close worker
- Dashboard analytics API
- Lifecycle event tracking

---

## Architecture

Modules:

Controller → Service → Repository → H2 Database

Rules are loaded dynamically from `rules.json`.

---

## Alert Model

Each alert contains:
