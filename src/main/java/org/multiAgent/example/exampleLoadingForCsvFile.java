package org.multiAgent.example;

import org.multiAgent.CsvTool;
import org.multiAgent.DialogueSystem;

/**
 * This example shows how initialize data from csv file , csvFileFormat.png show the format of constructing
 * an input data csv file.
 */
public class exampleLoadingForCsvFile {

    public static void main(String[] args) throws Exception {
        // import csvtool to read data from csv
        CsvTool csvtool = new CsvTool();
        // initialize dialogue with data from csv file
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.initialize(csvtool.loadForCsv("src/main/java/org/multiAgent/example/exampleCsvFile.csv"));
        // run the dialogue under a specifc goal
        String result = dialogue.run("go out");
        // get result
        System.out.println(result);
        dialogue.reset();
    }
}
