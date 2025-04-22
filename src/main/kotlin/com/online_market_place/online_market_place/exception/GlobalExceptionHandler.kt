package com.online_market_place.online_market_place.exception

import com.online_market_place.online_market_place.common.ErrorResponse
import com.online_market_place.online_market_place.common.FieldValidationError
import com.online_market_place.online_market_place.common.ValidationErrorResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message,
            errorCode = ex.errorCode,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false)
        )

        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ValidationErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.map { fieldError ->
            createFieldValidationError(fieldError)
        }

        val errorResponse = ValidationErrorResponse(
            errorResponse = ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Validation failed",
                errorCode = "VALIDATION_ERROR",
                timestamp = LocalDateTime.now(),
                path = request.getDescription(false)
            ),
            fieldErrors = fieldErrors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message ?: "Unexpected error occurred",
            errorCode = "INTERNAL_ERROR",
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false)
        )

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = ex.message ?: "You do not have permission to access this resource",
            errorCode = "FORBIDDEN",
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }



    @ExceptionHandler(ConstraintViolationException::class)  // FIXED: Changed to correct exception type
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<ValidationErrorResponse> {
        val fieldErrors = ex.constraintViolations.map { violation ->
            val propertyPath = violation.propertyPath.toString()
            val fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1)

            FieldValidationError(
                field = fieldName,
                message = violation.message,
                rejectedValue = violation.invalidValue
            )
        }

        val errorResponse = ValidationErrorResponse(
            errorResponse = ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Validation failed",
                errorCode = "VALIDATION_ERROR",
                timestamp = LocalDateTime.now(),
                path = request.getDescription(false)
            ),
            fieldErrors = fieldErrors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InsufficientInventoryException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInsufficientInventoryException(
        ex: InsufficientInventoryException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = ex.message,
            errorCode = ex.errorCode,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false)
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }



    @ExceptionHandler(EmailConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleEmailConflictException(
        ex: EmailConflictException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            message = ex.message,
            errorCode = ex.errorCode,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InvalidVerificationTokenException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidVerificationTokenException(
        ex: InvalidVerificationTokenException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = ex.message,
            errorCode = ex.errorCode,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }


    private fun createFieldValidationError(fieldError: FieldError): FieldValidationError {
        val localizedMessage = messageSource.getMessage(fieldError.defaultMessage ?: "Invalid value", null, LocaleContextHolder.getLocale())

        return FieldValidationError(
            field = fieldError.field,
            message = localizedMessage,
            rejectedValue = fieldError.rejectedValue
        )
    }
}