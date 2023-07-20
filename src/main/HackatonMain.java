package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HackatonMain {
    public static void main(String[] args) {

        String currencyCode = null;
        String dateString = null;

        //Парсим аргументы
        for (String arg : args) {
            if (arg.startsWith("--code=")) {
                currencyCode = arg.substring(7);
            } else if (arg.startsWith("--date=")) {
                dateString = arg.substring(7);
            }
        }

        //Проверка валидности аргументов
        if (currencyCode == null || dateString == null) {
            System.out.println("Usage: currency_rates --code=<currency_code> --date=<date>");
            return;
        }

        //Транформируем дату из аргументов в дату для работы с API
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            dateString = new SimpleDateFormat("dd/MM/yyyy").format(date);
            String url = CBRParserUtilities.constructURL(dateString);

            InputStream responseStream = CBRParserUtilities.sendRequest(url);
            double rate = CBRParserUtilities.findRateByCurrencyCode(responseStream, currencyCode);
            String currencyName = getCurrencyName(currencyCode);
            System.out.printf("%s (%s): %.4f\n", currencyCode, currencyName, rate);
        } catch (IOException | ParseException | ParserConfigurationException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Вывод названия валюты через API
    public static String getCurrencyName(String currencyCode) throws IOException, ParserConfigurationException, SAXException  {
        String url = "https://www.cbr.ru/scripts/XML_valFull.asp";
        InputStream responseStream = CBRParserUtilities.sendRequest(url);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(responseStream);
        NodeList nodeList = document.getElementsByTagName("Item");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String charCode = element.getElementsByTagName("ISO_Char_Code").item(0).getTextContent();
            if (charCode.equals(currencyCode)) {
                String currencyName = element.getElementsByTagName("Name").item(0).getTextContent();
                return currencyName;
            }
        }
        return "Unknown Currency";
    }
}