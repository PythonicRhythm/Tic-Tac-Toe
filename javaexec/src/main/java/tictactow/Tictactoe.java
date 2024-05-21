package tictactow;

import java.util.Scanner;

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
public class Tictactoe 
{
    static private BoardPiece[] boardMap;                       // An array that represents the board layout of tic-tac-toe
    static private Scanner reader = new Scanner(System.in);     // Console Input Reader
    static private BoardPiece currentPlayer = BoardPiece.X;     // currentPlayer will help us keep track of who's turn it currently is.
                                                                // By default player X goes first.

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
                size = Integer.parseInt(reader.nextLine());
                if(size < 3) {
                    System.out.println("The board size must be 3x3 minimum. Please try again.");
                    continue;
                }
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
        if(currentPlayer == BoardPiece.X) currentPlayer = BoardPiece.O;
        else currentPlayer = BoardPiece.X;

        int index = -1;
        char playerToken;
        if(currentPlayer == BoardPiece.X) playerToken = 'X';
        else playerToken = 'O';
        System.out.println("Player " + playerToken + "'s turn. Where would you like to place your piece? (Type 1, 2, 3, etc.)");

        InputTimer timedReader = new InputTimer(20);
        while(timedReader.getPosition() == -1) {
            try {
                Thread.sleep(16);
            } catch(InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        index = timedReader.getPosition();
        if(index == -2) return;
            
            // try {
            //     index = Integer.parseInt(reader.nextLine())-1;
            // } catch(NumberFormatException ex) {
            //     System.out.println("Invalid Input. Please enter the position of the box. (1, 2, 3, etc.)");
            //     continue;
            // }

            // if(index >= boardMap.length)
            //     System.out.println("The position given is greater than the size of the board. Please try again.");
            // else if(index < 0)
            //     System.out.println("The position given is smaller than the size of the board. Please try again.");
            // else if(!validPosition(index))
            //     System.out.println("The position already has an X or O. Please try again.");
            // else break;
            // break;

        insertPiece(index, currentPlayer);

    }

    public static void main( String[] args )
    {
        System.out.println("Welcome to Tic-Tac-Toe!");
        
        initializeBoardSize();
        printBoard();
        
        while(!boardIsFull()) {
            gatherPlayerPlacement();
            printBoard();

            if(checkIfPlayerWon()) {
                char playerToken;
                if(currentPlayer == BoardPiece.X) playerToken = 'X';
                else playerToken = 'O';
                System.out.println(playerToken + " player has won!");
                return;
            }
        }

        System.out.println("It's a draw!");

    }
}