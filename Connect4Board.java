// import java.util.ArrayList;

/**
 * RENERT CS2 : CONNECT 4
 * 
 * A simple implementation of the classic Connect Four game for us to study
 * and experiment with the minimax AI algorithm.
 * 
 * The Connect4Board class keeps track of the game board and state of play.
 * 
 * Authors:  Sonny Chan, CS2 2021
 * Date:    March 2021
 */

public class Connect4Board
{
    static final int ROWS = 6;
    static final int COLS = 7;

    // array of discs: -1 for empty, 0 or 1 as filled by player
    private int[][] discs = new int[ROWS][COLS];

    // current heights of each column on the board
    private int[] heights = new int[COLS];

    int turn = 0;       // keeps track of whose turn it is
    int lastRow = -1;   // row of last played piece
    int lastCol = -1;   // column of last played piece

    public Connect4Board() {
        clear();
    }

    public void clear() {
        // reset all state variables to initial values
        for (int c = 0; c < COLS; ++c) {
            for (int r = 0; r < ROWS; ++r) {
                discs[r][c] = -1;
            }
            heights[c] = 0;
        }
        turn = 0;
        lastRow = lastCol = -1;
    }

    public int[][] getDiscs() {
        return discs;
    }

    public int getTurn() {
        return turn;
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getLastCol() {
        return lastCol;
    }

    public boolean isFull() {
        // if we can no longer drop a disc in any column, the board is full
        for (int c = 0; c < COLS; ++c) {
            if (canDrop(c)) return false;
        }
        return true;
    }

    public boolean canDrop(int col) {
        return heights[col] < ROWS;
    }

    /**
     * Play a disc in the given column and advance the turn.
     * 
     * @param integer   the column to play the disc in
     * @return      true if the play was value, false otherwise
     */
    public boolean dropDisc(Integer integer) {
        int row = heights[integer];
        if (row < ROWS) {
            discs[row][integer] = turn;
            ++heights[integer];
            turn = 1 - turn;
            lastRow = row;
            lastCol = integer;
            return true;    
        }
        return false;
    }

    /**
     * Removes the top disc from the given column, and toggles the turn.
     * This method is useful for the AI to explore different moves.
     * WARNING: No error checking is done in this method!
     * 
     * @param col   the column from which to remove a disc
     */
    public void undropDisc(int col) {
        --heights[col];
        int row = heights[col];
        discs[row][col] = -1;
        turn = 1 - turn;
    }
}
