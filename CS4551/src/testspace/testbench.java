package testspace;

import java.util.HashMap;
import java.util.Map;

class Node
{
	int value;
	
	Node parent;
	Node left;
	Node right;
	
	public Node() {}
	
	public Node(int value)
	{
		this.value = value;
	}
	
	public Node(char key, int value) 
	{
		this.value = value;
	}
}

class AdaptiveHuffmanCoding
{
	Node root;
	
	Map<Character, Node> leafNodes = new HashMap<>();
	
	public AdaptiveHuffmanCoding() {} 
	
	public void add(Node node, char element) 
	{
		
	}
}

public class testbench
{
	public static void main(String[] args)
	{
		String stream = "AADCCDD";
		
		AdaptiveHuffmanCoding ahc = new AdaptiveHuffmanCoding();
		
		for(int i = 0; i < stream.length(); i++) 
		{
			char element = stream.charAt(i);
			
			ahc.add(ahc.root, element);
		}
	}

}
