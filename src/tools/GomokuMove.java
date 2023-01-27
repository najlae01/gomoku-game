/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 *
 * @author MSI
 */
public class GomokuMove extends Move {
    
    //----- The position index chosen by the player
    public int moveIndexX;
    public int moveIndexY;

    public GomokuMove() {
    }
    
    public GomokuMove(int moveIndexX, int moveIndexY) {
        this.moveIndexX = moveIndexX;
        this.moveIndexY = moveIndexY;
    }
    
    
}