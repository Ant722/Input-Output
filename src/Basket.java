import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.*;


public class Basket implements Serializable{
    private final Food[] products;
    private int [] quantity;
    private int sumProducts;

    public Basket(Food[] foods){
        quantity = new int[foods.length];
        this.products = foods.clone();

    }
    public void addToCart(int productNum, int amount){
        quantity[productNum] = quantity[productNum] + amount;
        products[productNum].setQuantity(amount);
    }
    public void printCart() {
        System.out.println("Ваша корзина: ");
        for (int i = 0; i < products.length; i++) {
            if (quantity[i] > 0) {
                System.out.println(products[i].getName() + " " + products[i].getQuantity() + " шт " + products[i].getPrices() +
                        " руб/шт " + (products[i].getPrices() * quantity[i]) + " руб в сумме.");
            }
            sumProducts = sumProducts + (products[i].getPrices() * quantity[i]);
        }
        System.out.println("Итого " + sumProducts + " руб");

    }

    public void saveTxt(File textFile) throws IOException {
        PrintWriter out = new PrintWriter(textFile);
        try {

            for (int i = 0; i < products.length; i++) {
                out.write(products[i].getName() + "/" + products[i].getQuantity() + "/[шт]/" + products[i].getPrices() +
                        "/[руб.шт]/" + (products[i].getPrices() * quantity[i]) + "/[руб в сумме]." + "\n");
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
            if(str.length > 5){
                name = str[0];
                price = parseInt(str[3]);
                quantity = parseInt(str[1]);
                foods.add(new Food(name, price, quantity));
            }
        }
        return new Basket(foods.toArray(Food[]::new));
    }
    public void saveBin(File file) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        Basket basket1;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){

            basket1 = (Basket) inputStream.readObject();
        }
        return new Basket(basket1.products);
    }

}