package com.oopproject;
import java.io.*;
import java.util.*;
import java.util.List;


public class Frequency {

    private String filePath = "full_data.csv";
    private List<Map<String, String>> frequencyTable = new ArrayList<>();


    // Reads the data and adds the rows of data to the frequency table
    public void reader()
    {
        try
        {
            // Clears the frequency table due to the fact
            // the predictions would stack up
            // hence adding the rows directly to the csv
            frequencyTable.clear();
            File file = new File(filePath);

            // Create a scanner object
            Scanner scanner = new Scanner(file);

            // First line is always header
            // Read the header line so we can use it to create the frequency table
            String[] headers ={};

            if (scanner.hasNextLine()) {

                headers = scanner.nextLine().split(",");

            }// end if

            // Read the file
            // Using the frequency table to train the data
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                String[] values = line.split(",");

                // Create a map to store the values for each row
                Map<String, String> row = new HashMap<>();

                // Create frequency
                for (int i = 0; i < headers.length && i< values.length; i++) {

                    row.put(headers[i], values[i]);

                }// end for

                // Add the row to the frequency table
                frequencyTable.add(row);

            }// end while



        }
        catch (Exception e)
        {

            System.out.println("Error: " + e.getMessage());

        }//end try

    }// end reader



    // Print the frequency table
    public void printFrequencyTable() {
        for (Map<String, String> row : frequencyTable) {
            System.out.println(row);
        }
    }

    // Predict the value of the door lock
    public String predict(String hasKey, String type, String day, String location) {

        // Yes and No counts to keep track of the predictions
        int yesCount = 0;
        int noCount = 0;

        // Loop over the data
        for (Map<String, String> row : frequencyTable) {

            // Check if the row matches the input parameters
            if (row.get("HasKey").equals(hasKey) &&
                    row.get("DoorType").equals(type) &&
                    row.get("TimeOfDay").equals(day) &&
                    row.get("Location").equals(location)) {

                if (row.get("DoorIsLocked").equals("Yes")) {
                    yesCount++;
                } else if (row.get("DoorIsLocked").equals("No")) {
                    noCount++;
                }// end if

            }// end if

        }// end for

        // Return result
        if (yesCount > noCount) {
            return "Yes";
        } else if (noCount > yesCount) {
            return "No";
        } else {
            return "Unknown";
        }// end if

    }// end predict


    // Calculate the accuracy of the predictor
    public double calculateAccuracy() {

        // Ensure there are at least 200 rows in the dataset
        if (frequencyTable.size() < 200) {
            throw new IllegalStateException("Dataset must have at least 200 rows.");
        }


        // Split the datase
        List<Map<String, String>> trainingData = new ArrayList<>(frequencyTable.subList(1, 150  ));

        // Include the header row in the testing data
        List<Map<String, String>> testingData = frequencyTable.subList(151, frequencyTable.size());


        // Test the predictor
        int correctPredictions = 0;
        int i = 0;
        int j = 0;

        // Loop over the training data
        for (Map<String, String> row : trainingData) {


            // resets the index if its at the end
                if (j>=testingData.size()-1) {
                    j = 0;
                }
                else {
                    j++;
                }


                // Check if the row matches the input parameters
            if (row.get("HasKey").equals(testingData.get(j).get("HasKey")) &&
                    row.get("DoorType").equals(testingData.get(j).get("DoorType")) &&
                    row.get("TimeOfDay").equals(testingData.get(j).get("TimeOfDay")) &&
                    row.get("Location").equals(testingData.get(j).get("Location"))) {
                if (row.get("DoorIsLocked").equals("Yes") && testingData.get(j).get("DoorIsLocked").equals("Yes")){

                    // if the prediction is correct
                    correctPredictions++;
                    i ++;
                } else if (row.get("DoorIsLocked").equals("No") && testingData.get(j).get("DoorIsLocked").equals("No")) {

                    // if the prediction is correct
                    correctPredictions++;
                    i ++;
                }
                else{
                    // if the prediction is incorrect
                    // or its the same
                    i ++;
                }

            }// end if


        }// end for


        // Calculate accuracy
        return (double) correctPredictions / i * 100;

    }// end calculateAccuracy

    // Add a new row to the csv which is trained later
    public void addRow(String key, String type, String timeDay, String location, String locked) {
        // Add a new row to the frequency table
        Map<String, String> newRow = new HashMap<>();
        newRow.put("HasKey", key);
        newRow.put("DoorType", type);
        newRow.put("TimeOfDay", timeDay);
        newRow.put("Location", location);
        newRow.put("DoorIsLocked", locked);

        // Append the new row to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            String row = String.join(",", key, type, timeDay, location, locked);
            writer.write(row);
            writer.newLine();
            System.out.println("Row added to CSV: " + row);

        }
        catch (IOException e) {

            System.out.println("Error writing to CSV: " + e.getMessage());

        }// end try

        // debugging
        System.out.println("Added row: " + newRow);
    }

    // Used for removing rows after row 200
    public void removeRowsAfter() {
        List<String> rows = new ArrayList<>();
        int maxRows = 201;

        // Read the CSV file and store rows in a list
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            int rowCount = 0;

            // Read and store rows up to the maxRows limit
            while ((line = reader.readLine()) != null) {

                rows.add(line);
                rowCount++;
                if (rowCount >= maxRows) {
                    break;
                }

            }
        } catch (IOException e) {

            e.printStackTrace();

        }// end try

        // Write the filtered rows back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // Rewrite the rows
            for (String row : rows) {

                // Write the row to the CSV
                writer.write(row);
                writer.newLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }// end try

        System.out.println("Rows after row 200 have been removed successfully.");
    }// end removeRowsAfter


}// end Frequency
