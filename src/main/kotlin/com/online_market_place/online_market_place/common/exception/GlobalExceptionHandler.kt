package com.online_market_place.online_market_place.common.exception

import com.online_market_place.online_market_place.auth.exception.AccessDeniedException
import com.online_market_place.online_market_place.auth.exception.EmailConflictException
import com.online_market_place.online_market_place.auth.exception.InvalidVerificationTokenException
import com.online_market_place.online_market_place.common.exception.model.ErrorResponse
import com.online_market_place.online_market_place.common.exception.model.FieldValidationError
import com.online_market_place.online_market_place.common.exception.model.ValidationErrorResponse
import com.online_market_place.online_market_place.notification.exception.EmailNotSentException
import com.online_market_place.online_market_place.order.exception.InsufficientInventoryException
import jakarta.servlet.http.HttpServletRequest
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
@Suppress("unused")
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
            message = ex.message,
            errorCode = "FORBIDDEN",
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(EmailNotSentException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleEmailNotSentException(
        ex: EmailNotSentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.message,
            errorCode = ex.errorCode,
            timestamp = LocalDateTime.now(),
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ConstraintViolationException::class)
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

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(
        ex: org.springframework.security.access.AccessDeniedException,
        request: HttpServletRequest
    ): Map<String, Any> {
        return mapOf(
            "status" to HttpStatus.FORBIDDEN.value(),
            "message" to "Access Denied: You don't have permission to access this resource.",
            "errorCode" to "FORBIDDEN",
            "timestamp" to LocalDateTime.now().toString(),
            "path" to request.requestURI
        )
    }



    private fun createFieldValidationError(fieldError: FieldError): FieldValidationError {
        val localizedMessage = messageSource.getMessage(
            fieldError.defaultMessage ?: "Invalid value",
            null,
            LocaleContextHolder.getLocale()
        )

        return FieldValidationError(
            field = fieldError.field,
            message = localizedMessage,
            rejectedValue = fieldError.rejectedValue
        )
    }
}