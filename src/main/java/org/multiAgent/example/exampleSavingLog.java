package org.multiAgent.example;

import org.multiAgent.CsvTool;
import org.multiAgent.DialogueSystem;
import org.multiAgent.RandomGenerator;

/**
 * This example show how to save dialogue result to csv file, and initialize dialogue via random generator.
 */
public class exampleSavingLog {

    public static  void main(String[] args) throws Exception {
        CsvTool csvtool = new CsvTool();
        DialogueSystem dialogue = new DialogueSystem();
        // load data from csv file
        dialogue.initialize(csvtool.loadForCsv("src/main/java/org/multiAgent/example/exampleCsvFile.csv"));

        //run and save dialogue result and log to target csv file
        dialogue.runAndSave("go out","src/main/java/org/multiAgent/example/result.csv",
                "src/main/java/org/multiAgent/example/log.csv");

        dialogue.reset();
    }
}
