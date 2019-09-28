package io.jfo.katmash.api.handler

import io.jfo.katmash.service.KatService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class KatPairHandler(val katService: KatService): RequestHandler {

    companion object {
        val logger = LoggerFactory.getLogger(KatPairHandler::class.java)
    }

    override fun handleRequest(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Requesting a pair of kats...")

        return katService.getTwoRandomKats()
                .collectList()
                .flatMap {
                    logger.debug("Pair of kats returned : $it")
                    ServerResponse.ok().body(BodyInserters.fromObject(it))
                }
                .doOnError {logger.error(it.message, it)}
                .onErrorResume { ServerResponse.badRequest().body(BodyInserters.fromObject("Something went wrong !!!")) }
    }
}