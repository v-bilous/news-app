package pro.bilous.newsapp.rest.error

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleEntityNotFound(exception: EntityNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            exception.message
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleIllegalArgument(exception: IllegalArgumentException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            exception.message
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}