package io.jfo.katmash.service

import io.jfo.katmash.data.entity.Kat
import io.jfo.katmash.data.repository.KatRepository
import io.jfo.katmash.exception.VoteException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class KatServiceTest {

    @Test
    fun `when voting for a kat update 0 row a VoteException should be thrown`() {
        // When voting for a kat update 0 rows
        val service = KatServiceImpl(KatRepositoryMock())
        val thrown = catchThrowable { service.voteForKatWithId(1).block() }

        // Then a Vote Exception is thrown
        assertThat(thrown).isInstanceOf(VoteException::class.java)
    }

    private class KatRepositoryMock(): KatRepository {
        override fun findById(id: Int): Mono<Kat> {
            return Mono.just(Kat(id = 1, url = "http://kats.org/1.jpg", score = 0))
        }

        override fun voteForKat(kat: Kat): Mono<Int> {
            return Mono.just(0)
        }

        override fun <S : Kat?> save(entity: S): Mono<S> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun findAll(): Flux<Kat> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun deleteById(id: Int): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun deleteById(id: Publisher<Int>): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun deleteAll(entities: MutableIterable<Kat>): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun deleteAll(entityStream: Publisher<out Kat>): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun deleteAll(): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun <S : Kat?> saveAll(entities: MutableIterable<S>): Flux<S> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun <S : Kat?> saveAll(entityStream: Publisher<S>): Flux<S> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun count(): Mono<Long> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun findAllById(ids: MutableIterable<Int>): Flux<Kat> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun findAllById(idStream: Publisher<Int>): Flux<Kat> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun existsById(id: Int): Mono<Boolean> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun existsById(id: Publisher<Int>): Mono<Boolean> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun findById(id: Publisher<Int>): Mono<Kat> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(entity: Kat): Mono<Void> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun findTwoRandomKats(): Flux<Kat> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}