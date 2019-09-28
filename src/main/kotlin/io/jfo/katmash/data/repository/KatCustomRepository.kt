package io.jfo.katmash.data.repository

import io.jfo.katmash.data.entity.Kat
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
}

@Repository
class KatCustomRepositoryImpl(private val databaseClient: DatabaseClient): KatCustomRepository {

    override fun findTwoRandomKats(): Flux<Kat> {
        return databaseClient
                .execute()
                .sql("SELECT id, url, score FROM kats ORDER BY random() LIMIT 2")
                .`as`(Kat::class.java)
                .fetch()
                .all()
    }

    override fun voteForKat(kat: Kat): Mono<Int> {
        return databaseClient.update()
                .table(Kat::class.java)
                .using(kat)
                .fetch()
                .rowsUpdated()
    }

}