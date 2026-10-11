package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

class DockerSearchApiTest {

    lateinit var api: DockerSearchApi

    @BeforeEach
    fun setUp() {
        val moshi = Moshi.Builder()
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

        api = retrofitMain.create(DockerSearchApi::class.java)
    }

    @Test
    fun searchImages() = runTest {
        val searchImages = api.searchImages("postgres")
        assertThat(searchImages.results).hasSize(25)
    }

}