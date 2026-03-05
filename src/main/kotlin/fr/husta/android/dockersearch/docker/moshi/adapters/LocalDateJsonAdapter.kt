package fr.husta.android.dockersearch.docker.moshi.adapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateJsonAdapter : JsonAdapter<LocalDate>() {

    override fun fromJson(reader: JsonReader): LocalDate? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull<LocalDate?>()
        }
        val string = reader.nextString()
        return LocalDate.parse(string, FORMATTER)
    }

    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.format(FORMATTER))
        }
    }

    companion object {
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

}