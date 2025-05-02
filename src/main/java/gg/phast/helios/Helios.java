package gg.phast.helios;

import gg.phast.helios.logging.HeliosLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Helios is a lightweight, high-performance toolkit packed
 * with essential Minecraft utilities — built to streamline your
 * gameplay, modding, and server management with the power of the sun.
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2025 phastgg
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author phastgg
 * @since 1.0-SNAPSHOT
 */
public final class Helios {

    private final static JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(Helios.class);
    private final static HeliosLogger LOGGER = new HeliosLogger();

    /**
     * Plugin which is using this API
     * @return plugin
     * @since 1.0-SNAPSHOT
     */
    public static JavaPlugin getPlugin() {
        return PLUGIN;
    }

    /**
     * Custom logger which is used for debug
     * @since 1.0-SNAPSHOT
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}
