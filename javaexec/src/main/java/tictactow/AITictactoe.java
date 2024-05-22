package tictactow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Project: (Single Dev): Tic-Tac-Toe with AI Opponent
 *  Basic Implementation
 *      Implement the basic rules of Tic-Tac-Toe.                                                                                                       X
 *      Players take turns placing their symbol (X or O) on a 3x3 grid.                                                                                 X
 *      Determine the winner or detect a draw.                                                                                                          X
 *  Adjustable Grid Size:
 *      Allow the user to choose the grid size (e.g., 3x3, 4x4, 5x5).                                                                                   X
 *      Dynamically create the game board based on the selected size.                                                                                   X
 *      Adjust the winning conditions based on the grid size.                                                                                           X
 *  Timed Moves:
 *      Introduce a time limit for each player's move.                                                                                                  X
 *      Display a countdown timer for each player's turn.                                                                                               X
 *      If a player exceeds the time limit, they forfeit their turn, and the other player makes the next move.                                          X
 *  AI Opponent with Difficulty Levels:                         
 *      Implement an AI opponent with different difficulty levels (easy, medium, hard).
 *      Easy: The AI makes random moves.
 *      Medium: The AI uses a basic strategy to block the player's winning moves and make its own winning moves.
 *      Hard: The AI uses advanced algorithms like the minimax algorithm to make optimal moves. https://www.neverstopbuilding.com/blog/minimax
 *
 */
