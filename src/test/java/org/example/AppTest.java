package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest2 {
    @Test
    void testPrintNumbers() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        App.printNumbers(3);

        String expected = "i = 1" + System.lineSeparator() +
                "i = 2" + System.lineSeparator() +
                "i = 3" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }
}