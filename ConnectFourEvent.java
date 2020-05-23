/*
 * Author: Neal Su
 * Project Name: Connect Four Event
 * Description: The calculation component for the project. Includes checking if a winner has been found and updates the board after each turn.
 */
package connectfour;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class ConnectFourEvent implements ActionListener {
    
    // variable for the ConnectFour class
    ConnectFour gui;
    // images for the red and yellows discs
    ImageIcon imgRedDisc = new ImageIcon("Red Disc.jpg");
    ImageIcon imgYellowDisc = new ImageIcon("Yellow Disc.jpg");
    // integer array to hold the values of the slots of the board 
    int[][] intArrGrid = new int[6][7];
    // integer counters
    int intPlayerTurn = 0;
    int intRedWon = 0;
    int intYellowWon = 0;
    int intTieCounter = 0;
    int intTopColumnCounter = 0;
    // String to be displayed on label 
    String strTurn;
    
    /**
     * @param in as a ConnectFour object 
     */
    public ConnectFourEvent(ConnectFour in){
        gui = in;
        
        /*
         * -1 are empty slots that have nothing under them and are not allowed to go in
         * 0 are emty slots that are allowed to go in (they are either bottom most or already have a piece under them)
         * 1 are pieces occupied by the first player (red player)
         * 2 are pieces occupies by the second player (yellow player)
        */
        
        // sets all the slots to -1 except the bottom most row (for the beginning of the game)
        for (int row=4; row>=0; row--)
        {
           for (int col=6; col>=0; col--)
           {
               intArrGrid[row][col] = -1;
           }
        }
        
        // displays info of the game 
        gui.lblTurn.setText("Red Player Starts");
        gui.txtRecord.setText("\nRed Wins: 0 \nYellow Wins: 0 \nTie: 0");
    }    
    
    /**
     * @param event mouse click
     */
    public void actionPerformed(ActionEvent event)
    {
        // if reset button is pressed
        if(gui.btnReset == event.getSource())
        {
            // sets all the slots to -1 except the bottom most row and removes images from buttons 
            for (int row=4; row>=0; row--)
            {
                for (int col=6; col>=0; col--)
                {
                    intArrGrid[row][col] = -1;
                    gui.btnArrButton[row][col].setIcon(null);
                }
            }
            
            // resets the bottom most row slots to 0 and removes images from bottom most buttons
            for (int col = 0; col < 7 ;col++)
            {
                intArrGrid[5][col] = 0;
                gui.btnArrButton[5][col].setIcon(null);
            }
            
            gui.lblTurn.setText("Red Player Starts");            
            
            // resets counters
            intTopColumnCounter = 0;
            intPlayerTurn = 0;
        }    

        else
        {
            // loops to go through every button in the array
            for (int row = 0; row < 6; row++)
            {
                for (int col = 0; col < 7; col++)
                {
                    // if the button is pressed
                    if(gui.btnArrButton[row][col] == event.getSource())
                    {
                        // if it was the red player
                        if (intPlayerTurn % 2 == 0 && intArrGrid[row][col]==0)
                        {                       
                            strTurn = "Yellow's Turn";
                            // sets appropriate values for the red player
                            gui.btnArrButton[row][col].setIcon(imgRedDisc);
                            intArrGrid[row][col] = 1;
                            // if piece is at the top most row, add to the appropriate counter
                            if(row == 0)
                            {  
                                intTopColumnCounter++;                        
                            }
                            // allows the slot above the piece to be open
                            else
                            {
                            intArrGrid[row-1][col] = 0;
                            }
                            
                            // if the red player won
                            if (checkWin())
                            {
                                // displays appropriate message
                                strTurn = "Red Won";
                                gui.lblTurn.setText(strTurn);
                                JOptionPane.showMessageDialog(null, "The Red Player is the Winner");
                                // stops the game after the red player wins
                                for (int _row = 0; _row < 6; _row++)
                                {
                                    for (int _col = 0; _col < 7; _col++)
                                    {
                                        intArrGrid[_row][_col] = -1;
                                    }
                                }    
                                intRedWon++;
                            } 
                            intPlayerTurn++;
                        }
                       
                        // if it was the yellow player
                        if (intPlayerTurn % 2 == 1 && intArrGrid[row][col] == 0)
                        {                        
                            strTurn = "Red's Turn";
                            // sets appropriate values for the yellow player
                            gui.btnArrButton[row][col].setIcon(imgYellowDisc);
                            intArrGrid[row][col] = 2;
                            // if piece is at the top most row, add to the appropriate counter
                            if(row == 0)
                            {  
                                intTopColumnCounter++;                        
                            }
                            // allows the slot above the piece to be open
                            else
                            {
                                intArrGrid[row-1][col] = 0;
                            }
                            
                            // if the yellow player won
                            if (checkWin())
                            {
                                // displays appropriate message
                                strTurn = "Yellow Won";
                                gui.lblTurn.setText(strTurn);
                                JOptionPane.showMessageDialog(null, "The Yellow Player \n is the Winner");
                                // stops the game after the yellow player wins 
                                for (int _row = 0; _row < 6; _row++)
                                {
                                    for (int _col = 0; _col < 7; _col++)
                                    {
                                        intArrGrid[_row][_col] = -1;
                                    }
                                }
                                intYellowWon++;
                            } 
                            intPlayerTurn++;
                        }                   
                    }    
                }
            }
            
            // if all slots of the top row have been filled
            if (intTopColumnCounter == 7)
            {
                // display it is a tie
                JOptionPane.showMessageDialog(null, "The Game is a Tie");
                intTieCounter++;
            }
            
        // displays approapriate information    
        gui.lblTurn.setText(strTurn);
        gui.txtRecord.setText("\nRed Wins: " + intRedWon + "\nYellow Wins: " + intYellowWon+ "\nTie: " + intTieCounter);
        }
    }
    
    /**
     * @return true if a winner has been found, false is there is no winner 
     */
    public boolean checkWin()
    {
        // check for horizontal win (only checks the necessary slots that will determine if four in a row has been made)
        for (int row = 0; row < 6; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                if (intArrGrid[row][col] != 0 && intArrGrid[row][col] != -1 && intArrGrid[row][col] == intArrGrid[row][col+1] && intArrGrid[row][col] == intArrGrid[row][col+2] && intArrGrid[row][col] == intArrGrid[row][col+3])
                {
                    return true;
                }    
            }            
        }    
        
        // check for vertical win (only checks the necessary slots that will determine if four in a row has been made)
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 7; col++)
            {
                if (intArrGrid[row][col] != 0 && intArrGrid[row][col] != -1 && intArrGrid[row][col] == intArrGrid[row+1][col] && intArrGrid[row][col] == intArrGrid[row+2][col] && intArrGrid[row][col] == intArrGrid[row+3][col])
                {
                    return true;
                }    
            }
        }    
        
        // check for diaganol win (negitive slope) (only checks the necessary slots that will determine if four in a row has been made)
        for (int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                if (intArrGrid[row][col] !=0 && intArrGrid[row][col] != -1 && intArrGrid[row][col] == intArrGrid[row+1][col+1] && intArrGrid[row][col] == intArrGrid[row+2][col+2]&& intArrGrid[row][col] == intArrGrid[row+3][col+3])
                {
                    return true;
                }   
            }    
        }       
        
        // check for diaganol win (positive slope) (only checks the necessary slots that will determine if four in a row has been made)
        for (int row = 3; row < 6; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                if (intArrGrid[row][col] !=0 && intArrGrid[row][col] != -1 && intArrGrid[row][col] == intArrGrid[row-1][col+1] && intArrGrid[row][col] == intArrGrid[row-2][col+2]&& intArrGrid[row][col] == intArrGrid[row-3][col+3])
                {
                    return true;
                }   
            }
        }
        // if no four in a row has been found
        return false;
    }    
}
