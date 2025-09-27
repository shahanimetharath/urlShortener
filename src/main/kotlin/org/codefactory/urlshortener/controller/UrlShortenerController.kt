package org.codefactory.urlshortener.controller

import org.codefactory.urlshortener.service.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/url")
class UrlShortenerController (private val service: UrlShortenerService){

    data class UrlRequest(val longUrl: String)
    data class UrlResponse(val shortUrl: String)

    @PostMapping("/shorten")
    fun shorten(@RequestBody request: UrlRequest): UrlResponse {
        println("Enter shorten URL api request")
        val shortUrl = service.shortenUrl(request.longUrl)
        println("Exit shorten URL api request wih")
        return UrlResponse("http://localhost:8080/$shortUrl")
    }
    @GetMapping("/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String): ResponseEntity<Any> {
        println("Enter into redirect")
        val longUrl = service.getLongUrl(shortUrl)
        return if (longUrl != null) {
            ResponseEntity.status(302)
                .header("Location", longUrl)
                .build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}