package model;

import Observer.Observer;
import Observer.Subject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebParser extends Subject{
    private int characterCount = 0;


    //MODIFIES:
    //EFFECTS: return extracted Web Data from theURL
    public StringBuilder ExtractWebData(String theURL) throws IOException {
        BufferedReader br = null;

        try {
            URL url = new URL(theURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {


                //notify Observers when seeing the string "210"
                if (line.contains("210")){
                    notifyObservers();
                }

                sb.append(line);
                sb.append(System.lineSeparator());

                //keep statistics on character length of the webData
                characterCount = sb.length();
                if (characterCount>2){
                    System.out.println("WOW, there are more than "+characterCount+" characters!");
                }

            }

            System.out.println("\nExtract Web Data Completes!");
            return sb;

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers){
            observer.update();
        }
    }
}

