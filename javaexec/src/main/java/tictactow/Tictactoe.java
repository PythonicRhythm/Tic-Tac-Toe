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
 *
 */
public class Tictactoe 
{
    static private BoardPiece[] boardMap;                       // An array that represents the board layout of tic-tac-toe
    static private BufferedReader reader = new 
            BufferedReader(new InputStreamReader(System.in));   // Console Input Reader
    static private BoardPiece currentPlayer = BoardPiece.O;     // keeps track of the current player's to manage turns.
    static private int size;                                    // The length and width of the tic-tac-toe board.

    // This enum represents tic-tac-toe pieces or the lack of it.
    enum BoardPiece {
        EMPTY,
        X,
        O
    }
    
    // Returns the amount of slots in the board.
    public static int getBoardPieceAmount() {
        return boardMap.length;
    }

    // Used for printing the board to the terminal.
    static void printBoard() {
        
        System.out.println();

        // For every tile in the board
        for(int i = 0; i < boardMap.length; i++) {

            // Gather size and specific element.
            BoardPiece element = boardMap[i];

            // Print the specific piece.
            if(element == BoardPiece.EMPTY) System.out.print(" ");
            else if(element == BoardPiece.X) System.out.print("X");
            else System.out.print("O");

            // if the tile is at the end of the board
            // create a separator line.
            if((i+1) % size == 0) {
                System.out.println();
                String separator = "-";
                String line = separator.repeat(size + size-1);
                System.out.println(line);
            }
            else System.out.print("|");
        }
        System.out.println();
    }

    // Initialize the board size using user input.
    static void initializeBoardSize() {
        
        int length;
        System.out.println("What is your designated board size?");
        System.out.println("For 3x3 type 3, 4x4 type 4, etc.");
        while(true) {
            System.out.print("> ");

            // Catch string input given by the user.
            try {
                length = Integer.parseInt(reader.readLine());
                if(length < 3) {
                    System.out.println("The board size must be 3x3 minimum. Please try again.");
                    continue;
                }
                break;
            } 
            // Let the user know that only numbers are allowed.
            catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number.");
                continue;
            }

        }

        boardMap = new BoardPiece[length*length];
        size = length;
        for(int i = 0; i < boardMap.length; i++) {
            boardMap[i] = BoardPiece.EMPTY;
        }
        System.out.println("Board Size is set!");
    }

    // Initialize the board size using parameter size.
    static void initializeBoardSize(int length) {

        // The minimum board size is a 3x3
        if(length < 3) {
            System.out.println("Board size must be a minimum of 3x3.");
            return;
        }

        // Initialize the BoardPiece array and set all elements as empty.
        boardMap = new BoardPiece[length*length];
        size = length;
        for(int i = 0; i < boardMap.length; i++) {
            boardMap[i] = BoardPiece.EMPTY;
        }
        System.out.println("Board Size is set!");
    }

    // Checks if a player has won following a piece placement.
    static boolean checkIfPlayerWon() {
        
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

        // if any of the row pieces do not match, no victory
        for(int i = row+1; i < row+size; i++) {
            if(boardMap[i] != startingPiece) return false;
        }

        return true;
    }

    // Check if a column is filled/valid for victory.
    static boolean checkValidColumn(int column) {
        
        BoardPiece startingPiece = boardMap[column];
        if(startingPiece == BoardPiece.EMPTY) return false;

        // if any of the column pieces do not match, no victory
        for(int i = column+size; i < boardMap.length; i=i+size) {
            if(boardMap[i] != startingPiece) return false;
        }

        return true;
    }

    // Check if the diagonal starting from the top left going
    // to bottom right has been filled by the same piece.
    static boolean checkLeftDiagonal() {

        BoardPiece leftDiag = boardMap[0];
        if(leftDiag == BoardPiece.EMPTY) return false;

        // if any of the diagonal pieces do not match, no victory
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

        BoardPiece rightDiag = boardMap[size-1];
        if(rightDiag == BoardPiece.EMPTY) return false;

        // if any of the diagonal pieces do not match, no victory
        if(rightDiag != BoardPiece.EMPTY) {
            for(int i = (size-1)*2; i < boardMap.length-(size-1); i=i+(size-1)) {
                if(boardMap[i] != rightDiag) return false;
            }
        }

        return true;
    }

    // Insert a proper piece (X or O) into the board.
    static void insertPiece(int index, BoardPiece piece) {

        // Do not allow the placement of empty pieces.
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
        
        // for every tile, if one of them is empty,
        // the board is not full.
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
        if(currentPlayer == BoardPiece.X) currentPlayer = BoardPiece.O;
        else currentPlayer = BoardPiece.X;

        int index = -1;         // index of the tile which will be filled
        char playerToken;       // the current players token
        int timeLimit = 10;     // the amount of time the user has to pick a choice.
        
        if(currentPlayer == BoardPiece.X) playerToken = 'X';
        else playerToken = 'O';
        
        System.out.println("Player " + playerToken + "'s turn. Where would you like to place your piece?  You have " + timeLimit + " seconds! (Type 1, 2, 3, etc.)");

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

        insertPiece(index, currentPlayer);

    }

    // run() manages the Main Loop of tic-tac-toe.
    // initializes the board size then enters
    // the main loop which places player pieces by turn
    // and after an insertion checks if the current 
    // player won.
    public static void run()
    {
        System.out.println("\nWelcome to Multiplayer Tic-Tac-Toe!");
        
        // initialize board size
        initializeBoardSize();

        // update with visual representation.
        printBoard();
        
        // Main Game Loop which only breaks
        // when a draw occurs (the board is full with no victory)
        while(!boardIsFull()) {

            // Begin with the current player's turn. 
            // Gather input and place player piece.
            gatherPlayerPlacement();
            printBoard();

            // Check if the current player won after insertion.
            if(checkIfPlayerWon()) {

                char playerToken;
                
                if(currentPlayer == BoardPiece.X) playerToken = 'X';
                else playerToken = 'O';
                
                System.out.println(playerToken + " player has won!");
                try {Thread.sleep(1000);} // slow down terminal output for a second
                catch(InterruptedException ex) {throw new RuntimeException(ex);}
                System.out.println("Closing Multiplayer Tic-Tac-Toe...");
                return;
            }
        }

        // If the main loop breaks, a draw has occurred.
        try {Thread.sleep(1000);}
        catch(InterruptedException ex) {throw new RuntimeException(ex);}
        System.out.println("It's a draw!");

    }
}