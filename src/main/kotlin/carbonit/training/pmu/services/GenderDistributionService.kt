package carbonit.training.pmu.services

import java.time.LocalDate

interface GenderDistributionService {
    fun getParticipantsGroupedBySex(date: LocalDate, eventNumber: Int, raceNumber: Int): Map<String, Int>
}