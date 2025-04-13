package com.dac.fly.reservationservice.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayMultiDbConfig {

    @Bean
    @ConfigurationProperties("spring.flyway.command")
    public FlywayProperties commandFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean(initMethod = "migrate")
    public Flyway flywayCommand(@Qualifier("commandDataSource") DataSource dataSource) {
        FlywayProperties props = commandFlywayProperties();
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(props.getLocations().toArray(String[]::new))
                .baselineOnMigrate(true)
                .load();
    }

    @Bean
    @ConfigurationProperties("spring.flyway.query")
    public FlywayProperties queryFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean(initMethod = "migrate")
    public Flyway flywayQuery(@Qualifier("queryDataSource") DataSource dataSource) {
        FlywayProperties props = queryFlywayProperties();
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(props.getLocations().toArray(String[]::new))
                .baselineOnMigrate(true)
                .load();
    }
}
