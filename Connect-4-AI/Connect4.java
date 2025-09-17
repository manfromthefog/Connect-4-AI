/**
 * RENERT CS2 : CONNECT 4
 * 
 * A simple implementation of the classic Connect Four game for us to study
 * and experiment with the minimax AI algorithm.
 * 
 * Author:  CS2
 * Date:    March 2021
 */

class Connect4
{
    public static void main(String[] args) {
        Connect4Board board = new Connect4Board();
        Connect4AI opponent = new Connect4AI(board);
        Connect4Graphics graphics = new Connect4Graphics(board, opponent);
    }
}

