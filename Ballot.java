import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Ballot extends Database {

	private static File ballotFile;
	private static File index;
	private static HashMap<String, Long> map;
	private static Set<String> keys;

	public static boolean checkKey(String key) { //true if key exists
		return keys.contains(key);
	}

	public static long getValue(String key) { //true if key exists
		return map.get(key);
	}

	public static boolean checkIfBallots() { //checks if there are any ballots cast
		return (0 < ballotFile.length());
	}

	public static void clear(String state) { //clears files, only to be called from Candidate.clear()
		ballotFile.delete();
		index.delete();
		loadData(state);
	}
	
	public static void loadData(String state) { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFiles(new String[] {"ballots", "indexB"}, state); 
		ballotFile = fs[0];
		index = fs[1];
		map = loadHash(index);
		keys = map.keySet();
	}

	public static void submit(String[] voterInfo) { //need to save ballot info to tally in another file later, takes voter info
		if (Candidate.isPresElec()) { //if there is a demographics line, update it
			Candidate.incrementDemo(Voter.getVoterDemo(voterInfo), Integer.valueOf(voterInfo[1])); //updates demographics using presidential candidate and the voter's demographic
		}
		for(int i = 0; i < voterInfo.length; i++) { //formats single digits to two
			voterInfo[i] = String.format("%02d", voterInfo[i]);
		}
		long l = writeFile(formatLine(voterInfo), ballotFile); //saves pointer from new registered info
		map.put(voterInfo[0], l); //generates hash maps as well as updates the serialization
		saveHash(map, index);
		keys = map.keySet();
	}
	
	public static String[] lookup(long l) { //returns line at pointer in file
		return readFile(l, ballotFile);
	}
	
}
