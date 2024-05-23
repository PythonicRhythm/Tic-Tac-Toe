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
 *  AI Opponent with Difficulty Levels:                         
 *      Implement an AI opponent with different difficulty levels (easy, medium, hard).                                                                 X
 *      Easy: The AI makes random moves.                                                                                                                X
 *      Medium: The AI uses a basic strategy to block the player's winning moves and make its own winning moves.                                        X
 *      Hard: The AI uses advanced algorithms like the minimax algorithm to make optimal moves. https://www.neverstopbuilding.com/blog/minimax
 *
 */
public class AITictactoe 
{
    static private BoardPiece[] boardMap;                       // An array that represents the board layout of tic-tac-toe.
    static private BufferedReader reader = new BufferedReader   // Console input reader.
                        (new InputStreamReader(System.in));     
    static private int AIDiff;                                  // Keeps track of the difficulty setting.
    static private int AITarget = -1;                           // Keeps track of target row for AI.
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

    // Returns the length/width of the board.
    public static int getBoardLength() {
        return size;
    }

    // printBoard() will print a representation of the board
    // and its tiles into the terminal.
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

        // Initialize the BoardPiece array and set all elements as empty.
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

    // setAIDifficulty() prompts the user for the difficulty setting
    // represented by an integer value and puts that setting into
    // member variable AIDiff
    static void setAIDifficulty() {
        System.out.println("\nEnter an AI difficulty setting.");
        System.out.println("1. Easy\n2. Medium\n3. Hard");

        int response;
        while(true) {

            System.out.print("> ");

            // Catch any string input from user.
            try {
                response = Integer.parseInt(reader.readLine());
            } 
            // Let the user know only numbers are allowed.
            catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number 1-3.");
                continue;
            }

            // Only 1,2,3 are valid responses from the user.
            if(response > 3 || response < 1) 
                System.out.println("Invalid Input. Please enter a number 1-3.");
            else {
                AIDiff = response;
                break;
            }
        }

    }

    // Checks if a player has won following a piece placement.
    static boolean checkForVictory() {
        
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

    // Check if a specified position is empty or not.
    static boolean validPosition(int index) {

        // If index provided greater than the size of board.
        if(index >= boardMap.length) {
            System.out.println("The position given is greater than the size of the board.");
            return false;
        }
        // If index provided smaller than the size of board.
        else if (index < 0) {
            System.out.println("The Position given is smaller than the size of the board.");
            return false;
        }
        // If index provided is already filled by a proper piece.
        else if(boardMap[index] != BoardPiece.EMPTY) {
            System.out.println("The position already has an X or O.");
            return false;
        }

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

    // Gather a position from the user and check if its a valid position. 
    // If so, call insertPiece(). If not, the user is prompted for another
    // valid position to slot in a game piece.
    static void gatherPlayerPlacement() {

        int index;
        System.out.println("Player's turn. Where would you like to place your piece? (Type 1, 2, 3, etc.)");

        while(true) {
            System.out.print("> ");

            // Catch any strings sent by the user.
            try {
                index = Integer.parseInt(reader.readLine())-1;
            } 
            // Let the user know that strings are not allowed and we are looking for a number. 
            catch(IOException | NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter a number.");
                continue;
            }

            // Validate the index provided by the user and make sure
            // its a valid position on the board.
            if(validPosition(index))
                break;
        }

        insertPiece(index, BoardPiece.X);

    }

    // AITurn() will simulate a turn made by the AI.
    // Should insert a piece based on the difficulty setting
    // and check if the AI won following that placement.
    static void AITurn() {

        if(AIDiff == 1)
            easyAIPlacement();
        else if(AIDiff == 2)
            mediumAIPlacement();
        else {
            System.out.println("Hard AI not implemented yet ... dropping to Medium Difficulty.");
            mediumAIPlacement();
            // hardAIPlacement(); // Not implemented.
        }

    }

    // Easy AI placement strategy involves placing randomly.
    static void easyAIPlacement() {

        while (true) {

            // Grab a random position on the board.
            int position = (int) (Math.random() * boardMap.length);
            // if its empty, place an O in the position
            // else the while loop continues.
            if(boardMap[position] == BoardPiece.EMPTY) {
                insertPiece(position, BoardPiece.O);
                break;
            }

        }
    }

    // Medium AI placement strategy is a simple defensive strategy which
    // involves blocking the third in a row if needed, else placing in a specific path.
    static void mediumAIPlacement() {
        
        System.out.println("Your opponent is making his move!");

        // Choose a row if none was selected before.
        if(AITarget == -1) AITarget = (int) (Math.random()*size);

        // Defensive Strategy
        // Find a possible 3 piece victory from the player
        // and close it. If one wasnt found, proceed to
        // offensive strategy.

        for(int i = 0; i < size; i++) {
            int slotToClose = checkPlayerRow(i*size);
            if(slotToClose != -1) {
                insertPiece(slotToClose, BoardPiece.O);
                return;
            }
        }

        // Offensive Strategy
        // Select a row and keep placing on that 
        // row unless no avenue for victory.
        // If blocked, switch to another row.
        // If all rows blocked, place randomly.
        
        // If the player blocks the AI's row, switch to
        // a random target row.
        if(countPlayerPiecesOnRow(AITarget*size) > 0) AITarget = (int) (Math.random()*size);

        int rowStartIndex = AITarget*size;

        // Keep placing on a selected row.
        for(int i = 0; i < size; i++) {   // For every item in row.
            BoardPiece currPosition = boardMap[rowStartIndex+i];

            // If there is an empty slot in the target row, place a piece.
            if(currPosition == BoardPiece.EMPTY) {
                insertPiece(rowStartIndex+i, BoardPiece.O);
                return;
            }

        }

        // If all rows blocked, place randomly.
        while (!boardIsFull()) {
            int position = (int) (Math.floor(Math.random() * boardMap.length));
            if(boardMap[position] == BoardPiece.EMPTY) {
                insertPiece(position, BoardPiece.O);
                break;
            }
        }

    }

    // Count the amount of player pieces on row.
    // Used to let MediumAIPlacement know to move on
    // to next row.
    static int countPlayerPiecesOnRow(int row) {
        int count = 0;
        for(int i = row+1; i < row+size; i++) {
            if(boardMap[i] == BoardPiece.X) count++; 
        }
        return count;
    }

    // Check the amount of player tokens on a row,
    // if it gets one before winning on a row, return
    // the index of the position needed to block the player.
    static int checkPlayerRow(int row) {
        
        int emptySlot = -1;
        int amountOfPlayertoken = 0;

        // for every tile in a given row.
        for(int i = row; i < row+size; i++) {
            if(boardMap[i] == BoardPiece.X) amountOfPlayertoken++;  // track amount of player tokens
            else if(boardMap[i] == BoardPiece.EMPTY) emptySlot = i; // track a free slot in that row
        }

        // If the row is almost filled by the player
        // return the specific empty slot so that
        // the AI can place it's piece there.
        if(amountOfPlayertoken >= size-1) return emptySlot;
        else return -1;

    }

    // Future Algorithm for hardAI
    static void hardAIPlacement() {
        return;
    }

    // run() manages the Main Loop of tic-tac-toe.
    // initializes the board and difficulty then enters
    // the main loop which places player pieces and AI
    // pieces while keeping track of win conditions.
    public static void run()
    {
        System.out.println("\nWelcome to AI Tic-Tac-Toe!");
        
        // Initialize game settings.
        initializeBoardSize();
        setAIDifficulty();

        // update with visual representation.
        printBoard();
        
        // Main Game Loop which only breaks
        // when a draw occurs (the board is full with no victory)
        while(!boardIsFull()) {

            // Begin with the player's turn. Gather input
            // and place player piece.
            gatherPlayerPlacement();
            printBoard();

            // Check if the player won.
            if(checkForVictory()) {
                System.out.println("Player has won!");
                try {Thread.sleep(900);}
                catch(InterruptedException ex) {throw new RuntimeException(ex);}
                System.out.println("Closing Multiplayer Tic-Tac-Toe...");
                return;
            }

            // AI's turn follows the player.
            // Based on difficulty strategy, place a piece.
            AITurn();
            printBoard();

            // Check if the AI won.
            if(checkForVictory()) {
                System.out.println("Player has lost!");
                try {Thread.sleep(900);}
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