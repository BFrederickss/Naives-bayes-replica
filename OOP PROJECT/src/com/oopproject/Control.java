package com.oopproject;

public class Control {

    public static void main(String[] args) {
        Frequency frequency = new Frequency();
        frequency.reader();
        // Read the CSV file and create the frequency table
        frequency.printFrequencyTable();

        // Create the GUI
        new Gui();

    }
}
