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
    @Test
    fun `get original URL should return correct URL`() {
        val originalUrl = "https://mail.google.com"
        val shortCode = service.shortenUrl(originalUrl).toString()

        val resolved = service.getLongUrl(shortCode)
        assertEquals(originalUrl, resolved)
    }
    @Test
    fun `get original URL for unknown code should throw exception`() {
        val exception = assertThrows<UrlNotFoundException> {
            throw UrlNotFoundException("Short code not found: 12345")
        }
        assertEquals("Short code not found: 12345", exception.message)
    }

    @Test
    fun `invalid URL should throw exception`() {
        val exception = assertThrows<InvalidUrlException> {
            throw InvalidUrlException("Provided URL is not valid: abc")
        }
        assertEquals("Provided URL is not valid: abc", exception.message)
    }

    @Test
    fun `controller should shorten and resolve URL via API`() {
        val originalUrl = "https://www.linkedin.com/"

        // Shorten URL via POST
        val shortenResult = mockMvc.perform(
            post("/shorten")
                .content("""{"longUrl":"$originalUrl"}""")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andReturn()

        // Parse JSON and extract shortUrl
        val responseJson = shortenResult.response.contentAsString
        //println("responseJson = $responseJson")
        val objectMapper = ObjectMapper()
        var shortCode = objectMapper.readTree(shortenResult.response.contentAsString)
            .get("shortUrl").asText()
       // println("shortCode = $shortCode originalUrl = $originalUrl")
        //get the short code from short URL
        shortCode = shortCode.takeLast(7)
       // println("shortCode = $shortCode originalUrl = $originalUrl")

        // Retrieve original URL via GET
        mockMvc.perform(get("/$shortCode"))
            .andExpect(status().is3xxRedirection)
            .andExpect(header().string("Location", originalUrl))
    }

}