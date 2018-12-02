package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Leaderboard is a serializable class with the leaderboard info of the game
 * Fields - PriorityQueue scoreList which has the list of score
 */
@SuppressWarnings("serial")
public class Leaderboard implements Serializable {

    private PriorityQueue<Score> scoreList;

    /**
     * Constructor for LeaderBoard
     */
    public Leaderboard() {
	    this.scoreList = new PriorityQueue<>(new Comparator<Score>() {
	        
	    	@Override
	        public int compare(Score o1, Score o2) {
	    		return o2.getScore() - o1.getScore();
	        }
	        
	    });
    }

    /**
     * addScore method for adding score to leader board (scoreList)
     * @param score
     */
    public void addScore(Score score){
        this.scoreList.add(score);
    }

    /**
     * getter for Leaderboard
     * @return ArrayList with top-10 scores
     */
    public ArrayList<Score> getLeaderboard(){

        ArrayList<Score> topTenList = new ArrayList<>();
        int length = this.scoreList.size();
        
        for(int i = 0; i < Math.min(10, length); i++){
        	topTenList.add(this.scoreList.poll());
        }

        for(int i = 0; i < topTenList.size(); i++){
        	
            this.scoreList.add(topTenList.get(i));
        
        }
        
        return topTenList;
        
    }

}
