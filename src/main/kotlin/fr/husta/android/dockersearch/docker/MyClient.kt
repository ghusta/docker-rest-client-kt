package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

const val BASE_URI = "https://index.docker.io"

fun main() {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val webProxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("100.78.112.201", 8001))

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .proxy(webProxy)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URI)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: DockerSearchRestService = retrofit.create(DockerSearchRestService::class.java)

    try {
        runBlocking {
            val result = service.searchImages("postgres")
            println(result.results.size)
        }
    } finally {
        okHttpClient.dispatcher.executorService.shutdown()
        okHttpClient.connectionPool.evictAll()
    }
//    exitProcess(0)
}