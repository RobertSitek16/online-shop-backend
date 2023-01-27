package com.robert.shop.admin.order.repository;

import com.robert.shop.admin.order.model.AdminOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminOrderLogRepository extends JpaRepository<AdminOrderLog, Long> {
}
