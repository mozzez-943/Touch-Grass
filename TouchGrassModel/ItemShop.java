package TouchGrassModel;

import java.util.*;
/** ItemShop class used to manage all the items. */
public class ItemShop {
    // Variables
    public HashMap<Item, String> shopInventory;

    // ItemShop constructor
    public ItemShop() {
        this.shopInventory = new HashMap<Item, String>();
    }

    // Add item to shop inventory
    public void addShopInventory(Item object, String path) {
        this.shopInventory.put(object, path);
    }

    // Change the path of an item
    public void changePath(Item item, String s) {
        try {
            // Attempt to update the string associated with the item
            shopInventory.put(item, s);
        } catch (NullPointerException e) {
            // Handle the case where the item does not exist in the shop inventory
            System.out.println("Item not found in inventory");
        }
    }

    // Fetch object by name, returns a List
    public List<Object> getObject(String objectName) {
        for (Map.Entry<Item, String> entry : shopInventory.entrySet()) {
            if (Objects.equals(entry.getKey().getName(), objectName)) {
                return Arrays.asList(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }

    // Also fetch object by name, but returns an Item
    public Item getItem(String objectName) {
        for (Map.Entry<Item, String> entry : shopInventory.entrySet()) {
            if (Objects.equals(entry.getKey().getName(), objectName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Object getInventory() {
        return shopInventory;
    }
}