public class AITictactoe 
{
    static private BoardPiece[] boardMap;                       // An array that represents the board layout of tic-tac-toe
    static private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));     // Console Input Reader
    static private int AIDiff;

    // This enum represents tic-tac-toe pieces or the lack of it.
    enum BoardPiece {
        EMPTY,
        X,
        O
    }

    public static int getBoardMapLength() {
        return boardMap.length;
    }

    // Used for printing the board to the terminal.
    static void printBoard() {
        
        System.out.println();
        for(int i = 0; i < boardMap.length; i++) {

            // Gather size and specific element.
            int rowSize = (int) Math.sqrt(boardMap.length);
            BoardPiece element = boardMap[i];

            // Print the specific piece.
            if(element == BoardPiece.EMPTY) System.out.print(" ");
            else if(element == BoardPiece.X) System.out.print("X");
            else System.out.print("O");

            if((i+1) % rowSize == 0) {
                System.out.println();
                String separator = "-";
                String line = separator.repeat(rowSize + rowSize-1);
                System.out.println(line);
            }
            else System.out.print("|");
        }
        System.out.println();
    }

    // Initialize the board size using user input.
    static void initializeBoardSize() {
        
        int size;
        System.out.println("What is your designated board size?");
        System.out.println("For 3x3 type 3, 4x4 type 4, etc.");
        while(true) {
            System.out.print("> ");

            // Catch string input.
            try {
                size = Integer.parseInt(reader.readLine());
                if(size < 3) {
                    System.out.println("The board size must be 3x3 minimum. Please try again.");
                    continue;
                }
                break;
            } catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number.");
                continue;
            }

        }

        boardMap = new BoardPiece[size*size];
        for(int i = 0; i < boardMap.length; i++) {
            boardMap[i] = BoardPiece.EMPTY;
        }
        System.out.println("Board Size is set!");
    }

    // Initialize the board size using parameter size.
    static void initializeBoardSize(int size) {
        if(size < 3) {
            System.out.println("Board size must be a minimum of 3x3.");
            return;
        }

        boardMap = new BoardPiece[size*size];
        for(int i = 0; i < boardMap.length; i++) {
            boardMap[i] = BoardPiece.EMPTY;
        }
        System.out.println("Board Size is set!");
    }

    static void setAIDifficulty() {
        System.out.println("Enter an AI difficulty setting.");
        System.out.println("1. Easy\n2. Medium\n3. Hard");

        int response;
        while(true) {

            System.out.print("> ");

            try {
                response = Integer.parseInt(reader.readLine());
            } catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number 1-3.");
                continue;
            }

            if(response > 3 || response < 1) 
                System.out.println("Invalid Input. Please enter a number 1-3.");
            else {
                AIDiff = response;
                break;
            }
        }

    }

    // Checks if a player has won following a piece placement.
    static boolean checkIfPlayerWon() {
        
        int size = (int) Math.sqrt(boardMap.length);
        
        // We need to check by row. Ex: assuming 3x3 we send index 0,3,6
        // so that checkValidRow() checks every piece on that row.
        for(int i = 0; i < size; i++) {
            if(checkValidRow(i*size))
                return true;
        }

        // We need to check by column. We check by reading
        // top down so we send the index of every piece on
        // the top row. Ex: assuming 3x3 .. send 0 1 2
        for(int i = 0; i < size; i++) {
            if(checkValidColumn(i))
                return true;
        }

        // We need to check by diagonal. We use checkLeftDiagonal()
        // and checkRightDiagonal() to confirm if there was a victory
        // via diagonal.
        if(checkLeftDiagonal() || checkRightDiagonal())
            return true;

        return false;
    }

    // Check if a row is filled/valid for victory.
    static boolean checkValidRow(int row) {

        BoardPiece startingPiece = boardMap[row];
        if(startingPiece == BoardPiece.EMPTY) return false;

        int size = (int) Math.sqrt(boardMap.length);
        for(int i = row+1; i < row+size; i++) {
            if(boardMap[i] != startingPiece) return false;
        }

        return true;
    }

    // Check if a column is filled/valid for victory.
    static boolean checkValidColumn(int column) {
        
        BoardPiece startingPiece = boardMap[column];
        if(startingPiece == BoardPiece.EMPTY) return false;

        int size = (int) Math.sqrt(boardMap.length);
        for(int i = column+size; i < boardMap.length; i=i+size) {
            if(boardMap[i] != startingPiece) return false;
        }

        return true;
    }

    // Check if the diagonal starting from the top left going
    // to bottom right has been filled by the same piece.
    static boolean checkLeftDiagonal() {

        int size = (int) Math.sqrt(boardMap.length);
        BoardPiece leftDiag = boardMap[0];
        if(leftDiag == BoardPiece.EMPTY) return false;

        if(leftDiag != BoardPiece.EMPTY) {
            for(int i = size+1; i < boardMap.length; i=i+size+1) {
                if(boardMap[i] != leftDiag) return false;
            }
        }

        return true;
    }

    // Check if the diagonal starting from the top right going
    // to bottom left has been filled by the same piece.
    static boolean checkRightDiagonal() {

        int size = (int) Math.sqrt(boardMap.length);
        BoardPiece rightDiag = boardMap[size-1];
        if(rightDiag == BoardPiece.EMPTY) return false;

        if(rightDiag != BoardPiece.EMPTY) {
            for(int i = (size-1)*2; i < boardMap.length-(size-1); i=i+(size-1)) {
                if(boardMap[i] != rightDiag) return false;
            }
        }

        return true;
    }

    // Insert a proper piece (X or O) into the board.
    static void insertPiece(int index, BoardPiece piece) {

        if(piece == BoardPiece.EMPTY) {
            System.out.println("You can not insert an empty piece. Insert an X or O game piece.");
            return;
        }

        boardMap[index] = piece;
    }

    // Check if specified position is empty or not.
    static boolean validPosition(int index) {
        if(boardMap[index] != BoardPiece.EMPTY)
            return false;
        else
            return true;
    }

    // Check if the board is full of valid pieces (X or O).
    static boolean boardIsFull() {
        
        for(BoardPiece element: boardMap) {
            if(element == BoardPiece.EMPTY) return false;
        }

        return true;
    }

    // Gather a position from the user and check if its a valid position
    // via validPosition(). If so, call insertPiece(). If not, ask the user
    // for another position.
    static void gatherPlayerPlacement() {

        // Swap turns!

        int index = -1;
        int timeLimit = 10;
        System.out.println("Player's turn. Where would you like to place your piece?  You have " + timeLimit + " seconds! (Type 1, 2, 3, etc.)");

        // Call alternate threads to handle input and timer at the same time.
        // Let the main thread sleep until the alternates are completed with
        // their task.
        InputTimer timedReader = new InputTimer(timeLimit);
        while(timedReader.getPosition() == -1) {
            try {
                Thread.sleep(16);
            } catch(InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        index = timedReader.getPosition();
        if(index == -2) return;

        insertPiece(index, BoardPiece.X);

    }

    // AITurn() will simulate a turn made by the AI.
    // Should insert a piece based on the difficulty setting
    // and check if the AI won following that placement.
    static void AITurn() {

        if(AIDiff == 1) easyAIPlacement();
        else if(AIDiff == 2) mediumAIPlacement();
        else hardAIPlacement();


    }

    static void easyAIPlacement() {

    }

    static void mediumAIPlacement() {

    }

    static void hardAIPlacement() {

    }

    public static void run()
    {
        System.out.println("\nWelcome to Multiplayer Tic-Tac-Toe!");
        
        initializeBoardSize();
        setAIDifficulty();
        printBoard();
        
        while(!boardIsFull()) {
            gatherPlayerPlacement();
            printBoard();

            if(checkIfPlayerWon()) {
                System.out.println("Player has won!");
                try {Thread.sleep(1000);}
                catch(InterruptedException ex) {throw new RuntimeException(ex);}
                System.out.println("Closing Multiplayer Tic-Tac-Toe...");
                return;
            }

            AITurn();
        }

        try {Thread.sleep(1000);}
        catch(InterruptedException ex) {throw new RuntimeException(ex);}
        System.out.println("It's a draw!");

    }
}