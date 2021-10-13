package home.bulls.and.cows;

import static home.bulls.and.cows.BullsAndCowsUtils.getPossibleSecretNumbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BullsAndCowsMaximaMinimaPlayer implements IBullsAndCowsPlayer {

	List<Integer> possibleSecretNumbers = new ArrayList<>();

	Map<Integer, Map<String, List<Integer>>> possibleSecretNumbersAndBullsCowsCombinations = new HashMap<>();

	public BullsAndCowsMaximaMinimaPlayer(
			Map<Integer, Map<String, List<Integer>>> possibleSecretNumbersAndBullsCowsCombinations) {
		this.possibleSecretNumbers = getPossibleSecretNumbers();
		this.possibleSecretNumbersAndBullsCowsCombinations = possibleSecretNumbersAndBullsCowsCombinations;
	}

	private int getNextGuessNumber() {
		Map<Integer, Integer> maximaMinimaMap = new HashMap<>();
		possibleSecretNumbers.parallelStream().forEach(i -> maximaMinimaMap.put(i,
				possibleSecretNumbers.size() - possibleSecretNumbersAndBullsCowsCombinations.get(i).entrySet()
						.parallelStream()
						.map(e -> e.getValue().parallelStream().filter(j -> possibleSecretNumbers.contains(j)).count())
						.mapToInt(Long::intValue).max().getAsInt()));
		return maximaMinimaMap
				.entrySet().parallelStream().filter(e -> e.getValue() == maximaMinimaMap.values().parallelStream()
						.mapToInt(Integer::intValue).max().getAsInt())
				.mapToInt(e -> e.getKey().intValue()).min().getAsInt();
	}

	@Override
	public int getNextGuessNumber(int guessNumber, long bulls, long cows) {
		evaluatePossibleSecretNumbers(possibleSecretNumbers, guessNumber, bulls, cows);
		return getNextGuessNumber();
	}

}
