package model;

public abstract class AbstractReadableWritable implements Printable{

    //MODIFIES: this
    //EFFECTS: open a file from the location specified as string
    public abstract void readFromFile(String fileLocation);


    //REQUIRES: myData needs to be in the format of gson.toJson(object)
    //EFFECTS: save an object to a file under savedFiles folder
    public abstract void writeToFile (String myData) throws Exception;
}
