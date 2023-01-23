/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import gui.Cell;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author MSI
 */
public class Stone {
    private int x;               // ----- L'abscisse
    private int y;              // ----- L'ordonée
    private Color color;       // ----- La variable qui prend la color de pion
                              // ----- Il sera soit la couleur blanc ou noir
    public ArrayList<Stone> stones = new ArrayList<Stone>(); // ----- liste des pions
    
    // ---------- Constructeur ------------
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

    public ArrayList<Stone> getPions() {
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

    public void setPions(ArrayList<Stone> pions) {
        this.stones = pions;
    }
    
    //-------- ToString --------------
    @Override
    public String toString() {
        return "Pion{" + "x=" + x + ", y=" + y + ", color=" + color + '}';
    }

    
    //--------- Dessiner pion ---------
    public void dessinerPion(Graphics2D g)
    {
        g.setColor(color);
        g.fillOval(x+2, y+2, Cell.TAILLE_CELL-5, Cell.TAILLE_CELL-5);
    }

    //------- Supprimer pion ----------
    public void remove(Stone p)
    {
        this.remove(p);
    }
}
