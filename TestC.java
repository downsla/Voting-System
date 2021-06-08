import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class TestC extends TestF{ //test candidate class
	
	private static File candidateFile;
	private static File indexC;
	private static HashMap<String, Long> mapC;
	private static Set<String> keysC;
	private static long firstPosition;
	
	public static boolean checkKeysC(String s) { //accessor method
		return keysC.contains(s);
	}
	
	public static long getPointerC(String s) { //accessor method
		return mapC.get(s);
	}
	
	public static boolean checkIfPositions() { //returns true if there are positions to vote on ballot
		return (firstPosition < candidateFile.length());
	}
	
	public static void setData(String s, int n) { //loads necessary global variable files and creates their hash maps and key sets, needs max number of candidates and state
		File[] fs = setFile(new String[] {"candidates", "indexC"}, s); 
		candidateFile = fs[0];
		indexC = fs[1];
		mapC = loadHash(indexC);
		keysC = mapC.keySet();
		if(candidateFile.length() == 0) { //checks if candidate file is empty
			writeFile("this is a test\n", candidateFile); //inserts first line containing data for election, needs formatting
		}
		firstPosition = candidateFile.length();
	}
	
	public static void createNew(String s, int n) { //clears files for new
		candidateFile.delete();
		TestB.resetBallot(s);
		setData(s, n);
	}
	
	public static void addPosition(String[] sa, int n) { //adds position to candidate file, designed to be constructed in loop and passed the iterative starting from 1
		String[] sr = new String[sa.length + ((sa.length - 1) / 2)]; //increases array size to hold vote count for each candidate
		System.arraycopy(sr, 0, sa, 0, sa.length);
		for(int i = sa.length - 1; i < sr.length; i++) { //adds empty vote count string
			sr[i] = "000000000";
		}
		long l = writeFile(formatRow(sa), candidateFile); //saves pointer from new registered info
		mapC.put(String.format("%02d", String.valueOf(n)), l); //adds two digit key to hash map
		saveHash(mapC, indexC);
		keysC = mapC.keySet();
	}
	
	public static void editFirstLine(String[] sa) { //overwrite data in first line, every ballot cast should update this
		
	}
	
	public static String pullCandidate(String n, String s) { //pulls candidate using ballot index then value
		String[] sa = readFile(mapC.get(n), candidateFile);
		return sa[Integer.valueOf(s) * 2];
	}
	
}
