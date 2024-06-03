import com.example.libbevantestingkmm.RocketLaunch
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class RocketComponent {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private suspend fun getDateOfLastSuccessfulLaunch(): String {
        val rockets: List<RocketLaunch> = httpClient.get("https://api.spacexdata.com/v4/launches").body()
        val lastSuccesLauch = rockets.last { it.launchSuccess == true }
        val date = Instant.parse(lastSuccesLauch.launchDateUTC).toLocalDateTime(TimeZone.currentSystemDefault())
        return "${date.month} ${date.dayOfMonth} ${date.year}"
    }

    suspend fun launchPhrase() : String =
        try {
            "The last successful launch was on ${getDateOfLastSuccessfulLaunch()}"
        } catch (e: Exception) {
            println("error")
            "error"
        }


    suspend fun getRocket() : List<RocketLaunch> {
        val rockets: List<RocketLaunch> = httpClient.get("https://api.spacexdata.com/v4/launches").body()
        return rockets
    }

    fun getRockeTFlow() : KmmFlow<List<RocketLaunch>> =  flow {
        val rockets: List<RocketLaunch> = httpClient.get("https://api.spacexdata.com/v4/launches").body()
        emit(rockets)
    }.asKmmFlow()
}

fun interface KmmSubscription {
    fun unsubscribe()
}

class KmmFlow<T>(private val source: Flow<T>) : Flow<T> by source {
    fun subscribe(onEach: (T) -> Unit, onCompletion: (Throwable?) -> Unit): KmmSubscription {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        source
            .onEach { onEach(it) }
            .catch { onCompletion(it) }
            .onCompletion { onCompletion(null) }
            .launchIn(scope)
        return KmmSubscription { scope.cancel() }
    }
}

fun <T> Flow<T>.asKmmFlow(): KmmFlow<T> = KmmFlow(this)