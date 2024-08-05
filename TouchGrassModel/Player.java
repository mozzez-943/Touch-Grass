package TouchGrassModel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import views.TouchGrassView;
import java.util.*;

/** Player class to handle all attributes, actions, and methods associated with the player */
public class Player {
    // Variables
    private int row;
    private int column;
    private float healthPoints;
    private ImageView playerImage;
    private Money money;
    public HashMap<Item, String> inventory;

    // Constructor
    public Player(double size, int initialRow, int initialColumn) {
        // Initialize position
        this.row = initialRow;
        this.column = initialColumn;
        Image playerImage = new Image("assets/player_skin.png");
        this.playerImage = new ImageView(playerImage);
        this.playerImage.setFitHeight(size);
        this.playerImage.setFitWidth(size);
        this.healthPoints = 100;
        this.money = Money.getInstance(0);
        this.inventory = new HashMap<Item, String>();

    }

    // Fetch the current row of the player
    public int getRow() {
        return row;
    }

    // Set the current row of the player
    public void setRow(int row) {
        this.row = row;
    }

    // Fetch the current column of the player
    public int getColumn() {
        return column;
    }

    // Fetch the image of the player
    public ImageView getPlayerImage() {
        return playerImage;
    }

    // Set the current column of the player
    public void setColumn(int column) {
        this.column = column;
    }

    // Set the players current balance
    public void setPlayerMoney(int money){
        this.money.setMoneyBalance(money);
    }

    // Fetch the players current balance
    public int getPlayerMoney(){
        return this.money.getMoneyBalance();
    }

    // Add money to the players balance
    public void addPlayerMoney(int money){
        this.money.addBalance(money);
    }

    // Remove money from the players balance
    public void removePlayerMoney(int money) {
        this.money.removeBalance(money);
    }

    // Fetch the player's HP
    public float getHP() {
        return this.healthPoints;
    }

    // Set the players HP
    public void setHP(float healthPoints) {
        this.healthPoints = healthPoints;
    }

    // Reduce player hp by set amount
    public void takeDamage(float damage) {
        this.healthPoints = Math.max(0, this.healthPoints - damage);
        // Check if health is 0 or less
        if (this.healthPoints <= 0) {
            // Create and display a JavaFX alert
            TouchGrassView.displayDeathMessage();
            TouchGrassView.stopTimer();
        }
    }

    // Add health to the players HP
    public void healPlayer(float heal) {
        this.healthPoints = Math.min(this.healthPoints + heal, 100);
    }

    // Add item to players inventory
    public void addToInventory(Item object, String path) {
        this.inventory.put(object, path);
    }

    // Remove item from players inventory
    public void removeFromInventory(Item object) {
        this.inventory.remove(object);
    }

    // Fetch object by object name
    public List<Object> getObject(String objectName) {
        for (Map.Entry<Item, String> entry : inventory.entrySet()) {
            if (Objects.equals(entry.getKey().getName(), objectName)) {
                return Arrays.asList(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }

    // Check if player already owns item
    public boolean checkIfObjectInInventory(String s) {
        for (Map.Entry<Item, String> entry : this.inventory.entrySet()) {
            Item item = entry.getKey();
            if (item.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public int getMoneyBalance() {
        return this.money.getMoneyBalance();
    }

    public double getHealthPoints() {
        return this.healthPoints;
    }
}
