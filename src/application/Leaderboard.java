package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings("serial")
public class Leaderboard implements Serializable {

    PriorityQueue<Score> scoreList = new PriorityQueue<>(new Comparator<Score>() {
        
    	@Override
        public int compare(Score o1, Score o2) {
    		return o2.getScore() - o1.getScore();
        }
        
    });

    public void addScore(Score score){
        scoreList.add(score);
    }

    public ArrayList<Score> getLeaderboard(){

        ArrayList<Score> top10 = new ArrayList<>();
        int l = scoreList.size();
        
        for(int i = 0; i < Math.min(10, l); i++){
            top10.add(scoreList.poll());
        }

        for(int i = 0; i < top10.size(); i++){
        	
            scoreList.add(top10.get(i));
        
        }
        
        return top10;
    }

}
