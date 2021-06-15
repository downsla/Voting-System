import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class Candidate extends Database {
	
	private static File candidateFile;
	private static File index;
	private static HashMap<String, Long> map;
	private static long secondLineVal;
	private static String currentState;
	private static String[] statesList;

	public static int numPos() { //number of positions
		return map.size();
	}

	public static long getKeyVal(String key) { //true if key exists
		return map.get(key);
	}

	public static void setStatesList(String[] states) { //mutator method, used to set list of all applicable states
		statesList = states;
	}
	
	public static boolean isStateValid(String voterState) {
		if(voterState.equals(currentState)) {
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
		} else if(getFirstPartOfLine(1, 0, candidateFile).charAt(0) != '\n') { //checks if demographic line is present by reading file upon reload
			secondLineVal = Long.parseLong(getFirstPartOfLine(9, 0, candidateFile));
		}
		currentState = state; //saves current state
	}

	public static void clear() { //clears files for new creation
		candidateFile.delete();
		index.delete();
		Ballot.clear(currentState);
		loadData(currentState);
	}

	public static void addPos(String[] positionInfo) { //adds position to candidate file, must be done after president if applicable
		String[] sa = new String[positionInfo.length + ((positionInfo.length - 1) / 2)]; //increases array size to hold vote count for each candidate
		System.arraycopy(positionInfo, 0, sa, 0, positionInfo.length);
		for(int i = positionInfo.length; i < sa.length; i++) { //adds empty vote count string
			sa[i] = "000000000";
		}
		long l = writeFile(formatLine(sa), candidateFile); //saves pointer from new registered info
		map.put(String.valueOf(numPos() + 1), l); //adds two digit key to hash map
		saveHash(map, index);
	}

	public static void addPres(String[] positionInfo) { //used to generate all states to vote for president, only use as first position added
		int n = ((positionInfo.length - 1) / 2); //holds number of candidates
		String[] d = new String[(n * 12) + 1]; //creates formatted header
		for(int i = 0; i < d.length; i++) {
			d[i] = "000000000";
		}
		String[] sa = new String[positionInfo.length + n]; //increases array size to hold vote count for each candidate
		System.arraycopy(positionInfo, 0, sa, 0, positionInfo.length);
		for(int i = positionInfo.length; i < sa.length; i++) { //adds empty vote count string
			sa[i] = "000000000";
		}
		String ogState = currentState; //returns to original saved state
		for(int i = 0; i < statesList.length; i++) { //number of states (for testing purposes, only 2)
			loadData(statesList[i]);
			overwriteFile(formatLine(d), 0, candidateFile); //inserts first line containing data for election
			secondLineVal = candidateFile.length();
			d[0] = String.format("%09d", secondLineVal); //stores secondLineVal in file
			overwriteFile(d[0], 0, candidateFile);
			long l = writeFile(formatLine(sa), candidateFile); //saves pointer from new registered info
			map.put(String.valueOf(1), l); //adds digit key to hash map
			saveHash(map, index);
		}
		loadData(ogState);
		secondLineVal = Long.parseLong(getFirstPartOfLine(9, 0, candidateFile)); //gets value from file
	}

	public static void incrDemo(boolean[] voterDemo, int candNum) { //overwrite data in first line using boolean array and candidate index, every ballot cast should update this
		String[] sa = readFile(0, candidateFile);
		int n = ((12 * (candNum)) - 11);
		for(int i = 0; i < voterDemo.length; i++) { //starts at voted candidate's position
			if(voterDemo[i]) { //increment if true in given boolean array
				Integer d = Integer.valueOf(sa[n + i]);
				d++;
				sa[n + i] = String.format("%09d", d); //updates and formats string array
			}
		}
		overwriteFile(formatLine(sa), 0, candidateFile); //overwrites header
	}
	
	public static void incrCandVote(String selectedNum, int ballotIndex) {
		long l = map.get(String.valueOf(ballotIndex));
		String[] sa = readFile(l, candidateFile);
		int c = sa.length - ((sa.length - 1) / 3) + Integer.valueOf(selectedNum) - 1;
		sa[c] = String.format("%09d", (Integer.valueOf(sa[c]) + 1));
		overwriteFile(formatLine(sa), l, candidateFile);
	}
	
	public static String[] lookup(String selectedNum, int ballotIndex) { //pulls all info pertaining to single candidate
		String[] sa = getPosLine(ballotIndex);
		String[] sr = new String[4];
		sr[0] = sa[0];
		int sn = Integer.valueOf(selectedNum);
		sr[1] = sa[(sn * 2) - 1];
		sr[2] = sa[sn * 2];
		sr[3] = sa[sa.length - ((sa.length - 1) / 3) + sn - 1];
		return sr;
	}
	
	public static String[][] getAllCand(String[] posLine) { //returns all candidates info for position, needs position line and number of candidates
		int n = ((posLine.length - 1) / 3);
		String[][] sr = new String[n][3];
		for(int i = 0; i < n; i++) {
			sr[i][0] = posLine[0];
			sr[i][1] = posLine[((i + 1) * 2) - 1];
			sr[i][2] = posLine[(i + 1) * 2];
		}
		return sr;
	}
	
	public static String[] getPosLine(int posLineNum) {
		String[] sa = readFile(map.get(String.valueOf(posLineNum)), candidateFile);
		return sa;
	}
	
	public static Integer[] getDemoLine() { //returns all demographics from all state files
		String ogState = currentState; //saves original state
		Integer[] ia = new Integer[readFile(0, candidateFile).length - 1];
		Arrays.fill(ia, 0);
		for(int i = 0; i < statesList.length; i++) { //number of states (for testing purposes, only 2)
			loadData(statesList[i]);
			String[] sa = readFile(0, candidateFile);
			for(int j = 0; j < ia.length; j++) {
				ia[j] += Integer.valueOf(sa[j + 1]);
			}
		}
		loadData(ogState);
		return ia;
	}
	
} 
