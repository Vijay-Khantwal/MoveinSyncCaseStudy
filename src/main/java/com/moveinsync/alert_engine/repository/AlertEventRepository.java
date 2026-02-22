package com.moveinsync.alert_engine.repository;

import com.moveinsync.alert_engine.entity.AlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertEventRepository
        extends JpaRepository<AlertEvent, Long> {

    List<AlertEvent>
    findTop10ByOrderByTimestampDesc();

    List<AlertEvent>
    findByAlertIdOrderByTimestampAsc(String alertId);

    List<AlertEvent> findAllByOrderByTimestampAsc();
}