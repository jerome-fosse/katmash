package io.jfo.katmash.configuration

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "katmash.database")
class DatabaseConfiguration {
    var host = "localhost"
    var port = 5432
    var schema = "postgres"
    var username = "root"
    var password = "admin"

    @Bean
    fun connectionFactory(): PostgresqlConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .username(username)
                        .password(password)
                        .host(host)
                        .port(port)
                        .schema(schema)
                        .build()
        )
    }
}