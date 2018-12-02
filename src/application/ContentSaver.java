package application;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentSaver implements Serializable {
	
//	Snake Data
	private int snakeLength;
    private double snakeX;
    private double snakeY;
    private double snakeSpeed;
    private boolean snakeMagnet;
    private double magnetDuration;
    private boolean snakeShield;
    private double shieldDuration;

//  Game Data  
    private int gameScore;
    private double gameSpeed;
    private int interactablesCount;
    
    private ArrayList<Double> positionBlockX;
    private ArrayList<Double> positionBlockY;
    private ArrayList<Integer> blockValue;

    private ArrayList<Double> positionWallX;
    private ArrayList<Double> positionWallY;
    private ArrayList<Double> wallLength;

    private ArrayList<Double> positionBallX;
    private ArrayList<Double> positionBallY;
    private ArrayList<Double> ballValue;

    private ArrayList<Double> positionPowerX;
    private ArrayList<Double> positionPowerY;
    private ArrayList<String> powerUpType;
    
    
    public void setSnakelength(int snakelength) {
        this.snakeLength = snakelength;
    }

    public void setSnakeX(double snakeX) {
        this.snakeX = snakeX;
    }

    public void setSnakeY(double snakeY) {
        this.snakeY = snakeY;
    }
    
    public void setSnakeSpeed(double snakeSpeed) {
        this.snakeSpeed = snakeSpeed;
    }
    
    public void setSnakeMagnet(boolean snakeMagnet) {
        this.snakeMagnet = snakeMagnet;
    }
    
    public void setMagnetDuration(double magnetDuration) {
        this.magnetDuration = magnetDuration;
    }
    
    public void setSnakeShield(boolean snakeShield) {
        this.snakeShield = snakeShield;
    }
    
    public void setShieldDuration(double shieldDuration) {
        this.shieldDuration = shieldDuration;
    }
    
    public int getSnakelength() {
        return this.snakeLength;
    }
    
    public double getSnakeX() {
        return this.snakeX;
    }
    
    public double getSnakeY() {
        return this.snakeY;
    }
    

    public double getSnakeSpeed() {
        return this.snakeSpeed;
    }
    
    public boolean getSnakeMagnet() {
        return this.snakeMagnet;
    }
    
    public double getMagnetDuration() {
        return this.magnetDuration;
    }
    
    public boolean getSnakeShield() {
        return this.snakeShield;
    }
    
    public double getShieldDuration() {
        return this.shieldDuration;
    }
    
    public int getGameScore() {
        return this.gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public double getGameSpeed() {
        return this.gameSpeed;
    }

    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
    
    public int getInteractablesCount() {
        return this.interactablesCount;
    }

    public void setInteractablesCount(int interactablesCount) {
        this.interactablesCount = interactablesCount;
    }
    
    public ArrayList<Double> getPositionBlockX() {
        return this.positionBlockX;
    }

    public ArrayList<Double> getPositionBlockY() {
        return this.positionBlockY;
    }

    public ArrayList<Integer> getBlockValue() {
        return this.blockValue;
    }
    
    public void setPositionBlockX(ArrayList<Double> positionBlockX) {
        this.positionBlockX = positionBlockX;
    }
    
    public void setPositionBlockY(ArrayList<Double> positionBlockY) {
        this.positionBlockY = positionBlockY;
    }
    
    public void setBlockValue(ArrayList<Integer> blockValue) {
        this.blockValue = blockValue;
    }
    
    public ArrayList<Double> getPositionWallX() {
        return this.positionWallX;
    }

    public ArrayList<Double> getPositionWallY() {
        return this.positionWallY;
    }
   
    public ArrayList<Double> getWallLength() {
        return this.wallLength;
    }
    
    public void setPositionWallX(ArrayList<Double> positionWallX) {
        this.positionWallX = positionWallX;
    }

    public void setPositionWallY(ArrayList<Double> positionWallY) {
        this.positionWallY = positionWallY;
    }

    public void setWallLength(ArrayList<Double> wallLength) {
        this.wallLength = wallLength;
    }

    public ArrayList<Double> getPositionBallX() {
        return this.positionBallX;
    }

    public ArrayList<Double> getPositionBallY() {
        return this.positionBallY;
    }
   
    public ArrayList<Double> getBallValue() {
        return this.ballValue;
    }
    
    public void setPositionBallX(ArrayList<Double> positionBallX) {
        this.positionBallX = positionBallX;
    }

    public void setPositionBallY(ArrayList<Double> positionBallY) {
        this.positionBallY = positionBallY;
    }

    public void setBallValue(ArrayList<Double> ballValue) {
        this.ballValue = ballValue;
    }

    public ArrayList<Double> getPositionPowerX() {
        return this.positionPowerX;
    }

    public ArrayList<Double> getPositionPowerY() {
        return this.positionPowerY;
    }
   
    public ArrayList<String> getPowerUpType() {
        return this.powerUpType;
    }
    
    public void setPositionPowerX(ArrayList<Double> positionPowerX) {
        this.positionPowerX = positionPowerX;
    }

    public void setPositionPowerY(ArrayList<Double> positionPowerY) {
        this.positionPowerY = positionPowerY;
    }

    public void setPowerUpType(ArrayList<String> powerUpType) {
        this.powerUpType = powerUpType;
    }
    
}
