import java.io.Serializable;

public class Food implements Serializable {
    private final String name;
    private final int prices;
    private int quantity = 0;

    public Food(String name, int prices) {
        this.name = name;
        this.prices = prices;
    }
    public Food(String name, int prices, int quantity){
        this.name = name;
        this.prices = prices;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrices() {
        return prices;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int amount) {

        this.quantity += amount;
    }
}
