package com.easymoney.easymoney.scylla.log.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.Instant;
import java.util.UUID;

@Entity
@CqlName("operation_log")
public class OperationLog {

    @PartitionKey
    private UUID id;

    @CqlName("user_id")
    private String userId;

    private String action;
    private Instant timestamp;

    private String level;
    private String description;
    private String source;

    public OperationLog() {
    }

    public OperationLog(String userId, String action, String level, String description, String source) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.action = action;
        this.level = level;
        this.description = description;
        this.source = source;
        this.timestamp = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSource(String source) {
        this.source = source;
    }
}