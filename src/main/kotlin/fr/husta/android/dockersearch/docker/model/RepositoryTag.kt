package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json
import java.time.Instant

data class RepositoryTag(

    @Json(name = "name")
    val name: String,

    @Json(name = "full_size")
    val fullSize: Long,

    @Json(name = "last_updated")
    val lastUpdated: Instant,

    @Json(name = "images")
    val imageVariants: List<ImageVariantByTag>

)