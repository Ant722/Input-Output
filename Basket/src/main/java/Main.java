import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException, ClassNotFoundException {

        Basket basket = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse("shop.xml");
        Node root = doc.getDocumentElement();

        NodeList nodeList = root.getChildNodes();
        Node load = nodeList.item(1);
        Node save = nodeList.item(3);
        Node log = nodeList.item(5);

        Scanner scanner = new Scanner(System.in);
        ClientLog client = new ClientLog(products);

        basket = getBasket(basket, load);

        addInBasket(basket, log, scanner, client);

        saveBasket(basket, save);

    }

    private static void addInBasket(Basket basket, Node log, Scanner scanner, ClientLog client) {
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
                client.log(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

            }

        }

        if (log.getChildNodes().item(1).getTextContent().equals("true")) {
            System.out.println("good");
            client.exportAsCSV(new File(log.getChildNodes().item(3).getTextContent()));
        }
    }

    private static Basket getBasket(Basket basket, Node load) throws IOException, ParseException {
        if (load.getChildNodes().item(1).getTextContent().equals("true")) {
            File txtFileLoad = new File(load.getChildNodes().item(3).getTextContent());
            String formatLoad = load.getChildNodes().item(5).getTextContent();

            if (formatLoad.equals("json")) {
                basket = Basket.loadFromJsonFile(txtFileLoad);
                basket.printCart();
            } else if (formatLoad.equals("txt")) {
                basket = Basket.loadFromTxtFile(txtFileLoad);
                basket.printCart();
            }
        } else {
            basket = new Basket(products);
        }
        return basket;
    }

    private static void saveBasket(Basket basket, Node save) throws IOException {
        if (save.getChildNodes().item(1).getTextContent().equals("true")) {
            File txtFileSave = new File(save.getChildNodes().item(3).getTextContent());
            String formatSave = save.getChildNodes().item(5).getTextContent();

            if (formatSave.equals("json")) {
                basket.saveJson(txtFileSave);
                basket.printCart();
            } else if (formatSave.equals("txt")) {
                basket.saveTxt(txtFileSave);
                basket.printCart();
            }
        }
    }


}