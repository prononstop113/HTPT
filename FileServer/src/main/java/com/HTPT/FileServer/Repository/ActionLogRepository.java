package com.HTPT.FileServer.Repository;

import com.HTPT.FileServer.Entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog,Long> {
}
