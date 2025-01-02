package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Parcel;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.utils.Helper;
import mubeenkhan.depot.view.DepotSystemGUI;
import mubeenkhan.depot.view.ReportWindow;
import mubeenkhan.depot.controller.ServeCustomersController;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DepotController {
    private Worker worker;
    private DepotSystemGUI gui;
    private LogsController logsController;
    private ReportWindow reportWindow;

    public DepotController(Worker worker, DepotSystemGUI gui) {
        this.worker = worker;
        this.gui = gui;
        this.logsController = new LogsController();
        
        reportWindow = new ReportWindow();
        reportWindow.setGenerateReportListener(e -> generateReport());
        reportWindow.setPrintReportListener(e -> printReport());


        // Set listeners for each button
        gui.reportButton.addActionListener(e -> reportWindow.setVisible(true));

//        gui.setAddParcelListener(e -> gui.showParcelForm()); // Show form when clicked
        gui.setViewUncollectedParcelsListener(e -> displayUncollectedParcels());
        gui.setViewCollectedParcelsListener(e -> displayCollectedParcels());

        gui.setAddParcelListener(e -> {
            gui.showParcelForm();
            refreshParcels(); // Refresh the displayed parcels after adding
        });
       // gui.setViewParcelsListener(e -> worker.viewParcels());
        gui.setDisplayParcelsListener(e -> worker.viewParcels());
        
       // gui.setDisplayQueueListener(e -> displayCSVContent("Queue.csv"));
        
        gui.setSearchParcelListener(e -> searchParcel());
        gui.setDisplayParcelsListener(e -> displayCSVContent("Parcels.csv"));
        gui.setDisplayQueueListener(e -> displayCSVContent("Queue.csv"));

        // Updated Add Customer Listener
        gui.setAddCustomerListener(e -> {
            // Create a custom panel for customer input
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField customerNameField = new JTextField(15);
            panel.add(new JLabel("Customer Name:"));
            panel.add(customerNameField);
            JButton submitButton = new JButton("Submit");
            panel.add(submitButton);

            // Create a new JFrame for the form
            JFrame customerFormFrame = new JFrame("Add Customer to Queue");
            customerFormFrame.setSize(300, 150);
            customerFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            customerFormFrame.add(panel);
            customerFormFrame.setVisible(true);

            // Attach an action listener to the submit button
            submitButton.addActionListener(submitEvent -> {
                String customerName = customerNameField.getText().trim();
                if (!customerName.isEmpty()) {
                    boolean isAdded = worker.addCustomerToQueue(customerName);
                    if (isAdded) {
                        JOptionPane.showMessageDialog(
                                gui.getFrame(),
                                "Customer " + customerName + " has been added to the queue.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear the customer name field to add another customer
                        customerNameField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(
                                gui.getFrame(),
                                "No uncollected parcel found for customer: " + customerName,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            gui.getFrame(),
                            "Customer name cannot be empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        
    }

    // Reads and displays CSV file content in the text area
    private void displayCSVContent(String fileName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
            gui.displayData(data.toString());
            logsController.writeLog("Displayed CSV content from: " + fileName);
        } catch (IOException ex) {
            gui.displayData("Error: Unable to read " + fileName);
            logsController.writeLog("Error reading CSV file: " + fileName);
        }
    }

    public void start() {
        gui.show(); // Start the main GUI (with buttons)
    }
    
    public void searchParcel() {
        String parcelID = gui.getSearchParcelID(); // Get the Parcel ID from the GUI
        if (parcelID != null && !parcelID.isEmpty()) {
            String result = worker.searchParcelByID(parcelID); // Call the worker method to search the parcel
            gui.displayData(result); // Display search result in the GUI
            logsController.writeLog("Searched for parcel with ID: " + parcelID);
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Please enter a Parcel ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void sortParcelsByName() {
        // Read parcels using CSVReader
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv");

        // Sort parcels by customer name
        parcels.sort(Comparator.comparing(Parcel::getCustomerName));

        // Prepare display data
        StringBuilder sortedData = new StringBuilder("Sorted Parcels by Name:\n\n");
        for (Parcel parcel : parcels) {
            sortedData.append("Parcel ID: ").append(parcel.getParcelID()).append("\n")
                       .append("Customer Name: ").append(parcel.getCustomerName()).append("\n")
                       .append("Length: ").append(parcel.getLength()).append("\n")
                       .append("Width: ").append(parcel.getWidth()).append("\n")
                       .append("Height: ").append(parcel.getHeight()).append("\n")
                       .append("Weight: ").append(parcel.getWeight()).append("\n")
                       .append("Days in Depot: ").append(parcel.getDaysInDepot()).append("\n")
                       .append("Collected: ").append(parcel.isCollected() ? "Yes" : "No").append("\n")
                       .append("-----\n");
        }

        if (parcels.isEmpty()) {
            sortedData.append("No parcels found.");
        }

        // Display sorted parcels in GUI
        gui.displayData(sortedData.toString());
        logsController.writeLog("Sorted parcels by customer name");
    }
    public void refreshParcels() {
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv"); // Reload parcels from the CSV
        StringBuilder data = new StringBuilder("All Parcels:\n");
        for (Parcel parcel : parcels) {
            data.append(parcel.toString()).append("\n");
        }
        gui.displayData(data.toString()); // Update the GUI display
        logsController.writeLog("Refreshed parcels display");
    }

    
    public void displayUncollectedParcels() {
        // Read all parcels from the CSV file
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv");

        // Filter uncollected parcels
        List<Parcel> uncollectedParcels = parcels.stream()
                .filter(parcel -> !parcel.isCollected())
                .toList();

        // Prepare data for display
        StringBuilder displayData = new StringBuilder("Uncollected Parcels:\n\n");
        for (Parcel parcel : uncollectedParcels) {
            displayData.append("Parcel ID: ").append(parcel.getParcelID()).append("\n")
                       .append("Customer Name: ").append(parcel.getCustomerName()).append("\n")
                       .append("Length: ").append(parcel.getLength()).append("\n")
                       .append("Width: ").append(parcel.getWidth()).append("\n")
                       .append("Height: ").append(parcel.getHeight()).append("\n")
                       .append("Weight: ").append(parcel.getWeight()).append("\n")
                       .append("Days in Depot: ").append(parcel.getDaysInDepot()).append("\n")
                       .append("Collected: ").append(parcel.isCollected() ? "Yes" : "No").append("\n")
                       .append("-----\n");
        }

        if (uncollectedParcels.isEmpty()) {
            displayData.append("No uncollected parcels found.");
        }

        // Display the result in the GUI
        gui.displayData(displayData.toString());
        logsController.writeLog("Displayed uncollected parcels");
    }



    public void displayCollectedParcels() {
        // Read all parcels from the CSV file
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv");

        // Filter collected parcels
        List<Parcel> collectedParcels = parcels.stream()
                .filter(Parcel::isCollected) // Check if isCollected is true
                .toList();

        // Prepare data for display
        StringBuilder displayData = new StringBuilder("Collected Parcels:\n\n");
        for (Parcel parcel : collectedParcels) {
            displayData.append("Parcel ID: ").append(parcel.getParcelID()).append("\n")
                       .append("Customer Name: ").append(parcel.getCustomerName()).append("\n")
                       .append("Length: ").append(parcel.getLength()).append("\n")
                       .append("Width: ").append(parcel.getWidth()).append("\n")
                       .append("Height: ").append(parcel.getHeight()).append("\n")
                       .append("Weight: ").append(parcel.getWeight()).append("\n")
                       .append("Days in Depot: ").append(parcel.getDaysInDepot()).append("\n")
                       .append("Collected: ").append(parcel.isCollected() ? "Yes" : "No").append("\n")
                       .append("-----\n");
        }

        if (collectedParcels.isEmpty()) {
            displayData.append("No collected parcels found.");
        }

        // Display the filtered parcels in the GUI
        gui.displayData(displayData.toString());
        logsController.writeLog("Displayed collected parcels");
    }


    
    private void generateReport() {
        File file = new File("Parcels.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Parcels.csv file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder report = new StringBuilder();
        double totalCollectedFees = 0.0;
        double totalFees = 0.0;

        // Add timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        report.append("Report Generated: ").append(now.format(formatter)).append("\n\n");

        report.append("Collected Parcels:\n");
        report.append("--------------------------------------------------\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && "true".equalsIgnoreCase(parts[1])) { // Collected parcels
                    String parcelID = parts[0];
                    double length = Double.parseDouble(parts[3]);
                    double width = Double.parseDouble(parts[4]);
                    double height = Double.parseDouble(parts[5]);
                    double weight = Double.parseDouble(parts[6]);
                    int daysInDepot = Integer.parseInt(parts[7]);

                    double volume = length * width * height;
                    double fee = Helper.calculateCollectionFee(volume, weight, daysInDepot);
                    totalFees += fee;

                    double discountedFee = Helper.applyDiscount(parcelID, fee);
                    totalCollectedFees += discountedFee;

                    report.append(String.format("Parcel ID: %s | Collected Fee: $%.2f\n", parcelID, discountedFee));
                }
            }
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error reading Parcels.csv: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        report.append("--------------------------------------------------\n");
        report.append(String.format("Total Collected Fees: $%.2f\n", totalCollectedFees));
        report.append(String.format("Total Original Fees: $%.2f\n", totalFees));

        // Display report in the main GUI
       // reportTextArea.setText(report.toString()); // Assuming `reportTextArea` exists in the main GUI
        reportWindow.displayReport(report.toString());

        // Log the report generation
        logsController.writeLog("Report generated successfully at " + now.format(formatter));
    }


    private void printReport() {
        String report = reportWindow.getReportText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ParcelReport.txt"))) {
            writer.write(report);
            JOptionPane.showMessageDialog(null, "Report saved as ParcelReport.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}