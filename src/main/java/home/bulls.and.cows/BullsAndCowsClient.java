package home.bulls.and.cows;

import static home.bulls.and.cows.BullsAndCowsUtils.endBullsAndCows;
import static home.bulls.and.cows.BullsAndCowsUtils.evaluateBulls;
import static home.bulls.and.cows.BullsAndCowsUtils.evaluateCows;
import static home.bulls.and.cows.BullsAndCowsUtils.getFirstGuessNumber;
import static home.bulls.and.cows.BullsAndCowsUtils.getPossibleSecretNumbersAndBullsCowsCombinations;
import static home.bulls.and.cows.BullsAndCowsUtils.getSecretNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class BullsAndCowsClient {

	private static Map<Integer, Map<String, List<Integer>>> possibleSecretNumbersAndBullsCowsCombinations = new HashMap<>();

	private static void evaluateComputerPlayer(Scanner scanner) {
		System.out.println("Choose ->");
		System.out.println("1. BullsAndCowsPlayer");
		System.out.println("2. BullsAndCowsMaximaMinimaPlayer");
		int choice = scanner.nextInt();
		if (choice == 2) {
			possibleSecretNumbersAndBullsCowsCombinations = getPossibleSecretNumbersAndBullsCowsCombinations();
		}
		List<Integer> guessPerSecretNumber = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		IntStream.range(1000, 10000).boxed().parallel()
				.forEach(i -> guessPerSecretNumber.add(playWithComputer(
						choice == 2 ? new BullsAndCowsMaximaMinimaPlayer(possibleSecretNumbersAndBullsCowsCombinations)
								: new BullsAndCowsPlayer(),
						i)));
		System.out.println(
				"Maximum turns taken : " + guessPerSecretNumber.parallelStream().mapToInt(i -> i).max().getAsInt());
		System.out.println("Average turns taken : "
				+ guessPerSecretNumber.parallelStream().mapToInt(i -> i).sum() / guessPerSecretNumber.size());
		System.out.println("Completed 'evaluateComputerPlayer' in " + (System.currentTimeMillis() - startTime) / 1000
				+ " seconds!");
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose ->");
		System.out.println("1. Play with human player");
		System.out.println("2. Evaluate computer player");
		int choice = scanner.nextInt();
		if (choice == 1) {
			playWithHumanPlayer(scanner);
		}
		if (choice == 2) {
			evaluateComputerPlayer(scanner);
		}
		if (!Arrays.asList(1, 2).contains(choice)) {
			System.out.println(String.format("Choice [%s] is invalid!", choice));
		}
	}

	private static int playWithComputer(IBullsAndCowsPlayer bullsAndCowsPlayer, int secretNumber) {
		int guessNumber = getFirstGuessNumber();
		int guesses = 1;
		while (secretNumber != guessNumber) {
			guessNumber = bullsAndCowsPlayer.getNextGuessNumber(guessNumber, evaluateBulls(secretNumber, guessNumber),
					evaluateCows(secretNumber, guessNumber));
			guesses++;
		}
		System.out.println(String.format("Correct guess! Secret Number -> %s, Guess Number -> %s, Guesses -> %s",
				secretNumber, guessNumber, guesses));
		return guesses;
	}

	private static void playWithHumanPlayer(Scanner scanner) {
		int secretNumber = getSecretNumber();
		int guessNumber = 0;
		int guesses = 0;
		do {
			System.out.println("Enter guess number ->");
			guessNumber = scanner.nextInt();
			guesses++;
			if (secretNumber == guessNumber) {
				endBullsAndCows(secretNumber, scanner, guessNumber, guesses);
			}
			if (guessNumber < 1000 || guessNumber > 9999) {
				System.out.println(String.format("Number %s not in the range 1000-9999. Try again!", guessNumber));
				continue;
			}
			System.out.println(String.format("Bulls -> %s, Cows -> %s", evaluateBulls(secretNumber, guessNumber),
					evaluateCows(secretNumber, guessNumber)));
		} while (secretNumber != guessNumber);
		endBullsAndCows(secretNumber, scanner, guessNumber, guesses);
	}

}
