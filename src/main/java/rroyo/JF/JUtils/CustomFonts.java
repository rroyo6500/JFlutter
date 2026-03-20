package rroyo.JF.JUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The CustomFonts interface provides static methods for managing a collection of custom TrueType fonts.
 * It allows adding, retrieving, and removing fonts by their respective names, using either file paths or File objects.
 *
 * @author _rroyo65_
 */
public final class CustomFonts {

    private CustomFonts(){}

    /**
     * A map that holds a collection of custom TrueType fonts, with unique string identifiers as keys and Font objects as values.
     * It serves as an internal storage for managing fonts added via the {@link CustomFonts} interface methods.
     * The fonts are stored by their associated names, which can be used to retrieve or remove them from the collection.
     */
    private static final Map<String, Font> fonts = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Adds a TrueType font to the internal font collection by specifying the font name,
     * the file's path to the font, and the desired font size.
     * This method internally delegates the font addition task to another implementation
     * that operates with a File object.
     *
     * @param key The identifier for the font to be added. This will be used when retrieving the font later.
     * @param path The file system path pointing to the TrueType font file.
     * @param size The desired size of the font in points.
     */
    public static void addFont (String key, String path, float size) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("Path cannot be blank");
        addFont(key, new File(path), size);
    }

    /**
     * Adds a TrueType font to the internal font collection using a file object.
     * The font is identified by a unique name and can be derived to a specific size.
     *
     * @param key The identifier for the font to be added. This name will be used for retrieving the font.
     * @param file The File object pointing to the TrueType font file.
     * @param size The desired font size to derive from the base font.
     */
    public static void addFont (String key, File file, float size) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        if (!file.exists()) throw new IllegalArgumentException("File does not exist");
        if (file.isDirectory()) throw new IllegalArgumentException("File cannot be a directory");
        if (!file.isFile()) throw new IllegalArgumentException("File is not a regular file");
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Key cannot be blank");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than zero");

        try {
            Font prev = CustomFonts.fonts.putIfAbsent(key, Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(size));
            if (prev != null) throw new IllegalArgumentException("Font key already exists: " + key);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a font from the internal font collection based on its name.
     *
     * @param key The name of the font to retrieve. This name serves as the unique identifier for the font in the collection.
     * @return The Font object associated with the specified name, or null if no font with the given name is found.
     */
    public static Font getFont (String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Key cannot be blank");
        if (!CustomFonts.fonts.containsKey(key)) throw new IllegalArgumentException("Font key does not exist: " + key);
        return CustomFonts.fonts.get(key);
    }

    /**
     * Retrieves a font from the internal font collection and derives it to the specified size.
     *
     * @param key The name of the font to retrieve. This name serves as the unique identifier
     *            for the font in the collection.
     * @param size The desired size to which the font will be derived. This value must not be null.
     * @return The derived Font object with the specified size, or null if no font with the
     *         given name is found in the collection.
     */
    public static Font getFont (String key, Float size) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Key cannot be blank");
        if (!CustomFonts.fonts.containsKey(key)) throw new IllegalArgumentException("Font key does not exist: " + key);
        return CustomFonts.fonts.get(key).deriveFont(size);
    }

    public static boolean containsFont(String key) {
        return CustomFonts.fonts.containsKey(key);
    }

    /**
     * Removes a font from the internal font collection based on its name.
     *
     * @param key The name of the font to remove. This name serves as the unique identifier
     *             for the font in the collection.
     */
    public static void removeFont (String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Key cannot be blank");
        if (!CustomFonts.fonts.containsKey(key)) throw new IllegalArgumentException("Font key does not exist: " + key);
        CustomFonts.fonts.remove(key);
    }

}
