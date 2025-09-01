package com.easymoney.easymoney.scylla.log.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * Configuración de conexión a ScyllaDB usando CqlSession.
 * Incluye tolerancia a fallos en el arranque mediante DriverConfigLoader.
 */
@Configuration
@Profile("!test")
public class ScyllaConfig {

    @Value("${scylla.contactPoint:localhost}")
    private String contactPoint;

    @Value("${scylla.port:9042}")
    private int port;

    @Value("${scylla.datacenter:datacenter1}")
    private String datacenter;

    @Value("${scylla.keyspace:easymoney}")
    private String keyspace;

    @Bean
    public CqlSession scyllaSession() {
        DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
            .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(10))
            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(5))
            .withDuration(DefaultDriverOption.HEARTBEAT_INTERVAL, Duration.ofSeconds(30))
            .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(10))
            .build();

        return CqlSession.builder()
            .addContactPoint(new InetSocketAddress(contactPoint, port))
            .withLocalDatacenter(datacenter)
            .withKeyspace(keyspace)
            .withConfigLoader(loader)
            .build();
    }
}