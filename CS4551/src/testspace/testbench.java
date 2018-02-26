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
	
	public AdaptiveHuffmanCoding() {} 
	
	public void add(char element) 
	{
		if(root == null) 
		{
			root = new Node(0);
			
			root.left = new Node('\0', 0);
			root.right = new Node(element, 1);
			update(root);
		}
	}
	
	private void addRecursive(Node node, char element) 
	{
		
	}
	
	private void update(Node node) 
	{
		if(node.left == null && node.right == null) 
		{
			
		}
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
			
			ahc.add(element);
		}
	}

}
