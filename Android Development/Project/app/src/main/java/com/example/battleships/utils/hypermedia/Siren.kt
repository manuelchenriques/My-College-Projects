package com.example.battleships.utils.hypermedia

import com.example.battleships.model.UserToken
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import java.lang.reflect.Type

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */

data class SirenModel<T>(
    @get:JsonProperty("class")
    val clazz: List<String>,
    val properties: T,
    val links: List<LinkModel>,
    val entities: List<EntityModel<*>>,
    val actions: List<ActionModel>
)

data class LinkModel(
    val rel: List<String>,
    val href: String
)

data class EntityModel<T>(
    val properties: T,
    val links: List<LinkModel>,
    val rel: List<String>
)

data class ActionModel(
    val name: String,
    val href: String,
    val method: String,
    val type: String,
    val needsAuthentication : Boolean,
    val fields: List<FieldModel>,
)

data class FieldModel(
    val name: String,
    val type: String,
    val value: String? = null,
)


fun takePropertiesFromBody(body: String?, type: Type):String{
    if (body==null) return ""
    if (type == object:TypeToken<UserToken>(){}.type) return body
    val aux = body.drop(1)
    val firstIdx = aux.indexOfFirst {
        it == '{'
    }
    val secondIdx = aux.indexOfFirst {
        it == '}'
    }

    return aux.substring(firstIdx,secondIdx+1)
}
