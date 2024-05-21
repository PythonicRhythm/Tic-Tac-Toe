package tictactow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InputTimer {

    // private final int timeLimit;
    // private boolean killFlag;

    // InputTimer(int timeLimit) {
    //     this.timeLimit = timeLimit;
    //     killFlag = false;
    // }

    // public void kill() {
    //     this.killFlag = true;
    // }

    // public void run() {

    //     try {
    //         for(int i = timeLimit; i >= 0; i--) {
    //             Thread.sleep(1000);         // sleep for a second
    //             System.out.println("Timer: " + i); // display time left. 
    //         }
    //         System.out.println("Ran out of time!");
    //     } catch(InterruptedException ex) {
    //         throw new RuntimeException(ex);
    //     }
    // }

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final int timeLimit;
    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
    private int position;

    InputTimer(int timeLimit) {
        this.timeLimit = timeLimit;
        position = -1;

        threadPool.submit(() -> handleInput());
        threadPool.submit(() -> handleTimer());

        try {
            threadPool.awaitTermination(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getPosition() {
        return position;
    }

    // handle all the player inputs for positions on the board.
    private void handleInput() {
        try {
            int input;
            while(true) {
                while(!reader.ready()) 
                {Thread.sleep(16);}
                try {
                    input = Integer.parseInt(reader.readLine())-1;
                } catch(NumberFormatException ex) {
                    System.out.println("Invalid Input. Please enter the position of the box. (1, 2, 3, etc.)");
                    continue;
                }

                if(input >= Tictactoe.getBoardMapLength())
                    System.out.println("The position given is greater than the size of the board. Please try again.");
                else if(input < 0)
                    System.out.println("The position given is smaller than the size of the board. Please try again.");
                else if(!Tictactoe.validPosition(input))
                    System.out.println("The position already has an X or O. Please try again.");
                else break;
            }
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
                Thread.sleep(1500);         // sleep for a second
                System.out.println("Timer: " + i); // display time left. 
            } catch(InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        System.out.println("Time has run out! Switching turns!");
        position = -2;
    }
    
}