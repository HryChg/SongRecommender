package tests;

import model.Printable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class abstractTestPrint {

    //REQUIRES: p is already initialized;
    //EFFECTS: verify the printing of a statement
    protected void verifyPrint(Printable p, String expectedOutput) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.
        p.print();
        assertEquals(expectedOutput, outContent.toString());
    }

}
