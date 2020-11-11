package carbonit.training.pmu.exceptions

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class HttpException(open val msg: String, val status: HttpStatus) : RuntimeException("$status: $msg")

class InternalServerError(override val msg: String = "Internal server error") : HttpException(msg, HttpStatus.INTERNAL_SERVER_ERROR)