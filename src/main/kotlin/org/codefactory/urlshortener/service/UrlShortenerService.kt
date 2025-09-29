package org.codefactory.urlshortener.service

import org.codefactory.urlshortener.entity.UrlMapping
import org.codefactory.urlshortener.repository.UrlMappingRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UrlShortenerService (private val repository: UrlMappingRepository){

    private val logger = LoggerFactory.getLogger(UrlShortenerService::class.java)

    private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun shortenUrl(longUrl: String): String? {
        logger.debug("Checking if long URL already exists: $longUrl")
       val existingShorUrl : String
        val existing = repository.findByLongUrl(longUrl)
        if (existing != null) {
            logger.info("Found existing long url: $longUrl")
            existingShorUrl = repository.findByLongUrl(longUrl)?.shortUrl.toString()
            logger.info("Found existing shortened URL for: $longUrl is $existingShorUrl")
            return existingShorUrl
        }
        // Generate random 7-character string until unique
        var shortUrl: String
        do {
            shortUrl = (1..7)
                .map { chars[Random.nextInt(chars.length)] }
                .joinToString("")
        } while (repository.findByShortUrl(shortUrl) != null)

        val mapping = UrlMapping(shortUrl = shortUrl, longUrl = longUrl)
        repository.save(mapping)
        logger.info("Created new short code mapping for $longUrl is $shortUrl")
       // println("Enter inside shortenUrl function")
        return shortUrl
    }
    fun getLongUrl(shortUrl: String): String? {

        val mapping = repository.findByShortUrl(shortUrl)?.longUrl
        logger.debug("Generated short code: $shortUrl for URL: $mapping")
            return mapping

    }
}