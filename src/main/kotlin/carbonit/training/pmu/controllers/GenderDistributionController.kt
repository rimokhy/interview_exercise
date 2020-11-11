package carbonit.training.pmu.controllers

import carbonit.training.pmu.services.GenderDistributionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class GenderDistributionController constructor(val genderDistributionService: GenderDistributionService) {
    @GetMapping("/date/{date}/reunion/{reunionNumber}/course/{courseNumber}/gender-distribution")
    fun participantsGroupedBySex(
            @PathVariable("date") @DateTimeFormat(pattern = "ddMMyyyy") date: LocalDate,
            @PathVariable("reunionNumber") eventNumber: Int,
            @PathVariable("courseNumber") raceNumber: Int
    ) = genderDistributionService.getParticipantsGroupedBySex(date, eventNumber, raceNumber)
}