import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;  

/**
 * A simple console program to generate a password by policies.
 * 
 * Arguments - [0]: Total Length [1]: Number Length [2]: Upper Length [3]: Lower Length [4]: Special Length
 * Length = 0 means disabling
 * 
 * Author: Wing Kui, Tsoi
 * Last Updated: 2024-3-3
 */
public class PwdGen {
	
	private static String[] argsInput;
	private static int argsIndex = -1, policiesNum, totalLength, numberLength, upperLength, lowerLength, specialLength;
	private static Random rand = new Random();
	
 	private static int inputLength(int def) {
		return argsInput != null && ++argsIndex < argsInput.length ? Integer.valueOf(argsInput[argsIndex]) : def;
	}
	
	private static int policyLength(int def) {
		if ((def = inputLength(def)) > 0) policiesNum++; // policy on
		return def;
	}
	
	private static int genLenght(int policyLength) {
		if (totalLength <= policyLength) return policyLength;
		int genLength = --policiesNum == 0 ? rand.nextInt(totalLength) : totalLength - 1;
		totalLength -= genLength;
		return policyLength + genLength;
	}

	public static void main(String[] args) {
		argsInput = args;
		System.out.println("Input: " + Arrays.toString(args));
		// random upper bound
		totalLength = (totalLength = inputLength(14) + 1)
				- (numberLength = policyLength(1)) 
				- (upperLength = policyLength(1))
				- (lowerLength = policyLength(1)) 
				- (specialLength = policyLength(1));
		numberLength = genLenght(numberLength);
		upperLength = genLenght(upperLength);
		lowerLength = genLenght(lowerLength);
		specialLength = genLenght(specialLength);
		// create instance of SecureRandom
		Random random = new SecureRandom();
		// use ints() method of random to get IntStream of number of the specified
		Stream<Character> pwdStream = Stream.empty();
		pwdStream = Stream.concat(pwdStream, random.ints(numberLength, 48, 57).mapToObj(data -> (char) data));
		pwdStream = Stream.concat(pwdStream, random.ints(upperLength, 'A', 'Z').mapToObj(data -> (char) data));
		pwdStream = Stream.concat(pwdStream, random.ints(lowerLength, 'a', 'z').mapToObj(data -> (char) data));
		pwdStream = Stream.concat(pwdStream, random.ints(specialLength, 33, 45).mapToObj(data -> (char) data));
		List<Character> listOfChar = pwdStream.collect(Collectors.toList());
		Collections.shuffle(listOfChar);
		String pwd = listOfChar.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
		System.out.println("Password: " + pwd);
		System.out.println("Length: " + pwd.length());
	}

}
