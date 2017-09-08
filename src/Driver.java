public class Driver {

	public static void main(String[] args) {
		
		if (args.length == 0) {
	    	System.err.println("No arguments provided.");
	    }
	        
	    ArgumentParser argParser = new ArgumentParser(args);
	    Compress data = new Compress();
	    
	    // If arguments contains "-c" flag -- compress the file
	    if (argParser.hasFlag("-c")) {
	    	// Create an instance of a file from the input (the file to be compressed)
	    	TextFile inputFile = new TextFile(args[args.length - 2], 'r');
	
	    	data.countFrequency(inputFile);
//	    	System.out.println(Arrays.toString(data.getFrequency()));
	    	data.buildFrequencyArray();
//	    	System.out.println(Arrays.toString(data.getFrequency()));
	    	data.buildHuffmanTree();
//	    	NodeLeaf.printTree(data.getNodes()[1], 0);
//	    	System.out.println();
	    	data.buildLookUpTable(data.getNodes()[0], "");
//	    	System.out.println(Arrays.toString(data.getTable()));
	    	BinaryFile outputBinary = new BinaryFile(args[args.length - 1], 'w');
	    	
	    	if (argParser.hasFlag("-f")) {
	    		data.calculateOriginalSize();
	    		data.calculateCompressedSize();
	    		data.compress(inputFile, outputBinary);
	    	}	    	
	    	else {
	    		if (data.calculateOriginalSize() > data.calculateCompressedSize()) {
	    			data.compress(inputFile, outputBinary);
	    		}
	    	}
	    	
	    	if (argParser.hasFlag("-v")) {
	    		data.printForC();
	    	}
	    	
	    	inputFile.close();
	    	outputBinary.close();
	    }
	    
	 // If arguments contains "-u" flag -- uncompress the file
	    if (argParser.hasFlag("-u")) {
	    	// Create an instance of a file from the input (the file to be uncompressed)
	    	BinaryFile inputFile = new BinaryFile(args[args.length - 2], 'r');
	    	
	    	if (inputFile.hasMagicNumber()) {
	    		NodeLeaf head = inputFile.buildTree();
	    		TextFile outputFile = new TextFile(args[args.length - 1], 'w');
	    		
//	    		NodeLeaf.printTree(head, 0);
	    		while (!inputFile.EndOfFile()) {
	    			inputFile.uncompress(head, outputFile);
	    		}

	    		if (argParser.hasFlag("-v")) {
	    			NodeLeaf.printTree(head, 0);
	    		}
	    		
	    		outputFile.close();
	    	}
	    	else {
	    		System.err.println("Decompression denied. "
	    				+ "Compression of data not performed with existing compression scheme ");
	    	}
	    	
	    	inputFile.close();
	    }
	}
}