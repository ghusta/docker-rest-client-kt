package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json

data class SearchItemsResponse(
    @Json(name = "results")
    val results: List<SearchItem>
)