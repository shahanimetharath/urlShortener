package org.codefactory.urlshortener.controller

import org.codefactory.urlshortener.exception.InvalidUrlException
import org.codefactory.urlshortener.exception.UrlNotFoundException
import org.codefactory.urlshortener.service.UrlShortenerService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UrlShortenerController (private val service: UrlShortenerService){

    private val logger = LoggerFactory.getLogger(UrlShortenerController::class.java)

    data class UrlRequest(val longUrl: String)
    data class UrlResponse(val shortUrl: String)

    @PostMapping("/shorten")
    fun shorten(@RequestBody request: UrlRequest): UrlResponse {
        val longUrl = request.longUrl
        logger.info("Received request to shorten URL: $longUrl")
        if (!longUrl.startsWith("http:") && !longUrl.startsWith("https:") && !longUrl.startsWith("www:") ) {
            throw InvalidUrlException("Provided URL is not valid: $longUrl")
        }
        val shortened = service.shortenUrl(longUrl)
        logger.info("Generated short URL: $shortened for original: $longUrl")

        val shortUrl = "http://localhost:8080/$shortened"

        return UrlResponse(shortUrl)
    }
    @GetMapping("/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String): ResponseEntity<Any> {
            logger.info("Looking up original URL for short code: $shortUrl")
            //println("Enter into redirect")
        val longUrl = service.getLongUrl(shortUrl)?: throw UrlNotFoundException("Short code not found: $shortUrl")
            return if (longUrl != null) {
            ResponseEntity.status(302)
                .header("Location", longUrl)
                .build()
        } else {
                ResponseEntity.notFound().build()
            }
    }
}