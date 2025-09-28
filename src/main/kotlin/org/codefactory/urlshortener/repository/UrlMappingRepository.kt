package org.codefactory.urlshortener.repository

import org.codefactory.urlshortener.entity.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository


interface UrlMappingRepository : JpaRepository<UrlMapping, Long> {
    fun findByShortUrl(shortUrl: String): UrlMapping?
    fun findByLongUrl(longUrl: String): UrlMapping?
   }
