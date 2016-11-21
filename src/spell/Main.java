package spell;

import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 * @throws spell.theDictionary.NoSimilarWordFoundException 
	 */
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException, spell.theDictionary.NoSimilarWordFoundException {
		
		String dictionaryFileName = args[0];
		String inputWord = args[1].toLowerCase();
		
		theDictionary corrector = new theDictionary();
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		System.out.println("Suggestion is: " + suggestion);
		int wordCount = corrector.getWordCount();
		int nodeCount = corrector.getNodeCount();
		System.out.println("wordCount: " + wordCount);
		System.out.println("nodeCount: " + nodeCount);
	}

}
