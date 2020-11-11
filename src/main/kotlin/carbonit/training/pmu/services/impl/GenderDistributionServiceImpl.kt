package carbonit.training.pmu.services.impl

import carbonit.training.pmu.resources.Participants
import carbonit.training.pmu.services.GenderDistributionService
import carbonit.training.pmu.services.PMUApi
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GenderDistributionServiceImpl constructor(val pmu: PMUApi<Participants>) : GenderDistributionService {
    override fun getParticipantsGroupedBySex(date: LocalDate, eventNumber: Int, raceNumber: Int): Map<String, Int> {
        val participants = pmu.fetchParticipants(date, eventNumber, raceNumber).participants
        pmu.fetchParticipants(date, eventNumber, raceNumber).participants

        return participants
                .filter { it.isParticipating }
                .groupingBy { it.sex }
                .eachCount()
                .toMap()
    }
}
