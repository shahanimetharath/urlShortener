package org.codefactory.urlshortener.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.InvalidUrlException

class UrlNotFoundException(message: String) : RuntimeException(message)

class InvalidUrlException(message: String) : RuntimeException(message)

data class ErrorResponse(
    val status: Int,
    val message: String,
    val details: String?
)

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFound(ex: UrlNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.warn("URL not found: ${ex.message}")
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = "URL not found",
            details = ex.message
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidUrlException::class)
    fun handleInvalidUrl(ex: InvalidUrlException, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.warn("Invalid URL: ${ex.message}")
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Invalid URL",
            details = ex.message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralError(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error", ex)
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Internal Server Error",
            details = ex.localizedMessage
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
