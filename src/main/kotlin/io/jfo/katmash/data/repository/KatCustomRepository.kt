package io.jfo.katmash.data.repository

import io.jfo.katmash.data.entity.Kat
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

interface KatCustomRepository {

    fun findTwoRandomKats(): Flux<Kat>
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

}