package model;

import com.google.gson.Gson;

import java.io.*;

import static org.apache.pdfbox.util.ErrorLogger.log;

public abstract class ReadableWritable {

    //REQUIRES: myData needs to be in the format of GSON string
    //EFFECTS: save myData to a file under savedFiles folder
    public abstract void writeToFile (String myData) throws Exception;

    //EFFECTS: construct a file at fileLocation if no such file exists.
    protected File generateFileAndDirectoryIfNotThere(String fileLocation) {
        File file = new File(fileLocation);
        if (!file.exists()) {
            try {
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                log("Exception Occurred: " + e.toString());
            }
        }
        return file;
    }

    //EFFECTS: wipe out old content, write myData to file, print a confirmation on fileLocation
    protected void writeFile(String myData, File file) {
        try {
            //Convenience Class for writing character files
            FileWriter songWriter = new FileWriter(file.getAbsoluteFile(), true);

            wipeFileClean(file);

            //Wrapping FileWriter with BufferWriter to increase efficiency
            BufferedWriter bufferedWriter = new BufferedWriter(songWriter);
            bufferedWriter.write(myData);
            bufferedWriter.close();
            System.out.println("Data saved at file location: " + "\n" + file.getAbsolutePath());

        } catch (IOException e) {
            log("Hmm... Got an error while saving Song data to file " + e.toString());
        }
    }

    //MODIFIES: file
    //EFFECTS: wipe clean the content in file
    protected void wipeFileClean(File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    //EFFECTS: Convert Object to GSON String
    public String convertToGsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }





    //--------------------------------------------------------------------------------------------------------//
    //MODIFIES: this
    //EFFECTS: open a file from fileLocation and load it to this
    public abstract void readFromFile(String fileLocation);

    //EFFECTS: return File if fileLocation exists. Otherwise, throw FileNotFoundException
    public File getFile(String fileLocation) throws FileNotFoundException{
        File file = new File(fileLocation);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    public void printLoadingSuccessMessage(String fileLocation) {
        System.out.println("Data loaded successfully from location:" + "\n" + fileLocation);
    }

    protected void printLoadingErrorMessage(Exception e) {

        log("error loading cache from file: " + e.toString());
    }

}
