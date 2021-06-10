import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class TestC extends TestF{ //test candidate class
	
	private static File candidateFile;
	private static File indexC;
	private static HashMap<String, Long> mapC;
	private static long firstPosition;
	private static String currentState;
	private static String[] states;
	
	public static int mapSizeC() { //number of positions
		return mapC.size();
	}
	
	public static long getPointerC(String s) { //accessor method
		return mapC.get(s);
	}
	
	public static void setStates(String[] sa) { //mutator method
		states = sa;
	}
	
	public static boolean checkIfPositions() { //returns true if there are positions to vote on ballot
		return (firstPosition < candidateFile.length());
	}
	
	public static boolean checkIfDemo() { //returns true if demographics are recorded for ballot
		return (1 < firstPosition);
	}
	
	public static void setData(String s) { //loads necessary global variable files and creates their hash maps and key sets, needs state
		File[] fs = setFile(new String[] {"candidates", "indexC"}, s); 
		candidateFile = fs[0];
		indexC = fs[1];
		mapC = loadHash(indexC);
		if(candidateFile.length() == 0) { //checks if candidate file is empty
			writeFile("\n", candidateFile); //inserts first line containing data for election
			firstPosition = candidateFile.length();
		}
		currentState = s; //saves current state
	}
	
	public static void createNew(String s) { //clears files for new
		candidateFile.delete();
		TestB.resetBallot(s);
		setData(s);
	}
	
	public static void addPosition(String[] sa, int n) { //adds position to candidate file, designed to be constructed in loop and passed the iterative starting from 1
		String[] sr = new String[sa.length + ((sa.length - 1) / 2)]; //increases array size to hold vote count for each candidate
		System.arraycopy(sr, 0, sa, 0, sa.length);
		for(int i = sa.length - 1; i < sr.length; i++) { //adds empty vote count string
			sr[i] = "000000000";
		}
		long l = writeFile(formatRow(sa), candidateFile); //saves pointer from new registered info
		mapC.put(String.valueOf(n), l); //adds two digit key to hash map
		saveHash(mapC, indexC);
	}
	
	public static void addPositionAll(String[] sa) { //used to generate all states to vote for position, only use as first position added
		int n = ((sa.length - 1) / 2); //holds number of candidates
		String[] d = new String[(n * 12) + 1]; //creates formatted header
		d[0] = " "; //index 0 will not be used for easier traversal functions
		for(int i = 1; i < n; i++) {
			d[i] = "000000000";
		}
		overwriteFile(formatRow(d), 0, candidateFile); //inserts first line containing data for election
		firstPosition = candidateFile.length(); //updates pointer
		String[] sr = new String[sa.length + n]; //increases array size to hold vote count for each candidate
		System.arraycopy(sr, 0, sa, 0, sa.length);
		for(int i = sa.length - 1; i < sr.length; i++) { //adds empty vote count string
			sr[i] = "000000000";
		}
		String ogState = currentState; //returns to original saved state
		for(int i = 0; i < states.length; i++) { //number of states (for testing purposes, only 2)
			setData(states[i]);
			long l = writeFile(formatRow(sa), candidateFile); //saves pointer from new registered info
			mapC.put(String.valueOf(1), l); //adds two digit key to hash map
			saveHash(mapC, indexC);
		}
		setData(ogState);
	}
	
	public static void incrementFirstLine(boolean[] ba, int n) { //overwrite data in first line using boolean array and candidate index, every ballot cast should update this
		String[] sa = readFile(0, candidateFile);
		for(int i = ((12 * (n - 1)) - 1); i < ba.length; i++) { //starts at voted candidate's position
			if(ba[i]) { //increment if true in given boolean array
				Integer d = Integer.valueOf(sa[i]);
				d++;
				sa[i] = String.format("%09d", String.valueOf(d)); //updates and formats string array
			}
		}
		overwriteFile(formatRow(sa), 0, candidateFile); //overwrites header
	}
	
	public static String pullCandidate(String s, int n) { //pulls candidate using ballot index then value
		String[] sa = readFile(mapC.get(String.valueOf(n)), candidateFile);
		return sa[Integer.valueOf(s) * 2];
	}
	
	public static int candidateNum(int n) { //gets number of candidates that are running for a position via the line in file, needed for ballot generation
		String[] sa = readFile(mapC.get(String.valueOf(n)), candidateFile);
		return ((sa.length - 1) / 3);
	}
	
} 
