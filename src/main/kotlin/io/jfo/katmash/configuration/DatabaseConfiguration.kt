package io.jfo.katmash.configuration

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration(
        @Value("\${katmash.database.host:localhost}")
        val host: String,
        @Value("\${katmash.database.port:5432}")
        val port: Int,
        @Value("\${katmash.database.name:postgres}")
        val database: String,
        @Value("\${katmash.database.username:root}")
        val username: String,
        @Value("\${katmash.database.password:admin}")
        val password: String
): AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): PostgresqlConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .username(username)
                        .password(password)
                        .host(host)
                        .port(port)
                        .database(database)
                        .build()
        )
    }
}