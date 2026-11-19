package dev.wingtweaks;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Settings saved in config/wingtweaks.properties
 */
public final class WingTweaksConfig {
    /** /wg wings on|off. Only affects YOUR wings on YOUR screen. */
    public static boolean ownWingsVisible = true;

    /** /wg wings all on|off. Hides or shows EVERYONE's Fantastic Wings (including yours) on your screen. */
    public static boolean allWingsVisible = true;

    /** Master switch for the paper fix. */
    public static boolean paperFixEnabled = true;

    /**
     * true  = the offhand paper is ALWAYS kept in the standing position on your back (except while sneaking,
     *         where the vanilla pose already follows the body lean). Fixes the slanted wings after you stop
     *         flying and drop down, and stops them swinging while you walk or fall.
     * false = only fix it while the body is tilted (old behaviour).
     */
    public static boolean paperAlwaysLock = true;

    /**
     * The arm angle (degrees) the offhand paper is drawn with while your body is tilted.
     * -18 is what vanilla uses when you stand still holding an item, so the paper should look
     * the same as when standing. If it looks slightly off, change these two numbers.
     */
    public static float paperArmXDeg = -18.0F;
    public static float paperArmZDeg = 0.0F;

    /** Body tilt (degrees from upright) where the fix starts blending in / is fully applied. */
    public static float tiltStartDeg = 12.0F;
    public static float tiltFullDeg = 30.0F;

    private WingTweaksConfig() {
    }

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("wingtweaks.properties");
    }

    public static void load() {
        Path path = file();
        if (!Files.exists(path)) {
            save();
            return;
        }
        Properties p = new Properties();
        try (Reader reader = Files.newBufferedReader(path)) {
            p.load(reader);
        } catch (IOException e) {
            System.err.println("[wingtweaks] Could not read config: " + e);
            return;
        }
        ownWingsVisible = Boolean.parseBoolean(p.getProperty("own_wings_visible", "true"));
        allWingsVisible = Boolean.parseBoolean(p.getProperty("all_wings_visible", "true"));
        paperFixEnabled = Boolean.parseBoolean(p.getProperty("paper_fix", "true"));
        paperAlwaysLock = Boolean.parseBoolean(p.getProperty("paper_always_lock", "true"));
        paperArmXDeg = parse(p.getProperty("paper_arm_x_deg"), paperArmXDeg);
        paperArmZDeg = parse(p.getProperty("paper_arm_z_deg"), paperArmZDeg);
        tiltStartDeg = parse(p.getProperty("tilt_start_deg"), tiltStartDeg);
        tiltFullDeg = parse(p.getProperty("tilt_full_deg"), tiltFullDeg);
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("own_wings_visible", Boolean.toString(ownWingsVisible));
        p.setProperty("all_wings_visible", Boolean.toString(allWingsVisible));
        p.setProperty("paper_fix", Boolean.toString(paperFixEnabled));
        p.setProperty("paper_always_lock", Boolean.toString(paperAlwaysLock));
        p.setProperty("paper_arm_x_deg", Float.toString(paperArmXDeg));
        p.setProperty("paper_arm_z_deg", Float.toString(paperArmZDeg));
        p.setProperty("tilt_start_deg", Float.toString(tiltStartDeg));
        p.setProperty("tilt_full_deg", Float.toString(tiltFullDeg));
        try (Writer writer = Files.newBufferedWriter(file())) {
            p.store(writer, "Wing Tweaks config");
        } catch (IOException e) {
            System.err.println("[wingtweaks] Could not save config: " + e);
        }
    }

    private static float parse(String value, float fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
