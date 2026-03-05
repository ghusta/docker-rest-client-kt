package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.husta.android.dockersearch.docker.moshi.adapters.InstantJsonAdapter
import fr.husta.android.dockersearch.docker.moshi.adapters.LocalDateJsonAdapter
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

const val BASE_URI = "https://index.docker.io"
const val BASE_REGISTRY_URI = "https://registry.hub.docker.com"

fun main() {

    val moshi = Moshi.Builder()
        .add(LocalDate::class.java, LocalDateJsonAdapter())
        .add(InstantJsonAdapter())
//        .add(Date::class.java, Rfc3339DateJsonAdapter()) // just for example
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val noProxy = Proxy.NO_PROXY

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .proxy(noProxy)
        .build()

    val retrofitMain = Retrofit.Builder()
        .baseUrl(BASE_REGISTRY_URI)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val dockerSearchApi = retrofitMain.create(DockerSearchApi::class.java)

    try {
        runBlocking {
            val result = dockerSearchApi.searchImages("postgres")
            println("🧮 ${result.results.size}")

            val repository = "library/eclipse-temurin"
            val resultTags = dockerSearchApi.listTags(repository, 1, 25)
            val dayOfMonthFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            resultTags.results.forEach {
                val formattedName = String.format("%-30s", it.name)
                val formattedSize = String.format("%,d", it.fullSize)
                val formattedDate = it.lastUpdated.atZone(ZoneId.systemDefault()).format(dayOfMonthFormatter)
                val filteredVariants = it.imageVariants.filter { variant -> variant.architecture != "unknown" }
                println("➡️ $formattedName -- $formattedSize -- $formattedDate -- variants: ${filteredVariants.size}")
            }
        }
    } finally {
        okHttpClient.dispatcher.executorService.shutdown()
        okHttpClient.connectionPool.evictAll()
    }
//    exitProcess(0)
}