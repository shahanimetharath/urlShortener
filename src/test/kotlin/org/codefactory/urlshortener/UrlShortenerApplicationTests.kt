package org.codefactory.urlshortener

import com.fasterxml.jackson.databind.ObjectMapper
import org.codefactory.urlshortener.exception.InvalidUrlException
import org.codefactory.urlshortener.exception.UrlNotFoundException
import org.codefactory.urlshortener.service.UrlShortenerService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerApplicationTests(
    @Autowired val service: UrlShortenerService,
    @Autowired val mockMvc: MockMvc,
    @Autowired private val jacksonObjectMapper: ObjectMapper
) {

    @Test
    fun `shorten URL should return a short code`() {
        val originalUrl = "https://www.google.com/"
        val shortCode = service.shortenUrl(originalUrl)

        assertNotNull(shortCode)
        if (shortCode != null) {
            assertTrue(shortCode.length in 4..10)
        }
    }
    @Test
    fun `same long URL should return same short code`() {
        val url = "https://www.youtube.com/"
        val code1 = service.shortenUrl(url)
        val code2 = service.shortenUrl(url)

        assertEquals(code1, code2)
    }



}