package com.shoplife.billing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CsvGenerator {

    private static final int TOTAL_ENTRIES = 5000;
    private static final int TOTAL_ITEMS = 1000;
    private static final int MAX_UNITS = 10;
    private static final int MAX_PRICE = 90;
    private static final int TOTAL_USERS = 3000;

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "order_Id,user,item_Id,nbr_of_units,date_time,amount";

    public static void main(String[] args) {
        String csvFile = "/Users/georgesalloum/IdeaProjects/georgesall/routing-service/services/billing/src/main/resources/purchases.csv";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(csvFile);
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

            List<String> users = new ArrayList<>();
            for (int i = 0; i < TOTAL_USERS; i++) {
                users.add("user" + i);
            }

            Map<Integer, Double> itemPrices = new HashMap<>();
            for (int i = 1; i <= TOTAL_ITEMS; i++) {
                itemPrices.put(i, ThreadLocalRandom.current().nextDouble(1, MAX_PRICE + 1));
            }

            int orderId = 1;
            String user = "user" + (TOTAL_USERS - 1); // Start with the last user
            String dateTime = generateRandomTime();

            int countPerOrder = 0;
            int desiredPerOrder = 1;

            for (int i = 0; i < TOTAL_ENTRIES; i++) {


                int itemId = ThreadLocalRandom.current().nextInt(1, TOTAL_ITEMS + 1);
                int numberOfUnits = ThreadLocalRandom.current().nextInt(1, MAX_UNITS + 1);
                double price = itemPrices.get(itemId);
                double amount = price * numberOfUnits;
                String line = orderId + COMMA_DELIMITER + user + COMMA_DELIMITER + itemId + COMMA_DELIMITER +
                        numberOfUnits + COMMA_DELIMITER + dateTime + COMMA_DELIMITER + amount;
                fileWriter.append(line);
                fileWriter.append(NEW_LINE_SEPARATOR);

                // Update orderId and user for the next entry in the same order
                if (++countPerOrder == desiredPerOrder) {
                    orderId++;
                    countPerOrder = 0;
                    desiredPerOrder = ThreadLocalRandom.current().nextInt(1, 6);
                    user = users.get(ThreadLocalRandom.current().nextInt(0, TOTAL_USERS));
                    dateTime = generateRandomTime();
                }
            }
            System.out.println("CSV file was created successfully!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter!");
                e.printStackTrace();
            }
        }
    }

    private static String generateRandomTime() {
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
