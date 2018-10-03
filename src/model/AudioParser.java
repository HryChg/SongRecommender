package model;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import java.io.*;


public class AudioParser {

    //REQUIRES: input File has to be of audio/mpeg format
    //EFFECTS: convert the audio file to Song
    public Song parseFileToSong(File file){
        try {
            InputStream input = new FileInputStream(file);
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

//            // The greyed out section print out all the meta data
//            // List all metadata
//            String[] metadataNames = metadata.names();

//            for(String name : metadataNames){
//                System.out.println(name + ": " + metadata.get(name));
//            }

//            // Retrieve the necessary info from metadata
//            // Names - title, xmpDM:artist etc. - mentioned below may differ based
//            System.out.println("----------------------------------------------");
//            System.out.println("Title: " + metadata.get("title"));
//            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
//            System.out.println("Composer : "+metadata.get("xmpDM:composer"));
//            System.out.println("Genre : "+metadata.get("xmpDM:genre"));
//            System.out.println("Album : "+metadata.get("xmpDM:album"));

            Song currentSong = new Song("noName");
            currentSong.setSongName(metadata.get("title"));

            return currentSong;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }

        //This is a placeholder, Java ask me to put a return statement here
        return null;

    }

}