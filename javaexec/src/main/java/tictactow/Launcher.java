package tictactow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Launcher {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("\nWelcome to Tic-Tac-Toe!");
            System.out.println("\n1. Multiplayer Mode\n2. AI Mode\n3. Exit");
            System.out.print("\n> ");

            int response;
            try {
                response = Integer.parseInt(reader.readLine());
            } catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number.");
                continue;
            }

            if(response == 3) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            else if(response == 1) {
                System.out.println("Launching Multiplayer Tic-tac-toe...");
                Tictactoe.run();
            }
            else if(response == 2) {
                System.out.println("Launching AI Tic-tac-toe...");
                AITictactoe.run();
            }
            else {
                System.out.println("Invalid response. Please enter a number from 1-3.");
            }
        }
    }

}
