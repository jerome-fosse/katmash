package io.jfo.katmash

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@ContextConfiguration(initializers = [DatabaseTestConfiguration.Initializer::class])
@ActiveProfiles("test")
open class DatabaseTestConfiguration {

    companion object {
        const val POSTGRESQL_PORT = 5432

        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:11.5")
                .apply {
                    withExposedPorts(POSTGRESQL_PORT)
                    withDatabaseName("katmashtest")
                    withUsername("test")
                    withPassword(("test"))
                    withFileSystemBind("src/db/init", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
                }
    }

    internal class Initializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(context: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                    "katmash.database.port=${postgres.getMappedPort(POSTGRESQL_PORT)}"
            ).applyTo(context.environment)
        }
    }
}