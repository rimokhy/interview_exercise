package carbonit.training.pmu.services.impl

import carbonit.training.pmu.exceptions.InternalServerError
import carbonit.training.pmu.resources.Participants
import carbonit.training.pmu.services.PMUApi
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Repository
class PMUApiImpl constructor(val restTemplate: RestTemplate): PMUApi<Participants> {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy")
    val baseUrl = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("online.turfinfo.api.pmu.fr")
            .path("/rest/client/1/programme/{date}/R{eventNumber}/C{raceNumber}/participants")
            .queryParam("specialisation", "INTERNET")

    override fun fetchParticipants(date: LocalDate, eventNumber: Int, raceNumber: Int): Participants {
        val url = baseUrl.cloneBuilder().buildAndExpand(
                dateFormatter.format(date),
                eventNumber,
                raceNumber
        ).toUriString()
        val result = restTemplate.getForEntity(url, Participants::class.java)

        if (!result.statusCode.is2xxSuccessful)
            throw InternalServerError("Error while requesting data to PMU Api. Error code: ${result.statusCode}")
        return result.body ?: throw InternalServerError("Error parsing result from PMU Api.")
    }
}