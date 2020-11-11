package carbonit.training.pmu.responses

import org.springframework.http.HttpStatus

data class ErrorResponse(val msg: String, val status: HttpStatus, val statusCode: Int = status.value())