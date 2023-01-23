/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gomokugame;

import gui.Cell;
import gui.Home;
import static gui.Home.leftHintText;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JOptionPane;
import tools.*;

/**
 *
 * @author MSI
 */
public class Gomoku extends GameSearch {

    // enum to represent available play mode types
    public static enum PlayModeType {
        HUMAN_VS_HUMAN, HUMAN_VS_COMPUTER
    };

    public static PlayModeType playMode;   // Stores the chosen play mode

    public BoardPanel board = new BoardPanel();
    public static boolean isClicked = false; //---- un boolean qui retourne true 
    //si le joueur clique sur le board pour dessiner un pion

    public static boolean playerTurn = HUMAN;
    public static boolean isFirstPlayerTurn = true;
    public static boolean canClick = true;
    public static int stoneX;       //----- la position de pion à dessiner
    public static int stoneY;

    public static boolean isAIThinking = false;

    // Stones left for the player 1
    public static int leftStonesForPlayer1;

    // Stones left for the player 2
    public static int leftStonesForPlayer2;

    // Stones left for the player 1
    public static int leftHintsForPlayer1;

    // Stones left for the player 2
    public static int leftHintsForPlayer2;

    public static GomokuPosition position = new GomokuPosition();
    public static int depth;           //----- la profendeur
    public static boolean gameOver = false;         //----- Si le joueur depasse le timer, on va retourner True
    public static GomokuPosition lastPosition = new GomokuPosition();

