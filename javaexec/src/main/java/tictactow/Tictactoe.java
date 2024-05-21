package tictactow;

import java.util.Scanner;

/**
 * Project: (Single Dev): Tic-Tac-Toe with AI Opponent
 *  Basic Implementation
 *      Implement the basic rules of Tic-Tac-Toe.
 *      Players take turns placing their symbol (X or O) on a 3x3 grid.
 *      Determine the winner or detect a draw.
 *  Adjustable Grid Size:
 *      Allow the user to choose the grid size (e.g., 3x3, 4x4, 5x5).
 *      Dynamically create the game board based on the selected size.
 *      Adjust the winning conditions based on the grid size.
 *  Timed Moves:
 *      Introduce a time limit for each player's move.
 *      Display a countdown timer for each player's turn.
 *      If a player exceeds the time limit, they forfeit their turn, and the other player makes the next move.
 *  AI Opponent with Difficulty Levels:                         
 *      Implement an AI opponent with different difficulty levels (easy, medium, hard).
 *      Easy: The AI makes random moves.
 *      Medium: The AI uses a basic strategy to block the player's winning moves and make its own winning moves.
 *      Hard: The AI uses advanced algorithms like the minimax algorithm to make optimal moves. https://www.neverstopbuilding.com/blog/minimax
 *
 */
public class Tictactoe 
{
    static private BoardPiece[] boardMap;
    static private Scanner reader = new Scanner(System.in);

    // This enum represents tictactoe pieces or the lack of it.
    enum BoardPiece {
        EMPTY,
        X,
        O
    }

    // Used for printing the board to the terminal.
    static void printBoard() {
        
        for(int i = 0; i < boardMap.length; i++) {
            int rowSize = (int) Math.sqrt(boardMap.length);
            BoardPiece element = boardMap[i];
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

    }

    // Initialize the board size using user input.
    static void initializeBoardSize() {
        
        int size;
        while(true) {
            System.out.print("> ");

            // Catch string input.
            try {
                size = Integer.parseInt(reader.nextLine());
                break;
            } catch(NumberFormatException ex) {
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

    // Checks if a player has won following a piece placement.
    // static boolean checkIfPlayerWon() {
        
    //     if(checkValidRow())
    // }

    // Check if a row is filled/valid for victory.
    // static boolean checkValidRow() {
    //     for(int i = 0; i < boardMap.length; i++)
    // }

    // Check if a column is filled/valid for victory.
    // static boolean checkValidColumn() {

    // }

    // Check if a diagonal is filled/valid for victory.
    // static boolean checkValidDiagonal() {

    // }

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

    // Gather a position from the user and check if its a valid position
    // via validPosition(). If so, call insertPiece(). If not, ask the user
    // for another position.
    static void gatherPlayerPlacement() {

        int index;
        while(true) {
            System.out.print("> ");
            
            try {
                index = Integer.parseInt(reader.nextLine())-1;

            } catch(NumberFormatException ex) {
                System.out.println("Invalid Input. Please enter the position of the box. (1, 2, 3, etc.)");
                continue;
            }

            if(index >= boardMap.length)
                System.out.println("The position given is greater than the size of the board. Please try again.");
            else if(index < 0)
                System.out.println("The position given is smaller than the size of the board. Please try again.");
            else if(!validPosition(index))
                System.out.println("The position already has an X or O. Please try again.");
            else break;
        }

        insertPiece(index, BoardPiece.X);

    }

    public static void main( String[] args )
    {
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("What is your designated board size?");
        System.out.println("For 3x3 type 3, 4x4 type 4, etc.");
        
        initializeBoardSize();
        printBoard();

        System.out.println("Player X's turn. Where would you like to place your piece? (Type 1, 2, 3, etc.)");
        
        while(!checkIfPlayerWon()) {
            gatherPlayerPlacement();
            printBoard();
            
        }
    }
}