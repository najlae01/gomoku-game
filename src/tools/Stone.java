/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author MSI
 */
public class Stone {
    
    private int x;               
    private int y;
    private Color color;
    
    public ArrayList<Stone> stones = new ArrayList<Stone>();

    //------------ Cosnstructor ----------
    public Stone(Color color) 
    {
        super();
        this.x = 0;
        this.y = 0;
        this.color = color;
    }

    // -------- Getters & Setters -----------
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPions(ArrayList<Stone> stones) {
        this.stones = stones;
    }
    
    //-------- ToString --------------
    @Override
    public String toString() {
        return "Stone{" + "x=" + x + ", y=" + y + ", color=" + color + '}';
    }

    
    //--------- Draw a stone ---------
    public void drawStone(Graphics2D g)
    {
        g.setColor(color);
        g.fillOval(x+2, y+2, Cell.TAILLE_CELL-5, Cell.TAILLE_CELL-5);
    }

    //------- Remove a stone ----------
    public void remove(Stone p)
    {
        this.remove(p);
    }
}
