package carbonit.training.pmu.controllers

import carbonit.training.pmu.resources.Participant
import carbonit.training.pmu.resources.Participation
import carbonit.training.pmu.services.RaceParticipantService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup


internal class RaceControllerTest {
    lateinit var mockMvc: MockMvc
    var mockRaceParticipantService: RaceParticipantService = Mockito.mock(RaceParticipantService::class.java)

    @BeforeEach
    fun setup() {
        Mockito.reset(mockRaceParticipantService)
        mockMvc = standaloneSetup(RaceController(mockRaceParticipantService)).build()
    }

    @Test
    fun participantsGroupedBySex() {
        whenever(mockRaceParticipantService.getAllParticipants(any(), any(), any())).thenReturn(listOf(
                Participant(1, Participation.PARTANT, "Christopher", "HONGRES"),
                Participant(1, Participation.NON_PARTANT, "Miguel", "HONGRES"),
                Participant(2, Participation.PARTANT, "Esteban", "MALES"),
                Participant(3, Participation.PARTANT, "Clementine", "FEMELLES")
        ))
        mockMvc.perform(get("/events/18092020/reunion/5/course/6/participants/group-by/sex"))
                .andExpect(status().isOk)
                .andExpect(content().json("{\"participationsBySex\":[{\"sex\":\"HONGRES\",\"numberOfParticipants\":1},{\"sex\":\"MALES\",\"numberOfParticipants\":1},{\"sex\":\"FEMELLES\",\"numberOfParticipants\":1}]}"))
                .andExpect(header().string("Content-Type", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
    }
}