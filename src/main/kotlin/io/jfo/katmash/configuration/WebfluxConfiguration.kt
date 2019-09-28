package io.jfo.katmash.configuration

import io.jfo.katmash.api.handler.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.*
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

    @Bean
    fun createDefaultPageRouter() = router {
        ("/").nest {
            GET("") { ok().contentType(MediaType.TEXT_HTML).body(fromResource(ClassPathResource("/static/index.html"))) }
        }
    }

    @Bean
    fun createStaticResourcesRouter() = router {
        resources("/**", ClassPathResource("/static/"))
    }
}