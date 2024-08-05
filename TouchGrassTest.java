import java.io.IOException;
import java.io.IOException;

import TouchGrassModel.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// The majority of the testing was done visually due to the nature of the game.

public class TouchGrassTest {
    @Test
    void defaultTextureTest() {
        assertEquals("./assets/Grass_002.png", FoliageGeneration.cut_grass);
        assertEquals("./assets/long_grass1.png", FoliageGeneration.long_grass);
        // Test other default textures...
    }

    @Test
    void itemCreationTest() {
        Item item = new Item("TestItem", "This is a test item", 100);
        assertEquals("TestItem", item.getObjectName());
        assertEquals("This is a test item", item.getDescription());
        assertEquals(100, item.getPrice());
    }

    @Test
    void itemShopInitializationTest() {
        ItemShop shop = new ItemShop();
        assertNotNull(shop.getInventory());
        // Assuming there is a getInventory method
    }


    @Test
    void moneyOperationTest() {
        Money money = new Money(100);
        assertEquals(100, money.getAmount());
        // Assuming Money has a constructor with amount and a getAmount method
    }

    @Test
    void itemCopyConstructorTest() {
        Item originalItem = new Item("TestItem", "This is a test item", 100);
        Item copiedItem = new Item(originalItem);
        assertEquals("TestItem", copiedItem.getObjectName());
        assertEquals("This is a test item", copiedItem.getDescription());
        assertEquals(100, copiedItem.getPrice());
        assertNotSame(originalItem, copiedItem); // Ensure they are not the same object
    }

    @Test
    void getItemFromInventoryTest() {
        ItemShop shop = new ItemShop();
        Item item = new Item("TestItem", "Description", 100);
        shop.addShopInventory(item, "/assets/Grass_002.png");
        Item retrievedItem = shop.getItem("TestItem");
        assertEquals(item, retrievedItem);
        // Assuming there is a getItem method that retrieves an item by name
    }




}
