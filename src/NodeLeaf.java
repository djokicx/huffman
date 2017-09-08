
public class NodeLeaf implements Comparable<NodeLeaf> {
	
	private char c;
	private int frequency;
	private NodeLeaf leftTree;
	private NodeLeaf rightTree;
	
	/**
	 * NodeLeaf constructor for element of the tree that has character and frequency.
	 * @param c
	 * @param frequency
	 */
	public NodeLeaf(char c, int frequency) {
		this.c = c;
		this.frequency = frequency;
		this.leftTree = null;
		this.rightTree = null;	
	}
	
	/**
	 * NodeLeaf constructor, used for uncompressing files. Internal nodes that have
	 * no null character.
	 * @param b
	 */
	public NodeLeaf(boolean b) {
		this.c = '\0';
		this.leftTree = null;
		this.rightTree = null;
		}
	
	/**
	 * NodeLeaf constructor, used for uncompressing files. Leaf nodes that have an
	 * actual character.
	 * @param c
	 */
	public NodeLeaf(char c) {
		this.c = c;
		this.leftTree = null;
		this.rightTree = null;
	}
	
	/**
	 *  Gets the character.
	 * @return
	 */
	public char getC() {
		return c;
	}
	
	/**
	 *  Gets the frequency.
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Gets the left child.
	 * @return leftTree
	 */
	public NodeLeaf getLeftTree() {
		return leftTree;
	}

	/**
	 * Sets the left child.
	 * @param leftTree
	 */
	public void setLeftTree(NodeLeaf leftTree) {
		this.leftTree = leftTree;
	}

	/**
	 * Gets the right child
	 * @return rightTree
	 */
	public NodeLeaf getRightTree() {
		return rightTree;
	}

	/**
	 * Sets the right child.
	 * @param rightTree
	 */
	public void setRightTree(NodeLeaf rightTree) {
		this.rightTree = rightTree;
	}

	/**
	 * CompareTo method, compares frequencies of two objects.
	 * @param that
	 * @return 1 for greater, -1 for smaller
	 */
	public int compareTo(NodeLeaf that) {
		if (this.frequency >= that.frequency) {
			return 1;
		}
		
		return -1;
	}
	
	/**
	 * Prints the tree with indentations.
	 * @param root
	 * @param indent
	 */
	public static void printTree(NodeLeaf root, int indent) {
		if (root == null) {
			return;
		}
		
		if (root != null) {
			for(int i=0; i<indent; i++) {
				System.out.print("\t");
			}
			
				System.out.println(root.toString());
		}
		
		printTree(root.getLeftTree(), indent + 1);
		printTree(root.getRightTree(), indent + 1);
	}
	
	/**
	 * Returns a string representation of the object.
	 * If character is null, it is an internal node (SUM) with frequency, 
	 * otherwise it is a leaf with character and frequency.
	 * @return
	 */
	@Override
	public String toString() {
		if (c != '\0') {
			return "NodeLeaf [c=" + c + ", frequency=" + frequency + "]";
		}
		else {
			return "InternalNode [SUM = " + frequency + "]";
		}
	}
}