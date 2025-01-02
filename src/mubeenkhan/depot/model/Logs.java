package mubeenkhan.depot.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logs {
    private List<String> logs;
    private static final String LOG_FILE_PATH = "logs.txt";  // Path to your log file

    public Logs() {
        logs = new ArrayList<>();
        loadLogs();  // Load existing logs from the file
    }

    // Method to load logs from the log file
    public void loadLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);  // Add each line to the internal logs list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to write log entry with timestamp
    public void writeLog(String logEntry) {
        // Get the current timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Create the log message with timestamp
        String logMessage = timestamp + " - " + logEntry;

        // Store the log entry in the internal list
        logs.add(logMessage);

        // Write the log message to the log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(logMessage);
            writer.newLine();  // Add a new line after each log entry
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all logs
    public List<String> getLogs() {
        return logs;
    }
}
