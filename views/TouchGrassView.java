package views;

import TouchGrassModel.TouchGrassModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.animation.PauseTransition;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.scene.text.Text;
import javafx.scene.AccessibleRole;


import TouchGrassModel.Tile;
import TouchGrassModel.FoliageGeneration;
import TouchGrassModel.Grass;
import TouchGrassModel.PoisonIvy;
import TouchGrassModel.Item;
import javafx.util.Duration;

import java.io.File;

import javafx.scene.media.MediaView;

import java.util.Random;
import java.util.*;

/** Main view for the Touch Grass Game/Model. Handles all the UI elements and all frontend. */
public class TouchGrassView {
    static TouchGrassModel model; //model of the game
    static Timeline timeline;
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton, Red_Green_Filter, Enhanced_Border, Sensory_Expereience;
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    HBox objectsInInventory; //to hold inventory items
    GridPane objectsInShop;
    ImageView roomImageView; //to hold room image
    private Scene scene;
    private static MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    private Label showingInstructionsWindow;
    private GridPane playableGrid;
    private Button selectTrackDir1Button;
    private BorderPane root;
    private GridPane gridForButtons = new GridPane();
    private boolean shiftPressed = false;
    private List<MediaPlayer> mediaPlayers = new ArrayList<>();
    private ColorblindModeState state;



