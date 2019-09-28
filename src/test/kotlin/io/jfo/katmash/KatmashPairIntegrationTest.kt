package io.jfo.katmash

import io.jfo.katmash.api.model.KatResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
@Testcontainers
class KatmashPairIntegrationTest(@LocalServerPort private val localServerPort: Int): DatabaseTestConfiguration() {

    private lateinit var webClient: WebClient

    @BeforeEach
    fun beforeEach() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:$localServerPort")
                .build()
    }

    @Test
    fun `should return a pair of two different Kats`() {
        // When I request a pair of Kats
        val response = webClient.get().uri("/kats/pair").exchange().block()

        // Then the response is OK
        assertThat(response?.statusCode()).isEqualTo(HttpStatus.OK)

        // And there is a pair of non null kats
        val kats = response!!.bodyToFlux(KatResponse::class.java).collectList().block()
        assertThat(kats!!.size).isEqualTo(2)
        assertThat(kats[0]).isNotNull
        assertThat(kats[1]).isNotNull

        // And the two cats are different
        assertThat(kats[0]).isNotEqualTo(kats[1])
    }

    @Test
    fun `When called twice the endpoint KatsPair should return two different pairs of Kats`() {
        // When I request two pairs of Kats
        val firstResponse = webClient.get().uri("/kats/pair").exchange().block()
        val secondResponse = webClient.get().uri("/kats/pair").exchange().block()

        // Then the responses are OK
        assertThat(firstResponse?.statusCode()).isEqualTo(HttpStatus.OK)
        assertThat(secondResponse?.statusCode()).isEqualTo(HttpStatus.OK)

        // And the two pairs are different
        val firstPair = firstResponse!!.bodyToFlux(KatResponse::class.java).collectList().block()
        val secondPair = secondResponse!!.bodyToFlux(KatResponse::class.java).collectList().block()
        assertThat(firstPair!!.size).isEqualTo(secondPair!!.size)
        assertThat(firstPair.containsAll(secondPair)).isFalse()
    }
}