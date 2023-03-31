import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.dsig.keyinfo.KeyName;
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
            Gson gson = new GsonBuilder().create();
            writer.write(gson.toJson(this,Basket.class));
        }

  /*      JSONObject basketJson = new JSONObject();
        JSONArray foodArrayJson = new JSONArray();
        for (Food food : products) {

                JSONObject foods = new JSONObject();
                foods.put("name", food.getName());
                foods.put("price", String.valueOf(food.getPrices()));
                foods.put("quantity", String.valueOf(food.getQuantity()));

                foodArrayJson.add(foods);

            basketJson.put("foodArrayJson", foodArrayJson);
        }

        basketJson.put("Итого", sumProducts);
        try (FileWriter file = new FileWriter(textFile)) {
            file.write(basketJson.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    public static Basket loadFromJsonFile(File file) throws IOException, ParseException {

/*        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject basketParsedJson = (JSONObject) obj;

            List<Food> foods = new ArrayList<>();
            String name;
            int price;
            int quantity;
            JSONArray listJson = (JSONArray) basketParsedJson.get("foodArrayJson");

            for (Object f : listJson) {
                JSONObject foodJson = (JSONObject) f;
                name = (String) foodJson.get("name");
                price = Integer.parseInt((String) foodJson.get("price"));
                quantity = Integer.parseInt((String) foodJson.get("quantity"));
                foods.add(new Food(name, price, quantity));
            }
            return new Basket(foods.toArray(Food[]::new));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
 */
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileReader reader = new FileReader(file);
        return gson.fromJson(reader, Basket.class);


    }

}