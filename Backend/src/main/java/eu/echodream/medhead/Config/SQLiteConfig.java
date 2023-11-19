package eu.echodream.medhead.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configuration
public class SQLiteConfig {

    @Value("${medhead.sqlite_file.dev}")
    private String devDB;

    @Value("${medhead.sqlite_file.prod}")
    private String prodDB;

    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + prodDB);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
