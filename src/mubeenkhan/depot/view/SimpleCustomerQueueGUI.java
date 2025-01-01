package mubeenkhan.depot.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimpleCustomerQueueGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final JButton loadQueueButton;
    private final JButton markCollectedNextButton; // Renamed button for marking collected
    private final JTextArea displayArea;

    public SimpleCustomerQueueGUI() {
        // GUI Setup
        setTitle("Serve Customers");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loadQueueButton = new JButton("Load Queue");
        markCollectedNextButton = new JButton("Mark Collected Next"); // Renamed the button
        markCollectedNextButton.setEnabled(false); // Initially disabled
        buttonPanel.add(loadQueueButton);
        buttonPanel.add(markCollectedNextButton); // Added the renamed button
        add(buttonPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setServeCustomerListener(ActionListener listener) {
        loadQueueButton.addActionListener(listener);
    }

    public void setNextButtonListener(ActionListener listener) {
        markCollectedNextButton.addActionListener(listener); // Changed to the renamed button
    }

    public void appendToDisplay(String text) {
        displayArea.setText(text); // Set new content
    }

    public void enableNextButton(boolean enabled) {
        markCollectedNextButton.setEnabled(enabled); // Enable or disable the renamed button
    }
}
