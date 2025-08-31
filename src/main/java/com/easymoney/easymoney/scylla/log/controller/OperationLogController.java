package com.easymoney.easymoney.scylla.log.controller;

import com.easymoney.easymoney.scylla.log.model.OperationLog;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/log")
public class OperationLogController {

    private final ScyllaLogService logService;

    public OperationLogController(ScyllaLogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public void logAction(@RequestParam String userId,
                          @RequestParam String action,
                          @RequestParam String level,
                          @RequestParam String description,
                          @RequestParam String source) {
        logService.log(userId, action, level, description, source);
    }

    @GetMapping("/{id}")
    public OperationLog getLog(@PathVariable UUID id) {
        return logService.findById(id);
    }
}