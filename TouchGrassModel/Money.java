package TouchGrassModel;

/** Money class to manage and interact with the players money */
public class Money {
    // Variables
    private static Money instance;
    private int globalMoney;

    // Money constructor
    public Money(int globalMoney){
        this.globalMoney = globalMoney;
    }

    // Fetch an instance of money
    public static Money getInstance(int globalMoney){
        if (instance == null){
            instance = new Money(globalMoney);
        }
        return instance;
    }

    // Fetch the current balance
    public int getMoneyBalance(){
        return globalMoney;
    }

    // Add money to the balance
    public void addBalance(int amount){
        this.globalMoney += amount;
    }

    // Remove money from the balance
    public void removeBalance(int amount){
        this.globalMoney -= amount;
    }

    // Set the balance to a specific amount
    public void setMoneyBalance(int money) {
        this.globalMoney = money;
    }

    public int getAmount() {
        return globalMoney;
    }
}
