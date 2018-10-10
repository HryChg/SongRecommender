package model;


public abstract class AbstractReadableWritable implements Printable{
    public abstract void readFromFile(String fileLocation);
    public abstract void writeToFile(String myData);
}
