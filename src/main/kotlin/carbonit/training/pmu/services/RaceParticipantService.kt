package carbonit.training.pmu.services

import carbonit.training.pmu.resources.Participant
import carbonit.training.pmu.resources.Participants
import carbonit.training.pmu.responses.ParticipationBySex
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class RaceParticipantService @Autowired constructor(val httpService: HttpService) {
    val objectMapper = ObjectMapper().apply { registerKotlinModule() }
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy")

    private fun getAllParticipants(date: String, eventNumber: Int, raceNumber: Int): List<Participant> = objectMapper.readValue<Participants>(
            httpService.get(
                    "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/${date}/R${eventNumber}/C${raceNumber}/participants",
                    "specialisation" to "INTERNET"
            )
    ).participants

    fun getAllParticipants(date: LocalDate, eventNumber: Int, raceNumber: Int): List<Participant> = getAllParticipants(date.format(dateFormatter), eventNumber, raceNumber)

    fun getParticipantsGroupedBySex(date: LocalDate, eventNumber: Int, raceNumber: Int): List<ParticipationBySex> {
        val participationsBySex = mutableListOf<ParticipationBySex>()
        val participants = getAllParticipants(date, eventNumber, raceNumber)

        return participants.filter { it.isParticipating }
                .groupingBy { it.sex }
                .eachCount()
                .mapTo(participationsBySex) { ParticipationBySex(it.key, it.value) }
    }
}
