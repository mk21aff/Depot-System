 package mubeenkhan.depot.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import mubeenkhan.depot.model.Logs;

public class LogsController {
    private Logs logs;

    public LogsController() {
        this.logs = new Logs();  // Initialize the Logs class
    }

    public void writeLog(String logEntry) {
        logs.writeLog(logEntry);  // Write the log entry using the Logs class
    }

    public java.util.List<String> getLogs() {
        return logs.getLogs();  // Retrieve all logs from the Logs class
    }
    
    // Method to log an action to a log file
    public void logAction(String action) {
        // Create a log entry with time stamp and action description
        String logEntry = LocalDateTime.now() + " - " + action;

        // Write the log entry to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("action_logs.txt", true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
    public void refreshLogs() {
        logs.loadLogs();  // Reload logs from the file
    }

}  

