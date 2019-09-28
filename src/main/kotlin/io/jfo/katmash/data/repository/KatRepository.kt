package io.jfo.katmash.data.repository

import io.jfo.katmash.data.entity.Kat
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface KatRepository: ReactiveCrudRepository<Kat, Int>, KatCustomRepository