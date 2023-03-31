import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Food[] products = {
            new Food("Тушёнка(упаковка-12шт)", 2000),
            new Food("Крупа гречневая(мешок-20кг)", 1600),
            new Food("Сгущеное молоко(жб-10л)", 3000),

    };

    public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException {
        Scanner scanner = new Scanner(System.in);
        Basket basket;
        //       File txtFile = new File("basket.txt");
        File txtFile = new File("basket.json");
        basket = inputMethod(scanner, txtFile);
        basket.printCart();
        basket.saveJson(txtFile);
    }

    private static Basket inputMethod(Scanner scanner, File txtFile) throws IOException, ClassNotFoundException, ParseException {
        Basket basket;
        ClientLog client;
        if (txtFile.exists()) {
//            basket = Basket.loadFromTxtFile(txtFile);
            basket = Basket.loadFromJsonFile(txtFile);
            client = new ClientLog(Basket.loadFromJsonFile(txtFile));
            basket.printCart();
        } else {
            basket = new Basket(products);
            client = new ClientLog(new Basket(products));
        }
        System.out.println("Покупательская корзина:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + "." + products[i].getName() + "/" + products[i].getPrices() + " руб/шт.");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите 'end'");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Ввод должен быть 'end' или состоять из двух частей \n" +
                        "через пробел номер товара и кол-во  ");

            } else if (!(parts[0].matches("\\d+")) || !(parts[1].matches("\\d+"))) {
                System.out.println("Ввод должен состоять из целых положительных чисел, но было введено: " + input);
                System.out.println("Ошибка класса: NumberFormatException");

            } else if (Integer.parseInt(parts[1]) < 1) {
                System.out.println("Кол-во товара должно быть больше 0.");

            } else if (Integer.parseInt(parts[0]) < 1 || Integer.parseInt(parts[0]) > products.length) {
                System.out.println("Введенный номер позиции отсутствует в предложенном списке. Введи корректно.");

            } else {
                basket.addToCart(Integer.parseInt(parts[0]) - 1, Integer.parseInt(parts[1]));
                client.log(Integer.parseInt(parts[0]) , Integer.parseInt(parts[1]));
/*
                try {
                    basket.saveTxt(txtFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
*/
            }
        }
        client.exportAsCSV(new File("log.csv"));
        return basket;

    }


}