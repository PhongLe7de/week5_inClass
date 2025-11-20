package org.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many numbers to display in the loop? ");
        int n = scanner.nextInt();
        scanner.close();

        printNumbers(n);
    }

    public static void printNumbers(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.println("i = " + i);
        }
    }
}