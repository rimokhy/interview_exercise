package carbonit.training.pmu.controllers

import carbonit.training.pmu.responses.Participations
import carbonit.training.pmu.services.RaceParticipantService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class RaceController @Autowired constructor(val raceParticipantService: RaceParticipantService) {
    @GetMapping("/events/{date}/reunion/{reunionNumber}/course/{courseNumber}/participants/group-by/sex", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun participantsGroupedBySex(
            @PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") date: LocalDate,
            @PathVariable("reunionNumber") eventNumber: Int,
            @PathVariable("courseNumber") raceNumber: Int
    ): ResponseEntity<Participations> {
        return ResponseEntity.ok(Participations(raceParticipantService.getParticipantsGroupedBySex(date, eventNumber, raceNumber)))
    }
}