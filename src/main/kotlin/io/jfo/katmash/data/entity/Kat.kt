package io.jfo.katmash.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("kats")
data class Kat(
        @Id
        val id: Int,
        val url: String,
        val score: Int
)