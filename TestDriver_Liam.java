import java.util.Arrays;

public class TestDriver {
	
	public static void loadAllData(String state) {
		Voter.loadData();
		Ballot.loadData(state);
		Candidate.loadData(state);
	}
	
	public static void main(String[] args) {
		
		loadAllData("TX"); //set data files
		
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
			System.out.println("State not valid"); //test print
		}
		
		if(Voter.isExpired(voterInfo[10])) { //checks expiration date of second test, because the test isn't expired, it shouldn't do anything, but it has been tested previously
			voterInfo = Voter.editLine(new String[] {Voter.genExpDate()}, 10, Voter.getKeyValNAD(searchKey));
			System.out.println("voterInfo: " + Arrays.toString(voterInfo)); //test print
		}
		Candidate.setStatesList(new String[] {"TX", "LS"}); //only will do two states for testing
		Candidate.setElecList(new Integer[] {38, 8});
		Candidate.addPres(new String[] {"President/Vice", "R", "James Robert and Jimmy Kriss", "L", "Elisha Bertha and Catherine Kate"});
		Candidate.addPos(new String[] {"Senator", "R", "Jeremy Bently", "R", "Helda Cameron"});
		
		int posCount = Candidate.getPosNum();
		if(0 < posCount) { //checks if there are any positions to cast ballot for
			System.out.println("\nchoices:");
			for(int i = 1; i < (posCount + 1); i++) { //loops positions, shifted up 1
				String[][] candidates = Candidate.getAllCand(i);
				System.out.println("\t" + Arrays.toString(candidates[0]));
				for(int j = 1; j < candidates.length; j++) {
					System.out.println("\t" + Arrays.toString(candidates[j]));
				}
				System.out.println();
			}
			Ballot.submit(new int[] {1, 2}, voterInfo); //votes for position 1 candidate 1 and position 2 candidate 2
			if(Ballot.checkIfBallots()) { //checks that there are ballots
				String[] ballotInfo = Ballot.lookup(Ballot.getKeyVal(Voter.getVUID(locInFile))); //uses saved searchKey for testing, admin would have to look this key up using Voter.lookup()
				System.out.println("ballot:");
				for(int i = 1; i < ballotInfo.length; i++) { //loops through ballot
					System.out.println("\t" + Arrays.toString(Candidate.lookup(ballotInfo[i], i)));
				}
				if(Candidate.isPresElec()) { //checks if it is a presidential election
					System.out.println("\n" + "stats: "); //test prints demographics
					
					String[][] presidents = Candidate.getAllPresStats();
					System.out.println("\t" + Arrays.toString(presidents[0]));
					for(int j = 1; j < presidents.length; j++) {
						System.out.println("\t" + Arrays.toString(presidents[j]));
					}
					
					System.out.println();
					for(int i = 2; i < (posCount + 1); i++) { 
						String[][] candidates = Candidate.getAllCandStats(i);
						System.out.println("\t" + Arrays.toString(candidates[0]));
						for(int j = 1; j < candidates.length; j++) {
							System.out.println("\t" + Arrays.toString(candidates[j]));
						}
						System.out.println();
					}
					System.out.println("winners: ");
					String[][] electorial = Candidate.getElecVotes(presidents);
					for(int i = 0; i < electorial.length; i++) { 
						System.out.println("\t" + Arrays.toString(electorial[i]));
					}
					System.out.println("\n\t" + Candidate.getPresWin(electorial));
					for(int i = 2; i < (posCount + 1); i++) { 
						String[][] candidates = Candidate.getAllCandStats(i);
						System.out.println("\t" + Candidate.getCandWin(candidates));
					}
				} else {
					for(int i = 1; i < (posCount + 1); i++) { 
						String[][] candidates = Candidate.getAllCandStats(i);
						System.out.println("\t" + Arrays.toString(candidates[0]));
						for(int j = 1; j < candidates.length; j++) {
							System.out.println("\t" + Arrays.toString(candidates[j]));
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
		
		Candidate.clear(); //clears files for test so upon restart, candidates aren't readded
		Candidate.loadData("LS");
		Candidate.clear();
		
	}

}
