package TouchGrassModel;

/** Item class used to construct and manage the shop and player inventory. */
public class Item {
    // Variables
    private final String objectName;
    private final String description;
    private final Integer price;

    // Item constructor
    public Item(String name, String description, Integer price){
        this.objectName = name;
        this.description = description;
        this.price = price;
    }

    // A copy constructor to allow for item duplications with different internal IDs
    public Item(Item anotherItem){
        this.objectName = anotherItem.objectName;
        this.description = anotherItem.description;
        this.price = anotherItem.price;
    }

    // Fetch the name of the item
    public String getName(){
        return this.objectName;
    }

    // Fetch the description of the item
    public String getDescription(){
        return this.description;
    }

    // Fetch the price of the item
    public int getPrice(){
        return this.price;
    }

    public String getObjectName() {
        return objectName;
    }
}