package carbonit.training.pmu.controllers

import carbonit.training.pmu.services.impl.GenderDistributionServiceImpl
import carbonit.training.pmu.services.impl.PMUApiImpl
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.client.RestTemplate

@WebMvcTest(GenderDistributionController::class, GenderDistributionServiceImpl::class, PMUApiImpl::class, RestTemplate::class)
internal class GenderDistributionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc
    @MockBean
    lateinit var genderDistributionService: GenderDistributionServiceImpl

    @Test
    fun `test gender distribution controller - 1 running participant by sex`() {
        doReturn(mapOf("HONGRES" to 1, "FEMELLES" to 1, "MALES" to 1)).`when`(genderDistributionService).getParticipantsGroupedBySex(any(), any(), any())

        mockMvc.perform(get("/date/18092020/reunion/5/course/6/gender-distribution"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap)
                .andExpect(jsonPath("$.MALES").value(1))
                .andExpect(jsonPath("$.HONGRES").value(1))
                .andExpect(jsonPath("$.FEMELLES").value(1))

        verify(genderDistributionService, times(1)).getParticipantsGroupedBySex(any(), any(), any())
        verifyNoMoreInteractions(genderDistributionService)
    }

    @Test
    fun `test gender distribution controller - multiple running participant by sex`() {
        doReturn(mapOf("HONGRES" to 5, "FEMELLES" to 4, "MALES" to 3)).`when`(genderDistributionService).getParticipantsGroupedBySex(any(), any(), any())

        mockMvc.perform(get("/date/18092020/reunion/5/course/6/gender-distribution"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap)
                .andExpect(jsonPath("$.MALES").value(3))
                .andExpect(jsonPath("$.HONGRES").value(5))
                .andExpect(jsonPath("$.FEMELLES").value(4))

        verify(genderDistributionService, times(1)).getParticipantsGroupedBySex(any(), any(), any())
        verifyNoMoreInteractions(genderDistributionService)
    }

    @Test
    fun `test gender distribution controller - no participants`() {
        doReturn(emptyMap<String, Int>()).`when`(genderDistributionService).getParticipantsGroupedBySex(any(), any(), any())

        mockMvc.perform(get("/date/18092020/reunion/5/course/6/gender-distribution"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap)

        verify(genderDistributionService, times(1)).getParticipantsGroupedBySex(any(), any(), any())
        verifyNoMoreInteractions(genderDistributionService)
    }
}