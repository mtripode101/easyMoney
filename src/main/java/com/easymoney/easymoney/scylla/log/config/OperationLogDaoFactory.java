package com.easymoney.easymoney.scylla.log.config;

import com.easymoney.easymoney.scylla.log.dao.OperationLogDao;
import com.easymoney.easymoney.scylla.log.mapper.ScyllaMapper;
import com.easymoney.easymoney.scylla.log.mapper.ScyllaMapperBuilder;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura y expone el DAO para OperationLog como bean inyectable.
 * Utiliza el Mapper generado por ScyllaDB y el keyspace definido en configuración.
 */
@Configuration
public class OperationLogDaoFactory {

    /**
     * Crea y expone el bean OperationLogDao para inyección en servicios.
     *
     * @param session  CqlSession activo conectado a ScyllaDB
     * @param keyspace Nombre del keyspace definido en application.properties
     * @return instancia de OperationLogDao
     */
    @Bean
    public OperationLogDao operationLogDao(CqlSession session,
                                           @Value("${scylla.keyspace}") String keyspace) {
        ScyllaMapper mapper = new ScyllaMapperBuilder(session).build();
        return mapper.operationLogDao(CqlIdentifier.fromCql(keyspace));
    }
}