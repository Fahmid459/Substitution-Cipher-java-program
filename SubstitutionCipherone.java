import java.io.*;
import java.util.*;

public class SubstitutionCipherone {
    private static Map<Character, Character> encryptionMap;
    private static Map<Character, Character> decryptionMap;
    private static String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Method to calculate the frequencies of letters in a given string
    public static Map<Character, Integer> calculateFrequencies(String text) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char f : text.toCharArray()) {
            // Exclude non-letter characters
            if (Character.isLetter(f)) {
                frequencies.put(f, frequencies.getOrDefault(f, 0) + 1);
            }
        }
        return frequencies;
    }

    // Method to calculate the frequencies of letters in a given string as percentages
    public static Map<Character, Double> calculateFrequenciesAsPercent(String text) {
        Map<Character, Double> frequenciesAsPercent = new HashMap<>();
        Map<Character, Integer> frequencies = calculateFrequencies(text);
        int totalLetters = text.replaceAll("[^a-zA-Z]", "").length();

        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            char letter = entry.getKey();
            int frequency = entry.getValue();
            double percent = (frequency / (double) totalLetters) * 100; // Calculate percentage
            frequenciesAsPercent.put(letter, percent);
        }

        return frequenciesAsPercent;
    }

    public static void main(String[] args) {
        // Prompting user input for text that can be encrypted and decrypted
        Scanner userInput = new Scanner(System.in);
        String exitSignal = "-1";
        String textInput = "C"; // Conditional variable for encrypting textInput
        String plainText;

        System.out.println("Enter a string to encrypt(-1 to quit) or (C for cryptoanalysis)");
        while (true) { // Infinite loop
            // Generate a new random key for each run
            Random random = new Random();
            int key = random.nextInt(alphabet.length());

            // Encryption map generation
            encryptionMap = new HashMap<>();
            for (int i = 0; i < alphabet.length(); i++) {
                char currentChar = alphabet.charAt(i);
                encryptionMap.put(currentChar, alphabet.charAt((i + key) % alphabet.length()));
            }

            // Decryption map generation
            decryptionMap = new HashMap<>();
            for (int i = 0; i < alphabet.length(); i++) {
                char currentChar = alphabet.charAt(i);
                decryptionMap.put(encryptionMap.get(currentChar), currentChar);
            }

            // Print the alphabet and corresponding ciphertext for the current key
            System.out.println("Alphabet:");
            for (int i = 0; i < alphabet.length(); i++) {
                char currentChar = alphabet.charAt(i);
                System.out.print(currentChar + " ");
            }
            System.out.println();

            System.out.println("Ciphertext:");
            for (int i = 0; i < alphabet.length(); i++) {
                char currentChar = alphabet.charAt(i);
                char encryptedChar = encryptionMap.get(currentChar);
                System.out.print(encryptedChar + " ");
            }
            System.out.println();

            plainText = userInput.nextLine(); // Update plainText inside the loop

            if (plainText.equals(exitSignal)) {
                break; // Exit the loop if user inputs "-1"
            }

            StringBuilder cipherText = new StringBuilder();
            if (plainText.length() > 0) {
                if (!plainText.equals(textInput)) {
                    // Encrypt the plain text
                    for (char e : plainText.toCharArray()) {
                        if (alphabet.indexOf(e) != -1) {
                            cipherText.append(encryptionMap.get(e));
                        } else {
                            cipherText.append(e);
                        }
                    }
                    System.out.println("Encryption: " + cipherText);

                    // Decrypt the cipher text
                    StringBuilder decryptedText = new StringBuilder();
                    for (char d : cipherText.toString().toCharArray()) {
                        if (alphabet.indexOf(d) != -1) {
                            decryptedText.append(decryptionMap.get(d));
                        } else {
                            decryptedText.append(d);
                        }
                    }
                    System.out.println("Decryption: " + decryptedText);
                } else {
                    // Imports txt file, replace file path with the correct one for your environment
                    File file = new File("corpus.txt");
                    try (Scanner scannedFile = new Scanner(file)) {
                        StringBuilder txtFile = new StringBuilder();
                        while (scannedFile.hasNextLine()) {
                            txtFile.append(scannedFile.nextLine());
                        }
                        plainText = txtFile.toString();
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: " + e.getMessage());
                        // You may want to handle this exception more gracefully
                    }

                    // Encrypt the corpus text
                    for (char e : plainText.toCharArray()) {
                        if (alphabet.indexOf(e) != -1) {
                            cipherText.append(encryptionMap.get(e));
                        } else {
                            cipherText.append(e);
                        }
                    }
                    System.out.println("Cipher Text is: " + cipherText.toString());

                    // Calculate letter frequencies in cipherText as percentages
                    Map<Character, Double> frequenciesAsPercent = calculateFrequenciesAsPercent(cipherText.toString());
                    // Prints the frequency of all the characters in the corpus as percentages
                    System.out.println("Frequencies of letters (as percentage): " + frequenciesAsPercent.toString());
                }
            }

            // Prompt for the next input after processing
            System.out.println("Enter a string to encrypt(-1 to quit) or (C for cryptoanalysis)");
        }

        // Close the scanner
        userInput.close();
    }
}
