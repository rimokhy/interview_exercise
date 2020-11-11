package carbonit.training.pmu.services

import carbonit.training.pmu.exceptions.InternalServerError
import carbonit.training.pmu.resources.Participant
import carbonit.training.pmu.resources.Participants
import carbonit.training.pmu.resources.Participation
import carbonit.training.pmu.services.impl.PMUApiImpl
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class PMUApiImplTest {
    @Mock
    lateinit var restTemplate: RestTemplate

    @InjectMocks
    lateinit var pmuApi: PMUApiImpl

    @Test
    fun `ensure date formatter usage`() {
        assertEquals("18092020", LocalDate.of(2020, 9, 18).format(pmuApi.dateFormatter))
    }

    @Test
    fun `test fetch participants - success`() {
        whenever(restTemplate.getForEntity(anyString(), Mockito.any(Class::class.java))).thenReturn(ResponseEntity(Participants(listOf(
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "HONGRES"),
                Participant(Participation.PARTANT, "MALES"),
                Participant(Participation.PARTANT, "FEMELLES")
        )), HttpStatus.OK))
        val result = pmuApi.fetchParticipants(LocalDate.of(2020, 9, 18), 5, 6)

        assertNotNull(result.participants)
        assertEquals(result.participants.size, 4)
        assertEquals(result.participants[0].sex, "HONGRES")
        assertEquals(result.participants[1].sex, "HONGRES")
        assertEquals(result.participants[2].sex, "MALES")
        assertEquals(result.participants[3].sex, "FEMELLES")
    }

    @Test
    fun `test fetch participants - bad status code response`() {
        whenever(restTemplate.getForEntity(anyString(), Mockito.any(Class::class.java))).thenReturn(ResponseEntity.badRequest().build())
        assertThrows<InternalServerError> {
            pmuApi.fetchParticipants(LocalDate.of(2020, 9, 18), 5, 6)
        }
    }

    @Test
    fun `test fetch participants - ill formatted body`() {
        whenever(restTemplate.getForEntity(anyString(), Mockito.any(Class::class.java))).thenReturn(ResponseEntity<Any>(HttpStatus.OK))
        assertThrows<InternalServerError> {
            pmuApi.fetchParticipants(LocalDate.of(2020, 9, 18), 5, 6)
        }
    }
}