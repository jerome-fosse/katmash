package io.jfo.katmash

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KatmashApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<KatmashApplication>(*args)
        }
    }
}