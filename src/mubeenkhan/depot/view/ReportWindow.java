package mubeenkhan.depot.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReportWindow extends JFrame {
    private final JTextArea reportArea;
    private final JButton generateReportButton;
    private final JButton printReportButton;

    public ReportWindow() {
        setTitle("Parcel Report");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Components
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        generateReportButton = new JButton("Generate Report");
        printReportButton = new JButton("Print Report");

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateReportButton);
        buttonPanel.add(printReportButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setGenerateReportListener(ActionListener listener) {
        generateReportButton.addActionListener(listener);
    }

    public void setPrintReportListener(ActionListener listener) {
        printReportButton.addActionListener(listener);
    }

    public void displayReport(String report) {
        reportArea.setText(report);
    }

    public String getReportText() {
        return reportArea.getText();
    }
}
