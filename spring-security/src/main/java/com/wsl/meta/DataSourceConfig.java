package com.wsl.meta;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wsl
 * @date 2019/6/13
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getMyDataSource() {
        return DataSourceBuilder.create().build();
    }
}
