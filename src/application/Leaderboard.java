package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings("serial")
public class Leaderboard implements Serializable {

    private PriorityQueue<Score> scoreList;
    
    public Leaderboard() {
	    this.scoreList = new PriorityQueue<>(new Comparator<Score>() {
	        
	    	@Override
	        public int compare(Score o1, Score o2) {
	    		return o2.getScore() - o1.getScore();
	        }
	        
	    });
    }

    public void addScore(Score score){
        this.scoreList.add(score);
    }

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
