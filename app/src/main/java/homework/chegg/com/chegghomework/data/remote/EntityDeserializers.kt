package homework.chegg.com.chegghomework.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import homework.chegg.com.chegghomework.data.entities.StoryItem
import java.lang.reflect.Type

fun String.generateStoryId() = this.hashCode()

class SourceADeserializer : JsonDeserializer<List<StoryItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<StoryItem> {
        try {
            if (json == null) {
                return emptyList()
            }
            val jo: JsonObject = json.asJsonObject
            val resultList: MutableList<StoryItem> = mutableListOf()
            jo.get("stories").asJsonArray.forEach foreach@{ item ->
                val ob: JsonObject = item.asJsonObject
                val title: String? = ob.get("title").asString
                val subtitle: String? = ob.get("subtitle").asString
                val imageUrl: String? = ob.get("imageUrl").asString
                if (title == null || subtitle == null || imageUrl == null) {
                    return@foreach
                }
                resultList.add(
                    StoryItem(
                        id = title.generateStoryId(),
                        title = title,
                        subtitle = subtitle,
                        imageUrl = imageUrl
                    )
                )
            }
            return resultList
        } catch (e: Error) {
            throw Exception(e.stackTraceToString())
        }
    }
}

class SourceBDeserializer : JsonDeserializer<List<StoryItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<StoryItem> {
        try {
            if (json == null) {
                return emptyList()
            }
            val jo: JsonObject = json.asJsonObject.get("metadata").asJsonObject
            val id: JsonArray = jo.get("innerdata").asJsonArray
            val resultList: MutableList<StoryItem> = mutableListOf()
            id.forEach foreach@{ item ->
                val aw: JsonObject = item.asJsonObject.get("articlewrapper").asJsonObject
                val title: String? = aw.asJsonObject.get("header").asString
                val subtitle: String? = aw.asJsonObject.get("description").asString
                val imageUrl: String? = item.asJsonObject.get("picture").asString
                if (title == null || subtitle == null || imageUrl == null) {
                    return@foreach
                }
                resultList.add(
                    StoryItem(id = title.generateStoryId(), title = title, subtitle = subtitle, imageUrl = imageUrl)
                )
            }
            return resultList
        } catch (e: Error) {
            throw Exception(e.stackTraceToString())
        }
    }
}

class SourceCDeserializer : JsonDeserializer<List<StoryItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<StoryItem> {
        try {
            if (json == null) {
                return emptyList()
            }
            val jo: JsonArray = json.asJsonArray
            val resultList: MutableList<StoryItem> = mutableListOf()
            jo.forEach foreach@{ item ->
                val title: String? = item.asJsonObject.get("topLine").asString
                val subtitle1: String? = item.asJsonObject.get("subLine1").asString
                val subtitle2: String? = item.asJsonObject.get("subline2").asString
                val imageUrl: String? = item.asJsonObject.get("image").asString

                val subtitle: String? = if (subtitle1 == null) {
                    null
                } else {
                    "$subtitle1${(subtitle2 ?: "")}"
                }
                if (title == null || subtitle == null || imageUrl == null) {
                    return@foreach
                }
                resultList.add(
                    StoryItem(id = title.generateStoryId(), title = title, subtitle = subtitle, imageUrl = imageUrl)
                )
            }
            return resultList
        } catch (e: Error) {
            throw Exception(e.stackTraceToString())
        }
    }
}
