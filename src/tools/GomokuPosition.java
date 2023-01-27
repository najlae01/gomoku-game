/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 *
 * @author MSI
 */
public class GomokuPosition extends Position {
    final static public int BLANK = 0;      //----- an empty cell is coded by the integer 0
    final static public int HUMAN = 1;      //----- a cell filled by the player (black) is coded by the integer 1
    final static public int PROGRAM = -1;   //----- a cell filled by the enemy (white) is coded by the integer -1
    public int [][] board = new int[19][19];      //----- The game's board of size 19x19
    
    //------------ Cosnstructor ----------
    public GomokuPosition() {
        
        for(int i = 0 ; i < 19; i ++)
            for(int j = 0; j < 19; j++)
                board[i][j] = 0;
    }
    
    //---------- Getters & Setters --------------
    public int[][] getBoard() {
        return this.board;
    }
    
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    
    /*----- This method returns true if the game is over,
    otherwise, returns false */
    public boolean state() {
        for(int i = 0 ; i < 19; i ++)
            for(int j = 0; j < 19; j++)
            if(this.board[i][j] == 0)
                return false;
        return true;
    }
    
    //---------- toString()-----------------
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for(int i = 0 ; i < 19; i ++)
            for(int j = 0; j < 19; j++)
                sb.append(""+board[i][j]+",");
        sb.append("]");
        return sb.toString();
    }
 
}




