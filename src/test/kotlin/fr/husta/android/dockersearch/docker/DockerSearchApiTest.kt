package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import fr.husta.android.dockersearch.docker.model.comparator.SearchItemComparators
import fr.husta.android.dockersearch.docker.moshi.adapters.InstantJsonAdapter
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class DockerSearchApiTest {

    lateinit var api: DockerSearchApi

    @BeforeEach
    fun setUp() {
        val moshi = Moshi.Builder()
            .add(InstantJsonAdapter())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .proxy(null)
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

    @Test
    fun searchImages_sorted() = runTest {
        val searchImages = api.searchImages("postgres")
        assertThat(searchImages.results).hasSizeGreaterThan(12)

        val sortedItems = searchImages.results.sortedWith(SearchItemComparators.BY_OFFICIAL_AND_STARS)
        assertThat(sortedItems[0].isOfficial).isTrue
        assertThat(sortedItems[9].isOfficial).isFalse

        val searchItem2 = sortedItems[1]
        val searchItem3 = sortedItems[2]
        val searchItem4 = sortedItems[3]
        assertThat(searchItem2.starCount).isGreaterThanOrEqualTo(searchItem3.starCount)
        assertThat(searchItem3.starCount).isGreaterThanOrEqualTo(searchItem4.starCount)
    }

    @Test
    fun listTags() = runTest {
        val tags = api.listTags("library/postgres")
        assertThat(tags.results).hasSize(25)
    }

}