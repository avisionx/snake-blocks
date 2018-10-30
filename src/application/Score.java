package application;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Score implements Serializable{

	private int score;
    private Date date;

    public Score(int score, Date date) {
    	this.score = score;
    	this.date = date;
    }
    
    public int getScore() {
        return score;
    }

    public String getDate() {
    	return date.toString().substring(0, 19);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setScore(int score) {
        this.score = score;
    }

}