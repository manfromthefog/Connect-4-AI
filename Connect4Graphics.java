/**
 * RENERT CS2 : CONNECT 4
 * 
 * A simple implementation of the classic Connect Four game for us to study
 * and experiment with the minimax AI algorithm.
 * 
 * The Connect4Graphics class takes care of drawing the game board to a window
 * and taking mouse input from the human player. Some of the game logic exists
 * in this class too.
 * 
 * Authors:  Sonny Chan, CS2 2021
 * Date:    March 2021
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Connect4Graphics extends JPanel implements MouseInputListener
{
    /**
     * Wow!
     */
    private static final long serialVersionUID = -7859916278849733078L;
    static final int WIDTH = 700;
    static final int HEIGHT = 600;
    static final boolean DRAW_DEBUG_VALUES = true;

    Connect4Board connect4;
    Connect4Analyzer analyzer;
    Connect4AI opponent;

    // some useful game state for painting purposes
    int currentColumn = -1;
    int winner = -1;

    public Connect4Graphics(Connect4Board board, Connect4AI ai) {
        connect4 = board;
        analyzer = new Connect4Analyzer(board);
        opponent = ai;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JFrame frame = new JFrame("Renert CS2 : Connect 4");
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // respond to mouse clicks and mouse motion
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // this method, with this exact name, parameter and return type, overrides the paint() method in JPanel
    public void paint(Graphics g) {
        // fill the play area with classic Connect 4 blue
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int step = WIDTH / Connect4Board.COLS;

        // highlight the current column to indicate possible drop
        if (winner < 0 && currentColumn >= 0 && connect4.canDrop(currentColumn)) {
            int x = step * currentColumn;
            g.setColor(Color.BLUE);
            g.fillRect(x, 0, step, HEIGHT);
        }

        // draw player discs, or white for empty
        int[][] discs = connect4.getDiscs();
        for (int r = 0; r < discs.length; ++r) {
            int y = HEIGHT - step * (r+1);
            for (int c = 0; c < discs[r].length; ++c) {
                int x = step * c;
                // draw an additional outline if this was the last disc dropped
                if (r == connect4.getLastRow() && c == connect4.getLastCol()) {
                    g.setColor(Color.GRAY);
                    g.fillOval(x + 5, y + 5, step - 10, step - 10);
                }
                // draw the disc proper
                switch (discs[r][c]) {
                    case -1: g.setColor(Color.WHITE); break;
                    case  0: g.setColor(Color.CYAN); break;
                    case  1: g.setColor(Color.ORANGE); break;
                }
                g.fillOval(x + 10, y + 10, step - 20, step - 20);
            }
        }

        // debug output for AI
        if (opponent != null && DRAW_DEBUG_VALUES) {
            double[] values = opponent.getColumnValues();
            int y = step / 2;
            g.setColor(Color.GRAY);
            for (int c = 0; c < Connect4Board.COLS; ++c) {
                int x = step * c + step / 3;
                g.drawString(String.valueOf(values[c]), x, y);
            }
        }

        // if the game is over, show a message at the bottom
        if (winner >= 0) {
            String[] message = {"YOU WIN!", "THE AI WINS!", "IT'S A DRAW"};
            String fontName = g.getFont().getName();
            g.setColor(Color.BLACK);
            g.setFont(new Font(fontName, Font.BOLD, 72));
            g.drawString(message[winner], 20, HEIGHT - 20);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: Our game loop logic lives here right now, which isn't really the most logical place for this to be...


        // if the game is over, a click will reset the game
        if (winner >= 0) {
            connect4.clear();
            winner = -1;
            repaint();
        }
        // otherwise play a move if possible
        else if (currentColumn >= 0 && connect4.canDrop(currentColumn)) {
            connect4.dropDisc(currentColumn);
            winner = analyzer.getWinner();
            // if this player move doesn't end the game, play the AI move
            if (opponent != null && winner < 0) {
                opponent.playMove();
                winner = analyzer.getWinner();
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        // do a fancy column highlight as the mouse is moved
        int column =  e.getX() * Connect4Board.COLS / WIDTH;
        if (column != currentColumn) {
            currentColumn = column;
            repaint();
        }
    }

}
