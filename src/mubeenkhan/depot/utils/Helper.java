package mubeenkhan.depot.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {

    public static boolean isParcelInQueue(String parcelID, String queueFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(queueFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[2].equals(parcelID)) {
                    return true; // Parcel ID found in the queue
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Parcel ID not found in the queue
    }

    public static void saveCustomerToQueueCSV(String customerName, String parcelID, String queueFilePath, int queueNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(queueFilePath, true))) {
            writer.write(queueNumber + "," + customerName + "," + parcelID);
            writer.newLine();
            System.out.println("Customer " + customerName + " has been saved to " + queueFilePath + ".");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save customer to " + queueFilePath + ".");
        }
    }

    public static String readCSV(String fileName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            data.append("Error reading ").append(fileName);
        }
        return data.toString();
    }
    
    public static boolean isValidParcelID(String parcelID) {
        // Regex pattern: Starts with C or X, followed by exactly two digits
        String regex = "^[CX]\\d{2}$";
        return parcelID.matches(regex);
    }
    
    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    public static boolean isParcelCollected(String parcelID) {
        File file = new File("Parcels.csv");
        if (!file.exists()) {
            System.err.println("Parcels.csv file not found.");
            return true; // Assume collected if file is missing
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(parcelID)) {
                    return parts[1].equalsIgnoreCase("true"); // Check isCollected field
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading Parcels.csv: " + ex.getMessage());
        }
        return true; // Assume collected if parcel ID is not found
    }
}


