package carbonit.training.pmu.responses

data class Participations(val participationsBySex: List<ParticipationBySex>)
data class ParticipationBySex(val sex: String,  val numberOfParticipants: Int)