    /**
     * *************************************************************************
     * On declare un tableau sur lequel on met des valeurs tactiques qui
     * representent l'interet de chaque case
     * ************************************************************************
     */
    //--------- Fonction : drawnPsoition ------------
    @Override
    public boolean drawnPosition(Position p) {
        if (GameSearch.DEBUG) {
            System.out.println("drawnPosition(" + p + ")");
        }
        boolean ret = true;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == GomokuPosition.BLANK) {
                    ret = false;
                    break;
                }
            }
        }
        if (GameSearch.DEBUG) {
            System.out.println("     ret=" + ret);
        }
        return ret;
    }

    private boolean winCheck(boolean player, GomokuPosition pos) {
        int b = 0;
        if (player) {
            b = GomokuPosition.HUMAN;
        } else {
            b = GomokuPosition.PROGRAM;
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == pos.board[i + 1][j] && pos.board[i + 1][j] == pos.board[i + 2][j]
                        && pos.board[i + 2][j] == pos.board[i + 3][j] && pos.board[i + 3][j] == pos.board[i + 4][j]
                        && pos.board[i + 4][j] == b) {
                    return true;
                }
            }
        }
        for (int j = 0; j < 19; j++) {
            if (pos.board[14][j] == pos.board[15][j] && pos.board[15][j] == pos.board[16][j]
                    && pos.board[16][j] == pos.board[17][j] && pos.board[17][j] == pos.board[18][j]
                    && pos.board[14][j] == b) {
                return true;
            }
        }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 15; j++) {
                if (pos.board[i][j] == pos.board[i][j + 1] && pos.board[i][j + 1] == pos.board[i][j + 2]
                        && pos.board[i][j + 2] == pos.board[i][j + 3] && pos.board[i][j + 3] == pos.board[i][j + 4]
                        && pos.board[i][j + 4] == b) {
                    return true;
                }
            }
        }
        for (int i = 0; i < 19; i++) {
            if (pos.board[i][14] == pos.board[i][15] && pos.board[i][15] == pos.board[i][16]
                    && pos.board[i][16] == pos.board[i][17] && pos.board[i][17] == pos.board[i][18]
                    && pos.board[i][14] == b) {
                return true;
            }
        }
        for (int g = 0; g < 19; g++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0, j = g; j < 19; i++, j++) {
                if (pos.board[i][j] == b) {
                    list.add(pos.board[i][j]);
                    //System.out.println("Diag 1 ("+i+", "+j+"): "+pos.board[i][j]);
                }
            }
            int count = 0;
            for (int i = 0; i < list.size() - 2; i++) {
                if (list.get(i) == list.get(i + 1)) {
                    count++;
                }
            }
            if (list.size() > 2 && list.get(list.size() - 2) == list.get(list.size() - 1)) {
                count++;
            }
            //System.out.println("Diag 1 count : "+count);
            if (count == 4) {
                return true;
            }
        }
        for (int g = 0; g < 19; g++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0, j = g + 1; j < 19; i++, j++) {
                if (pos.board[i][j] == b) {
                    list.add(pos.board[i][j]);
                    //System.out.println("Diag 2 ("+i+", "+j+"): "+pos.board[i][j]);
                }
            }
            int count = 0;
            for (int i = 0; i < list.size() - 2; i++) {
                if (list.get(i) == list.get(i + 1)) {
                    count++;
                }
            }
            if (list.size() > 2 && list.get(list.size() - 2) == list.get(list.size() - 1)) {
                count++;
            }
            //System.out.println("Diag 1 count : "+count);
            if (count == 4) {
                return true;
            }
        }
        for (int col = 0; col < 19; col++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            int startcol = col, startrow = 0;

            while (startcol >= 0 && startrow < 19) {
                if (pos.board[startrow][startcol] == b) {
                    list.add(pos.board[startrow][startcol]);
                    //System.out.println("Diag 3 ("+startrow+", "+ startcol+"): "+pos.board[startrow][startcol]);
                }

                startcol--;

                startrow++;
            }
            int count = 0;
            for (int i = 0; i < list.size() - 2; i++) {
                if (list.get(i) == list.get(i + 1)) {
                    count++;
                }
            }
            if (list.size() > 2 && list.get(list.size() - 2) == list.get(list.size() - 1)) {
                count++;
            }
            //System.out.println("Diag 1 count : "+count);
            if (count == 4) {
                return true;
            }
        }
        for (int row = 1; row < 19; row++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            int startrow = row, startcol = 19 - 1;

            while (startrow < 19 && startcol >= 0) {
                if (pos.board[startrow][startcol] == b) {
                    list.add(pos.board[startrow][startcol]);
                    //System.out.println("Diag 4 ("+startrow+", "+ startcol+"): "+pos.board[startrow][startcol]);
                }
                startcol--;

                startrow++;
            }
            int count = 0;
            for (int i = 0; i < list.size() - 2; i++) {
                if (list.get(i) == list.get(i + 1)) {
                    count++;
                }
            }
            if (list.size() > 2 && list.get(list.size() - 2) == list.get(list.size() - 1)) {
                count++;
            }
            //System.out.println("Diag 1 count : "+count);
            if (count == 4) {
                return true;
            }
        }
        return false;
    }

    // --------- Fonction : wonPosition -------------
    // This function calculates the score by evaluating the stone positions in
    // horizontal direction
    @Override
    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) {
            System.out.println("wonPosition(" + p + "," + player + ")");
        }
        GomokuPosition pos = (GomokuPosition) p;
        boolean ret = winCheck(player, pos);
        if (GameSearch.DEBUG) {
            System.out.println("     ret=" + ret);
        }
        return ret;
    }

    // --------- Fonction : positionEvaluation ------------
    @Override
    public float positionEvaluation(Position p, boolean player) {
        int cnt = 0;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == 0) {
                    cnt++;
                }
            }
        }
        //System.out.println("CNT1 "+cnt);
        cnt = 362 - cnt;
        //System.out.println("CNT2 "+cnt);
        float count = 0.1f;

        for (int i = 0; i < 18; i++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == pos.board[i + 1][j] && pos.board[i][j] == 1) {
                    human.add(pos.board[i][j]);
                } else if (pos.board[i][j] == pos.board[i + 1][j] && pos.board[i][j] == -1) {
                    program.add(pos.board[i][j]);
                }
            }
            for (int j = 0; j < 19; j++) {
                if (pos.board[17][j] == pos.board[18][j] && pos.board[18][j] == 1) {
                    human.add(pos.board[18][j]);
                } else if (pos.board[17][j] == pos.board[18][j] && pos.board[18][j] == -1) {
                    program.add(pos.board[18][j]);
                }
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }
        for (int i = 0; i < 19; i++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            for (int j = 0; j < 18; j++) {
                if (pos.board[i][j] == pos.board[i][j + 1] && pos.board[i][j] == 1) {
                    human.add(pos.board[i][j]);
                } else if (pos.board[i][j] == pos.board[i][j + 1] && pos.board[i][j] == -1) {
                    program.add(pos.board[i][j]);
                }
            }
            for (int j = 0; j < 19; j++) {
                if (pos.board[j][17] == pos.board[j][18] && pos.board[j][18] == 1) {
                    human.add(pos.board[j][18]);
                }

                if (pos.board[j][17] == pos.board[j][18] && pos.board[j][18] == -1) {
                    program.add(pos.board[j][18]);
                }
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }

        for (int g = 0; g < 19; g++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            for (int i = 0, j = g; j < 19; i++, j++) {
                if (pos.board[i][j] == 1) {
                    human.add(pos.board[i][j]);
                } else if (pos.board[i][j] == -1) {
                    program.add(pos.board[i][j]);
                }
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }
        for (int g = 0; g < 19; g++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            for (int i = 0, j = g + 1; j < 19; i++, j++) {
                if (pos.board[i][j] == 1) {
                    human.add(pos.board[i][j]);
                } else if (pos.board[i][j] == -1) {
                    program.add(pos.board[i][j]);
                }
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }
        for (int col = 0; col < 19; col++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            int startcol = col, startrow = 0;

            while (startcol >= 0 && startrow < 19) {
                if (pos.board[startrow][startcol] == 1) {
                    human.add(pos.board[startrow][startcol]);
                } else if (pos.board[startrow][startcol] == -1) {
                    program.add(pos.board[startrow][startcol]);
                }
                startcol--;

                startrow++;
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }
        for (int row = 1; row < 19; row++) {
            ArrayList<Integer> human = new ArrayList<Integer>();
            ArrayList<Integer> program = new ArrayList<Integer>();
            int startrow = row, startcol = 19 - 1;

            while (startrow < 19 && startcol >= 0) {
                if (pos.board[startrow][startcol] == 1) {
                    human.add(pos.board[startrow][startcol]);
                } else if (pos.board[startrow][startcol] == -1) {
                    program.add(pos.board[startrow][startcol]);
                }
                startcol--;

                startrow++;
            }
            int consecutiveForProgram = 0;
            int consecutiveForHuman = 0;
            for (int k = 0; k < human.size() - 2; k++) {
                if (human.get(k) == human.get(k + 1)) {
                    consecutiveForHuman++;
                }
            }
            for (int k = 0; k < program.size() - 2; k++) {
                if (program.get(k) == program.get(k + 1)) {
                    consecutiveForProgram++;
                }
            }
            if (human.size() > 2 && human.get(human.size() - 2) == human.get(human.size() - 1)) {
                consecutiveForHuman++;
            }
            if (program.size() > 2 && program.get(program.size() - 2) == program.get(program.size() - 1)) {
                consecutiveForProgram++;
            }
            if (consecutiveForHuman == 2) {
                count = count + 0.2f;
            }
            if (consecutiveForProgram == 2) {
                count = count - 0.2f;
            }
            if (consecutiveForHuman == 3) {
                count = count + 0.3f;
            }
            if (consecutiveForProgram == 3) {
                count = count - 0.3f;
            }
            if (consecutiveForHuman == 4) {
                count = count + 0.4f;
            }
            if (consecutiveForProgram == 4) {
                count = count - 0.4f;
            }
        }

        float ret = (count - 1.0f);
        if (wonPosition(p, player)) {
            return count + (1.0f / cnt);
        }
        if (wonPosition(p, !player)) {
            return -(count + (1.0f / cnt));
        }
        //System.out.println("COUNT "+count);
        //System.out.println("CNT "+cnt);
        return ret;
    }

    @Override
    public void printPosition(Position p) {
        System.out.println("Board position:");
        GomokuPosition pos = (GomokuPosition) p;
        //Home.board.setBoardFromGomokuPosition(pos);
        for (int row = 0; row < 19; row++) {
            System.out.println();
            for (int col = 0; col < 19; col++) {
                if (pos.board[row][col] == GomokuPosition.HUMAN) {
                    System.out.print("H");
                } else if (pos.board[row][col] == GomokuPosition.PROGRAM) {
                    System.out.print("P");
                } else {
                    System.out.print("0");
                }

            }
        }
        System.out.println();
    }

    // --------- Fonction : possibleMoves ------------
    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) {
            System.out.println("posibleMoves(" + p + "," + player + ")");
        }
        GomokuPosition pos = (GomokuPosition) p;
        int count = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == 0) {
                    count++;
                }
            }
        }
        if (count == 0) {
            return null;
        }
        Position[] ret = new Position[count];
        count = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == 0) {
                    GomokuPosition pos2 = new GomokuPosition();
                    for (int l = 0; l < 19; l++) {
                        for (int k = 0; k < 19; k++) {
                            pos2.board[l][k] = pos.board[l][k];
                        }
                    }

                    if (player) {
                        pos2.board[i][j] = 1;
                    } else {
                        pos2.board[i][j] = -1;
                    }
                    ret[count++] = pos2;
                    if (GameSearch.DEBUG) {
                        System.out.println("    " + pos2);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        if (GameSearch.DEBUG) {
            System.out.println("Entered Gomoku.makeMove");
        }
        GomokuMove m = (GomokuMove) move;
        GomokuPosition pos = (GomokuPosition) p;
        GomokuPosition pos2 = new GomokuPosition();

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                pos2.board[i] = pos.board[i];
            }
        }
        int pp;
        if (player) {
            pp = 1;
            Gomoku.leftStonesForPlayer1 -= 1;
            Home.leftBlackStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer1));

        } else {
            pp = -1;
            Gomoku.leftStonesForPlayer2 -= 1;
            Home.leftWhiteStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer2));
        }
        if (GameSearch.DEBUG) {
            System.out.println("makeMove: m.moveIndex = " + m.moveIndexX + " " + m.moveIndexY);
        }
        System.out.println("makeMove: m.moveIndex = " + m.moveIndexX + " " + m.moveIndexY);
        pos2.board[m.moveIndexX][m.moveIndexY] = pp; //on attribut a la case actuelle une valeur selon qui joue

        return pos2;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
