package io.jfo.katmash.service

import io.jfo.katmash.api.model.KatResponse
import io.jfo.katmash.data.repository.KatRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface KatService {
    fun getTwoRandomKats(): Flux<KatResponse>
}

@Component
class KatServiceImpl(private val katRepository: KatRepository): KatService {

    companion object {
        private val logger = LoggerFactory.getLogger(KatServiceImpl::class.java)
    }

    override fun getTwoRandomKats(): Flux<KatResponse> {
        logger.debug("getting two random kats...")

        return katRepository
                .findTwoRandomKats()
                .map { KatResponse(id = it.id, url = it.url, score = it.score) }
    }

}