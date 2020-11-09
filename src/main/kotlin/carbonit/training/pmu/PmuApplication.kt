package carbonit.training.pmu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PmuApplication

fun main(args: Array<String>) {
	runApplication<PmuApplication>(*args)
}