    /**
     * Touch Grass Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public TouchGrassView(TouchGrassModel model, Stage stage) {
        this.model = model;
        this.stage = stage;
        state = new NormalMode();
        intiUI();
    }

    // Fetch the help text for the game
    private static Text getText() {
        Text text = new Text("Welcome to TouchGrass!\n\nThis is a Speed Run Adventure Game.\n\nYour goal is " +
                "to purchase the Winning Trophy as quickly as possible from the Shop. \nYou can achieve this by mowing as " +
                "much grass as you can in the shortest time possible. \nBut be careful! Running into Poison Ivy will cause damage. " +
                "Try your best to avoid it :)\n\nYou can also buy Health Potions from the Shop to restore health and acquire the Diamond Hoe to earn money at a faster rate.\nGood luck!");
        text.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    // Stop the speed run timer
    public static void stopTimer() {
        timeline.stop();
    }

    // Display pop-up window for when the user health = 0
    public static void displayDeathMessage() {
        ImageView gifView = new ImageView(new Image("assets" + File.separator + "skeleton-skeleton-meme.gif"));
        StackPane layout = new StackPane();
        layout.getChildren().add(gifView);
        Scene scene = new Scene(layout, 250, 400); // Adjust size as needed
        Stage stage = new Stage();
        stage.setTitle("Game Over");
        stage.setScene(scene);
        stage.show();
        stopTimer();
        mediaPlayer.stop();
        // create a restart button
        Button restartButton = new Button("Restart");
        restartButton.setPrefSize(100, 50);
        restartButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        // create an exit button
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(100, 50);
        exitButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        restartButton.setOnAction(e -> {

            // close the stage
            stage.close();
            // restart the game
            model.restartGame();
        });
        // set the exit button to close the stage and exit the game
        exitButton.setOnAction(e -> {
            // close the stage
            stage.close();
            // exit the game
            Platform.exit();
        });
        // add the restart and exit buttons to the layout
        layout.setAlignment(Pos.CENTER);
        // the insets move the buttons away from the edges of the window
        // v1 is the top, v2 is the right, v3 is the bottom, v4 is the left
        layout.setMargin(restartButton, new Insets(0, 0, 60, 0));
        layout.setMargin(exitButton, new Insets(60, 0, 0, 0));
        layout.getChildren().addAll(restartButton, exitButton);
    }

    // Function to help make buttons accessible, changes the style of the buttons
    public static void makeButtonAccessible(Button inputButton, String name) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {
        root = new BorderPane();
        // Get the screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        StackPane healthBarPane = new StackPane(model.healthBar);

        // Instantiate buttons before adding them
        gridForButtons.setAlignment(Pos.CENTER);
        gridForButtons.setHgap(10);
        gridForButtons.setVgap(10);
        // Red/Green colourblind filter button
        Red_Green_Filter = createButton("Red/Green Filter", 1, 0, "assets/redgreen_button.png");
        helpButton = createButton("Help", 2, 0, "assets/help_button.png");
        addHelpEvent();
        // Enhanced border and sensory experience button
        Enhanced_Border = createButton("Enhanced Borders", 3, 0, "assets/enhanceborder_button.png");
        Sensory_Expereience = createButton("Sensory Experience", 4, 0, "assets/sensory_button.png");
        Text healthBarText = new Text("Your Health Bar:");
        healthBarText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        // Speed run timer
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> model.updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Grid itself
        playableGrid = model.gridmodel.getGridPane();
        playableGrid.setFocusTraversable(true);
        playableGrid.setId("playableGrid");

        // Instantiate new buttons for selecting tracks
        selectTrackDir1Button = new Button("Next track from Jukebox");
        selectTrackDir1Button.setFocusTraversable(false);

        // Set actions for the buttons
        selectTrackDir1Button.setOnAction(event -> selectRandomTrack("juke1"));

        // Create an VBox and put the playable grid in it
        VBox vboxForGrid = new VBox(playableGrid);
        vboxForGrid.setFillWidth(true); // This makes the VBox stretch its children to fill its width

        // Center the grid within the VBox
        vboxForGrid.setAlignment(Pos.CENTER);
        vboxForGrid.backgroundProperty().setValue(new Background(new BackgroundFill(Color.web("#2EAEE1"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Add the HBox to the BorderPane
        root.setCenter(vboxForGrid);

        //Making RightPane To store all the info
        VBox vboxrightPane = new VBox();
        StackPane speedRunTimer = new StackPane(model.speedRunTimer);
        speedRunTimer.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid; -fx-background-color: #2EAEE1;");
        StackPane playerBalanceLabel = new StackPane(model.playerBalanceLabel);
        playerBalanceLabel.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid; -fx-background-color: #2EAEE1;");

        // Player inventory
        Text playerInventoryLabel = new Text("Your Inventory:");
        playerInventoryLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        objectsInInventory = new HBox(10);
        Text shopInventoryLabel = new Text("Shop Items For Sale!");
        shopInventoryLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
        objectsInShop = new GridPane();
        objectsInShop.setAlignment(Pos.CENTER);

        // Item attributes
        Item healthPotion = new Item("Health Potion", "Regular Health Potion\nRestore Player's Health by 25%", 100);
        Item goldenPotion = new Item("Golden Potion", "Super healing potion\nFully Restores Player's Health to 100%", 250);
        Item diamondHoe = new Item("Diamond Hoe", "Rewards 5x More Money \nNever Waste Your Diamonds on a Hoe...", 500);
        Item trophy = new Item("Winning Trophy", "Get this, win the game", 1000);
        model.itemShop.addShopInventory(healthPotion, "assets/Health-potion.png");
        model.itemShop.addShopInventory(goldenPotion, "assets/Golden Potion.png");
        model.itemShop.addShopInventory(diamondHoe, "assets/diamond_hoe.png");
        model.itemShop.addShopInventory(trophy, "assets/Winning-Trophy.png");
        //Alter how much money the player starts with
        model.player.addPlayerMoney(0);
        model.updateMoneyDisplay();
        updateItems();

        vboxrightPane.getChildren().addAll(healthBarText, healthBarPane, gridForButtons, speedRunTimer, playerBalanceLabel,
                playerInventoryLabel, objectsInInventory, shopInventoryLabel, objectsInShop,
                selectTrackDir1Button);

        vboxrightPane.setAlignment(Pos.TOP_CENTER);
        vboxrightPane.setSpacing(14);
        vboxrightPane.backgroundProperty().setValue(new Background(new BackgroundFill(Color.web("#D9DDDC"), CornerRadii.EMPTY, Insets.EMPTY)));

        root.setRight(vboxrightPane);
        root.backgroundProperty().setValue(new Background(new BackgroundFill(Color.web("#D9DDDC"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Add the BorderPane to the scene
        scene = new Scene(root, screenBounds.getWidth() * 0.75, screenBounds.getHeight() * 0.75);

        addKeyHandlingEvent();
        GridPane.setHalignment(model.player.getPlayerImage(), HPos.CENTER);
        GridPane.setValignment(model.player.getPlayerImage(), VPos.CENTER);
        this.stage.setScene(scene);

        initAudio();
        this.stage.show();
        timeline.play();

    }

    // Helper method to create and configure buttons
    private Button createButton(String text, int col, int row, String image_path) {
        Button button = new Button(text);
        button.setAlignment(Pos.CENTER);
        gridForButtons.add(button, col, row);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        button.setFocusTraversable(false);
        button.setStyle("-fx-background-image: url('" + image_path + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-radius: 100em; " +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center;" +
                "-fx-border: none;" + // Remove border if it's not part of the design
                "-fx-text-fill: transparent;" + // Hide the text if you only want the image
                "-fx-pref-width: 100;" + // Set the width as per your image size
                "-fx-pref-height: 100;" + // Set the height as per your image size
                // Add the focus style below
                "-fx-focus-color: #00008B;" + // Replace #color with your desired color code
                "-fx-faint-focus-color: #00008B;" + // Same color for a thicker line
                "-fx-focus-radius: 100px;"); // Adjust the size to make the focus outline thicker
        return button;
    }
    // Display pop-up window showing the help text
    public void addHelpEvent() {
        helpButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Game Help");
            VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER);
            Text text = getText();
            layout.getChildren().add(text);
            Scene scene = new Scene(layout, 1150, 350);
            stage.setScene(scene);
            stage.show();
        });
    }

    // Method to select a random track from the given directory
    private void selectRandomTrack(String directory) {
        // Replace "dir1" and "dir2" with the actual paths to your directories
        File dir = new File("assets/audio/" + directory);
        File[] files = dir.listFiles();

        // Select a random file from the directory
        Random random = new Random();
        File randomFile = files[random.nextInt(files.length)];
        playAudio(randomFile.getAbsolutePath());
    }

    // Method to play the specific audio
    private void playAudio(String audioFilePath) {
        // Stop the currently playing audio (if any)
        mediaPlayer.stop();
        mediaPlaying = false;
        Media audio = new Media(new File(audioFilePath).toURI().toString());
        mediaPlayer = new MediaPlayer(audio);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.volumeProperty().setValue(0.2);
        mediaPlayer.play();
        mediaPlaying = true;
    }

    // Initialize the first audio track when the game first starts
    private void initAudio() {
        String audioFile = "assets/audio/juke1/2019-01-02_-_8_Bit_Menu_-_David_Renda_-_FesliyanStudios.com.mp3";
        Media audio = new Media(new File(audioFile).toURI().toString());
        mediaPlayer = new MediaPlayer(audio);

        // loop song
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.volumeProperty().setValue(0.2);

        mediaPlayer.play();
        mediaPlaying = true;
    }

    // Main function to update model/view when player moves
    private void addKeyHandlingEvent() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                shiftPressed = true;
                setButtonsFocusTraversable(true);
            } else if (e.getCode().isArrowKey()) {
                if (!shiftPressed) {
                    playableGrid.requestFocus();
                    e.consume(); // Consume the event to prevent other handlers from executing
                    // Player movement cases
                    switch (e.getCode()) {
                        case UP:
                            if (model.player.getRow() > 0) model.movePlayerUp();
                            break;
                        case DOWN:
                            if (model.player.getRow() < model.gridmodel.getGridVertical() - 1) model.movePlayerDown();
                            break;
                        case LEFT:
                            if (model.player.getColumn() > 0) model.movePlayerLeft();
                            break;
                        case RIGHT:
                            if (model.player.getColumn() < model.gridmodel.getGridHorizontal() - 1)
                                model.movePlayerRight();
                            break;
                    }
                    // Update the player's position in the grid
                    GridPane.setRowIndex(model.player.getPlayerImage(), model.player.getRow());
                    GridPane.setColumnIndex(model.player.getPlayerImage(), model.player.getColumn());
                    Platform.runLater(() -> {
                        // Iterate through all tiles in the grid and decrease their timers
                        for (int row = 0; row < model.gridmodel.getGridVertical(); row++) {
                            for (int col = 0; col < model.gridmodel.getGridHorizontal(); col++) {
                                Node tileNode = findTileNode(row, col);
                                // Time for the tile to regenerate
                                if (tileNode != null && tileNode instanceof Tile) {
                                    Tile tile = (Tile) tileNode;
                                    tile.decreaseTimer();

                                    // Time for foliage gen
                                    if (tile.getTimer() == 0 && tile.getStatus() == 0) {
                                        double probability = 0.1; // Probability of poisonIvy Gen
                                        // Generate Poison Ivy
                                        if (Math.random() < probability) {
                                            new PoisonIvy().foliageGeneration(tile);
                                        } else { // Generate Grass
                                            new Grass().foliageGeneration(tile);
                                        }
                                    }
                                }
                            }
                        }
                        // Helper function to find the tile that the player is currently on
                        Node tileNode = findTileNode(model.player.getRow(), model.player.getColumn());
                        // Cut foliage
                        if (tileNode != null && tileNode instanceof Tile) {
                            Tile tile = (Tile) tileNode;
                            // Long Grass
                            if (tile.getStatus() == 1) {
                                FoliageGeneration grass = new Grass();
                                grass.cutFoliage(tile, model.player);
                                // Give more money to player if player owns special tool
                                if (model.player.checkIfObjectInInventory("Diamond Hoe")) {
                                    model.player.addPlayerMoney(5);
                                    model.updateMoneyDisplay();
                                } else {
                                    model.player.addPlayerMoney(1);
                                    model.updateMoneyDisplay();
                                }
                            }
                            // Poison Ivy
                            else if (tile.getStatus() == 2) {
                                FoliageGeneration poisonIvy = new PoisonIvy();
                                poisonIvy.cutFoliage(tile, model.player);
                                model.updateHealthBar();
                                // Give more money to player if player owns special tool
                                if (model.player.checkIfObjectInInventory("Diamond Hoe")) {
                                    model.player.addPlayerMoney(25);
                                    model.updateMoneyDisplay();
                                } else {
                                    model.player.addPlayerMoney(5);
                                    model.updateMoneyDisplay();
                                }
                            }
                        }
                    });

                }
            }
        });
        // Buttons accessibility keyboard only function
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                shiftPressed = false;
                // Disable focus traversable for buttons when shift is released
                setButtonsFocusTraversable(false);
            }
        });
        // Red/Green colourblind filter enable/disable function
        Red_Green_Filter.setOnAction(e -> {
            e.consume();
            toggleColorblindMode();
        });
        // Enhanced borders enable/disable function
        Enhanced_Border.setOnAction(e -> {
            e.consume();
            if (Enhanced_Border.getText().equals("Enhanced Borders")) {
                Enhanced_Border.setText("No Borders");

                model.gridmodel.changeTileImage("assets/Grass_002border.png", "assets/long_grass1border.png", "assets/poisenivy1border.png");

            } else {
                Enhanced_Border.setText("Enhanced Borders");
                model.gridmodel.changeTileImage("assets/Grass_002.png", "assets/long_grass1.png", "assets/poisenivy2.png");
            }
        });
        // Sensory experience enable/disable function
        Sensory_Expereience.setOnAction(e -> {
            if (Sensory_Expereience.getText().equals("Sensory Experience")) {
                Sensory_Expereience.setText("Normal");
                startSensoryExperience();
            } else {
                Sensory_Expereience.setText("Sensory Experience");
                stopAndClearMediaPlayers();
            };
        });
    }

    // Function to update shop and player inventory items
    public void updateItems() {
        //To create and populate inventory slots
        objectsInInventory.getChildren().clear();
        helperPlayerInvUpdateItems();
        objectsInShop.getChildren().clear();
        objectsInShop.setHgap(10); // Horizontal gap
        helperShopInvUpdateItems();
    }

    // Helper function to update styling and model for player inventory
    private void helperPlayerInvUpdateItems() {
        for (HashMap.Entry<Item, String> object : model.player.inventory.entrySet()) {
            Item key = object.getKey();
            String value = object.getValue();
            StackPane slot = new StackPane();
            ImageView itemImage = new ImageView(new Image(value));
            itemImage.setFitWidth(100);  // Set the fit width to 50 pixels
            itemImage.setFitHeight(100); // Set the fit height to 50 pixels
            Text itemLabel = new Text(key.getName());
            itemLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: red;");
            itemLabel.setTranslateY(60);
            slot.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #ffffff;");
            slot.setId(key.getName());
            slot.getChildren().addAll(itemImage, itemLabel);
            objectsInInventory.getChildren().add(slot);
            objectsInInventory.setMaxSize(50, 50);
            //event handler for left-click on the player inventory
            slot.setOnMouseClicked((event) -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Item temp = (Item) model.player.getObject(slot.getId()).get(0);
                    //ONLY FOR HEALTH POTS
                    if (Objects.equals(temp.getName(), "Health Potion")) {
                        model.player.healPlayer(25);
                        model.updateHealthBar();
                        model.player.removeFromInventory(temp);
                    } else if (Objects.equals(temp.getName(), "Golden Potion")) {
                        model.player.healPlayer(100);
                        model.updateHealthBar();
                        model.player.removeFromInventory(temp);
                    }
                    updateItems();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    displayItemDescription(slot.getId());
                }
            });
        }
        //To create the rest of the empty inventory slots
        for (int i = 0; i < (4 - model.player.inventory.size()); i++) {
            StackPane slot = new StackPane();
            ImageView itemImage = new ImageView(new Image("assets/Blank.png"));
            itemImage.setFitWidth(100);
            itemImage.setFitHeight(100);
            Text itemLabel = new Text();
            itemLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: red;");
            itemLabel.setTranslateY(60);
            slot.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #ffffff;");
            slot.getChildren().addAll(itemImage, itemLabel);
            objectsInInventory.getChildren().add(slot);
            objectsInInventory.setMaxSize(50, 50);
        }
    }
    // Helper function to update style and model for item shop
    private void helperShopInvUpdateItems() {
        Iterator<Map.Entry<Item, String>> iterator = model.itemShop.shopInventory.entrySet().iterator();
        for (int i = 0; i < 4; i++) {
            VBox outerBox = new VBox(5); // Spacing between image and labels
            StackPane imageStackPane = new StackPane();
            Map.Entry<Item, String> entry = iterator.next();
            ImageView imageView = new ImageView(new Image(entry.getValue(), 100, 100, false, false));
            Text label = new Text(entry.getKey().getName());
            label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: black;");
            label.setWrappingWidth(100);
            label.setTextAlignment(TextAlignment.CENTER);
            Text itemMoney = new Text("$" + entry.getKey().getPrice());
            itemMoney.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-fill: #1fd40b;");
            itemMoney.setWrappingWidth(100);
            itemMoney.setTextAlignment(TextAlignment.CENTER);
            // Creating a VBox for label and money
            VBox labelBox = new VBox(4); // Spacing between label and money
            labelBox.setAlignment(Pos.CENTER);
            labelBox.getChildren().addAll(label, itemMoney);
            imageStackPane.setAlignment(Pos.CENTER);
            imageStackPane.setPrefSize(100, 100);
            imageStackPane.getChildren().add(imageView);
            imageStackPane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #ffffff;");
            // Creating an outer VBox for image and label box
            outerBox.setAlignment(Pos.CENTER);
            outerBox.getChildren().addAll(imageStackPane, labelBox);
            imageStackPane.setId(entry.getKey().getName());
            imageStackPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    displayItemDescription(imageStackPane.getId()); // To display item description
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    List<Object> result = model.itemShop.getObject(imageStackPane.getId());
                    Item temp = (Item) result.get(0);
                    if (Objects.equals(temp.getName(), "Health Potion") && model.player.inventory.size() < 4) {
                        if (model.player.getPlayerMoney() > 100) {
                            model.player.addToInventory(new Item((Item) result.get(0)), (String) result.get(1));
                            model.player.removePlayerMoney(100);
                            model.updateMoneyDisplay();
                            updateItems();
                        } else {
                            displayBrokeAlert();
                        }
                    } else if (Objects.equals(temp.getName(), "Golden Potion") && model.player.inventory.size() < 4) {
                        if (model.player.getPlayerMoney() > 250) {
                            model.player.addToInventory(new Item((Item) result.get(0)), (String) result.get(1));
                            model.player.removePlayerMoney(250);
                            model.updateMoneyDisplay();
                            updateItems();
                        } else {
                            displayBrokeAlert();
                        }
                    } else if (Objects.equals(temp.getName(), "Diamond Hoe") && !model.player.checkIfObjectInInventory("Diamond Hoe") && model.player.inventory.size() < 4) {
                        if (model.player.getPlayerMoney() > 500) {
                            model.player.addToInventory(new Item((Item) result.get(0)), (String) result.get(1));
                            model.player.removePlayerMoney(500);
                            model.itemShop.changePath((Item) result.get(0), "assets/diamond_hoe_sold.png");
                            model.updateMoneyDisplay();
                            updateItems();
                        } else {
                            displayBrokeAlert();
                        }
                    } else if (Objects.equals(temp.getName(), "Winning Trophy") && !model.player.checkIfObjectInInventory("Winning Trophy") && model.player.inventory.size() < 4) {
                        if (model.player.getPlayerMoney() > 1000) {
                            model.player.addToInventory(new Item((Item) result.get(0)), (String) result.get(1));
                            model.itemShop.changePath((Item) result.get(0), "assets/Winning-Trophy-sold.png");
                            model.player.removePlayerMoney(1000);
                            model.updateMoneyDisplay();
                            updateItems();
                            timeline.stop();
                            displayWinningGame();
                        } else {
                            displayBrokeAlert();
                        }
                    }
                }
            });
            objectsInShop.add(outerBox, i, 1);
        }
    }
    // Display pop-up window showing the item description
    private void displayItemDescription(String s) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Item Information");
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Item temp = (Item) model.itemShop.getObject(s).get(0);
        Text text = new Text("Information about " + s + "\n\n\n" + temp.getDescription());
        text.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Comic Sans MS'; -fx-fill: black;");
        text.setTextAlignment(TextAlignment.CENTER);
        layout.getChildren().add(text);
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    // Helper function to find the tile that the player is currently on
    private Node findTileNode(int row, int column) {
        GridPane gridPane = model.gridmodel.getGridPane();
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeColumn = GridPane.getColumnIndex(node);
            // Verify that the node is valid and correct
            if (nodeRow != null && nodeColumn != null && nodeRow == row && nodeColumn == column) {
                // Node found at the specified row and column
                return node;
            }
        }
        return null;
    }

    // Function to display pop-up window when user wins the game
    public void displayWinningGame() {
        ImageView gifView = new ImageView(new Image("assets\\Win-cutscene2.gif"));
        // Create a StackPane and add the ImageView to it
        StackPane layout = new StackPane();
        layout.getChildren().add(gifView);
        // Create a Scene with the StackPane
        Scene scene = new Scene(layout, 400, 250); // Adjust size as needed
        // Create a Stage to display the Scene
        Stage stage = new Stage();
        stage.setTitle("You win!");
        stage.setScene(scene);
        // Display the Stage
        stage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            stage.close();
            this.stage.close();
        }); // Close the stage when the delay is over

        delay.play(); // Start the delay
    }

    // Function to display pop-up window when user attempts to purchase item but does not have sufficient funds
    public void displayBrokeAlert() {
        ImageView gifView = new ImageView(new Image("assets\\fool-poor.gif"));
        // Create a StackPane and add the ImageView to it
        StackPane layout = new StackPane();
        layout.getChildren().add(gifView);
        // Create a Scene with the StackPane
        Scene scene = new Scene(layout, 525, 400); // Adjust size as needed
        // Create a Stage to display the Scene
        Stage stage = new Stage();
        stage.setTitle("Broke Boy Alert");
        stage.setScene(scene);
        // Display the Stage
        stage.show();
    }

    // Function to update the styles when a button is selected for focus
    private void setButtonsFocusTraversable(boolean value) {
        helpButton.setFocusTraversable(value);
        Red_Green_Filter.setFocusTraversable(value);
        Enhanced_Border.setFocusTraversable(value);
        Sensory_Expereience.setFocusTraversable(value);
        selectTrackDir1Button.setFocusTraversable(value);
    }

    // Show the sensory experience videos
    private void startSensoryExperience() {
        VBox videoBox = new VBox(0); // Spacing between videos
        // List of media file paths
        List<String> mediaFiles = Arrays.asList(
                "/assets/Subway_Surfers.mp4",
                "/assets/satisfying_sand.mp4",
                "/assets/Minecraft_parkour.mp4",
                "/assets/THE_PLATYPUS!.mp4"
        );
        for (String filePath : mediaFiles) {
            Media media = new Media(getClass().getResource(filePath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayers.add(mediaPlayer);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(135);
            mediaView.setFitHeight(197);
            mediaView.setPreserveRatio(false);
            mediaPlayer.play();
            videoBox.getChildren().add(mediaView);
        }
        root.setLeft(videoBox);
    }

    // Stop and clear all media players so audio does not overlap
    private void stopAndClearMediaPlayers() {
        for (MediaPlayer player : mediaPlayers) {
            player.stop();
            player.dispose();
        }
        mediaPlayers.clear();
        root.setLeft(null); // Remove video box from the UI
    }

    public void setState(ColorblindModeState newState) {
        this.state = newState;
    }

    public void toggleColorblindMode() {
        state.toggleColorblindMode(this);
        // Additional code to refresh the view if necessary
    }

    public void updateToColorblindMode() {
        Red_Green_Filter.setText("No Filter");
        model.gridmodel.changeTileImage("assets/short_c2.png", "assets/c2_long.png", "assets/poisenivy2_colourblind.png");
    }

    public void updateToNormalMode() {
        Red_Green_Filter.setText("Red/Green Filter");
        model.gridmodel.changeTileImage("assets/Grass_002.png", "assets/long_grass1.png", "assets/poisenivy2.png");
    }
}
