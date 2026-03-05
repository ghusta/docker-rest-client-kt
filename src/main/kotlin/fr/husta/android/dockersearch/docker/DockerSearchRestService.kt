package fr.husta.android.dockersearch.docker

import fr.husta.android.dockersearch.docker.model.SearchItemsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DockerSearchRestService {

    @GET("v1/search")
    suspend fun searchImages(@Query("q") term: String, @Query("n") size: Int = 25): SearchItemsResponse

}