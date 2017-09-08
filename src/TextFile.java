import java.io.*;
public class TextFile {
    
/**
 * Text File constructor.  Open a file for reading, or create
 * a file for writing.  If we create a file, and a file already
 * exists with that name, the old file will be removed.
 * @param filename The name of the file to read from or write to
 * @param readOrWrite 'w' or 'W' for an output file (open for writing), 
 *        and 'r' or 'R' for an input file (open for reading)
 */
    public TextFile(String filename, char readOrWrite) 
    {
	try 
        {
	    if (readOrWrite == 'w' || readOrWrite == 'W') 
            {
		inputFile = false;
		file = new RandomAccessFile(filename, "rw");
	    } else if (readOrWrite == 'r' || readOrWrite == 'R') 
            {  
		inputFile = true;
		file = new RandomAccessFile(filename, "r");
	    }
	} 
        catch(Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}
	position = 0;
    }
    
/**
 * Checks to see if we are at the end of a file.  This method is only 
 * valid for input files, calling EndOfFile on an output fill will
 * cause the program to exit.
 * @return True if we are at the end of an input file, and false otherwise
 */
    public boolean EndOfFile() 
    {
	Assert.notFalse(inputFile,"EndOfFile only relevant for input files");
	try 
        {
	    return position == file.length();
	} 
        catch (Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}
	return true;
    }
    
/**
 * Calculates the size of the file in bits. For every character it adds 8 bits to the
 * size count.
 *    
 * @return size - size of the text file
 */
    public int calculateFileSize() {
    	int size = 0;
    	
    	while (!EndOfFile()) {
    		readChar();
    		size += 8;
    	}
    	
    	System.out.println(size);
    	return size;
    }

/**
 * Read in the next character from the input file
 * This method is only valid for input files, and
 * will throw an halt program execution if called on an output file. 
 * This method will also halt execution if you try to read past the
 * end of a file.
 * @return The next character from an input file
 */    
    public char readChar() 
    {
	char returnchar = 0;
	try 
        {
	    Assert.notFalse(inputFile,"Can only read from input files!");
	    Assert.notFalse(!EndOfFile(),"Read past end of file!");
	    position++;
	    returnchar = (char) file.read();
	} 
        catch (Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}

	return returnchar;
    }

/**
 * Write a character to an output file. This method is only valid for
 * output files, and will halt execution if called on an input file.
 * @param c The character to write to the output file.
 */   
    public void writeChar(char c) 
    {
	try 
        {
	    Assert.notFalse(!inputFile,"Can only write to output files!");
	    file.write((byte) c);
	} 
        catch (Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}
    }

/**
 * Close the file (works for input and output files). Output files will
 * not be properly written to disk if this method is not called.
 */    
    public void close() 
    {
	try 
        {
	    file.close();
	} 
        catch (Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}

    }
    
/**
 * Rewind the input file to the beginning, so we can reread 
 * the file.  Only valid for input files.
 */    
    public void rewind() 
    {
	try 
        {
	    Assert.notFalse(inputFile,"Can only rewind input files!");
	    file.seek(0);
	    position = 0;
	} 
        catch (Exception e) 
        {
	    System.out.println(e.getMessage());
	    System.exit(0);
	}
    }

    private boolean inputFile;
    private RandomAccessFile file;
    private long position;
}