import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Food[] productFood = {
            new Food("Тушёнка(упаковка-12шт)", 2000),
            new Food("Крупа гречневая(мешок-20кг)", 1600),
            new Food("Сгущеное молоко(жб-10л)", 3000),

    };

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Basket basket;
        //File textFile = new File("basket.txt");
        File binFile = new File("basket.bin");

        basket = inputMethod(scanner, binFile);

        basket.printCart();

    }

    private static Basket inputMethod(Scanner scanner, File binFile) throws IOException, ClassNotFoundException {
        Basket basket;
        if (binFile.exists()) {
            System.out.println("Корзина покупок,нажмите ввод.");
            basket = Basket.loadFromBinFile(binFile);
        } else {
            basket = new Basket(productFood);
        }
        System.out.println("Покупательская корзина:");
        for (int i = 0; i < productFood.length; i++) {
            System.out.println((i + 1) + "." + productFood[i].getName() + "/" + productFood[i].getPrices() + " руб/шт.");
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

            } else if (Integer.parseInt(parts[0]) < 1 || Integer.parseInt(parts[0]) > productFood.length) {
                System.out.println("Введенный номер позиции отсутствует в предложенном списке. Введи корректно.");

            } else {
                basket.addToCart(Integer.parseInt(parts[0]) - 1, Integer.parseInt(parts[1]));
                basket.saveBin(binFile);
            }

        }
        return basket;
    }


}