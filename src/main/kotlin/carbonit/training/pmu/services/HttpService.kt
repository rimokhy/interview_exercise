package carbonit.training.pmu.services

import org.springframework.stereotype.Service
import java.net.URL

@Service
class HttpService {
    fun makeUrl(url: String, vararg queryParams: Pair<String, *>): String = url + (takeIf { queryParams.isEmpty() }?.run { "" }
            ?: "?" + queryParams.joinToString(separator = "&") { "${it.first}=${it.second.toString()}" })

    fun get(url: String, vararg queryParams: Pair<String, *>): String = URL(makeUrl(url, *queryParams)).readText()
}