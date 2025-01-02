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
    
    public static double calculateCollectionFee(double volume, double weight, int daysInDepot) {
        final double BASE_FEE = 5.0;
        final double VOLUME_FACTOR = 0.05; // $0.05 per cubic inch
        final double WEIGHT_FACTOR = 0.20; // $0.20 per 0.5 kg
        final double STORAGE_FACTOR = 1.0; // $1 per day

        double volumeCost = VOLUME_FACTOR * volume;
        double weightCost = WEIGHT_FACTOR * (weight / 0.5); // Assuming weight in kilograms
        double storageCost = STORAGE_FACTOR * daysInDepot;

        return BASE_FEE + volumeCost + weightCost + storageCost;
    }

    public static double applyDiscount(String parcelID, double originalFee) {
        final double DISCOUNT_RATE = 0.10; // 10% discount

        double discountedFee = originalFee; // Initialize discountedFee with originalFee

        // Check if discount applies
        if (parcelID.startsWith("X")) {
            discountedFee = originalFee * (1 - DISCOUNT_RATE); // Apply discount if parcelID starts with 'X'
        }

        // Optional: Add a logging utility in Helper to log details
        System.out.printf("Applied discount for parcelID=%s: Original Fee = %.2f, Discounted Fee = %.2f%n",
                parcelID, originalFee, discountedFee);

        return discountedFee;
    }
}




