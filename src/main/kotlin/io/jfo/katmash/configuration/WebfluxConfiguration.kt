package io.jfo.katmash.configuration

import io.jfo.katmash.api.handler.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.router

@Configuration
@EnableWebFlux
class WebfluxConfiguration {

    @Bean
    fun createKatsRouter(@Qualifier("katPairHandler") pairHandler: RequestHandler,
                         @Qualifier("boardHandler") boardHandler: RequestHandler) = router {
        ("/kats" and accept(MediaType.APPLICATION_JSON)).nest {
            GET("/pair", pairHandler::handleRequest)
            GET("/board", boardHandler::handleRequest)
        }
    }

    @Bean
    fun createVoteRouter(@Qualifier("voteHandler")handler: RequestHandler) = router {
        ("/kat/{id}/vote").nest {
            POST("", handler::handleRequest)
        }
    }
}