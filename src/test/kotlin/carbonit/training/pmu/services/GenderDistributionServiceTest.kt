package carbonit.training.pmu.services

import carbonit.training.pmu.resources.Participant
import carbonit.training.pmu.resources.Participants
import carbonit.training.pmu.resources.Participation
import carbonit.training.pmu.services.impl.GenderDistributionServiceImpl
import carbonit.training.pmu.services.impl.PMUApiImpl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
@WebMvcTest(GenderDistributionServiceImpl::class, PMUApiImpl::class, RestTemplate::class)
internal class GenderDistributionServiceTest {
    @MockBean
    lateinit var pmuApiImpl: PMUApiImpl

    @Autowired
    lateinit var genderDistributionService: GenderDistributionServiceImpl

    @Test
    fun `test gender distribution service - 1 running participant by sex`() {
        whenever(pmuApiImpl.fetchParticipants(any(), any(), any())).thenReturn(Participants(listOf(
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.PARTANT, "MALES"),
                Participant(Participation.PARTANT, "FEMELLES")
        )))
        val runners = genderDistributionService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(runners.entries.size, 3)
        assertNotNull(runners["HONGRES"])
        assertNotNull(runners["MALES"])
        assertNotNull(runners["FEMELLES"])
        assertEquals(1, runners["HONGRES"])
        assertEquals(1, runners["MALES"])
        assertEquals(1, runners["FEMELLES"])
    }

    @Test
    fun `test gender distribution service - multiple running participant by sex`() {
        whenever(pmuApiImpl.fetchParticipants(any(), any(), any())).thenReturn(Participants(listOf(
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.PARTANT, "MALES"),
                Participant(Participation.PARTANT, "MALES"),
                Participant(Participation.PARTANT, "FEMELLES"),
                Participant(Participation.PARTANT, "FEMELLES"),
                Participant(Participation.PARTANT, "FEMELLES"),
                Participant(Participation.PARTANT, "FEMELLES")
        )))
        val runners = genderDistributionService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(3, runners.entries.size)
        assertNotNull(runners["HONGRES"])
        assertNotNull(runners["MALES"])
        assertNotNull(runners["FEMELLES"])
        assertEquals(3, runners["HONGRES"])
        assertEquals(2, runners["MALES"])
        assertEquals(4, runners["FEMELLES"])
    }

    @Test
    fun `test gender distribution service - 1 running participants among non running participants`() {
        whenever(pmuApiImpl.fetchParticipants(any(), any(), any())).thenReturn(Participants(listOf(
                Participant(Participation.PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "MALES"),
                Participant(Participation.NON_PARTANT, "MALES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES")
        )))
        val runners = genderDistributionService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(1, runners.entries.size)
        assertNotNull(runners["HONGRES"])
        assertEquals(1, runners["HONGRES"])
    }


    @Test
    fun `test gender distribution service - no running participants`() {
        whenever(pmuApiImpl.fetchParticipants(any(), any(), any())).thenReturn(Participants(listOf(
                Participant(Participation.NON_PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "HONGRES"),
                Participant(Participation.NON_PARTANT, "MALES"),
                Participant(Participation.NON_PARTANT, "MALES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES"),
                Participant(Participation.NON_PARTANT, "FEMELLES")
        )))
        val runners = genderDistributionService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(0, runners.entries.size)
    }

    @Test
    fun `test gender distribution service - no participants`() {
        whenever(pmuApiImpl.fetchParticipants(any(), any(), any())).thenReturn(Participants(emptyList()))
        val runners = genderDistributionService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(0, runners.entries.size)
    }
}