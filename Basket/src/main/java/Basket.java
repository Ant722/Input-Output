import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.*;

import static java.lang.Integer.*;


public class Basket implements Serializable {
    static final long serialVersionUID = 1L;
    private final Food[] products;

    private int sumProducts;

    public Basket(Food[] foods) {
        this.products = foods.clone();
    }


    public void addToCart(int productNum, int amount) {
        products[productNum].setQuantity(amount);
        sumProducts += products[productNum].getPrices() * amount;
    }

    public void printCart() {
        System.out.println("Ваша корзина: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i].toString());

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

    public void saveJson(File textFile) throws IOException {

        try (FileWriter writer = new FileWriter(textFile)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(this,Basket.class));
        }

    }

    public static Basket loadFromJsonFile(File file) throws IOException, ParseException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileReader reader = new FileReader(file);
        return gson.fromJson(reader, Basket.class);


    }

}