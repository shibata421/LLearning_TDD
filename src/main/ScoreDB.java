package main;

import java.util.HashMap;
import java.util.Map;

public class ScoreDB {
	private Map<String, Double> db = new HashMap<>();

	public boolean save(String word, double score) {
		db.put(word, score);
		return true;
	}

	public double checkWord(String word) {
		if(db.containsKey(word)) {
			return db.get(word);
		} else {
			return 0;			
		}
	}
}
