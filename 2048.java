package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import com.codegym.engine.cell.Game;

public class Game2048 extends Game {
    
    private boolean isGameStopped = false;
    
    private int score = 0;
    
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
        
    }
    
    private void drawScene() {

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }
    
    private void createNewNumber() {
        int x;
        int y;
        
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);
        
        if (getMaxTileValue() == 2048) {
            win();
        } else if (gameField[x][y] == 0 && getRandomNumber(10) == 9) {
            gameField[x][y] = 4;
        } else {
            gameField[x][y] = 2;
        } 
        
    }
    
    private Color getColorByValue(int value) {
        if (value == 2) {
            return Color.CYAN;
        } else if (value == 4) {
            return Color.BLUE;
        } else if (value == 8) {
            return Color.TEAL;
        } else if (value == 16) {
            return Color.GREEN;
        } else if (value == 32) {
            return Color.ORANGE;
        } else if (value == 64) {
            return Color.YELLOW;
        } else if (value == 128) {
            return Color.PINK;
        } else if (value == 256) {
            return Color.MAGENTA;
        } else if (value == 512) {
            return Color.PURPLE;
        } else if (value == 1024) {
            return Color.SALMON;
        } else if (value == 2048) {
            return Color.RED;
        } else {
            return Color.WHITE;
        }
    }
    
    private void setCellColoredNumber(int x, int y, int value) {
        Color cellColor = getColorByValue(value);
        String valueString = "" + value;
        
        if (value == 0) {
            setCellValueEx(x, y, cellColor, "");
        } else {
        setCellValueEx(x, y, cellColor, valueString);
        }
    }
    
    private boolean compressRow(int[] row){
        boolean isChanged = false;
        int temp = 0;
        int[] tempArr= row.clone();
        for (int i = 0;i<tempArr.length;i++){
            for(int j = 0; j < tempArr.length-i-1; j++){
               if(tempArr[j] == 0) {
                    temp = tempArr[j];
                    tempArr[j] = tempArr[j+1];
                    tempArr[j+1] = temp;
                    
                }
        }
    }
     for(int i=0;i<row.length;i++){
         if(tempArr[i]!=row[i]){
             row[i]= tempArr[i];
             isChanged=true;
         }
     }
     
     return isChanged;
        
    }
    
    private boolean mergeRow(int [] row) {
        boolean isChanged = false;
        
        for (int m = 0; m < row.length - 1; m++) {
            if (row[m] != 0 && row[m] == row[m + 1]) {
                isChanged = true;
                row[m] *= 2;
                row[m + 1] = 0;
                score += row[m];
                setScore(score);
            }
        }
     
     return isChanged;
    }
    
    private void moveLeft() {
        boolean compress;
        boolean merge;
        int move = 0;
        for (int i = 0; i < SIDE; i++) {
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compressRow(gameField[i]);
            if (merge || compress) {
                move++;
            }
        }
        if (move > 0) {
            createNewNumber();
        }
    }
    
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void rotateClockwise() {
        int[][] tempMat = new int[SIDE][SIDE];
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE; c++) {
                tempMat[c][SIDE - 1 - r] = gameField[r][c];
            }
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE; c++) {
                gameField[r][c] = tempMat[r][c];
            }
    }
    
    private int getMaxTileValue() {
        int max = 0;
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE; c++)
                if (gameField[r][c] > max) {
                    max = gameField[r][c];
                }
        return max;
    }
    
    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GOLD, "You win!", Color.BLACK, 75);
    }
    
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "You lose :(", Color.BLACK, 75);
    }
    
    private boolean canUserMove() {
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE; c++)
                if (gameField[r][c] == 0) {
                 return true;   
                }
                    
        for (int r = 0; r < SIDE - 1; r++)
            for (int c = 0; c < SIDE; c++)
                if (gameField[r][c] == gameField[r + 1][c]) {
                    return true;
                }
                
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE - 1; c++)
                if (gameField[r][c] == gameField[r][c + 1]) {
                    return true;
                }
        return false;
    }
    
    @Override
    public void onKeyPress(Key key){
        
        if (isGameStopped = true) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            }
        } else if (canUserMove() == true) {
            if (key == Key.LEFT) {
                moveLeft();
                drawScene();
            } else if (key == Key.RIGHT) {
                moveRight();
                drawScene();
            } else if (key == Key.UP) {
                moveUp();
                drawScene();
            } else if (key == Key.DOWN) {
                moveDown();
                drawScene();
        }
    }   
    }
    
    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
        createNewNumber();
        createNewNumber();
    }
}