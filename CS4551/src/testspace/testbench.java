package testspace;

class Node
{
	char key;
	int value;
	Node parent;
	
	Node left;
	Node right;
	
	public Node() {}
	
	public Node(int value)
	{
		this.value = value;
	}
	
	public Node(Node parent, char key, int value) 
	{
		this.parent = parent;
		this.key = key;
		this.value = value;
	}
}

class AdaptiveHuffmanCoding
{
	Node root;
	
	public AdaptiveHuffmanCoding() {} 
	
	public void increment(char element) 
	{
		if(root == null) 
		{
			root = new Node(0);
			
			root.left = new Node(root, '\0', 0);
			root.right = new Node(root, element, 1);
			
			root.value = root.left.value + root.right.value;
		}
		else 
		{
			recursiveIncrement(root, element);
		}
		
//		update(root.left);
//		update(root.right);
//		
//		root.value = root.left.value + root.right.value;	

	}
	
	private boolean recursiveIncrement(Node node, char element) 
	{
		{// check leaf node for same element. increment if same
			if(node.left == null && node.right == null) 
			{
				boolean hasElement = node.key == element;
				if(hasElement) 
				{
					node.value++;
				}
				
				return hasElement;
			}	
		}
		
		// recursive step in non-leaf node's child
		boolean hasElementLeft = recursiveIncrement(node.left, element);		
		boolean hasElementRight = false;
		if(!hasElementLeft) // don't waste time if element has found
		{
			hasElementRight = recursiveIncrement(node.right, element);
		}
		
		// case if element is new to the tree
		if(node.equals(root) && !(hasElementLeft || hasElementRight)) 
		{
			addElement(root, element);
		}
		
		return hasElementLeft || hasElementRight;
	}

	// purpose of returning boolean: not to waste time recuring in the tree if element was added
	private boolean addElement(Node node, char element)
	{
		if(node.left == null && node.right == null) 
		{
			boolean hasKey = node.key == '\0';
			if(hasKey) 
			{
				Node newNode = new Node(0);
				newNode.parent = node.parent;
				
				if(node.parent.left == node) 
				{
					node.parent.left = newNode;
				}
				else 
				{
					node.parent.right = newNode;
				}
				
				node.parent = newNode;
				newNode.left = node;
				newNode.right = new Node(newNode, element, 1);
				
				newNode.value = newNode.left.value + newNode.right.value;
			}
			
			return hasKey;
		}
		else 
		{// recursive step
			boolean hasAddLeft = addElement(node.left, element);
			boolean hasAddRight = false;
			if(!hasAddLeft) 
			{
				hasAddRight = addElement(node.right, element);	
			}
			
			return hasAddLeft || hasAddRight;
		}
	}

	private void update(Node node) 
	{
		if(node.left != null && node.right != null) 
		{
			update(root.left);
			update(root.right);
			
			root.value = root.left.value + root.right.value;
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
			
			ahc.increment(element);
		}
		
		System.out.println();
	}

}
