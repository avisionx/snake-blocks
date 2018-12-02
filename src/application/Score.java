package application;

import java.io.Serializable;
import java.util.Date;

/**
 * Score class objects are used for storing the score of a user
 * Fields - Score and Date
 */
@SuppressWarnings("serial")
public class Score implements Serializable{

	private int score;
    private Date date;

    /**
     * constructor for Score
     * @param score - user score
     * @param date - java.util.Date object for the time and date at which the score was achieved
     */
    public Score(int score, Date date) {
    	this.score = score;
    	this.date = date;
    }

    /**
     * getter for score of the user
     * @return Integer Score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * getter for date at which the score was achieved
     * @return String format representation of Date
     */
    public String getDate() {
    	return this.date.toString().substring(0, 19);
    }

}