package spell;


public class Words implements ITrie 
{
	private WordNode root;
	private int uniqueWordCount;
	private int nodeCount;
	
	public Words()
	{
		root = new WordNode();
		uniqueWordCount = 0;
		nodeCount = 1;
	}
	/**
	 * Adds the specified word to the trie (if necessary) and increments the word's frequency count
	 * 
	 * @param word The word being added to the trie
	 */
	public void add(String word)
	{
		StringBuilder sb = new StringBuilder(word);
		addHelper(root,sb);
	}
	
	private void addHelper(WordNode node, StringBuilder sb)
	{
		if(sb.length() == 0)
		{
			if(node.getValue() == 0)
			{
				uniqueWordCount++;
			}
			node.incFrequency();
			return;
		}
		int letterValue = Character.getNumericValue(sb.charAt(0))-10;
		if(letterValue < 0 || letterValue > 25)
		{
			System.out.println("\nNot a valid character\n");
			return;
		}
		if (node.nodes[letterValue] == null)
		{
			nodeCount++;
			node.nodes[letterValue] = new WordNode();
		}
		sb.deleteCharAt(0);
		addHelper(node.nodes[letterValue], sb);
	}
	
	/**
	 * Searches the trie for the specified word
	 * 
	 * @param word The word being searched for
	 * 
	 * @return A reference to the trie node that represents the word,
	 * 			or null if the word is not in the trie
	 */
	public spell.ITrie.INode find(String word)
	{
		StringBuilder sb = new StringBuilder(word);
		return findHelper(root,sb);
	}
	
	private spell.ITrie.INode findHelper(WordNode n, StringBuilder sb)
	{
		if(sb.length() == 0)
		{
			if(n.getValue() > 0)
			{
				return n;
			}
			else
			{
				return null;
			}
		}
		int letterValue = Character.getNumericValue(sb.charAt(0))-10;
		if(n.nodes[letterValue] == null)
		{
			return null;
		}
		sb.deleteCharAt(0);
		return findHelper(n.nodes[letterValue], sb);
	}
	
	
	
	
	
	/**
	 * Returns the number of unique words in the trie
	 * 
	 * @return The number of unique words in the trie
	 */
	public int getWordCount()
	{
		return uniqueWordCount;
	}
	
	/**
	 * Returns the number of nodes in the trie
	 * 
	 * @return The number of nodes in the trie
	 */
	public int getNodeCount()
	{
		return nodeCount;
	}
	
	/**
	 * The toString specification is as follows:
	 * For each word, in alphabetical order:
	 * <word>\n
	 */
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		StringBuilder word = new StringBuilder();
		toStringHelper(root, word, output);
		return output.toString();
	}
	private void toStringHelper(WordNode n, StringBuilder word, StringBuilder output)
	{
		if(n == null)
		{
			return;
		}
		if(n.getValue() > 0)
		{
			output.append(word.toString() + "\n");
		}
		String alph = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i < 26; ++i)
		{
			word.append(alph.charAt(i));
			toStringHelper(n.nodes[i], word,output);
			word.setLength(word.length()-1);
		}
	}
	
	@Override
	public int hashCode()
	{
		return (uniqueWordCount+nodeCount)%23;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (getClass() != o.getClass())
		{
			return false;
		}
		Words other = (Words)o;
		if(!(uniqueWordCount == other.uniqueWordCount &&
				nodeCount == other.nodeCount &&
				hashCode() == other.hashCode()))
		{
			return false;
		}
		String thisString = toString();
		String otherString = other.toString();
		if(!(thisString.equals(otherString)))
		{
			return false;
		}
		String[] theseWords = thisString.split("\\s+");
		String[] otherWords = otherString.split("\\s+");
		for (int i = 0; i < theseWords.length; i++)
		{
			if(find(theseWords[i]).getValue() != other.find(otherWords[i]).getValue())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Your trie node class should implement the ITrie.INode interface
	 */
	public interface INode 
	{
	
		/**
		 * Returns the frequency count for the word represented by the node
		 * 
		 * @return The frequency count for the word represented by the node
		 */
		public int getValue();
	}
}