package com.easymoney.easymoney.scylla.log.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.easymoney.easymoney.scylla.log.model.OperationLog;

import java.util.UUID;

@Dao
public interface OperationLogDao {

    @Insert
    void save(OperationLog log);

    @Select
    OperationLog findById(UUID id);
}