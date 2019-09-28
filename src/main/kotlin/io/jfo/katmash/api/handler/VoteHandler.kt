package io.jfo.katmash.api.handler

import io.jfo.katmash.exception.KatNotFoundException
import io.jfo.katmash.exception.VoteException
import io.jfo.katmash.service.KatService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono

@Component
class VoteHandler(val katService: KatService): RequestHandler {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(VoteHandler::class.java)
    }

    override fun handleRequest(request: ServerRequest): Mono<ServerResponse> {
        val katId = request.pathVariable("id").toInt()
        logger.info("New vote for Kat $katId received !!!")

        return katService.voteForKatWithId(katId)
                .then(noContent().build())
                .doOnError { KatPairHandler.logger.error(it.message, it) }
                .onErrorResume {
                    when (it) {
                        is KatNotFoundException -> status(HttpStatus.NOT_FOUND).body(BodyInserters.fromObject(it.message ?: "Kat not found"))
                        is VoteException -> badRequest().body(BodyInserters.fromObject(it.message))
                        else -> status(HttpStatus.INTERNAL_SERVER_ERROR).body(BodyInserters.fromObject(it.message ?: "Unexpected Error."))
                    }
                }
    }
}