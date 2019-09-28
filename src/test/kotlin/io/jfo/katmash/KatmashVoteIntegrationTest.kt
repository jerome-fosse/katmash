package io.jfo.katmash

import io.jfo.katmash.data.entity.Kat
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
@Testcontainers
class KatmashVoteIntegrationTest(@LocalServerPort private val localServerPort: Int,
                                 @Autowired private val databaseClient: DatabaseClient): DatabaseTestConfiguration() {
    private lateinit var webClient: WebClient

    @BeforeEach
    fun beforeEach() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:$localServerPort")
                .build()
    }

    @Test
    fun `a vote for a kat should increase his score by 1`() {
        // Given a kat that has the id 10
        val kat = databaseClient.select()
                .from(Kat::class.java)
                .matching(Criteria.where("id").`is`(10))
                .`as`(Kat::class.java)
                .first()
                .block()

        // When I vote for the kat that has the id 10
        val response = webClient.post().uri("/kat/10/vote").exchange().block()

        // Then the status response is 204 (no content)
        assertThat(response!!.statusCode()).isEqualTo(HttpStatus.NO_CONTENT)

        // And the kat as been updated and is score was increased by 1
        val updatedKat = databaseClient.select()
                .from(Kat::class.java)
                .matching(Criteria.where("id").`is`(10))
                .`as`(Kat::class.java)
                .first()
                .block()
        assertThat(kat).isNotNull
        assertThat(updatedKat).isNotNull
        assertThat(kat!!.id).isEqualTo(updatedKat!!.id)
        assertThat(updatedKat.score).isEqualTo(kat.score + 1)
    }

    @Test
    fun `a vote for a kat that does not exists should return a not found response`() {
        // When I vote for a kat tha does not exists
        val response = webClient.post().uri("/kat/1000/vote").exchange().block()

        // Then the response's status should be NOT FOUND
        assertThat(response!!.statusCode()).isEqualTo(HttpStatus.NOT_FOUND)

        // And the body should contain the error message
        assertThat(response.bodyToMono(String::class.java).block()).isEqualTo("The kat with the id 1000 was not found!!!")
    }
}