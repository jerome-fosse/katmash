package io.jfo.katmash.data.repository

import io.jfo.katmash.data.entity.Kat
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface KatCustomRepository {

    /**
     * Get a pair of kats
     *
     * @return a pair of kats
     */
    fun findTwoRandomKats(): Flux<Kat>

    /**
     * Vote for a kat
     *
     * @param kat the Kat to vote for
     * @return the number of rows updated
     */
    fun voteForKat(kat: Kat): Mono<Int>

    /**
     * Return the n first kats ordered by score
     *
     * @param limit the number of kats to return
     * @return a Flux of kats
     */
    fun findKatOrderByScoreDescLimitBy(limit: Long): Flux<Kat>
}

@Repository
class KatCustomRepositoryImpl(private val databaseClient: DatabaseClient): KatCustomRepository {

    override fun findTwoRandomKats(): Flux<Kat> {
        return databaseClient.select()
                .from(Kat::class.java)
                .orderBy(Sort.by("random()").descending())
                .`as`(Kat::class.java)
                .all()
                .limitRequest(2)
    }

    override fun voteForKat(kat: Kat): Mono<Int> {
        return databaseClient.update()
                .table(Kat::class.java)
                .using(kat)
                .fetch()
                .rowsUpdated()
    }

    override fun findKatOrderByScoreDescLimitBy(limit: Long): Flux<Kat> {
        return databaseClient.select()
                .from(Kat::class.java)
                .orderBy(Sort.by("score").descending())
                .`as`(Kat::class.java)
                .all()
                .limitRequest(limit)
    }

}