/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gomokugame;

import gui.Home;
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

    // Stores the chosen play mode
    public static PlayModeType playMode;

    // represents the game board
    public BoardPanel board = new BoardPanel();
    
    // returns true if the user clicked on the board to draw the stone
    public static boolean isClicked = false; 

    // represents the current player turn
    public static boolean playerTurn = HUMAN;
    
    // true : the user can click in the board = can play --- false : he can not
    public static boolean canClick = true;
    
    // The stone to draw position
    public static int stoneX;
    public static int stoneY;

    // true : it's the machine turn, used to prints a flag indicating it on the board
    public static boolean isAIThinking = false;

    // Stones left for the player 1
    public static int leftStonesForPlayer1;

    // Stones left for the player 2
    public static int leftStonesForPlayer2;

    // Stones left for the player 1
    public static int leftHintsForPlayer1;

    // Stones left for the player 2
    public static int leftHintsForPlayer2;

    // Game position
    public static GomokuPosition position = new GomokuPosition();
    
    // Difficulty level chosen 
    public static int depth;   
    
    // indicates if the game is over or not
    public static boolean gameOver = false; 
    
    
    // Win score should be greater than all possible board scores
    private static final int winScore = 100000000;


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
        int b;
        if (player) {
            b = GomokuPosition.HUMAN;
        } else {
            b = GomokuPosition.PROGRAM;
        }

        if (checkFiveInVertical(pos, b)) {
            return true;
        }

        if (checkFiveInHorizantal(pos, b)) {
            return true;
        }

        if (checkFiveInDiagonalInWholeBoard(pos)) {
            return true;
        }

        return false;
    }

    /* This method returns true if there is any 5 consecutive stones of
    * the enemy on the board in a any diagonal line
    */
    public boolean checkFiveInDiagonalInWholeBoard(GomokuPosition pos) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] != GomokuPosition.BLANK) {
                    if (checkTimesInDiagonal(pos, i, j, 5)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /* This method returns true if there is any 5 consecutive stones of
    * the enemy on the board in any vertical line
    */
    public boolean checkFiveInVertical(GomokuPosition pos, int target) {
        for (int i = 0; i < 19; i++) {
            if (pos.board[i][14] == pos.board[i][15] && pos.board[i][15] == pos.board[i][16]
                    && pos.board[i][16] == pos.board[i][17] && pos.board[i][17] == pos.board[i][18]
                    && pos.board[i][14] == target) {
                System.out.println("VERTICAL 4 LAST!");
                return true;
            }
        }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 15; j++) {
                if (pos.board[i][j] == pos.board[i][j + 1] && pos.board[i][j + 1] == pos.board[i][j + 2]
                        && pos.board[i][j + 2] == pos.board[i][j + 3] && pos.board[i][j + 3] == pos.board[i][j + 4]
                        && pos.board[i][j + 4] == target) {
                    System.out.println("VERTICAL !");
                    return true;
                }
            }
        }
        return false;
    }

    /* This method returns true if there is any 5 consecutive stones of
    * the enemy on the board in any horizontal line
    */
    public boolean checkFiveInHorizantal(GomokuPosition pos, int target) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == pos.board[i + 1][j] && pos.board[i + 1][j] == pos.board[i + 2][j]
                        && pos.board[i + 2][j] == pos.board[i + 3][j] && pos.board[i + 3][j] == pos.board[i + 4][j]
                        && pos.board[i + 4][j] == target) {
                    System.out.println("HORIZONTAL !");
                    return true;
                }
            }
        }
        for (int j = 0; j < 19; j++) {
            if (pos.board[14][j] == pos.board[15][j] && pos.board[15][j] == pos.board[16][j]
                    && pos.board[16][j] == pos.board[17][j] && pos.board[17][j] == pos.board[18][j]
                    && pos.board[14][j] == target) {
                System.out.println("HORIZONTAL 4 LAST!");
                return true;
            }
        }
        return false;
    }

    /* This method returns true if there is any consecutive stones of
    * the enemy on the board in a diagonal line, the number of consecutive stones 
    * is specified in the parameters, as well as the starting position to check
    */
    public boolean checkTimesInDiagonal(GomokuPosition pos, int x, int y, int numberOfCells) {
        int count = 0;
        int target = pos.board[x][y];
        int i = x;
        int j = y;
        //check for right diagonal
        while (i < 19 && j < 19) {
            if (pos.board[i++][j++] == target) {
                count++;
                if (count == numberOfCells) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        count = 0;
        i = x;
        j = y;
        //check for left diagonal
        while (i >= 0 && j >= 0) {
            if (pos.board[i--][j--] == target) {
                count++;
                if (count == numberOfCells) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        count = 0;
        i = x;
        j = y;
        //check for right down diagonal
        while (i >= 0 && j < 19) {
            if (pos.board[i--][j++] == target) {
                count++;
                if (count == numberOfCells) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        count = 0;
        i = x;
        j = y;
        //check for left down diagonal
        while (i < 19 && j >= 0) {
            if (pos.board[i++][j--] == target) {
                count++;
                if (count == numberOfCells) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }


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

    
    @Override
    public float positionEvaluation(Position p, boolean player) {

        //evaluationCount++;

        // Get board score of both players.
        float blackScore = getScore(p, true, player);
        float whiteScore = getScore(p, false, player);

        if (whiteScore == 0) {
            whiteScore = 1.0f;
        }

        // Calculate relative score of white against black
        return blackScore / whiteScore;
    }

    /* This function calculates the board score of the specified player
    */
    public float getScore(Position p, boolean player, boolean blacksTurn) {

        GomokuPosition pos = (GomokuPosition) p;

        // Calculate score for each of the 3 directions
        return evaluateHorizontal(pos.board, player, blacksTurn)
                + evaluateVertical(pos.board, player, blacksTurn)
                + evaluateDiagonal(pos.board, player, blacksTurn);
    }

    /* This function calculates the score by evaluating the stone 
    * positions in horizontal direction
    */
    public static int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

        int consecutive = 0;
        // blocks variable is used to check if a consecutive stone set is blocked by the opponent or
        // the board border. If the both sides of a consecutive set is blocked, blocks variable will be 2
        // If only a single side is blocked, blocks variable will be 1, and if both sides of the consecutive
        // set is free, blocks count will be 0.
        // By default, first cell in a row is blocked by the left border of the board.
        // If the first cell is empty, block count will be decremented by 1.
        // If there is another empty cell after a consecutive stones set, block count will again be 
        // decremented by 1.
        int blocks = 2;
        int score = 0;

        // Iterate over all rows
        for (int i = 0; i < boardMatrix.length; i++) {
            // Iterate over all cells in a row
            for (int j = 0; j < boardMatrix[0].length; j++) {
                // Check if the selected player has a stone in the current cell
                if (boardMatrix[i][j] == (forBlack ? 1 : -1)) {
                    // Increment consecutive stones count
                    consecutive++;
                } // Check if cell is empty
                else if (boardMatrix[i][j] == 0) {
                    // Check if there were any consecutive stones before this empty cell
                    if (consecutive > 0) {
                        // Consecutive set is not blocked by opponent, decrement block count
                        blocks--;
                        // Get consecutive set score
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        // Reset consecutive stone count
                        consecutive = 0;
                        // Current cell is empty, next consecutive set will have at most 1 blocked side.
                        blocks = 1;
                    } else {
                        // No consecutive stones.
                        // Current cell is empty, next consecutive set will have at most 1 blocked side.
                        blocks = 1;
                    }
                } // Cell is occupied by opponent
                // Check if there were any consecutive stones before this empty cell
                else if (consecutive > 0) {
                    // Get consecutive set score
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    // Reset consecutive stone count
                    consecutive = 0;
                    // Current cell is occupied by opponent, next consecutive set may have 2 blocked sides
                    blocks = 2;
                } else {
                    // Current cell is occupied by opponent, next consecutive set may have 2 blocked sides
                    blocks = 2;
                }
            }
            // End of row, check if there were any consecutive stones before we reached right border
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
            }
            // Reset consecutive stone and blocks count
            consecutive = 0;
            blocks = 2;
        }

        return score;
    }

    /* This function calculates the score by evaluating the stone positions in vertical direction
    * The procedure is the exact same of the horizontal one.
    */
    public static int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for (int j = 0; j < boardMatrix[0].length; j++) {
            for (int i = 0; i < boardMatrix.length; i++) {
                if (boardMatrix[i][j] == (forBlack ? 1 : -1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }
            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }

    /* This function calculates the score by evaluating the stone positions in diagonal directions
    * The procedure is the exact same of the horizontal calculation.
    */
    public static int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        // From bottom-left to top-right diagonally
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if (boardMatrix[i][j] == (forBlack ? 1 : -1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        // From top-left to bottom-right diagonally
        for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if (boardMatrix[i][j] == (forBlack ? 1 : -1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        return score;
    }

    /* This function returns the score of a given consecutive stone set.
    * count: Number of consecutive stones in the set
    * blocks: Number of blocked sides of the set (2: both sides blocked, 1: 
    * single side blocked, 0: both sides free)
    */
    public static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 1000000;
        // If both sides of a set is blocked, this set is worthless return 0 points.
        if (blocks == 2 && count < 5) {
            return 0;
        }

        switch (count) {
            case 5: {
                // 5 consecutive wins the game
                return winScore;
            }
            case 4: {
                // 4 consecutive stones in the user's turn guarantees a win.
                // (User can win the game by placing the 5th stone after the set)
                if (currentTurn) {
                    return winGuarantee;
                } else {
                    // Opponent's turn
                    // If neither side is blocked, 4 consecutive stones guarantees a win in the next turn.
                    if (blocks == 0) {
                        return winGuarantee / 4;
                    } // If only a single side is blocked, 4 consecutive stones limits the opponents move
                    // (Opponent can only place a stone that will block the remaining side, otherwise the game is lost
                    // in the next turn). So a relatively high score is given for this set.
                    else {
                        return 200;
                    }
                }
            }
            case 3: {
                // 3 consecutive stones
                if (blocks == 0) {
                    // Neither side is blocked.
                    // If it's the current player's turn, a win is guaranteed in the next 2 turns.
                    // (User places another stone to make the set 4 consecutive, opponent can only block one side)
                    // However the opponent may win the game in the next turn therefore this score is lower than win
                    // guaranteed scores but still a very high score.
                    if (currentTurn) {
                        return 50000;
                    } // If it's the opponent's turn, this set forces opponent to block one of the sides of the set.
                    // So a relatively high score is given for this set.
                    else {
                        return 200;
                    }
                } else {
                    // One of the sides is blocked.
                    // Playmaker scores
                    if (currentTurn) {
                        return 10;
                    } else {
                        return 5;
                    }
                }
            }
            case 2: {
                // 2 consecutive stones
                // Playmaker scores
                if (blocks == 0) {
                    if (currentTurn) {
                        return 7;
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }
            }
            case 1: {
                return 1;
            }
        }

        // More than 5 consecutive stones? 
        return winScore * 2;
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

    
    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        GomokuPosition pos = (GomokuPosition) p;
        int count = 0;
        for (int i = 0; i < pos.board.length; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == GomokuPosition.BLANK) {
                    count++;
                }
            }
        }

        Position[] ret = new Position[count];
        count = 0;
        for (int i = 0; i < pos.board.length; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == GomokuPosition.BLANK) {
                    GomokuPosition pos2 = new GomokuPosition();
                    for (int k = 0; k < 19; k++) {
                        for (int l = 0; l < 19; l++) {
                            pos2.board[k][l] = pos.board[k][l];
                        }
                    }
                    if (player) {
                        pos2.board[i][j] = GomokuPosition.HUMAN;
                    } else {
                        pos2.board[i][j] = GomokuPosition.PROGRAM;
                    }
                    ret[count++] = pos2;
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
        //System.out.println("reachedMaxDepth");
        boolean ret = false;
        if (depth >= Gomoku.depth) {
            return true;
        }
        if (wonPosition(p, false)) {
            ret = true;
        } else if (wonPosition(p, true)) {
            ret = true;
        } else if (drawnPosition(p)) {
            ret = true; 
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

    /*
    * This method is called whenever the Hint Button is clicked
    */
    public void giveHint() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                board.setAIThinking(true);
                board.setBoardFromGomokuPosition(position);
                Position startingPosition = Gomoku.position;
                printPosition(startingPosition);
                Vector v = alphaBeta(0, startingPosition, playerTurn);
                Enumeration enum2 = v.elements();

                startingPosition = (Position) v.elementAt(1);
                Gomoku.position = (GomokuPosition) startingPosition;

                if (playerTurn) {
                    Gomoku.leftStonesForPlayer1 -= 1;
                    Home.leftBlackStonesText.setText(Integer.toString(Gomoku.leftStonesForPlayer1));
                    board.setBoardFromGomokuPosition(Gomoku.position);
                    Gomoku.playerTurn = GameSearch.PROGRAM;
                    if (Gomoku.playMode == Gomoku.PlayModeType.HUMAN_VS_COMPUTER) {
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
                    board.setBoardFromGomokuPosition(Gomoku.position);
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
