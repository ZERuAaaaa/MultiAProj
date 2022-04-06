package org.multiAgent;

public class dialogueFromCsv {

    public static void main(String[] args) throws Exception{
        try{
            DialogueSystem dialogue = new DialogueSystem();

            CsvTool tool = new CsvTool();
            dialogue.initialize(tool.loadForCsv(args[0]));

            //run and save dialogue result and log to target csv file
            dialogue.runAndSave(args[1],args[2],args[3]);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("variables are in format \"[url of inputting data] [dialogue goal] [url to save result] [url to save log]\"" +
                    "\n please ensure that you entered right url"+"]");
        }

    }
}
