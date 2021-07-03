import java.util.Arrays;

public class TestDriver_Liam {
	
	public static String formatPrint(String[][] sArr) {
		StringBuilder sb = new StringBuilder();
		int max = sArr[0][0].length();
		for(int i = 1; i < sArr.length; i++) {
			int temp = sArr[i][0].length();
			if(max < temp) {
				max = temp;
			}
		}
		for(int i = 0; i < sArr.length; i++) {
			int[] chLen = new int[sArr[i].length - 1];
			int[] temp = new int[] {max + 2, 12, 16, 16, 19, 7, 8, 7, 34, 7, 27, 20, 43, 7};
			for(int j = 0; j < chLen.length && j < temp.length; j++) {
				chLen[j] = temp[j];
			}
			if(14 < chLen.length) {
				System.arraycopy(temp, 0, chLen, 0, temp.length);
				for(int j = 14; j < chLen.length - 1; j++) {
					chLen[j] = 6;
				}
				chLen[chLen.length - 1] = 11;
			}
			for(int j = 0; j < sArr[i].length - 1; j++) {
				char[] ch = new char[chLen[j] - sArr[i][j].length()];
				Arrays.fill(ch, ' ');
				sb.append(sArr[i][j]).append(ch);
			}
			sb.append(sArr[i][sArr[i].length - 1] + "\n");
		}
		return sb.toString();
		
	}
	
	public static void main(String[] args) {
		
		Candidate.loadInitData(); //set data files
		Voter.loadData();
		
		String[] voterInfo = new String[11]; //creates array and populates with data for registered user
		String[] input = new String[] {"DOE", "JOHN", "1234 ROAD ST", "CITY", "TX", "123456", "01022003"}; //should get this from user
		for(int i = 0; i < input.length; i++) {
			voterInfo[i + 1] = input[i]; //voterInfo lastName is at 1
		}
		String searchKey = Voter.getSearchKeyNAD(voterInfo); //gets string to search with
		long locInFile; //saves location in voterFile of voterInfo
		
		if(!Voter.checkKeyNAD(searchKey)) { //if user isn't registered, gets other necessary info to register
			voterInfo[8] = new String("M"); //should get this from user
			voterInfo[9] = new String("W"); //should get this from user
			voterInfo = Voter.register(voterInfo); //registers voter, returns complete info
			locInFile = Voter.getKeyValNAD(searchKey);
		} else { //otherwise overwrite user inputed info with array containing all stored info
			locInFile = Voter.getKeyValNAD(searchKey);
			voterInfo = Voter.lookup(locInFile);
		}
		System.out.println("voterInfo: " + Arrays.toString(voterInfo)); //test print
		
		if(!Candidate.isStateValid(voterInfo[5])) { //checks if voter is registered in this state
			System.out.println("STATE IS NOT VALID"); //test print
		}
		
		if(Voter.isExpired(voterInfo[10])) { //checks expiration date of second test, because the test isn't expired, it shouldn't do anything, but it has been tested previously
			voterInfo = Voter.editLine(new String[] {Voter.genExpDate()}, 10, Voter.getKeyValNAD(searchKey));
			System.out.println("voterInfo: " + Arrays.toString(voterInfo)); //test print
		}
		Candidate.addPres(new String[] {"PRESIDENT AND VICE", "REPUBLICAN", "JAMES ROBERT AND JIMMY KRISS", "DEMOCRAT", "ELISHA BERTHA AND CATHERINE KATE"});
		Candidate.addPos(new String[] {"SENATOR", "REPUBLICAN", "JEREMY BENTLY", "REPUBLICAN", "HELDA CAMERON"});
		
		int posCount = Candidate.getPosNum();
		if(0 < posCount) { //checks if there are any positions to cast ballot for
			System.out.println("\nchoices:");
			for(int i = 1; i < (posCount + 1); i++) { //loops positions, shifted up 1
				String[][] candidates = Candidate.getAllCand(i);
				System.out.println(formatPrint(candidates));
				System.out.println();
			}
			if(!Ballot.checkKey(voterInfo[0])) { //checks that voter hasn't submitted already
				Ballot.submit(new int[] {1, 2}, voterInfo); //votes for position 1 candidate 1 and position 2 candidate 2
				if(Ballot.checkIfBallots()) { //checks that there are ballots
					String[] ballotInfo = Ballot.lookup(Ballot.getKeyVal(Voter.getVUID(locInFile))); //uses saved searchKey for testing, admin would have to look this key up using Voter.lookup()
					System.out.println("ballot:" + "\n");
					String[][] ballotArr = new String[ballotInfo.length - 1][3];
					for(int i = 1; i < ballotInfo.length; i++) { //loops through ballot
						ballotArr[i - 1] = Candidate.lookup(ballotInfo[i], i);
					}
					System.out.println(formatPrint(ballotArr));
					if(Candidate.isPresElec()) { //checks if it is a presidential election
						System.out.println("\n" + "stats: " + "\n"); //test prints demographics
						
						String[][] presidents = Candidate.getAllPresStats();
						System.out.println(formatPrint(presidents));
						
						System.out.println();
						for(int i = 2; i < (posCount + 1); i++) { 
							String[][] candidates = Candidate.getAllCandStats(i);
							System.out.println(formatPrint(candidates));
							System.out.println();
						}
						System.out.println("winners: " + "\n");
						String[][] electorial = Candidate.getElecVotes(presidents);
						System.out.println(formatPrint(electorial));
						System.out.println("\n" + Candidate.getPresWin(electorial));
						for(int i = 2; i < (posCount + 1); i++) { 
							String[][] candidates = Candidate.getAllCandStats(i);
							System.out.println(Candidate.getCandWin(candidates));
						}
					} else {
						for(int i = 1; i < (posCount + 1); i++) { 
							String[][] candidates = Candidate.getAllCandStats(i);
							System.out.println(Arrays.toString(candidates[0]));
							for(int j = 1; j < candidates.length; j++) {
								System.out.println(Arrays.toString(candidates[j]));
							}
							System.out.println();
						}
						for(int i = 1; i < (posCount + 1); i++) { 
							String[][] candidates = Candidate.getAllCandStats(i);
							System.out.println("\t" + Candidate.getCandWin(candidates));
							System.out.println();
						}
					}
				}
			}
		}

		
		Candidate.clear(); //clears files for test so upon restart, candidates aren't readded
		Candidate.loadData("LA");
		Candidate.clear();
		
	}

}
