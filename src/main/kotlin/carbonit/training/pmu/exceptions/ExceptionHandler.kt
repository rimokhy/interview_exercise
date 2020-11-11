package carbonit.training.pmu.exceptions

import carbonit.training.pmu.responses.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(HttpException::class)
    @ResponseBody
    fun httpException(exception: HttpException) = ResponseEntity(
            ErrorResponse(exception.msg, exception.status),
            exception.status
    )

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun serverError(exception: Throwable) = ErrorResponse(
            exception.message ?: "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR
    )
}