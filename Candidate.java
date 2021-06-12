import java.io.File;
import java.util.HashMap;

public class Candidate extends Database {
	
	private static File candidateFile;
	private static File index;
	private static HashMap<String, Long> map;
	private static long secondLineVal;
	private static String currentState;
	private static String[] statesList;

	public static int numPositions() { //number of positions
		return map.size();
	}

	public static long getValue(String key) { //true if key exists
		return map.get(key);
	}

	public static void setStatesList(String[] states) { //mutator method, used to set list of all applicable states
		statesList = states;
	}
	
	public static boolean isStateValid(String voterState) {
		if(voterState == currentState) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPresElec() { //returns true if demographics are recorded for ballot (president election)
		return (1 < secondLineVal);
	}

	public static void loadData(String state) { //loads necessary global variable files and creates their hash maps and key sets, needs state
		File[] fs = setFiles(new String[] {"candidates", "indexC"}, state); 
		candidateFile = fs[0];
		index = fs[1];
		map = loadHash(index);
		if(candidateFile.length() == 0) { //checks if candidate file is empty
			writeFile("\n", candidateFile); //inserts first line containing data for election
			secondLineVal = candidateFile.length();
		}
		currentState = state; //saves current state
	}

	public static void clear(String state) { //clears files for new creation
		candidateFile.delete();
		Ballot.clear(state);
		loadData(state);
	}

	public static void addPosition(String[] positionInfo) { //adds position to candidate file, must be done after president if applicable
		String[] sr = new String[positionInfo.length + ((positionInfo.length - 1) / 2)]; //increases array size to hold vote count for each candidate
		System.arraycopy(sr, 0, positionInfo, 0, positionInfo.length);
		for(int i = positionInfo.length - 1; i < sr.length; i++) { //adds empty vote count string
			sr[i] = "000000000";
		}
		long l = writeFile(formatLine(positionInfo), candidateFile); //saves pointer from new registered info
		map.put(String.valueOf(numPositions() + 1), l); //adds two digit key to hash map
		saveHash(map, index);
	}

	public static void addPresident(String[] positionInfo) { //used to generate all states to vote for president, only use as first position added
		int n = ((positionInfo.length - 1) / 2); //holds number of candidates
		String[] d = new String[(n * 12) + 1]; //creates formatted header
		d[0] = " "; //index 0 will not be used for easier traversal functions
		for(int i = 1; i < n; i++) {
			d[i] = "000000000";
		}
		overwriteFile(formatLine(d), 0, candidateFile); //inserts first line containing data for election
		secondLineVal = candidateFile.length(); //updates pointer
		String[] sr = new String[positionInfo.length + n]; //increases array size to hold vote count for each candidate
		System.arraycopy(sr, 0, positionInfo, 0, positionInfo.length);
		for(int i = positionInfo.length - 1; i < sr.length; i++) { //adds empty vote count string
			sr[i] = "000000000";
		}
		String ogState = currentState; //returns to original saved state
		for(int i = 0; i < statesList.length; i++) { //number of states (for testing purposes, only 2)
			loadData(statesList[i]);
			long l = writeFile(formatLine(positionInfo), candidateFile); //saves pointer from new registered info
			map.put(String.valueOf(1), l); //adds two digit key to hash map
			saveHash(map, index);
		}
		loadData(ogState);
	}

	public static void incrementDemo(boolean[] voterDemo, int candidateNum) { //overwrite data in first line using boolean array and candidate index, every ballot cast should update this
		String[] sa = readFile(0, candidateFile);
		for(int i = ((12 * (candidateNum - 1)) - 1); i < voterDemo.length; i++) { //starts at voted candidate's position
			if(voterDemo[i]) { //increment if true in given boolean array
				Integer d = Integer.valueOf(sa[i]);
				d++;
				sa[i] = String.format("%09d", String.valueOf(d)); //updates and formats string array
			}
		}
		overwriteFile(formatLine(sa), 0, candidateFile); //overwrites header
	}

	public static String getCandidate(String selectionNum, int ballotIndex) { //pulls candidate using ballot index then value
		String[] sa = readFile(map.get(String.valueOf(ballotIndex)), candidateFile);
		return sa[Integer.valueOf(selectionNum) * 2];
	}

	public static int numberOfCandidates(int positionLineNum) { //gets number of candidates that are running for a position via the line in file, needed for ballot generation
		String[] sa = readFile(map.get(String.valueOf(positionLineNum)), candidateFile);
		return ((sa.length - 1) / 3);
	}
	
} 
