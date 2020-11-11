package carbonit.training.pmu.services

import carbonit.training.pmu.resources.Participants
import java.time.LocalDate

interface PMUApi<T> {
    fun fetchParticipants(date: LocalDate, eventNumber: Int, raceNumber: Int): Participants
}