package io.jfo.katmash

import io.jfo.katmash.api.model.KatResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.data.r2dbc.query.Update
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
@Testcontainers
class KatmashBoardIntegrationTest(@LocalServerPort private val localServerPort: Int,
                                  @Autowired private val databaseClient: DatabaseClient): DatabaseTestConfiguration() {

    private lateinit var webClient: WebClient

    @BeforeEach
    fun beforeEach() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:$localServerPort")
                .build()
    }

    @Test
    fun `the board should contains 10 kats`() {
        // When the board is requested
        val response = webClient.get().uri("/kats/board").exchange().block()

        // Then the response's status is OK
        assertThat(response!!.statusCode()).isEqualTo(HttpStatus.OK)

        // And contains 10 kats
        val kats = response.bodyToFlux(KatResponse::class.java).collectList().block()
        assertThat(kats!!.size).isEqualTo(10)
    }

    @Test
    fun `the board should contains the cats with the highest score ordered in descending order`() {
        // Given a number of kats with a score
        databaseClient.update().table("kats").using(Update.update("score", 102)).matching(Criteria.where("id").`is`(10)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 84)).matching(Criteria.where("id").`is`(45)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 75)).matching(Criteria.where("id").`is`(90)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 72)).matching(Criteria.where("id").`is`(1)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 51)).matching(Criteria.where("id").`is`(48)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 40)).matching(Criteria.where("id").`is`(3)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 34)).matching(Criteria.where("id").`is`(51)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 33)).matching(Criteria.where("id").`is`(15)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 27)).matching(Criteria.where("id").`is`(60)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 9)).matching(Criteria.where("id").`is`(41)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 7)).matching(Criteria.where("id").`is`(66)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 5)).matching(Criteria.where("id").`is`(47)).then().block()
        databaseClient.update().table("kats").using(Update.update("score", 4)).matching(Criteria.where("id").`is`(80)).then().block()

        // When the board is requested
        val response = webClient.get().uri("/kats/board").exchange().block()

        // Then the 10 first kats are ordered by their score
        val kats = response!!.bodyToFlux(KatResponse::class.java).collectList().block()
        assertThat(kats!![0]).isEqualTo(KatResponse(id = 10, score = 102, url = kats[0].url))
        assertThat(kats[1]).isEqualTo(KatResponse(id = 45, score = 84, url = kats[1].url))
        assertThat(kats[2]).isEqualTo(KatResponse(id = 90, score = 75, url = kats[2].url))
        assertThat(kats[3]).isEqualTo(KatResponse(id = 1, score = 72, url = kats[3].url))
        assertThat(kats[4]).isEqualTo(KatResponse(id = 48, score = 51, url = kats[4].url))
        assertThat(kats[5]).isEqualTo(KatResponse(id = 3, score = 40, url = kats[5].url))
        assertThat(kats[6]).isEqualTo(KatResponse(id = 51, score = 34, url = kats[6].url))
        assertThat(kats[7]).isEqualTo(KatResponse(id = 15, score = 33, url = kats[7].url))
        assertThat(kats[8]).isEqualTo(KatResponse(id = 60, score = 27, url = kats[8].url))
        assertThat(kats[9]).isEqualTo(KatResponse(id = 41, score = 9, url = kats[9].url))
    }
}