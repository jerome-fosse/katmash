package io.jfo.katmash.api.handler

import io.jfo.katmash.service.KatService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class BoardHandler(val katService: KatService): RequestHandler {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BoardHandler::class.java)
    }

    override fun handleRequest(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Requesting the board...")

        return katService.getBoardOftheTenFirstKats()
                .collectList()
                .flatMap {
                    logger.debug("The board kats are : $it")
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(it))
                }
                .doOnError { KatPairHandler.logger.error(it.message, it)}
                .onErrorResume { ServerResponse.badRequest().body(BodyInserters.fromObject("Something went wrong !!!")) }
    }
}