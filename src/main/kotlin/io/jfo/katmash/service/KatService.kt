package io.jfo.katmash.service

import io.jfo.katmash.api.model.KatResponse
import io.jfo.katmash.data.repository.KatRepository
import io.jfo.katmash.exception.KatNotFoundException
import io.jfo.katmash.exception.VoteException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface KatService {
    /**
     * Get a pair of kats
     *
     * @return a pait of kats
     */
    fun getTwoRandomKats(): Flux<KatResponse>

    /**
     * Vote for a kat
     *
     * @param id the id of the kat to vote for
     * @return true if the vote was saved
     * @throws KatNotFoundException if no kat with the given if exist
     * @throws VoteException if an error occurred during the vote
     */
    fun voteForKatWithId(id: Int): Mono<Boolean>

    fun getBoardOftheTenFirstKats(): Flux<KatResponse>
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

    @Transactional
    override fun voteForKatWithId(id: Int): Mono<Boolean> {
        logger.debug("voting for kat with id $id")

        return katRepository.findById(id)
                .switchIfEmpty(Mono.error(KatNotFoundException(id)))
                .map { it.copy(score = it.score + 1) }
                .flatMap {
                    kat -> katRepository.voteForKat(kat).flatMap {
                        updatesRows -> when (updatesRows) {
                            0 -> Mono.error(VoteException("Unexpected error. The vote was not taken into an account."))
                            else -> Mono.just(true)
                        }
                    }
                }
    }

    override fun getBoardOftheTenFirstKats(): Flux<KatResponse> {
        logger.debug("getting the board of the 10 firts kats...")

        return katRepository.findKatOrderByScoreDescLimitBy(10)
                .map { KatResponse(id = it.id, url = it.url, score = it.score) }
    }

}