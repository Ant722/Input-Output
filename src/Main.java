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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Basket basket;
        File textFile = new File("basket.txt");

        basket = inputMethod(scanner, textFile);

        basket.printCart();

    }

    private static Basket inputMethod(Scanner scanner, File textFile) throws FileNotFoundException {
        Basket basket;
        if (textFile.exists()) {
            System.out.println("Корзина покупок,нажмите ввод.");
            basket = Basket.loadFromTxtFile(textFile);
        } else {
            basket = new Basket(products);
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
                try {
                    basket.saveTxt(textFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return basket;
    }


}