package carbonit.training.pmu.controllers

import carbonit.training.pmu.resources.Participant
import carbonit.training.pmu.resources.Participation
import carbonit.training.pmu.services.HttpService
import carbonit.training.pmu.services.RaceParticipantService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
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
    var mockHttpService: HttpService = Mockito.mock(HttpService::class.java)
    var spyRaceParticipantService: RaceParticipantService = Mockito.spy(RaceParticipantService(mockHttpService))

    @BeforeEach
    fun setup() {
        Mockito.reset(spyRaceParticipantService)
        Mockito.reset(mockHttpService)
        whenever(mockHttpService.get(any(), any())).thenReturn("{\"participants\":[{\"nom\":\"DONNE LE CHANGE\",\"numPmu\":1,\"age\":7,\"sexe\":\"HONGRES\",\"race\":\"AQPS\",\"statut\":\"PARTANT\",\"oeilleres\":\"SANS_OEILLERES\",\"proprietaire\":\"CH.CH.DUBOURG (S)\",\"entraineur\":\"CH.DUBOURG (S)\",\"driver\":\"B.DUBOURG\",\"driverChange\":false,\"robe\":{\"code\":\"020\",\"libelleCourt\":\"BAI\",\"libelleLong\":\"BAI\"},\"indicateurInedit\":false,\"musique\":\"5s1h1s(19)4s1s7h6h2h1h\",\"nombreCourses\":13,\"nombreVictoires\":5,\"nombrePlaces\":6,\"nombrePlacesSecond\":3,\"nombrePlacesTroisieme\":0,\"gainsParticipant\":{\"gainsCarriere\":8524500,\"gainsVictoires\":6144000,\"gainsPlace\":2380500,\"gainsAnneeEnCours\":3675000,\"gainsAnneePrecedente\":4297500},\"handicapValeur\":60,\"nomPere\":\"Saddler maker\",\"nomMere\":\"Tampabella\",\"nomPereMere\":\"Bonnet rouge\",\"ordreArrivee\":1,\"jumentPleine\":false,\"engagement\":false,\"supplement\":0,\"handicapPoids\":720,\"poidsConditionMonteChange\":false,\"dernierRapportDirect\":{\"typePari\":\"E_SIMPLE_GAGNANT\",\"rapport\":2.9,\"typeRapport\":\"DIRECT\",\"indicateurTendance\":\" \",\"nombreIndicateurTendance\":0,\"dateRapport\":1600447352000,\"permutation\":1,\"favoris\":false,\"numPmu1\":1,\"grossePrise\":false},\"dernierRapportReference\":{\"typePari\":\"E_SIMPLE_GAGNANT\",\"rapport\":5.5,\"typeRapport\":\"REFERENCE\",\"indicateurTendance\":\"-\",\"nombreIndicateurTendance\":-1.63,\"dateRapport\":1600445525000,\"permutation\":1,\"favoris\":false,\"numPmu1\":1,\"grossePrise\":false},\"urlCasaque\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/P1.png\",\"eleveur\":\"MR GILLES LE BIHAN\",\"allure\":\"GALOP\"}],\"ecuries\":[],\"spriteCasaques\":[{\"heightSize\":19,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_19px_size.png\",\"originalSize\":false},{\"heightSize\":40,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_40px_size.png\",\"originalSize\":false},{\"heightSize\":261,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_original_size.png\",\"originalSize\":true}],\"cached\":false}")
        mockMvc = standaloneSetup(RaceController(spyRaceParticipantService)).build()
    }

    @Test
    fun participantsGroupedBySex() {
        doReturn(listOf(
                Participant(1, Participation.PARTANT, "Christopher", "HONGRES"),
                Participant(1, Participation.NON_PARTANT, "Miguel", "HONGRES"),
                Participant(2, Participation.PARTANT, "Esteban", "MALES"),
                Participant(3, Participation.PARTANT, "Clementine", "FEMELLES")
        )).`when`(spyRaceParticipantService).getAllParticipants(any(), any(), any())

        mockMvc.perform(get("/events/18092020/reunion/5/course/6/participants/group-by/sex"))
                .andExpect(status().isOk)
                .andExpect(content().json("{\"participationsBySex\":[{\"sex\":\"HONGRES\",\"numberOfParticipants\":1},{\"sex\":\"MALES\",\"numberOfParticipants\":1},{\"sex\":\"FEMELLES\",\"numberOfParticipants\":1}]}"))
                .andExpect(header().string("Content-Type", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
    }
}