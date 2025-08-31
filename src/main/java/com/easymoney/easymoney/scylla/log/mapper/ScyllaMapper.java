package com.easymoney.easymoney.scylla.log.mapper;

import com.easymoney.easymoney.scylla.log.dao.OperationLogDao;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * Mapper que expone los DAOs generados por el annotation processor de ScyllaDB.
 * Se utiliza para construir DAOs con keyspace dinámico.
 */
@Mapper
public interface ScyllaMapper {

    /**
     * Expone el DAO para la entidad OperationLog, usando el keyspace especificado.
     *
     * @param keyspace Identificador del keyspace en ScyllaDB
     * @return DAO para operaciones sobre OperationLog
     */
    @DaoFactory
    OperationLogDao operationLogDao(@DaoKeyspace CqlIdentifier keyspace);
}