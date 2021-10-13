package home.bulls.and.cows;

import static home.bulls.and.cows.BullsAndCowsUtils.getPossibleSecretNumbers;

import java.util.ArrayList;
import java.util.List;

public class BullsAndCowsPlayer implements IBullsAndCowsPlayer {

	List<Integer> possibleSecretNumbers = new ArrayList<>();

	public BullsAndCowsPlayer() {
		this.possibleSecretNumbers = getPossibleSecretNumbers();
	}

	@Override
	public int getNextGuessNumber(int guessNumber, long bulls, long cows) {
		evaluatePossibleSecretNumbers(possibleSecretNumbers, guessNumber, bulls, cows);
		return possibleSecretNumbers.isEmpty() ? 0 : possibleSecretNumbers.get(0);
	}

}
