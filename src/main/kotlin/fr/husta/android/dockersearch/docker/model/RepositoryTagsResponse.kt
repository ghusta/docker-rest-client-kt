package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json

data class RepositoryTagsResponse(

    @Json(name = "count")
    val totalCount: Int,

    @Json(name = "results")
    val results: List<RepositoryTag>

)