package home.bulls.and.cows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BullsAndCowsUtils {

	public static void endBullsAndCows(int secretNumber, Scanner scanner, int guessNumber, int guesses) {
		System.out.println(String.format("Correct guess! Secret Number -> %s, Guess Number -> %s, Guesses -> %s",
				secretNumber, guessNumber, guesses));
		scanner.close();
		System.exit(0);
	}

	public static long evaluateBulls(int secretNumber, int guessNumber) {
		Map<Integer, Integer> secretMap = getDigitsMap(String.valueOf(secretNumber).split(""));
		Map<Integer, Integer> guessMap = getDigitsMap(String.valueOf(guessNumber).split(""));
		return IntStream.range(0, secretMap.size()).boxed().parallel().filter(i -> secretMap.get(i) == guessMap.get(i))
				.count();
	}

	public static long evaluateCows(int secretNumber, int guessNumber) {
		Map<Integer, Integer> secretMap = getDigitsMap(String.valueOf(secretNumber).split(""));
		Map<Integer, Integer> guessMap = getDigitsMap(String.valueOf(guessNumber).split(""));
		Map<Integer, Long> secretFrequencyMap = getFrequencyMap(secretMap, guessMap, secretMap);
		Map<Integer, Long> guessFrequencyMap = getFrequencyMap(secretMap, guessMap, guessMap);
		return guessFrequencyMap.entrySet().parallelStream()
				.mapToInt(
						e -> Stream
								.of(e.getValue(),
										null == secretFrequencyMap.get(e.getKey()) ? 0
												: secretFrequencyMap.get(e.getKey()))
								.parallel().mapToInt(Long::intValue).min().getAsInt())
				.sum();
	}

	private static List<Integer> getBullsCowsCombinationCodes(int secretNumber, String[] bullsCowsCombination) {
		return IntStream
				.range(1000, 10000).boxed().parallel().filter(i -> matchesBullsAndCows(secretNumber, i,
						Long.valueOf(bullsCowsCombination[0]), Long.valueOf(bullsCowsCombination[1])))
				.collect(Collectors.toList());
	}

	private static Map<String, List<Integer>> getBullsCowsCombinationCodesForSecretNumber(int secretNumber) {
		Map<String, List<Integer>> bullsCowsCombinationCodesForSecretNumber = new HashMap<>();
		getBullsCowsCombinations().parallelStream().forEach(e -> bullsCowsCombinationCodesForSecretNumber.put(e,
				getBullsCowsCombinationCodes(secretNumber, e.split("-"))));
		return bullsCowsCombinationCodesForSecretNumber;
	}

	private static List<String> getBullsCowsCombinations() {
		return Arrays.asList("0-0", "0-1", "0-2", "0-3", "0-4", "1-0", "1-1", "1-2", "1-3", "2-0", "2-1", "2-2", "3-0");
	}

	// no parallel
	private static Map<Integer, Integer> getDigitsMap(String[] digitsArray) {
		Map<Integer, Integer> digitsMap = new HashMap<>();
		IntStream.range(0, digitsArray.length).boxed().forEach(i -> digitsMap.put(i, Integer.valueOf(digitsArray[i])));
		return digitsMap;
	}

	public static int getFirstGuessNumber() {
		return 1122;
	}

	private static Map<Integer, Long> getFrequencyMap(Map<Integer, Integer> secretMap, Map<Integer, Integer> guessMap,
			Map<Integer, Integer> frequencyForMap) {
		return IntStream.range(0, secretMap.size()).boxed().parallel().filter(i -> secretMap.get(i) != guessMap.get(i))
				.map(frequencyForMap::get).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public static List<Integer> getPossibleSecretNumbers() {
		return IntStream.range(1000, 10000).boxed().parallel().collect(Collectors.toList());
	}

	public static Map<Integer, Map<String, List<Integer>>> getPossibleSecretNumbersAndBullsCowsCombinations() {
		Map<Integer, Map<String, List<Integer>>> possibleSecretNumbersAndBullsCowsCombinations = new HashMap<>();
		System.out.println("Started populating 'possibleSecretNumbersAndBullsCowsCombinations'!");
		long startTime = System.currentTimeMillis();
		IntStream.range(1000, 10000).boxed().parallel().forEach(i -> possibleSecretNumbersAndBullsCowsCombinations
				.put(i, getBullsCowsCombinationCodesForSecretNumber(i)));
		System.out.println("Completed populating 'possibleSecretNumbersAndBullsCowsCombinations' in "
				+ (System.currentTimeMillis() - startTime) / 1000 + " seconds!");
		return possibleSecretNumbersAndBullsCowsCombinations;
	}

	public static int getSecretNumber() {
		int randomNumber = new Random().nextInt(10000);
		return randomNumber < 1000 ? randomNumber + 1000 : randomNumber;
	}

	public static boolean matchesBullsAndCows(int possibleSecretNumber, int guessNumber, long bulls, long cows) {
		return bulls == evaluateBulls(possibleSecretNumber, guessNumber)
				&& cows == evaluateCows(possibleSecretNumber, guessNumber);
	}

}
