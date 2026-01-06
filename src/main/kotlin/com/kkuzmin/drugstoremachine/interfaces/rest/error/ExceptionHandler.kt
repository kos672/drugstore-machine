package com.kkuzmin.drugstoremachine.interfaces.rest.error

import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(ex: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()))
            .body<ErrorResponse>(ErrorResponse(HttpStatus.CONFLICT.value(), ex.message ?: "no error details"))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()))
            .body<ErrorResponse>(ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message ?: "no error details"))
    }
}