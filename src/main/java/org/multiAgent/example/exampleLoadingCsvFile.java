package org.multiAgent.example;

import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.CsvTool;
import org.multiAgent.DialogueSystem;

import java.util.ArrayList;

/**
 * This example shows how initialize data from csv file , csvFileFormat.png show the format of constructing
 * an input data csv file.
 */
public class exampleLoadingCsvFile {

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
        // get log
        ArrayList<Move> log = dialogue.getLog();
        // reset the dialogue
        dialogue.reset();
    }
}
