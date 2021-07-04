import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class Candidate extends Database {
	
	private static File candidateFile;
	private static File index;
	private static HashMap<String, Long> map;
	private static long secondLineVal;
	private static String currentState;
	private static String[] statesList = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
			  							  "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
			  							  "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			  							  "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
			  							  "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
										 };
	private static Integer[] elecList = {9,  3, 11,  6, 55,  9,  7,  3, 29, 16,
			 							 4,  4, 20, 11,  6,  6,  8,  8,  4, 10,
			 							11, 16, 10,  6, 10,  3,  5,  6,  4, 14,
			 							 5, 29, 15,  3, 18,  7,  7, 20,  4,  9,
			 							 3, 11, 38,  6,  3, 13, 12,  5, 10,  3
			 							};
	private static String[] sexList = {"Male", "Female", "Other"};
	private static String[] raceList = {"American Indian or Alaska Native", "Asian", "Black or African American", "Hispanic or Latinx", "Native Hawaiian or other Pacific Islander", "White"};
	private static String[] commonStrings = {"Party", "Votes", "Percent", "Electoral Votes"};

	public static int getPosNum() { //number of positions
		return map.size();
	}
	
	public static int getCandNum(int posLineLen) {
		return ((posLineLen - 1) / 3);
	}
	
	public static String[] getStatesList() {
		return statesList;
	}
	
	public static String[] getSexList() {
		return sexList;
	}
	
	public static String[] getRaceList() {
		return raceList;
	}

	public static long getKeyVal(String key) { //returns position in file
		return map.get(key);
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
	
	public static void loadInitData() { //only for initial startup, loads first state in list by default
		loadData(statesList[0]);
		Ballot.loadData(statesList[0]);
	}

	public static void loadData(String state) { //loads necessary global variable files and creates their hash maps and key sets, needs state
		File[] fs = setFiles(new String[] {"files/candidates", "files/indexC"}, state); 
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
	
	public static void clearAll() { //clears files for new creation
		for(int i = 0; i < statesList.length; i++) {
			loadData(statesList[i]);
			clear();
		}
	}

	public static void addPos(String[] positionInfo) { //adds position to candidate file, must be done after president if applicable
		String[] sa = new String[positionInfo.length + ((positionInfo.length - 1) / 2)]; //increases array size to hold vote count for each candidate
		System.arraycopy(positionInfo, 0, sa, 0, positionInfo.length);
		for(int i = positionInfo.length; i < sa.length; i++) { //adds empty vote count string
			sa[i] = "000000000";
		}
		long l = writeFile(formatLine(sa), candidateFile); //saves pointer from new registered info
		map.put(String.valueOf(getPosNum() + 1), l); //adds two digit key to hash map
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
		String[] sr = new String[3];
		sr[0] = sa[0];
		int sn = Integer.valueOf(selectedNum);
		
		if(sn != 0) {
			sr[1] = sa[(sn * 2) - 1];
			sr[2] = sa[sn * 2];
		} else {
			sr[1] = sr[2] = "NONE";
		}
		return sr;
	}
	
	public static String[][] getAllCand(int posLineNum) { //returns all candidates info for position, needs position line and number of candidates
		String [] posLine = getPosLine(posLineNum);
		int n = getCandNum(posLine.length);
		if(0 < n) {
			String[][] sr = new String[n + 1][2];
			sr[0] = new String[] {posLine[0], commonStrings[0]};
			for(int i = 1; i < n + 1; i++) {
				sr[i][0] = posLine[i * 2];
				sr[i][1] = posLine[(i * 2) - 1];
			}
			return sr;
		}
		return new String[][] {{""}};
	}
	
	public static String[] getPosLine(int posLineNum) {
		Long l = map.get(String.valueOf(posLineNum));
		if(l != null) {
			return readFile(l, candidateFile);
		} else {
			return new String[] {""}; //returns empty string array if position doesn't exist
		}
	}
	
	public static Integer[][] getDemoLine() { //returns all demographics from all state files
		String[] sa = readFile(0, candidateFile);
		Integer[][] ia = new Integer[(sa.length - 1) / 12][12];
		for(int i = 0; i < ia.length; i++) {
			for(int j = 0; j < ia[0].length; j++) {
				ia[i][j] = Integer.valueOf(sa[(i * 12) + j + 1]);
			}
		}
		return ia;
	}
	
	public static String[][] getAllCandStats(int posLineNum) { //returns all candidates info for position, needs position line and number of candidates
		String [] posLine = getPosLine(posLineNum);
		if(1 < posLine.length) {
			int cn = getCandNum(posLine.length);
			Integer t = 0; //total votes for position
			for(int i = 0; i < cn; i++) {
				t += Integer.valueOf(posLine[posLine.length - cn + i]);
			}
			String[][] sr = new String[cn + 1][4];
			sr[0] = new String[]{posLine[0], commonStrings[0], commonStrings[1], commonStrings[2]};
			for(int i = 1; i < cn + 1; i++) {
				sr[i][0] = posLine[i * 2];
				sr[i][1] = posLine[(i * 2) - 1];
				sr[i][2] = String.valueOf(Integer.valueOf(posLine[posLine.length - cn + i - 1]));
				sr[i][3] = getPercent(Integer.valueOf(sr[i][2]), t);
			}
			return sr;
		}
		return new String[][] {{""}};
	}
	
	public static String[][] getAllPresStats() {
		String ogState = currentState; //saves original state
		String[][] cand = getAllCand(1);
		int cn = (cand.length - 1);
		Integer[][] demo = new Integer[cn][12];
		for(int i = 0; i < demo.length; i++) {
			Arrays.fill(demo[i], 0);
		}
		Integer[][] vc = new Integer[cn][statesList.length];
		String[][] stats = new String[cn + 1][2 + demo[0].length + statesList.length + 2];
		for(int i = 0; i < statesList.length; i++) { //number of states (for testing purposes, only 2)
			loadData(statesList[i]);
			Integer[][] ia = getDemoLine();
			for(int j = 0; j < ia.length; j++) {
				for(int k = 0; k < ia[0].length; k++) {
					demo[j][k] += ia[j][k];
				}
			}
			String[] posLine = getPosLine(1);
			for(int j = 0; j < vc.length; j++) {
				vc[j][i] = Integer.valueOf(posLine[posLine.length - cn + j]);
			}
		}
		loadData(ogState);
		Integer[] vt = new Integer[cn];
		Arrays.fill(vt, 0);
		Integer t = 0;
		for(int i = 0; i < vc.length; i++) {
			for(int j = 0; j < vc[i].length; j++) {
				vt[i] += vc[i][j];
			}
			t += vt[i];
		}
		String[][] sDemo = convertToPercent(demo, t);
		String[][] sVC = convertToPercent(vc, t);
		String[] temp = new String[]{cand[0][0], cand[0][1], 
				"18 to 35 years", "36 to 65 years", "65 years and over", 
				sexList[0], sexList[1], sexList[2], 
				raceList[0], raceList[1], raceList[2], raceList[3], raceList[4], raceList[5]};
		System.arraycopy(temp, 0, stats[0], 0, temp.length);
		System.arraycopy(statesList, 0, stats[0], temp.length, statesList.length);
		String[] temp2 = new String[] {commonStrings[1], commonStrings[2]};
		System.arraycopy(temp2, 0, stats[0], (temp.length + statesList.length), temp2.length);
		for(int i = 1; i < stats.length; i++) {
			int ix = 0;
			System.arraycopy(cand[i], 0, stats[i], ix, cand[i].length);
			ix += cand[i].length;
			System.arraycopy(sDemo[i - 1], 0, stats[i], ix, sDemo[i - 1].length);
			ix += sDemo[i - 1].length;
			System.arraycopy(sVC[i - 1], 0, stats[i], ix, sVC[i - 1].length);
			ix += sVC[i - 1].length;
			stats[i][ix] = String.valueOf(vt[i - 1]);
			ix += 1;
			stats[i][ix] = String.valueOf(getPercent(vt[i - 1], t));
		}
		return stats;
	}
	
	public static String getPercent(Integer i, Integer t) {
		if(i == 0) {
			return "0.0";
		} else {
			return (String.format("%.1f", ((double) i / t) * 100));
		}
	}
	
	public static String[][] convertToPercent(Integer[][] iaa, int t) {
		String[][] saa = new String[iaa.length][iaa[0].length];
		for(int i = 0; i < iaa.length; i++) {
			for(int j = 0; j < iaa[0].length; j++) {
				saa[i][j] = getPercent(iaa[i][j], t);
			}
		}
		return saa;
	}
	
	public static String[][] getElecVotes(String[][] stats) {
		String[][] sr = new String[stats.length][3];
		double[] max = new double[statesList.length];
		int[] ix = new int[statesList.length];
		Arrays.fill(max, 0);
		sr[0] = new String[] {stats[0][0], stats[0][1], "Electoral Votes"};
		for(int i = 1; i < stats.length; i++) {
			for(int j = 0; j < statesList.length; j++) {
				double temp = Double.parseDouble(stats[i][j + 14]);
				if(max[j] < temp) {
					max[j] = temp;
					ix[j] = i;
				}
			}
			sr[i][0] = stats[i][0];
			sr[i][1] = stats[i][1];
			sr[i][2] = "0";
		}
		for(int i = 1; i < statesList.length; i++) {
			if(ix[i] != 0) {
				sr[ix[i]][2] = String.valueOf(Integer.valueOf(sr[ix[i]][2]) + elecList[i]);
			}
		}
		return sr;
	}
	
	public static String getCandWin(String[][] candStats) {
		if(1 < candStats[0].length) {
			Integer max = 0;
			int ix = 0;
			for(int i = 1; i < candStats.length; i++) {
				Integer temp = Integer.valueOf(candStats[i][2]);
				if(max < temp) {
					max += temp;
					ix = i;
				}
			}
			return new String(candStats[ix][1] + " " + candStats[ix][0] + " wins " + candStats[0][0] + ".");
		}
		return new String("");
	}
	
	public static String getPresWin(String[][] elecVotes) {
		Integer max = 0;
		int ix = 0;
		for(int i = 1; i < elecVotes.length; i++) {
			Integer temp = Integer.valueOf(elecVotes[i][2]);
			if(max < temp) {
				max += temp;
				ix = i;
			}
		}
		if(ix != 0) {
			return new String(elecVotes[ix][1] + "s " + elecVotes[ix][0] + " win " + elecVotes[0][0] + ".");
		} else {
			return "";
		}
		
	}
	
} 
