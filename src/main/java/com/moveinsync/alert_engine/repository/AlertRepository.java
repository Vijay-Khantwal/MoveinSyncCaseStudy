package com.moveinsync.alert_engine.repository;

import com.moveinsync.alert_engine.entity.Alert;
import com.moveinsync.alert_engine.entity.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, String> {

    List<Alert> findBySourceTypeAndTimestampAfter(
            String sourceType,
            LocalDateTime time
    );

    List<Alert> findByStatus(AlertStatus status);
}