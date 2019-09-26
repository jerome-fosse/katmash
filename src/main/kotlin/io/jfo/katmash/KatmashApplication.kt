package io.jfo.katmash

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@EnableTransactionManagement
class KatmashApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<KatmashApplication>(*args)
        }
    }
}