import java.util.Arrays;
import java.util.Collections;


public class Compress {
	private int frequency[];
	private NodeLeaf nodes[];
	private String table[];
	private int size;
	private int compressedSize;
	
	public Compress () {
		this.frequency = new int[256];
		this.table = new String[256];
		this.size = 0;
		this.compressedSize = 0;
	}
	
	/**
	 * Gets the nodes array.
	 * @return nodes
	 */
	public NodeLeaf[] getNodes() {
		return nodes;
	}
	
	
	public int[] getFrequency() {
		return frequency;
	}

	public void setFrequency(int[] frequency) {
		this.frequency = frequency;
	}

	public String[] getTable() {
		return table;
	}

	public void setTable(String[] table) {
		this.table = table;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCompressedSize() {
		return compressedSize;
	}

	public void setCompressedSize(int compressedSize) {
		this.compressedSize = compressedSize;
	}

	public void setNodes(NodeLeaf[] nodes) {
		this.nodes = nodes;
	}

	/**
	 * Size of the compressed file (in bits)
	 * Adds up:
	 * For each character c in the input file, (frequency of c) * size of the encoding for c
	 * Size of the tree (1 bit for each internal node, 9 bits for each leaf
	 * An extra 2 bytes (16 bits) for the magic number
	 * An extra 4 bytes (32 bits) for header information used in the BinaryFile class
	 * Compressed file size needs to be a multiple of 8 bits -- 
	 * so the calculated binary file size has to be a multiple of 8
	 * @return size of the compressed file
	 */
	public int calculateCompressedSize() {
		countTreeCharacters(nodes[0]);
		compressedSize += 16; // for the magic number
		compressedSize += 32; // for head information
		compressedSize += (8 - (compressedSize % 8));
//		System.out.println(compressedSize);
		return compressedSize;
	}
	
	/**
	 * Partly calculates the compression size.
	 * Adds up:
	 * For each character c in the input file, (frequency of c) * size of the encoding for c
	 * Size of the tree (1 bit for each internal node, 9 bits for each leaf
	 * @param root
	 */
	private void countTreeCharacters(NodeLeaf root) {
		
		if(root == null) {
			return;
		}
		//(frequency of c) * size of the encoding for c
		if(root.getLeftTree() == null && root.getRightTree() == null) {
	    	compressedSize += (root.getFrequency() * table[(int) root.getC()].length());
	    	compressedSize += 9;

		}
		else {
			compressedSize += 1;
		}
		countTreeCharacters(root.getLeftTree());
		countTreeCharacters(root.getRightTree());   
	}
	
	/**
	 * Calculates the size of the original file
	 * @return size - size of file
	 */
    public int calculateOriginalSize() {
    	int size = 0;
    	
    	for (int i = 0; i < frequency.length; i++) {
    		if (frequency[i] > 0) {
    			size += frequency[i] * 8;
    		}
    	}
    	
//    	System.out.println(size);
		return size;   	
    }
    
    
	/**
	 * Creates an array of frequencies (ASCII 256 elements) where ASCII numbers are array indexes.
	 * Every time a letter is encountered, its count is updated.
	 * @param file
	 */
	public void countFrequency(TextFile file) {
		while (!file.EndOfFile()) {
			int ascii = (int) file.readChar();
//			System.out.println(ascii);
			
			if (frequency[ascii] == 0) {
				size++;
			}
			
			frequency[ascii] = frequency[ascii] + 1;
		}
		
		file.rewind();			
		this.nodes = new NodeLeaf[size];	
	}
	
	
	/**
	 * Builds an array of NodeLeafs based on the frequencies. The nodes have the character they
	 * represent as well as the frequency.
	 */
	public void buildFrequencyArray() {
		for (int i = 0; i < frequency.length; i++) {
			if (frequency[i] > 0) {
				nodes[size - 1] = new NodeLeaf((char) i, frequency[i]);
				size--;
			}
		}
		
		Arrays.sort(nodes, Collections.reverseOrder());
	}
	
	
	/**
	 * Builds HuffmanTree from an array of NodeLeafs, by starting at the end of array,
	 * and all the way to 0th index, which becomes the root of the tree.
	 */
	public void buildHuffmanTree() {
		size = nodes.length - 1;
		//Sort this list by frequency and make the two-lowest elements into leaves,
		//creating a parent node with a frequency that is the sum of the two lower element's frequencies:
		while (size >= 1) {
			NodeLeaf sum = new NodeLeaf ('\0', 
					(nodes[size].getFrequency() + nodes[size - 1].getFrequency()));
			sum.setLeftTree(nodes[size]);
			sum.setRightTree(nodes[size - 1]);
			nodes[size - 1] = sum;
			Arrays.sort(nodes, 0, size, Collections.reverseOrder());
			size--;
		}		
	}
	
		
	/**
	 * Builds a lookup table from the leaves of the tree. The leaves are nodes
	 * with actual characters and frequencies, while the internal nodes contain
	 * the sum of their children's frequencies.
	 * @param root
	 * @param bytecode
	 */
	public void buildLookUpTable(NodeLeaf root, String bytecode)
	{	
		if(root == null) {       
			return;
		}
		
		else if(root.getLeftTree() == null && root.getRightTree() == null) {
	    	table[(int) root.getC()] = bytecode;
//	    	System.out.println(bytecode);
		}
	    	buildLookUpTable(root.getLeftTree(), bytecode + "0");
	    	buildLookUpTable(root.getRightTree(), bytecode + "1");   
	}
	
	/**
	 * Only possible if "-v" flag exists. Prints out:
	 * The frequency of each character in the input file 
	 * The Huffman tree
	 * The Huffman codes for each character that has a code
	 * The size of the uncompressed file and the size of the compressed file
	 */
	
	public void printForC() {
		System.out.println("THE FREQUENCY OF EACH CHARACTER IN THE INPUT FILE:");
		printInOrderFrequencies(nodes[0]);
		System.out.println();
		System.out.println("THE HUFFMAN TREE:");
		NodeLeaf.printTree(nodes[0], 0);
		System.out.println();
		System.out.println("THE HUFFMAN CODES FOR EACH CHARACTER THAT HAS A CODE:");
		printLookUpTable();
		System.out.println();
		System.out.println("UNCOMPRESSED FILE SIZE: " + calculateOriginalSize() + " BITS");
		System.out.println("COMPRESSED FILE SIZE: " + compressedSize + " BITS");
	}
	
	/**
	 * Prints the tree in order. Only used for "-v" flag.
	 * @param root
	 */	
	private void printInOrderFrequencies(NodeLeaf root) {
		if (root == null) {
			return;
		}
		
		printInOrderFrequencies(root.getLeftTree());
		if (root.getC() != '\0') {
			System.out.println("char: " + (short) root.getC() + "\t" + "frequency: " + root.getFrequency());
		}
		
		printInOrderFrequencies(root.getRightTree());
	}
	
	
	/**
	 * Prints the array - LookUpTable. Only used for "-v" flag.
	 */
	private void printLookUpTable() {
		for (int i = 0; i < table.length; i ++) {
			if (table[i] != null) {
				System.out.println("char: " + i + "\t" + "binary code: " + table[i]);
			}
		}
	}
	
	
	// table [(short) iFile.readChar()].toCharArray() -- takes an element from the file;
	// finds its binary representation in the LookUpTable and converts it into a string
	/**
	 * Writes a preorder traversal of the tree in binary representation.
	 * For leaf write : 1'character'
	 * For internal node write : 0
	 * 
	 * @param root
	 * @param file
	 */
	private void preorderList(NodeLeaf root, BinaryFile file) {
		if (root == null) {       
			return;
		}
		
		else if (root.getLeftTree() == null && root.getRightTree() == null) { // CHANGED IT FROM IF -> ELSE IF -- DID NOT TEST
	    	file.writeBit(true);
	    	file.writeChar(root.getC());
//	    	System.out.print("1" + "'" + root.getC() + "'");
		}
	    
	    else {
//	    	System.out.print("0");
	    	file.writeBit(false);
	    }
	    preorderList(root.getLeftTree(), file);
	    preorderList(root.getRightTree(), file); 
	}
	
	
	/**
	 * Compresses the file by comparing the characters and the lookup table
	 * binary representations. Done in a binary form.
	 * @param iFile
	 * @param oFile
	 */
	public void compress(TextFile iFile, BinaryFile oFile) {
		oFile.writeChar('H'); oFile.writeChar('F');
		
		preorderList(nodes[0], oFile);
	
		while (!iFile.EndOfFile()) {
			short ascii = (short) iFile.readChar();
			
			for (char c : table [ascii].toCharArray()) {
				if (c == '1') {
//					System.out.print("1");
					oFile.writeBit(true);
				}
				else {
//					System.out.print("0");
					oFile.writeBit(false);
				}
			}
		}
	}
}