package org.codefactory.urlshortener.service

import org.codefactory.urlshortener.entity.UrlMapping
import org.codefactory.urlshortener.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UrlShortenerService (private val repository: UrlMappingRepository){

    private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun shortenUrl(longUrl: String): String? {
        println("Enter inside shortenUrl function")
       val existingShorUrl : String
        val existing = repository.findByLongUrl(longUrl)
        if (existing != null) {
            println("Found existing long url: $longUrl")
            existingShorUrl = repository.findByLongUrl(longUrl)?.shortUrl.toString()
            println("Existing short url: $existingShorUrl")
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
        println("Enter inside shortenUrl function")
        return shortUrl
    }
    fun getLongUrl(shortUrl: String): String? {
        //return repository.findByShortUrl(shortUrl)?.longUrl
        try{
        val mapping = repository.findByShortUrl(shortUrl)?.longUrl
            return mapping
        } catch (ex: Exception) {
            ex.printStackTrace()
           return null
        }
    }
}