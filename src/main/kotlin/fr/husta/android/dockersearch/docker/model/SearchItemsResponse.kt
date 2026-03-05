package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json

data class SearchItemsResponse(

    @Json(name = "results")
    val results: List<SearchItem>,

    @Json(name = "page")
    val page: Int,

    @Json(name = "page_size")
    val pageSize: Int

)