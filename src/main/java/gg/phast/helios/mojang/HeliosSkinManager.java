package gg.phast.helios.mojang;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import gg.phast.helios.mojang.textures.SkinData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Skin Manager for managing player's textures
 * in more intuitive way to speed up
 * workflow
 *
 * @author phastgg
 * @since 1.1
 */
public class HeliosSkinManager {

    private static final HeliosSkinManager SINGLETON = new HeliosSkinManager();

    private final MojangService mojangService;

    private HeliosSkinManager() {
        mojangService = new MojangService();
    }

    /**
     * Changes GameProfile's textures to the ones specified
     *
     * @param profile profile
     * @param skinData new textures
     */
    public void changeSkinDataFor(@NotNull GameProfile profile, @NotNull SkinData skinData) {
        Preconditions.checkNotNull(profile, "profile cannot be null");
        Preconditions.checkNotNull(skinData, "skinData cannot be null");

        clearSkinDataFor(profile);
        profile.properties().put("textures", new Property("textures", skinData.getTexture(), skinData.getSignature()));
    }

    /**
     * Changes GameProfile's textures to the ones of specified player.
     * Special Warning: <b>SHOULD be run async</b>
     *
     * @param profile profile
     * @param playerName from which player to fetch textures
     */
    public void changeSkinDataFor(@NotNull GameProfile profile, @NotNull String playerName) throws URISyntaxException, IOException {
        Preconditions.checkNotNull(profile, "profile cannot be null");
        Preconditions.checkNotNull(playerName, "playerName cannot be null");

        clearSkinDataFor(profile);

        SkinData skinData = requestSkinDataFor(playerName);
        boolean skinFound = skinData != null;

        Preconditions.checkState(skinFound, "skinData not found for %s", playerName);

        changeSkinDataFor(profile, skinData);
    }

    /**
     * Clears all textures from GameProfile
     *
     * @param profile profile
     */
    public void clearSkinDataFor(@NotNull GameProfile profile) {
        Preconditions.checkNotNull(profile, "profile cannot be null");

        profile.properties().removeAll("textures");
    }

    /**
     * Requests for textures from Mojang API using {@link MojangService}.
     * Special Warning! We highly recommend running it on another thread
     * to prevent freeze or crash of the server
     *
     * @param playerName player name
     * @return SkinData returned from Mojang API, can be null and should be used with this in mind
     */
    public @Nullable SkinData requestSkinDataFor(@NotNull String playerName) throws URISyntaxException, IOException {
        return mojangService.requestTexturesForPlayer(playerName);
    }

    /**
     * Gets singleton instance of this class
     *
     * @return singleton instance
     */
    public static HeliosSkinManager getInstance() {
        return SINGLETON;
    }
}
