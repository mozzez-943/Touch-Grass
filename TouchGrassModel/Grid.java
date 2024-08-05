package TouchGrassModel;

import javafx.scene.layout.*;

/** Grid class to create the grid composed of tiles. */
public class Grid{
    // Variables
    private static final int GRID_SIZE = 9;
    private static final int GRID_HORIZONTAL = 13;
    private static final int GRID_VERTICAL = 9;
    private static final int CELL_SIZE = 70;
    private Tile[][] tiles = new Tile[GRID_HORIZONTAL][GRID_VERTICAL];
    private GridPane gridPane = new GridPane();
    FoliageGeneration foliageGeneration;

    // Constructor for the grid class
    private void initializeGrid() {
        gridPane.setGridLinesVisible(true); // For debugging, can be removed later
        for (int i = 0; i < GRID_HORIZONTAL; i++) {
            for (int j = 0; j < GRID_VERTICAL; j++) {
                Tile tile = createGrassTile(i, j);
                tiles[i][j] = tile;
                gridPane.add(tile, i, j);
            }
        }
        gridPane.setMaxSize(GRID_HORIZONTAL * CELL_SIZE, GRID_VERTICAL * CELL_SIZE);
    }

    // Calls the constructor
    public Grid() {
        initializeGrid();
    }

    // Creates a tile of grass
    private Tile createGrassTile(int row, int column) {
        String imagePath = "assets/long_grass1.png";
        Tile tile = new Tile(imagePath, CELL_SIZE, row, column);
        tile.setStatus(1);
        return tile;
    }

    // Fetch a specific tile status
    public int getTileStatus(int row, int column) {
        return tiles[row][column].status;
    }

    // Set the status of a specific tile
    public void setTileStatus(int row, int column, int status) {
        tiles[row][column].setStatus(status);
        // update the image of the tile
        if (status == 0) {
            tiles[row][column].setImage("assets/Grass_002.png");
        } else if (status == 1) {
            tiles[row][column].setImage("assets/long_grass1.png");
        } else if (status == 2) {
            tiles[row][column].setImage("assets/poisenivy2.png");
        }
    }

    // Fetch the grid pane
    public GridPane getGridPane() {
        return gridPane;
    }

    // Fetch the horizontal attribute of the grid
    public int getGridHorizontal() {
        return GRID_HORIZONTAL;
    }

    // Fetch the vertical attribute of the grid
    public int getGridVertical() {
        return GRID_VERTICAL;
    }

    // Fetch the cell size attribute of the grid
    public int getCellSize() {
        return CELL_SIZE;
    }

    // Change the image of a specific tile
    public void changeTileImage(String shortGrassImagePath, String longGrassImagePath, String poisonIvyImagePath) {

        for(int i = 0; i < GRID_HORIZONTAL; i++) {
            for(int j = 0; j < GRID_VERTICAL; j++) {
                if (tiles[i][j].getStatus() == 0) {
                    tiles[i][j].setImage(shortGrassImagePath);
                } else if(tiles[i][j].getStatus() == 1) {
                    tiles[i][j].setImage(longGrassImagePath);
                } else if(tiles[i][j].getStatus() == 2) {
                    tiles[i][j].setImage(poisonIvyImagePath);
                }
            }
        }
        // Change the state of the colourblind setting
        foliageGeneration.colourblindsetting(shortGrassImagePath, longGrassImagePath, poisonIvyImagePath);
    }

    public int[] getTiles() {
        int[] tileStatus = new int[GRID_HORIZONTAL * GRID_VERTICAL];
        int index = 0;
        for(int i = 0; i < GRID_HORIZONTAL; i++) {
            for(int j = 0; j < GRID_VERTICAL; j++) {
                tileStatus[index] = tiles[i][j].getStatus();
                index++;
            }
        }
        return tileStatus;
    }
}
