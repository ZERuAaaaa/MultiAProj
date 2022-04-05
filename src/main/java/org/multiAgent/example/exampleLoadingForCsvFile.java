package org.multiAgent.example;

import org.multiAgent.CsvTool;
import org.multiAgent.DialogueSystem;

/**
 * Hello world!
 *
 */
public class exampleLoadingForCsvFile {

    public static void main(String[] args) throws Exception {

        CsvTool csvtool = new CsvTool();
        DialogueSystem dialogue = new DialogueSystem();

        dialogue.initialize(csvtool.loadForCsv("src/main/java/org/multiAgent/example/exampleCsvFile.csv"));

        String result = dialogue.run("go out");

        System.out.println(result);
    }
}
