package TouchGrassModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/** Subclass of Foliage Generation, to handle Grass actions. Methods in this class override the classes
 * in the Foliage Gen class. */
public class Grass extends FoliageGeneration {
    // Generate Long Grass
    @Override
    public void foliageGeneration(Tile tile) {
        tile.setImage(long_grass);
        tile.setStatus(1);
    }

    // Cut Grass
    @Override
    public void cutFoliage(Tile tile, Player player) {
        // Update tile
        tile.setImage(cut_grass);
        tile.setStatus(0);
        tile.setTimer();

        // Audio
        Media cutSound = new Media(new File("assets/bush-cut-103503.mp3").toURI().toString());
        MediaPlayer cutSoundPlayer = new MediaPlayer(cutSound);
        cutSoundPlayer.setVolume(0.5);

        // Play cut sound
        cutSoundPlayer.stop();
        cutSoundPlayer.play();

    }
}
