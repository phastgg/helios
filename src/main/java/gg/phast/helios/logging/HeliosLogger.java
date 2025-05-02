package gg.phast.helios.logging;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger for Helios api, should not be used outside this API
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
@ApiStatus.Internal
public class HeliosLogger extends Logger {

    /**
     * Constructor
     */
    public HeliosLogger() {
        super("Helios-Logger", null);
        setLevel(Level.ALL);
        setParent(Bukkit.getLogger());
    }
}
