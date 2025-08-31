package com.easymoney.easymoney.scylla.log.service;

import com.easymoney.easymoney.scylla.log.dao.OperationLogDao;
import com.easymoney.easymoney.scylla.log.model.OperationLog;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScyllaLogService {

    private final OperationLogDao dao;

    public ScyllaLogService(OperationLogDao dao) {
        this.dao = dao;
    }

    public void log(String userId, String action, String level, String description, String source) {
        OperationLog log = new OperationLog(userId, action, level, description, source);
        dao.save(log);
    }

    public OperationLog findById(UUID id) {
        return dao.findById(id);
    }
}