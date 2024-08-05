package TouchGrassModel;

import java.io.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import views.TouchGrassView;

/** Model for the Touch Grass game, contains all the details and status' of the game */
public class TouchGrassModel implements Serializable {
    // Variables of the game
    public Player player;
    public Grid gridmodel;
    public ItemShop itemShop;
    public ProgressBar healthBar;
    private Text labelText;
    private Text timeText;
    private int minutes = 0, seconds = 0, hundredths = 0;
    public TextFlow speedRunTimer;
    private Text labelText2;
    private Text moneyText;
    public TextFlow playerBalanceLabel;

    // Initialize the model and set up the game
    public TouchGrassModel() {
        try {
            setUpGame();
        } catch (Exception e) {
            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    // Restart the game from the beginning
    public void restartGame() {
        //start a new game
        TouchGrassModel model = new TouchGrassModel();
        model.player.setPlayerMoney(0);
        Stage stage = new Stage();
        stage.close();
        TouchGrassView view = new TouchGrassView(model, stage);
    }

    // Set up the game
    public void setUpGame() {
        labelText = new Text("Speedrun Timer: ");
        labelText.setStyle("-fx-font-size: 28px; -fx-fill: black;");
        timeText = new Text("00:00");
        timeText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        timeText.setTranslateY(3);
        speedRunTimer = new TextFlow(labelText, timeText);
        speedRunTimer.setTextAlignment(TextAlignment.CENTER);
        labelText2 = new Text("Current balance: ");
        labelText2.setStyle("-fx-font-size: 28px; -fx-fill: black;");
        moneyText = new Text("$0.00");
        moneyText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        moneyText.setTranslateY(3);
        playerBalanceLabel = new TextFlow(labelText2, moneyText);
        playerBalanceLabel.setTextAlignment(TextAlignment.CENTER);
        this.gridmodel = new Grid();
        this.healthBar = new ProgressBar(1.0);
        this.healthBar.setStyle("-fx-accent: #1fd40b;");
        this.healthBar.setPrefWidth(500);
        this.itemShop = new ItemShop();
        this.player = new Player((double) this.gridmodel.getCellSize(), 0, 0);
        this.gridmodel.getGridPane().getChildren().add(this.player.getPlayerImage());
    }

    // Update the speedrun timer
    public void updateTimer(){
        hundredths++;
        if (hundredths == 100) {
            seconds++;
            hundredths = 0;
        }
        if (seconds == 60) {
            minutes++;
            seconds = 0;
        }
        timeText.setText(String.format("%02d:%02d:%02d", minutes, seconds, hundredths));
    }

    // Update the money display
    public void updateMoneyDisplay(){
        moneyText.setText("$" + this.player.getPlayerMoney());
    }

    // Update the health bar of the player
    public void updateHealthBar() {
        // Ensure that the new health value is within the valid range [0.0, 1.0]
        // Update the ProgressBar with the new health value
        this.healthBar.setProgress(Math.max(0.0, Math.min(1.0, this.player.getHP() / 100.0)));
    }

    // Move the player up one tile
    public void movePlayerUp(){

        this.player.setRow(this.player.getRow() - 1);
    }

    // Move the player down one tile
    public void movePlayerDown(){

        this.player.setRow(this.player.getRow() + 1);
    }

    // Move the player left one tile
    public void movePlayerLeft(){

        this.player.setColumn(this.player.getColumn() - 1);
    }

    // Move the player right one tile
    public void movePlayerRight(){

        this.player.setColumn(this.player.getColumn() + 1);
    }

    public Object getPlayer() {
        return this.player;
    }

    public Object getGridmodel() {
        return this.gridmodel;
    }

    public Object getItemShop() {
        return this.itemShop;
    }

}
