package spell;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import spell.ITrie.INode;


public class theDictionary implements ISpellCorrector
{
	private ITrie myTrie;
	
	public theDictionary()
	{
		myTrie = new Words();
	}
	
	@SuppressWarnings("serial")
	public static class NoSimilarWordFoundException extends Exception 
	{
		
	}
	
	/**
	 * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions. 
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	public void useDictionary(String dictionaryFileName) throws IOException
	{
		File newFile = new File(dictionaryFileName);
		FileReader read = new FileReader(newFile);
		Scanner scan = new Scanner(read);
		scan.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
		while(scan.hasNext())
		{
			String newWord = scan.next();
			newWord.toLowerCase();
			myTrie.add(newWord);
		}
		scan.close();
	}
		
	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion
	 * @throws NoSimilarWordFoundException If no similar word is in the dictionary
	 */
	public String suggestSimilarWord(String inputWord) throws spell.ISpellCorrector.NoSimilarWordFoundException
	{
		INode n = myTrie.find(inputWord);
		if((n!=null)&&(n.getValue() > 0))
		{
			return inputWord;
		}
		else
		{
			ArrayList<String> words = new ArrayList<String>();
			StringBuilder sb = new StringBuilder(inputWord);
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);
				sb.deleteCharAt(i);
				words.add(sb.toString());
				sb.insert(i, c);
			}
			String alph = "abcdefghijklmnopqrstuvwxyz";
			for (int i = 0; i <= sb.length(); i++)
			{
				for(int j = 0; j < 26; j++)
				{
					char c = alph.charAt(j);
					sb.insert(i, c);
					if(!words.contains(sb.toString()))
					{
						words.add(sb.toString());
					}
					
					sb.deleteCharAt(i);
				}
			}
			for(int i = 0; i < sb.length(); i++)
			{
				for(int j = 0; j < sb.length(); j++)
				{
					if(i != j)
					{
						char c = sb.charAt(i);
						sb.deleteCharAt(i);
						sb.insert(j, c);
						if(!words.contains(sb.toString()))
						{
							words.add(sb.toString());
						}
						sb.deleteCharAt(j);
						sb.insert(i, c);
					}
				}
			}
			alph = "abcdefghijklmnopqrstuvwxyz";
			for (int i = 0; i < sb.length(); i++)
			{
				for(int j = 0; j < 26; j++)
				{
					char stringChar = sb.charAt(i);
					char c = alph.charAt(j);
					sb.deleteCharAt(i);
					sb.insert(i, c);
					if(!words.contains(sb.toString()))
					{
						words.add(sb.toString());
					}
					sb.deleteCharAt(i);
					sb.insert(i, stringChar);
				}
			}
			int most = 0;
			String bestWord = null;
			Iterator<String> i = words.iterator();
			while (i.hasNext()) 
			{
				String currentWord = i.next();
				INode otherNode = myTrie.find(currentWord);
//				if(otherNode!=null)
//				{
//					System.out.println(currentWord);
//					System.out.println(otherNode.getValue());
//				}
				if((otherNode != null)&&(otherNode.getValue()>=most))
				{
					if((bestWord == null)||((otherNode.getValue() == most)&&(currentWord.compareTo(bestWord)<0)))
					{
						most = otherNode.getValue();
						bestWord = currentWord;
					}
					else if(((otherNode.getValue() == most)&&(currentWord.length()<bestWord.length())))
					{
						bestWord = currentWord;
					}
					else if(otherNode.getValue() > most)
					{
						most = otherNode.getValue();
						bestWord = currentWord;
					}
				}
			}
			if(bestWord != null)
			{
				return bestWord;
			}
			bestWord = null;
			ArrayList<String> otherWords = new ArrayList<String>();
			Iterator<String> j = words.iterator();
			while (j.hasNext()) 
			{
				String newWord = secondSug(j.next());
				if((newWord !=null)&&!(otherWords.contains(newWord)))
				{
					otherWords.add(newWord);
				}
			}
			Iterator<String> k = otherWords.iterator();
			most = 0;
			while (k.hasNext()) 
			{
				String currentWord = k.next();
				INode otherNode = myTrie.find(currentWord);
				if((otherNode != null)&&(otherNode.getValue()>=most))
				{
					if((bestWord == null)||(otherNode.getValue() == most)&&((currentWord.compareTo(bestWord)<0)||(currentWord.length() < bestWord.length())))
					{
						bestWord = currentWord;
					}
					else if(otherNode.getValue() > most)
					{
						most = otherNode.getValue();
						bestWord = currentWord;
					}
				}
			}
			if(bestWord != null)
			{
				return bestWord;
			}
			throw new spell.ISpellCorrector.NoSimilarWordFoundException();
		}
	}
	private String secondSug(String inputWord)
	{
		//System.out.println("Second Suggestion option for: " + inputWord);
		ArrayList<String> words = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(inputWord);
		for (int i = 0; i < sb.length(); i++)
		{
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			if(!words.contains(sb.toString()))
			{
				words.add(sb.toString());
				//System.out.println(sb.toString());
				
			}			
			sb.insert(i, c);
		}
		String alph = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i <= sb.length(); i++)
		{
			for(int j = 0; j < 26; j++)
			{
				char c = alph.charAt(j);
				sb.insert(i, c);
				if(!words.contains(sb.toString()))
				{
					words.add(sb.toString());
					//System.out.println(sb.toString());
				}
				
				sb.deleteCharAt(i);
			}
		}
		for(int i = 0; i < sb.length(); i++)
		{
			for(int j = 0; j < sb.length(); j++)
			{
				if(i != j)
				{
					char c = sb.charAt(i);
					sb.deleteCharAt(i);
					sb.insert(j, c);
					if(!words.contains(sb.toString()))
					{
						words.add(sb.toString());
						//System.out.println(sb.toString());
					}
					sb.deleteCharAt(j);
					sb.insert(i, c);
				}
			}
		}
		alph = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < sb.length(); i++)
		{
			for(int j = 0; j < 26; j++)
			{
				char stringChar = sb.charAt(i);
				char c = alph.charAt(j);
				sb.deleteCharAt(i);
				sb.insert(i, c);
				if(!words.contains(sb.toString()))
				{
					words.add(sb.toString());
					//System.out.println(sb.toString());
				}
				sb.deleteCharAt(i);
				sb.insert(i, stringChar);
			}
		}
		int most = 0;
		String bestWord = null;
		Iterator<String> i = words.iterator();
		while (i.hasNext()) 
		{
			String currentWord = i.next();
			INode otherNode = myTrie.find(currentWord);
			if((otherNode != null)&&(otherNode.getValue()>most))
			{
				if((bestWord == null)||(otherNode.getValue() == most)&&((currentWord.compareTo(bestWord)<0)||(currentWord.length() < bestWord.length())))
				{
					bestWord = currentWord;
				}
				else if(otherNode.getValue() > most)
				{
					most = otherNode.getValue();
					bestWord = currentWord;
				}
			}
		}
		return bestWord;
	}
	
	public int getWordCount()
	{
		return myTrie.getWordCount();
	}
	
	public int getNodeCount()
	{
		return myTrie.getNodeCount();
	}
	
	@Override
	public String toString()
	{
		return myTrie.toString();
	}
}
