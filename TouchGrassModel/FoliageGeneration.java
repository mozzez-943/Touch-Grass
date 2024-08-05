package TouchGrassModel;


/** Class to facilitate the foliage generation of the entire program.
 * Functions are abstract and are intended to be overridden by subclasses. */
public abstract class FoliageGeneration {
    //normal textures
    public static String cut_grass = "./assets/Grass_002.png";
    public static String long_grass = "./assets/long_grass1.png";
    public static String poisen_ivy = "./assets/poisenivy2.png";

    // High contrast images
    public static String cutGrassHC = "./assets/Grass_002.png";
    public static String longGrassHC = "./assets/long_grass1acc.png";
    public static String poisonIvyHC = "./assets/poisenivy2acc.png";

    // Accessibility Variables
    private boolean isColorBlindMode = false;
    private boolean isHighContrastMode = false;

    // Abstract methods that subclasses need to implement
    // Generate foliage from cut state
    public abstract void foliageGeneration(Tile tile);

    // Cut foliage from grown state
    public abstract void cutFoliage(Tile tile, Player player);

    // Colour blind settings
    public static void colourblindsetting(String shortGrassPutter, String longGrassPutter, String poisenIvyPutter) {
        cut_grass = shortGrassPutter;
        long_grass = longGrassPutter;
        poisen_ivy = poisenIvyPutter;
    }
}