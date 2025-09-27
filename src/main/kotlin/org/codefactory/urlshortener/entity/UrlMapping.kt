package org.codefactory.urlshortener.entity


import jakarta.persistence.*

@Entity
data class UrlMapping(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val shortUrl: String = "",

    @Column(nullable = false)
    val longUrl: String = ""
)