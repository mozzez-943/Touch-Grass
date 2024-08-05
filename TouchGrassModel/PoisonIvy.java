package TouchGrassModel;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/** Subclass of Foliage Generation, to handle Poison Ivy actions. Methods in this class override the classes
 * in the Foliage Gen class. */
public class PoisonIvy extends FoliageGeneration {
    // Generate poison Ivy
    @Override
    public void foliageGeneration(Tile tile) {
        tile.setImage(poisen_ivy);
        tile.setStatus(2);
    }

    // Cut poison Ivy
    @Override
    public void cutFoliage(Tile tile, Player player) {
        // Update tile
        tile.setImage(cut_grass);
        tile.setStatus(0);
        tile.setTimer();

        // Audio
        Media cutSound = new Media(new File("assets/boom_edit.mp3").toURI().toString());
        MediaPlayer cutSoundPlayer = new MediaPlayer(cutSound);
        cutSoundPlayer.setVolume(0.5);

        cutSoundPlayer.stop();
        cutSoundPlayer.play();

        // Update player
        player.takeDamage(10.0F);
    }
}
