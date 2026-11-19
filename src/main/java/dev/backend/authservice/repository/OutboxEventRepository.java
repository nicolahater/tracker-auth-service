package dev.backend.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.backend.authservice.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}