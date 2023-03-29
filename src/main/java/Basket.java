import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.*;


public class Basket implements Serializable {

    private final Food[] products;
    private int[] quantity;
    private int sumProducts;

    public Basket(Food[] foods) {
        quantity = new int[foods.length];
        this.products = foods.clone();

    }

    public void addToCart(int productNum, int amount) {
        quantity[productNum] = quantity[productNum] + amount;
        products[productNum].setQuantity(amount);
    }

    public void printCart() {
        System.out.println("Ваша корзина: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i].toString());
            sumProducts = sumProducts + (products[i].getPrices() * products[i].getQuantity());
        }
        System.out.println("Итого " + sumProducts + " руб");

    }

    public void saveTxt(File textFile) throws IOException {
        PrintWriter out = new PrintWriter(textFile);
        try {

            for (int i = 0; i < products.length; i++) {
                out.write(products[i].getName() + "/" + products[i].getQuantity() + "/[шт]/" + products[i].getPrices() +
                        "/[руб.шт]/" + "\n");
            }
        } finally {
            out.close();
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws FileNotFoundException {
        Scanner scan = new Scanner(textFile);
        List<Food> foods = new ArrayList<>();
        String name;
        int price;
        int quantity;
        while (scan.hasNext()) {
            String[] str = scan.nextLine().split("/");
            if (str.length > 4) {
                name = str[0];
                price = parseInt(str[3]);
                quantity = parseInt(str[1]);
                foods.add(new Food(name, price, quantity));
            }
        }
        return new Basket(foods.toArray(Food[]::new));
    }

}