package spell;


public class WordNode implements ITrie.INode 
{
	private int frequency;
	WordNode[] nodes;
	
	public WordNode()
	{
		frequency = 0;
		nodes = new WordNode[26];
	}
	
	public void incFrequency()
	{
		frequency++;
	}
	
	public int getValue()
	{
		return frequency;
	}
}