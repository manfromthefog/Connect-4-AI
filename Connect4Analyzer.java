/**
 * RENERT CS2 : CONNECT 4
 * 
 * A simple implementation of the classic Connect Four game for us to study
 * and experiment with the minimax AI algorithm.
 * 
 * The Connect4Analyzer class contains a few useful methods to analyze the
 * game board for useful patterns of discs.
 * 
 * Author:  CS2 Class 2021
 * Date:    March 2021
 */

public class Connect4Analyzer
{
    Connect4Board connect4;

    public Connect4Analyzer(Connect4Board board) {
        connect4 = board;
    }

    /**
     * Searches the board along horizontal, vertical, and diagonal lines for
     * the given pattern, counting matches.
     * 
     * @param pattern   array of 1's for match player's token, -1 for empty
     * @param player    the player to match
     * @return          number of occurrences of pattern found
     */
    public int countPattern(int[] pattern, int player) {
        int total = 0;

        int np = pattern.length;
        int nr = Connect4Board.ROWS;
        int nc = Connect4Board.COLS;
        int[][] discs = connect4.getDiscs();

        // horizontal and upward-diagonal
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c <= nc - np; ++c) {                
                for (int dr = 0; dr < (r + np <= nr ? 2 : 1); ++dr) {                    
                    int tally = 0;
                    for (int i = 0; i < np; ++i) {
                        int colour = discs[r+i*dr][c+i];
                        if (pattern[i] < 0 && colour < 0) ++tally;
                        if (pattern[i] > 0 && colour == player) ++tally;
                    }
                    if (tally == np) ++total;
                }
            }
        }

        // vertical and downward-diagonal
        for (int r = nr-1; r >= np-1; --r) {
            for (int c = 0; c < nc; ++c) {
                for (int dc = 0; dc < (c + np <= nc ? 2 : 1); ++dc) {
                    int tally = 0;
                    for (int i = 0; i < np; ++i) {
                        int colour = discs[r-i][c+i*dc];
                        if (pattern[i] < 0 && colour < 0) ++tally;
                        if (pattern[i] > 0 && colour == player) ++tally;
                    }
                    if (tally == np) ++total;
                }
            }
        }

        return total;
    }

    int countFours(int player) {
        int[] pattern = {1,1,1,1};
        return countPattern(pattern, player);
    }

    int countOpenThrees(int player) {
        int[] pattern = {-1,1,1,1,-1};
        return countPattern(pattern, player);
    }

    int countThrees(int player) {
        int[] pattern = {1,1,1,1};
        int total = 0;
        for (int i = 0; i < 4; ++i) {
            pattern[i] = -1;
            total += countPattern(pattern, player);
            pattern[i] = 1;
        }
        return total;
    }

    int countOpenTwos(int player) {
        int[] pattern = {-1,1,1,-1};
        return countPattern(pattern, player);
    }

    int getWinner() {
        for (int i = 0; i < 2; ++i) {
            if (countFours(i) > 0)
                return i;
        }
        return connect4.isFull() ? 2 : -1;
    }
}
