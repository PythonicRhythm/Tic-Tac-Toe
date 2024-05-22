package tictactow;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for TicTacToe and AITicTacToe
 */
public class TictactoeTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testingBoardSizeInitialization()
    {
        Tictactoe.initializeBoardSize(3);
        assertTrue(Tictactoe.getBoardMapLength() == 3*3);

        Tictactoe.initializeBoardSize(4);
        assertTrue(Tictactoe.getBoardMapLength() == 4*4);

        Tictactoe.initializeBoardSize(5);
        assertTrue(Tictactoe.getBoardMapLength() == 5*5);

        Tictactoe.initializeBoardSize(10);
        assertTrue(Tictactoe.getBoardMapLength() == 10*10);
    }
}
