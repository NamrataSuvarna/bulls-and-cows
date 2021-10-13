package home.bulls.and.cows;

import static home.bulls.and.cows.BullsAndCowsUtils.matchesBullsAndCows;

import java.util.List;

public interface IBullsAndCowsPlayer {

	public default void evaluatePossibleSecretNumbers(List<Integer> possibleSecretNumbers, int guessNumber, long bulls,
			long cows) {
		int counter = 0;
		while (!possibleSecretNumbers.isEmpty() && counter < possibleSecretNumbers.size()) {
			if (!matchesBullsAndCows(possibleSecretNumbers.get(counter), guessNumber, bulls, cows)) {
				possibleSecretNumbers.remove(counter);
				continue;
			}
			counter++;
		}
	}

	public int getNextGuessNumber(int guessNumber, long bulls, long cows);

}
