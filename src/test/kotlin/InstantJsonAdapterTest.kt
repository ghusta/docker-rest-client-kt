import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.husta.android.dockersearch.docker.moshi.adapters.InstantJsonAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class InstantJsonAdapterTest {

    private val moshi = Moshi.Builder()
        .add(InstantJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    data class Event(
        val name: String,
        val createdAt: Instant
    )

    @Test
    fun `should serialize instant to ISO string`() {
        val instant = Instant.parse("2026-02-16T12:00:00Z")
        val event = Event("Test", instant)

        val json = moshi.adapter(Event::class.java).toJson(event)

        assertThat(json).isEqualTo(
            // language=json
            """{"name":"Test","createdAt":"2026-02-16T12:00:00Z"}"""
        )
    }

    @Test
    fun `should deserialize ISO string to instant`() {
        // language=json
        val json = """
            {
              "name":"Test",
              "createdAt":"2026-02-16T12:00:00Z"
            }
            """.trimIndent()

        val event = moshi.adapter(Event::class.java).fromJson(json)

        assertThat(event?.name).isEqualTo("Test")
        assertThat(event?.createdAt).isEqualTo(Instant.parse("2026-02-16T12:00:00Z"))
    }
}
