package tictactow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * InputTimer is used to create multiple threads managed in one thread pool
 * to gather input while keep track of a time limit at the same time.
 * Used to limit players to only have a certain amount of seconds before
 * their turn is skipped. 
 */
public class InputTimer {

    private final BufferedReader reader = new               // Reads console input.
        BufferedReader(new InputStreamReader(System.in));
    private final int timeLimit;                            // amount of time user has to type a position on the board.
    private final ExecutorService threadPool =              // Keep track of pool of threads, used to create multiple
        Executors.newVirtualThreadPerTaskExecutor();        // threads using methods in this class.
    private int position;                                   // Position the player wants to place his tile.

    InputTimer(int timeLimit) {
        this.timeLimit = timeLimit;
        position = -1;

        // Create threads for the timer and input.
        threadPool.submit(() -> handleInput());
        threadPool.submit(() -> handleTimer());

        // terminate all threads in the pool after delay
        try {
            threadPool.awaitTermination(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    // return the position the user has selected
    public int getPosition() {
        return position;
    }

    // handle all the player inputs for positions on the board.
    private void handleInput() {
        try {
            int input;
            while(true) {
                
                // while the reader has nothing to read, keep waiting
                while(!reader.ready()) 
                {Thread.sleep(10);}

                // once there is something to read, grab it and confirm
                // its a number.
                try {
                    input = Integer.parseInt(reader.readLine())-1;
                } catch(NumberFormatException ex) {
                    System.out.println("Invalid Input. Please enter the position of the box. (1, 2, 3, etc.)");
                    continue;
                }

                // If index provided greater than the size of board.
                if(input >= Tictactoe.getBoardPieceAmount())
                    System.out.println("The position given is greater than the size of the board. Please try again.");
                // If index provided smaller than the size of board.
                else if(input < 0)
                    System.out.println("The position given is smaller than the size of the board. Please try again.");
                // If index provided is already filled by a proper piece.
                else if(!Tictactoe.validPosition(input))
                    System.out.println("The position already has an X or O. Please try again.");
                else break;
            }
            
            // once position gathered, shutdown thread pool.
            position = input;
            threadPool.shutdownNow();
        } catch(NumberFormatException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    // handle the time limit restriction on the players.
    private void handleTimer() {

        for(int i = timeLimit; i >= 0; i--) {
            try {
                Thread.sleep(1000); // sleep for a second
            } catch(InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        System.out.println("Time has run out! Switching turns!");
        position = -2;
    }
    
}