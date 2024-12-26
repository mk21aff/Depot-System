package mubeenkhan.depot.utils;

import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;
import java.io.*;
import java.util.*;

public class CSVReader {

    // Method to read parcels from CSV
    public static List<Parcel> readParcelsFromCSV(String filename) {
        List<Parcel> parcels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true; // Skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip the first line (header)
                    continue;
                }
                String[] data = line.split(",");
                try {
                    // Parsing parcel data
                    String parcelID = data[0].trim();
                    boolean isCollected = Boolean.parseBoolean(data[1].trim());  // Parse boolean
                    String customerName = data[2].trim();
                    double length = Double.parseDouble(data[3].trim());
                    double width = Double.parseDouble(data[4].trim());
                    double height = Double.parseDouble(data[5].trim());
                    double weight = Double.parseDouble(data[6].trim());
                    int daysInDepot = Integer.parseInt(data[7].trim());
                    
                    // Create Parcel object and add to list
                    parcels.add(new Parcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in CSV file: " + Arrays.toString(data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parcels;
    }

    // Method to read customers from CSV
    public static List<Customer> readCustomersFromCSV(String filename) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true; // Skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip the first line (header)
                    continue;
                }
                String[] data = line.split(",");
                try {
                    // Parsing customer data
                    int queueNumber = Integer.parseInt(data[0].trim());
                    String customerName = data[1].trim();
                    String parcelID = data[2].trim();
                    
                    // Create Customer object and add to list
                    customers.add(new Customer(queueNumber, customerName, parcelID));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in CSV file: " + Arrays.toString(data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
