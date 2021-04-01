/**
 * RENERT CS2 : CONNECT 4
 * 
 * A simple implementation of the classic Connect Four game for us to study
 * and experiment with the minimax AI algorithm.
 * 
 * The Connect4AI class implements the decision-making logic for the computer
 * opponent. It is a great play to apply the minimax algorithm.
 * 
 * Author:  Sonny Chan
 * Date:    March 2021
 */

import java.util.Random;
// import java.lang.reflect.Array;
import java.util.ArrayList;

public class Connect4AI
{
    Connect4Board connect4;
    Connect4Analyzer analyzer;

    // minimax search depth limit (number of moves to look ahead)
    static final int DEPTH_LIMIT = 6;
    int evaluations = 0;

    // for remember the best move from our recursive minimax search
    ArrayList<Integer> bestMove = new ArrayList<>();

    // the colour of the AI player's discs
    int colour = -1;

    // a handy random number generator
    Random rng = new Random();

    // visual values
    double[] columnValues = new double[Connect4Board.COLS];
    double[] getColumnValues() {
        return columnValues;
    }

    Connect4AI(Connect4Board board) {
        connect4 = board;
        analyzer = new Connect4Analyzer(board);
    }

    public void playMove() {
        // assign ourselves the colour of the current turn
        colour = connect4.getTurn();

        playMinimax();
    }

/*
 *  private void playRandom() {
 *      // choose a random move from all valid ones
        ArrayList<Integer> valid = new ArrayList<>();
        for (int col = 0; col < Connect4Board.COLS; ++col) {
            if (connect4.canDrop(col)) {
                valid.add(col);
            }
        }
        int pick = rng.nextInt(valid.size());
        connect4.dropDisc(valid.get(pick));
    }
*/

/* Old code to play this game. Now, it is dead.
    private void playToWin() {
        // try placing a disc in each column, and check if we won
        int winningColumn = -1;
        for (int col = 0; col < Connect4Board.COLS; ++col) {
            if (connect4.canDrop(col)) {
                connect4.dropDisc(col);
                // check if we won
                double value = positionValue(colour);
                if (value > 0.0) {
                    winningColumn = col;
                }
                // undo my move
                connect4.undropDisc(col);
            }
        }

        if (winningColumn >= 0) {
            connect4.dropDisc(winningColumn);
        }
        else {
            playRandom();
        }
    }
*/

    private void playMinimax() {
        evaluations = 0;
        minimax(colour, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        System.out.println(evaluations + " Minimax evaluations.");
        int pick = rng.nextInt(bestMove.size());
        connect4.dropDisc(bestMove.get(pick));
    }

    // recursively calculate the minimax value for a given level in our search tree
    private double minimax(int player, int depth, double alpha, double beta) {
        evaluations++;

        // recursive base case: if we reach a depth limit, return the value of the present position
        int winner = analyzer.getWinner();
        if (depth == DEPTH_LIMIT || winner >= 0) {
            // if draw
            if (winner == 2) {
                return 0;
            }
            double value = positionValue(colour) - positionValue(1 - colour);
            return value;
        }

        // if the player is us (AI), we maximize on value
        if (player == colour) {
            double best = Double.NEGATIVE_INFINITY;
            for (int col = 0; col < Connect4Board.COLS; ++col) {
                if (connect4.canDrop(col)) {
                    connect4.dropDisc(col);
                    // recursive play the next move on the opponent's turn
                    double value = minimax(1 - player, depth + 1, alpha, beta);
                    alpha = Math.max(alpha, value);
                    // if we found a better move, remember it
                    if (value > best) {
                        best = value;
                        // remember the best move only if it's the first move we're analyzing
                        if (depth == 0) {
                            bestMove.clear();
                            bestMove.add(col);
                        }
                    }
                    else if (value  == best && depth == 0) {
                        bestMove.add(col);
                    }

                    if (depth == 0) {
                        columnValues[col] = value;
                    }
                    // undo my move
                    connect4.undropDisc(col);
                }
                // if we can achieve position value high enough, then deny branch access for minimizer
                if (best > beta) {
                    break;
                }
            }
            return best;
        }
        // otherwise, we minimize on the value
        else {
            double worst = Double.POSITIVE_INFINITY;
            for (int col = 0; col < Connect4Board.COLS; ++col) {
                if (connect4.canDrop(col)) {
                    connect4.dropDisc(col);
                    // recursive play the next move on the opponent's turn
                    double value = minimax(1 - player, depth + 1, alpha, beta);
                    beta = Math.min(beta, value);
                    // if we found a better move, remember it
                    if (value < worst) {
                        worst = value;
                    }
                    // undo my move
                    connect4.undropDisc(col);
                }
                // likewise, if the maximizer value is smaller than beta, maximizer is denied branch access
                if (worst < alpha) {
                    break;
                }
            }
            return worst;
        }
    }

    private double positionValue(int player) {
        double value = 0.0;

        int fours = analyzer.countFours(player);
        value += fours;
        // if (fours > 0) return 1.0;

        // set up for win
        int openThrees = analyzer.countOpenThrees(player);
        value += 0.5 * openThrees;

        // assign some value to threes and win
        int openTwos = analyzer.countOpenTwos(player);
        value += 0.25 * openTwos;

        return value;
    }
}
