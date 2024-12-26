package mubeenkhan.depot.view;

import mubeenkhan.depot.model.Parcel;
import mubeenkhan.depot.model.Customer;
import java.io.*;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private BufferedWriter writer; // Used to write reports to a file

    // Constructor to initialize the report file for writing
    public ReportGenerator() {
        try {
            writer = new BufferedWriter(new FileWriter("depot_report.txt")); // Open the report file
        } catch (IOException e) {
            e.printStackTrace(); // Print error if file cannot be opened
        }
    }

    // Method to generate a report when a parcel is collected by a customer
    public void generateCollectedReport(Parcel parcel, Customer customer) {
        try {
            // Write the collected parcel details along with the customer info and fee to the report
            writer.write("Collected Parcel: " + parcel + " - Customer: " + customer + " - Fee: " + parcel.calculateCollectionFee() + "\n");
        } catch (IOException e) {
            e.printStackTrace(); // Print error if writing fails
        }
    }

    // Method to generate a report when a parcel is uncollected
    public void generateUncollectedReport(Parcel parcel) {
        try {
            // Write the uncollected parcel details to the report
            writer.write("Uncollected Parcel: " + parcel + "\n");
        } catch (IOException e) {
            e.printStackTrace(); // Print error if writing fails
        }
    }

    // Method to generate a final report after processing all customers
    public void generateFinalReport(List<Parcel> collectedParcels, Map<String, Parcel> parcelDepot) {
        try {
            // Write a summary of the collected and uncollected parcels to the report file
            writer.write("\nFinal Report:\n");
            writer.write("Collected Parcels: \n");
            for (Parcel p : collectedParcels) {
                writer.write(p + "\n"); // Write details of collected parcels
            }
            writer.write("\nUncollected Parcels: \n");
            for (Parcel p : parcelDepot.values()) {
                if (!p.isCollected()) {
                    writer.write(p + "\n"); // Write details of uncollected parcels
                }
            }
            writer.close(); // Close the report file after writing
        } catch (IOException e) {
            e.printStackTrace(); // Print error if writing fails
        }
    }
}
