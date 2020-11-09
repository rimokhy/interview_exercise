package carbonit.training.pmu.resources

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

enum class Participation {
    PARTANT,
    NON_PARTANT
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Participants(val participants: List<Participant>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Participant(
        @JsonProperty("numPmu") val id: Int,
        @JsonProperty("statut") val status: Participation,
        @JsonProperty("nom") val name: String,
        @JsonProperty("sexe") val sex: String
) {
    val isParticipating
        get() = status == Participation.PARTANT
}

