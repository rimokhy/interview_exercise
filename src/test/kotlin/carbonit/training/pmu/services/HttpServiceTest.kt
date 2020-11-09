package carbonit.training.pmu.services

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class HttpServiceTest {
    lateinit var httpService: HttpService

    @BeforeEach
    fun setUp() {
        httpService = HttpService()
    }

    @Test
    fun `test simple url no parameters`() {
        assertEquals("https://something.com", httpService.makeUrl("https://something.com"))
    }

    @Test
    fun `test simple url with parameters`() {
        assertEquals("https://something.com?name=Paul&age=32", httpService.makeUrl("https://something.com", "name" to "Paul", "age" to 32))
    }
}