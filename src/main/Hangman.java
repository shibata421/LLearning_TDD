package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hangman {

	Set<String> usedWordsSet = new HashSet<>();
	List<String> wordsList = new ArrayList<>();

	public int countAlphabet(String word, char alphabet) {
		int result = 0;

		for (char c : word.toCharArray()) {
			if (c == alphabet) {
				result++;
			}
		}

		return result;
	}

	public String fetchWord(int requestedLength) {
		for (String result : wordsList) {
			if (result.length() != requestedLength)
				continue;
			else if (usedWordsSet.add(result))
				return result;
		}
		return null;
	}

	public void loadWords() {
		String word = null;
		try (BufferedReader br = new BufferedReader(new FileReader("WordSource.txt"))) {
			while ((word = br.readLine()) != null) {
				wordsList.add(word);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
