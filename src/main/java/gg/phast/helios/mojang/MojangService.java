package gg.phast.helios.mojang;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.phast.helios.mojang.textures.SkinData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Mojang Service specifically crafted
 * for Mojang's API, therefore can be unstable
 *
 * @author phastgg
 * @since 1.1
 */
class MojangService {

    private static final String PROFILE_LOOKUP_URL = "https://api.mojang.com/users/profiles/minecraft/{NAME}";
    private static final String SESSION_LOOKUP_URL = "https://sessionserver.mojang.com/session/minecraft/profile/{UUID}?unsigned=false";

    public @Nullable SkinData requestTexturesForPlayer(@NotNull String target) throws URISyntaxException, IOException {
        URL profileUrl = new URI(PROFILE_LOOKUP_URL.replace("{NAME}", target)).toURL();
        String uuid;

        // response for profile lookup
        try (InputStream response = profileUrl.openStream(); InputStreamReader responseReader = new InputStreamReader(response)) {
            uuid = JsonParser
                    .parseReader(responseReader)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString();
        }

        URL sessionUrl = new URI(SESSION_LOOKUP_URL.replace("{UUID}", uuid)).toURL();
        JsonObject sessionObject;
        // response for session lookup
        try (InputStream response = sessionUrl.openStream(); InputStreamReader responseReader = new InputStreamReader(response)) {
            sessionObject = JsonParser
                    .parseReader(responseReader)
                    .getAsJsonObject()
                    .get("properties")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();
        }

        return SkinData.of(sessionObject);
    }
}
