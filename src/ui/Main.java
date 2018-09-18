package ui;

import model.Song;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    // ---------------------------Field
    private ArrayList<Song> playList = new ArrayList<Song>();
    private Scanner scanner = new Scanner(System.in);


    // ---------------------------Constructor
    private Main() {

        // asking user to input songs and add it to a list, print list when done
        while (true) {
            System.out.println("Please input a song name:");
            String name = scanner.nextLine();
            Song curr_song = new Song();
            curr_song.setSongName(name);
            playList.add(curr_song);
            System.out.println("The songs <" + name + "> has been added");

            System.out.println("Continue to add more songs? (Y/N)");
            String answer = scanner.nextLine();


            if (answer.equals("Y")) {
                continue;
            } else if (answer.equals("N")) {
                break;
            } else {
                System.out.println("Sorry. Wrong Input.");
            }

        }

        // Printing out all songNames
        System.out.println("The current playlist (below): ");
        for(Song song: playList) {
            System.out.println("- "+song.getSongName());
        }
    }

    public static void main(String[] args) { new Main(); }

}
