package carbonit.training.pmu.services

import carbonit.training.pmu.resources.Participation
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.IllegalStateException
import java.time.LocalDate

internal class RaceParticipantServiceTest {
    var mockHttpService: HttpService = Mockito.mock(HttpService::class.java)
    var raceParticipantService: RaceParticipantService = RaceParticipantService(mockHttpService)

    @BeforeEach
    fun setup() {
        Mockito.reset(mockHttpService)
        raceParticipantService = RaceParticipantService(mockHttpService)
    }

    @Test
    fun `ensure date formatter usage`() {
        assertEquals("18092020", LocalDate.of(2020, 9, 18).format(raceParticipantService.dateFormatter))
    }

    @Test
    fun `ensure json parsing is working`() {
        whenever(mockHttpService.get(any(), any())).thenReturn("{\"participants\":[{\"nom\":\"DONNE LE CHANGE\",\"numPmu\":1,\"age\":7,\"sexe\":\"HONGRES\",\"race\":\"AQPS\",\"statut\":\"PARTANT\",\"oeilleres\":\"SANS_OEILLERES\",\"proprietaire\":\"CH.CH.DUBOURG (S)\",\"entraineur\":\"CH.DUBOURG (S)\",\"driver\":\"B.DUBOURG\",\"driverChange\":false,\"robe\":{\"code\":\"020\",\"libelleCourt\":\"BAI\",\"libelleLong\":\"BAI\"},\"indicateurInedit\":false,\"musique\":\"5s1h1s(19)4s1s7h6h2h1h\",\"nombreCourses\":13,\"nombreVictoires\":5,\"nombrePlaces\":6,\"nombrePlacesSecond\":3,\"nombrePlacesTroisieme\":0,\"gainsParticipant\":{\"gainsCarriere\":8524500,\"gainsVictoires\":6144000,\"gainsPlace\":2380500,\"gainsAnneeEnCours\":3675000,\"gainsAnneePrecedente\":4297500},\"handicapValeur\":60,\"nomPere\":\"Saddler maker\",\"nomMere\":\"Tampabella\",\"nomPereMere\":\"Bonnet rouge\",\"ordreArrivee\":1,\"jumentPleine\":false,\"engagement\":false,\"supplement\":0,\"handicapPoids\":720,\"poidsConditionMonteChange\":false,\"dernierRapportDirect\":{\"typePari\":\"E_SIMPLE_GAGNANT\",\"rapport\":2.9,\"typeRapport\":\"DIRECT\",\"indicateurTendance\":\" \",\"nombreIndicateurTendance\":0,\"dateRapport\":1600447352000,\"permutation\":1,\"favoris\":false,\"numPmu1\":1,\"grossePrise\":false},\"dernierRapportReference\":{\"typePari\":\"E_SIMPLE_GAGNANT\",\"rapport\":5.5,\"typeRapport\":\"REFERENCE\",\"indicateurTendance\":\"-\",\"nombreIndicateurTendance\":-1.63,\"dateRapport\":1600445525000,\"permutation\":1,\"favoris\":false,\"numPmu1\":1,\"grossePrise\":false},\"urlCasaque\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/P1.png\",\"eleveur\":\"MR GILLES LE BIHAN\",\"allure\":\"GALOP\"}],\"ecuries\":[],\"spriteCasaques\":[{\"heightSize\":19,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_19px_size.png\",\"originalSize\":false},{\"heightSize\":40,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_40px_size.png\",\"originalSize\":false},{\"heightSize\":261,\"url\":\"https://turfinfo.pmucdn.fr/casaques/18092020/R5/C6/sprite_casaques_original_size.png\",\"originalSize\":true}],\"cached\":false}")

        val runners = raceParticipantService.getAllParticipants(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(1, runners.size)
        assertEquals(1, runners[0].id)
        assertEquals("DONNE LE CHANGE", runners[0].name)
        assertEquals(Participation.PARTANT, runners[0].status)
        assertEquals("HONGRES", runners[0].sex)
        assertEquals(true, runners[0].isParticipating)
    }

    @Test
    fun getParticipantsGroupedBySex() {
        val jsonContent = String(Thread.currentThread().contextClassLoader.getResourceAsStream("participants.json")?.readAllBytes() ?: throw IllegalStateException("Cannot find file participants.json in test resource directory"))
        whenever(mockHttpService.get(any(), any())).thenReturn(jsonContent)

        val runners = raceParticipantService.getParticipantsGroupedBySex(LocalDate.of(2020, 9, 18), 5, 6)

        assertEquals(3, runners.size)
        assertEquals("MALES", runners[0].sex)
        assertEquals("FEMELLES", runners[1].sex)
        assertEquals("HONGRES", runners[2].sex)
        assertEquals(9, runners[0].numberOfParticipants)
        assertEquals(3, runners[1].numberOfParticipants)
        assertEquals(1, runners[2].numberOfParticipants)
    }
}