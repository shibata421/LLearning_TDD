package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Hangman;
import main.ScoreDB;

class TestHangman {

	/**
	 * tests countAlphabet() in Hangman Example: if the word is "pizza" and alphabet
	 * to count is p, then count is 1, if z then 2, and if x then 0
	 */
	static Hangman hangman;
	static Random random;
	int requestedLength;
	ScoreDB db;
	
	@BeforeAll
	static void setUp() {
		hangman = new Hangman();
		random = new Random();
		hangman.loadWords();
	}

	@BeforeEach
	void setUpEach() {
		requestedLength = random.nextInt(6) + 5;
		db = new ScoreDB();
	}

	@Test
	void test_alphabetCountInWord() {
		String word = "pizza";
		char alphabet = 'a';
		int count = hangman.countAlphabet(word, alphabet);
		assertEquals(1, count);
	}

	/**
	 * tests that the fetchcWord returns a word of requestedlength where the
	 * requestedLength can vary randomly between 5 and 10
	 */
	@Test
	void test_lengthOfFetchedWordRandom() {
		String word = hangman.fetchWord(requestedLength);
		assertEquals(requestedLength, word.length());
	}

	/**
	 * tests that fetchWord returns a unique word every time across 100 rounds of of
	 * Hangman.
	 */
	@Test
	void test_uniquenessOfFetchedWord() {
		Set<String> usedwordsSet = new HashSet<>();
		int round = 0;
		while (round < 100) {
			assertTrue(usedwordsSet.add(hangman.fetchWord(requestedLength)));
			round++;
		}
	}

	@Test
	void test_fetchClueBeforeAnyGuess() {
		String clue = hangman.fetchClue("pizza");
		assertEquals("-----", clue);
	}

	@Test
	void test_fetchClueAfterCorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		String newClue = hangman.fetchClue("pizza", clue, 'a');
		assertEquals("----a", newClue);
	}

	@Test
	void test_fetchClueAfterIncorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		String newClue = hangman.fetchClue("pizza", clue, 'x');
		assertEquals("-----", newClue);
	}

	@Test
	void test_whenInvalidGuessThenFetchClueThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> hangman.fetchClue("pizza", "-----", '1'));
	}
	
	@Test
	void test_whenInvalidGuessThenFetchClueThrowsExceptionWithMessage() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> hangman.fetchClue("pizza", "-----", '1'));
		assertEquals("Invalid character", e.getMessage());
	}
	
	@Test
	void test_fetchRemainingTrialBeforeAnyGuess() {
		hangman.fetchClue("pizza");
		int remainingTrials = hangman.getRemainingTrials();
		assertEquals(Hangman.MAX_TRIALS, remainingTrials);
	}
	
	@Test
	void test_fetchRemainingTrialAfterFirstCorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		hangman.fetchClue("pizza", clue, 'a');
		int remainingTrials = hangman.getRemainingTrials();
		assertEquals(Hangman.MAX_TRIALS - 1, remainingTrials);
	}
	
	@Test
	void test_fetchRemainingTrialAfterSecondCorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		String newClue = hangman.fetchClue("pizza", clue, 'a');
		hangman.fetchClue("pizza", newClue, 'p');
		int remainingTrials = hangman.getRemainingTrials();
		assertEquals(Hangman.MAX_TRIALS - 2, remainingTrials);
	}
	
	@Test
	void test_fetchRemainingTrialAfterIncorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		hangman.fetchClue("pizza", clue, 'b');
		int remainingTrials = hangman.getRemainingTrials();
		assertEquals(Hangman.MAX_TRIALS - 1, remainingTrials);
	}
	
	@Test
	void test_fetchScoreBeforeAnyGuess() {
		hangman.fetchClue("pizza");
		double score = hangman.getScore();
		assertEquals(0.0, score, 0.0);
	}
	
	@Test
	void test_fetchScoreAfterFirstCorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		hangman.fetchClue("pizza", clue, 'a');		
		double score = hangman.getScore();
		double actualScore = Hangman.MAX_TRIALS/"pizza".length();
		assertEquals(actualScore, score, 0.01);
	}
	
	@Test
	void test_fetchScoreAfterSecondCorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		String newClue = hangman.fetchClue("pizza", clue, 'a');		
		hangman.fetchClue("pizza", newClue, 'p');
		double score = hangman.getScore();
		double actualScore = 2 * Hangman.MAX_TRIALS/"pizza".length();
		assertEquals(actualScore, score, 0.01);
	}
	
	@Test
	void test_fetchScoreAfterIncorrectGuess() {
		String clue = hangman.fetchClue("pizza");
		hangman.fetchClue("pizza", clue, 'b');		
		double score = hangman.getScore();
		double actualScore = 0.0;
		assertEquals(actualScore, score, 0.01);
	}
	
	@Test
	void test_saveToDB() {
		String word = "pizza";
		Double score = 10.0;
		assertTrue(db.save(word, score));
	}
	
	@Test
	void test_fetchAScoreGivenTheWord() {
		String word = "pizza";
		Double score = 10.0;
		db.save(word, score);
		assertEquals(10.0, db.checkWord(word), 0.001);
	}
	
	@Test
	void test_fetchAScoreGivenTheWordIsNotContainedInDB() {
		String word = "pizzb";
		assertEquals(0.0, db.checkWord(word), 0.001);
	}
}
