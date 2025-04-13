package com.dac.fly.reservationservice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.dac.fly.reservationservice.repository.command",
    entityManagerFactoryRef = "commandEntityManagerFactory",
    transactionManagerRef = "commandTransactionManager"
)
public class CommandDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.command")
    public DataSource commandDataSource() {
        return org.springframework.boot.jdbc.DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.jpa.command")
    public JpaProperties commandJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean commandEntityManagerFactory(
            @Qualifier("commandDataSource") DataSource dataSource,
            @Qualifier("commandJpaProperties") JpaProperties jpaProperties
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.dac.fly.reservationservice.entity.command");
        emf.setPersistenceUnitName("command");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaPropertyMap(jpaProperties.getProperties());
        return emf;
    }

    @Bean
    public PlatformTransactionManager commandTransactionManager(
            @Qualifier("commandEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf
    ) {
        return new JpaTransactionManager(emf.getObject());
    }
}
