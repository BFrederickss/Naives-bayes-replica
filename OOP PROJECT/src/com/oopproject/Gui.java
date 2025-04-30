package com.oopproject;

// All the imports needed for the gui
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.io.File;
import java.util.ArrayList;
import static java.awt.AWTEventMulticaster.add;

import java.util.Scanner;




// This class is used to create a gui for the user
public class Gui extends JFrame implements ActionListener{

    private JButton prediction;
    private String hasKey;
    private String doorType;
    private String timeOfDay;
    private String location;
    private String doorLocked;
    private JComboBox<String> hashKeyBox;
    private JComboBox<String> doorTypeBox;
    private JComboBox<String> timeOfDayBox;
    private JComboBox<String> locationBox;
    private JComboBox<String> doorLockedBox;
    private Scanner scanner;
    private JLabel resultLabel;
    private JButton addButton;
    private JButton trainButton;
    Frequency frequency = new Frequency();


    public Gui(){
        // Set title and default close operation
        setTitle("Door Locked Predictor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Create a main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Create a GridBagConstraints object for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to the panel
        JLabel eflabel = new JLabel("Enter your feature values:");
        eflabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(eflabel, gbc);

        // Has Key
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Has Key:"), gbc);
        hashKeyBox = new JComboBox<>(new String[]{"Yes", "No"});
        gbc.gridx = 1;
        mainPanel.add(hashKeyBox, gbc);

        // Door Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Door Type:"), gbc);
        doorTypeBox = new JComboBox<>(new String[]{"Metal", "Wooden"});
        gbc.gridx = 1;
        mainPanel.add(doorTypeBox, gbc);

        // Time of Day
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Time of Day:"), gbc);
        timeOfDayBox = new JComboBox<>(new String[]{"Night", "Day"});
        gbc.gridx = 1;
        mainPanel.add(timeOfDayBox, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Location:"), gbc);
        locationBox = new JComboBox<>(new String[]{"Urban", "Rural"});
        gbc.gridx = 1;
        mainPanel.add(locationBox, gbc);

        // Door Locked
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Door Locked:"), gbc);
        doorLockedBox = new JComboBox<>(new String[]{"Yes", "No"});
        gbc.gridx = 1;
        mainPanel.add(doorLockedBox, gbc);

        // Buttons
        prediction = new JButton("Predict");
        addButton = new JButton("Add");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(prediction);
        buttonPanel.add(addButton);

        // Add action listeners
        prediction.addActionListener(this);
        addButton.addActionListener(this);

        // Add panels to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set size and make the frame visible
        setSize(600, 600);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        // When the app closes, it will run the cleanup method
        // basically deletes rows after the 200th row
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Feature to run when the program closes
                System.out.println("Program is closing. Running cleanup...");
                frequency.removeRowsAfter(); // Example feature
            }
        });

        // Make the window visible
        setVisible(true);

    }// End of constructor


    @Override
    public void actionPerformed(ActionEvent e) {

        // Check which button was clicked
        // if the prediction button was clicked
        // collect user inputs
        // create an instance of Frequency and call the prediction method
        if (e.getSource() == prediction) {
            // Collect user inputs
            String hasKey = (String) hashKeyBox.getSelectedItem();
            String doorType = (String) doorTypeBox.getSelectedItem();
            String timeOfDay = (String) timeOfDayBox.getSelectedItem();
            String location = (String) locationBox.getSelectedItem();

            // Create an instance of Frequency and call the prediction method

            frequency.reader(); // Ensure the frequency table is populated

            // Call the prediction method
            String predictionResult = frequency.predict(hasKey, doorType, timeOfDay, location);
            double accuracy = frequency.calculateAccuracy();

            System.out.println(frequency.calculateAccuracy());

            String formattedAccuracy = String.format("%.2f", accuracy);


            // Display the result
            if (predictionResult.equals("Yes")) {
                JOptionPane.showMessageDialog(this, "Prediction: Door is locked \nAccuracy: " + formattedAccuracy + "%");
            }else {
                JOptionPane.showMessageDialog(this, "Prediction: Door is not locked \nAccuracy: " + formattedAccuracy + "%");
            }// end if

        }
        // If the add button was clicked
        // collect user inputs
        // add whatever the user input is to the csv file
        else if (e.getSource() == addButton)
        {

            String hasKey = (String) hashKeyBox.getSelectedItem();
            String doorType = (String) doorTypeBox.getSelectedItem();
            String timeOfDay = (String) timeOfDayBox.getSelectedItem();
            String location = (String) locationBox.getSelectedItem();
            String doorLocked = (String) doorLockedBox.getSelectedItem();

            frequency.addRow(hasKey, doorType, timeOfDay, location, doorLocked);

        }// end if

    }// end actionPerformed

}// End of gui class