//        System.out.println("reachedMaxDepth");
        boolean ret = false;
        if (depth >= Gomoku.depth) {
            return true;
        }
        if (wonPosition(p, false)) {
            ret = true;
        } else if (wonPosition(p, true)) {
            ret = true;
        } else if (drawnPosition(p)) {
            ret = true; //personne n'a gagner  mais on est a la fin 
        }
        if (GameSearch.DEBUG) {
            System.out.println("reachedMaxDepth: pos=" + p.toString() + ", depth=" + depth
                    + ", ret=" + ret);
        }
        if (ret) {
            System.out.println(ret);
        }
        return ret;
    }

    @Override
    public Move createMove() {
        if (GameSearch.DEBUG) {
            System.out.println("Enter blank square index [0,8]:");
        }
        //System.out.println("x :");
        GomokuMove mm = new GomokuMove();
        if (Gomoku.isClicked) {
            mm.moveIndexX = Gomoku.stoneX;
            mm.moveIndexY = Gomoku.stoneY;
            System.out.print("Your move : (" + mm.moveIndexX + ", " + mm.moveIndexY + ")");

            Gomoku.isClicked = false;
            Gomoku.gameOver = false;
        }
        return mm;
    }

    public class BoardPanel extends javax.swing.JPanel {

        public Cell board[][];          //------ Tables des cellule qui vont construire notre othellier
        public int TAILLE_BOARD = 570;    //------ Dimension de notre plateau   

        //------- Constructeur -----------
        public BoardPanel() {
            initComponents();
            creation();
        }

        //----------- Getter--------------
        public Cell[][] getBoard() {
            return board;
        }

        public void setBoard(Cell[][] board) {
            this.board = board;
        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {

            setBackground(new java.awt.Color(255, 204, 204));
            setPreferredSize(new java.awt.Dimension(760, 760));

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 487, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 428, Short.MAX_VALUE)
            );
        }// </editor-fold>                        

        //------ Creation des cellules -----------
        public void creation() {

            //------ L'instanciation 
            board = new Cell[19][19];

            int count = 0;
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    count++;
                    if (count % 20 == 0) {
                        break;
                    }
                    board[i][j] = new Cell(i, j);
                    board[i][j].setBounds(j * Cell.TAILLE_CELL, i * Cell.TAILLE_CELL, Cell.TAILLE_CELL, Cell.TAILLE_CELL);
                    int indexX = i;
                    int indexY = j;
                    Cell cel = board[i][j];
                    board[i][j].addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            //setAIThinking(false);
                            System.out.println("Mouse Clicked\n ");
                            Position startingPosition = Gomoku.position;
                            System.out.println("x:" + indexX);
                            System.out.println("x:" + indexY);
                            if (Gomoku.playMode == Gomoku.PlayModeType.HUMAN_VS_COMPUTER && !gameOver && canClick && playerTurn) {

                                for (int i = 0; i < 19; i++) {
                                    for (int j = 0; j < 19; j++) {
                                        if (indexX == i && indexY == j) {
                                            Gomoku.isClicked = true;
                                            Gomoku.stoneX = indexX;
                                            Gomoku.stoneY = indexY;
                                            Move move = createMove();
                                            startingPosition = (Position) makeMove(startingPosition, playerTurn, move);
                                            cel.drawPion(Color.BLACK);
                                            printPosition(startingPosition);
                                            if (wonPosition(startingPosition, GameSearch.HUMAN)) {
                                                JOptionPane.showMessageDialog(null, "Human won!");
                                                Home.resultText.setText("Human won!");
                                                Gomoku.gameOver = true;
                                                return;
                                            } else if (drawnPosition(startingPosition)) {
                                                JOptionPane.showMessageDialog(null, "Draw game!");
                                                Home.resultText.setText("Draw game!");
                                                Gomoku.gameOver = true;
                                                return;
                                            }
                                        }
                                    }
                                }
                                cel.repaint();
                                Gomoku.position = (GomokuPosition) startingPosition;
                                System.out.println("Printing\n" + Gomoku.position);
                                setBoardFromGomokuPosition(Gomoku.position);

                                Gomoku.playerTurn = GameSearch.PROGRAM;
                                if (Gomoku.playMode == Gomoku.PlayModeType.HUMAN_VS_COMPUTER) {
                                    Gomoku.canClick = false;
                                } else {
                                    Gomoku.canClick = true;
                                }

                                if (!gameOver && !canClick && !playerTurn) {

                                    Thread thread = new Thread(new Runnable() {
                                        public void run() {
                                            setAIThinking(true);
                                            Position startingPosition = Gomoku.position;
                                            printPosition(startingPosition);
                                            Vector v = alphaBeta(0, startingPosition, GameSearch.PROGRAM);
                                            Enumeration enum2 = v.elements();

                                            startingPosition = (Position) v.elementAt(1);
                                            Gomoku.position = (GomokuPosition) startingPosition;
                                            setBoardFromGomokuPosition(Gomoku.position);
                                            Gomoku.leftStonesForPlayer2 -= 1;
                                            Home.leftWhiteStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer2));
                                            Gomoku.playerTurn = GameSearch.HUMAN;
                                            Gomoku.canClick = true;

                                            if (wonPosition(startingPosition, GameSearch.PROGRAM)) {
                                                JOptionPane.showMessageDialog(null, "Computer won!");
                                                Home.resultText.setText("Computer won!");
                                                Gomoku.gameOver = true;

                                                return;
                                            } else if (drawnPosition(startingPosition)) {
                                                JOptionPane.showMessageDialog(null, "Draw game!");
                                                Home.resultText.setText("Draw game!");
                                                Gomoku.gameOver = true;
                                                return;
                                            }
                                        }
                                    });

                                    thread.setPriority(Thread.MAX_PRIORITY);
                                    thread.start();
                                    Gomoku.canClick = true;
                                    setAIThinking(false);
                                    setBoardFromGomokuPosition(Gomoku.position);
                                }
                            } else if (Gomoku.playMode == Gomoku.PlayModeType.HUMAN_VS_HUMAN && !gameOver && canClick) {

                                startingPosition = Gomoku.position;
                                if (playerTurn) {
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            if (indexX == i && indexY == j) {
                                                Gomoku.isClicked = true;
                                                Gomoku.stoneX = indexX;
                                                Gomoku.stoneY = indexY;
                                                Move move = createMove();
                                                startingPosition = (Position) makeMove(startingPosition, GameSearch.HUMAN, move);

                                                Gomoku.position = (GomokuPosition) startingPosition;
                                                setBoardFromGomokuPosition(Gomoku.position);
                                                printPosition(startingPosition);
                                                if (wonPosition(startingPosition, GameSearch.HUMAN)) {
                                                    JOptionPane.showMessageDialog(null, "First Player won!");
                                                    Home.resultText.setText("First Player won!");
                                                    Gomoku.gameOver = true;
                                                    return;
                                                } else if (drawnPosition(startingPosition)) {
                                                    JOptionPane.showMessageDialog(null, "Draw game!");
                                                    Home.resultText.setText("Draw game!");
                                                    Gomoku.gameOver = true;
                                                    return;
                                                }
                                            }
                                        }

                                        cel.repaint();
                                        Gomoku.playerTurn = GameSearch.PROGRAM;
                                        Gomoku.canClick = true;
                                        Gomoku.position = (GomokuPosition) startingPosition;
                                        System.out.println("Printing\n" + Gomoku.position);
                                        setBoardFromGomokuPosition(Gomoku.position);
                                    }

                                } else {
                                    for (int i = 0; i < 19; i++) {
                                        for (int j = 0; j < 19; j++) {
                                            if (indexX == i && indexY == j) {
                                                Gomoku.isClicked = true;
                                                Gomoku.stoneX = indexX;
                                                Gomoku.stoneY = indexY;
                                                Move move = createMove();
                                                startingPosition = (Position) makeMove(startingPosition, GameSearch.PROGRAM, move);

                                                Gomoku.position = (GomokuPosition) startingPosition;
                                                setBoardFromGomokuPosition(Gomoku.position);
                                                printPosition(startingPosition);
                                                if (wonPosition(startingPosition, GameSearch.PROGRAM)) {
                                                    JOptionPane.showMessageDialog(null, "Second Player won!");
                                                    Home.resultText.setText("Second Player won!");
                                                    Gomoku.gameOver = true;
                                                    return;
                                                } else if (drawnPosition(startingPosition)) {
                                                    JOptionPane.showMessageDialog(null, "Draw game!");
                                                    Home.resultText.setText("Draw game!");
                                                    Gomoku.gameOver = true;
                                                    return;
                                                }
                                            }
                                        }

                                        cel.repaint();
                                        Gomoku.playerTurn = GameSearch.HUMAN;
                                        Gomoku.canClick = true;
                                        Gomoku.position = (GomokuPosition) startingPosition;
                                        System.out.println("Printing\n" + Gomoku.position);
                                        setBoardFromGomokuPosition(Gomoku.position);
                                    }

                                }

                            }
                        }
                    });

                    this.add(board[i][j]);

                }

                count = 0;
            }

            this.repaint();

        }

        public void setBoardFromGomokuPosition(GomokuPosition position) {

            int count = 0;
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    count++;
                    if (count % 20 == 0) {
                        break;
                    }
                    if (position.board[i][j] == 1) {
                        board[i][j].drawPion(Color.BLACK);
                        //System.out.println("Black("+i+", "+j+")");
                    } else if (position.board[i][j] == -1) {
                        board[i][j].drawPion(Color.WHITE);
                        //System.out.println("White ("+i+", "+j+")");
                    } else if (position.board[i][j] == 0) {
                        board[i][j].eraseCellule();
                    }
                }
                count = 0;
            }
            if (Gomoku.playerTurn) {
                leftHintText.setText(Integer.toString(Gomoku.leftHintsForPlayer1));
                if (Gomoku.leftHintsForPlayer1 == 0) {
                    Home.hintBtn.setEnabled(false);
                }else
                    Home.hintBtn.setEnabled(true); 
            } else {
                leftHintText.setText(Integer.toString(Gomoku.leftHintsForPlayer2));
                if (Gomoku.leftHintsForPlayer2 == 0) {
                    Home.hintBtn.setEnabled(false);
                }else
                    Home.hintBtn.setEnabled(true); 
            }
            //this.repaint();
            setAIThinking(false);
        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D) g.create();

            if (isAIThinking) {
                printThinking(g2D);
            }
        }

        private void printThinking(Graphics2D g2D) {

            FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());
            String text = "Thinking...";

            g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2D.setFont(new java.awt.Font("Times New Roman", 1, 38));

            g2D.setColor(Color.WHITE);

            int x = (570 / 2 - metrics.stringWidth(text) * 2);
            int y = 570 / 2;

            g2D.drawString(text, x, y);
        }

        public void setAIThinking(boolean flag) {
            isAIThinking = flag;
            repaint();
        }

        // Variables declaration - do not modify                     
        // End of variables declaration                   
    }

    public void giveHint() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                board.setAIThinking(true);
                Position startingPosition = Gomoku.position;
                printPosition(startingPosition);
                Vector v = alphaBeta(0, startingPosition, playerTurn);
                Enumeration enum2 = v.elements();

                startingPosition = (Position) v.elementAt(1);
                Gomoku.position = (GomokuPosition) startingPosition;
                board.setBoardFromGomokuPosition(Gomoku.position);
                if (playerTurn) {
                    Gomoku.leftStonesForPlayer1 -= 1;
                    Home.leftBlackStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer1));
                    Gomoku.playerTurn = GameSearch.PROGRAM;
                    if (Gomoku.playMode == PlayModeType.HUMAN_VS_COMPUTER) {
                        Gomoku.canClick = false;
                    } else {
                        Gomoku.canClick = true;
                    }
                    if (wonPosition(startingPosition, playerTurn)) {
                        JOptionPane.showMessageDialog(null, "First Player won!");
                        Home.resultText.setText("First Player won!");
                        Gomoku.gameOver = true;

                        return;
                    } else if (drawnPosition(startingPosition)) {
                        JOptionPane.showMessageDialog(null, "Draw game!");
                        Home.resultText.setText("Draw game!");
                        Gomoku.gameOver = true;
                        return;
                    }
                } else {
                    Gomoku.leftStonesForPlayer2 -= 1;
                    Home.leftWhiteStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer2));
                    Gomoku.playerTurn = GameSearch.HUMAN;

                    Gomoku.canClick = true;

                    if (wonPosition(startingPosition, playerTurn)) {
                        JOptionPane.showMessageDialog(null, "Second Player won!");
                        Home.resultText.setText("Second Player won!");
                        Gomoku.gameOver = true;

                        return;
                    } else if (drawnPosition(startingPosition)) {
                        JOptionPane.showMessageDialog(null, "Draw game!");
                        Home.resultText.setText("Draw game!");
                        Gomoku.gameOver = true;
                        return;
                    }

                }
            }
        }
        );
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.start();

        board.setAIThinking(false);
        board.setBoardFromGomokuPosition(Gomoku.position);
    }

}
