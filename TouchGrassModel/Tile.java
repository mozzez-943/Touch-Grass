package TouchGrassModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Tile class sued to manage tile status', construct tiles for the grid and update the game state */
public class Tile extends ImageView {
    // Variables per tile
    int row;
    int column;
    int status;
    int timer;

    // Constructor for tile
    public Tile(String imagePath, double size, int row, int column) {
        Image image = new Image(imagePath);
        this.setImage(image);
        this.setFitWidth(size);
        this.setFitHeight(size);
        this.row = row;
        this.column = column;
    }

    // Print the tile, for testing purposes
    public void printTile() {
        System.out.print("[" + this.row + "," + this.column + "]");
    }

    // Fetch the row of the tile
    public int getRow() {
        return row;
    }

    // Fetch the column of the tile
    public int getCol() {
        return column;
    }

    // Set the status of the tile, 0 -> cut | 1 -> long grass | 2 -> Poison Ivy
    public void setStatus(int newStatus) {
        this.status = newStatus;
    }

    // Set the image of the tile, used to update the view upon a player action
    public void setImage(String newImagePath) {
        Image newImage = new Image(newImagePath);
        this.setImage(newImage);
    }

    // Fetch the status of the tile
    public int getStatus() {
        return status;
    }

    // Set the random timer for tile regrowth
    public void setTimer() {
        Random random = new Random();
        int randomTime = random.nextInt(40) + 20;
        this.timer = randomTime;
    }

    // Increment the counter down 1
    public void decreaseTimer() {
        if (timer > 0) {
            timer--;
            status = 0;
        }
    }

    // Fetch the timer
    public int getTimer() {
        return timer;
    }
}
