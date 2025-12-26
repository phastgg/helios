package gg.phast.helios.mojang.textures

import com.google.gson.JsonObject
import com.mojang.authlib.properties.Property

/**
 * SkinData simple wrapper holding texture and signature
 *
 * @author phastgg
 * @since 1.1
 */
data class SkinData(val texture: String, val signature: String) {
    companion object {
        @JvmStatic
        fun of(texture: String, signature: String): SkinData {
            return SkinData(texture, signature)
        }

        @JvmStatic
        fun of(jsonObject: JsonObject): SkinData {
            return of(
                jsonObject.get("value").asString,
                jsonObject.get("signature").asString
            );
        }

        @JvmStatic
        fun of(property: Property): SkinData {
            return of(
                property.value,
                property.signature!!
            );
        }
    }

}
