import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class TestDriver_Kirk {
	
	public static void loadAllData(String state) {
		Voter.loadData();
		Ballot.loadData(state);
		Candidate.loadData(state);
	}

	/*
	This used for testing bulk registration data. Used the full names of everyone currently registered in CPSC 4360.
	Full names are split, the address is Lamar University and DOB is set to 01012000. Sex and Race are set to O.
 	*/
	public static void registerNewTestVoterData() {
		String[] fullNames = new String[]{"KIRK SARRINE", "STEFAN ANDREI", "ERIC BARROW", "KALAN BONNETTE",
				"COURTNEY BOWE", "HENRY DAO", "LIAM DOWNS", "JANUARIO FUENTES", "REAGAN GRIFFIS", "VIET-TUAN LUONG",
				"MUHAMMAD MUBASHIR", "NICHOLAS MUGLESTON", "ADETOLA OLUKOYA", "CHRISLYN PETTUS"};
		for(String name: fullNames){
			String[] voterInfo = new String[11];
			String[] splitName = name.split(" ");
			String[] input = new String[]{splitName[0], splitName[1], "4400 ML KING JR PKWY", "BEAUMONT", "TX", "77705",
			"01012000","O","O"};
			//voterInfo lastName is at 1
			System.arraycopy(input, 0, voterInfo, 1, input.length);

			String searchK = Voter.getSearchKeyNAD(voterInfo); //gets string to search with
			long locInFile; //saves location in voterFile of voterInfo

			System.out.println("voterFullName: " + name);
			System.out.print("voterInfo: " + Arrays.toString(voterInfo));

			if(Voter.checkKeyNAD(searchK)) {
				System.out.println("\nVoter already database, registration not required\n");
			}
			else {
				System.out.println("\nVoter not in database, registering voter");
				Voter.register(voterInfo); //registers voter, returns complete info
				locInFile = Voter.getKeyValNAD(searchK);
				if(Voter.checkKeyNAD(searchK))
					voterInfo = Voter.lookup(locInFile);
					System.out.println("Voter now in database, registration successful\n");
					System.out.println("voterInfo: " + Arrays.toString(voterInfo) + "\n"); //test print
				}
		}
	}

	/* Get user input from the terminal to register a new voter. If the voter is already in the database,
	the voter is not added. Otherwise, new voter info is added to database and written to voters.csv
	 */
	public static void registerNewVoter()
	{
		int age = 18; // Used for checking potential voters age based on entered DOB, assume at first they are 18
		Scanner scan = new Scanner(System.in);
		String answer; // Used for user response
		// Used for terminal output
		String[] fields = new String[]{"first name", "last name", "street",
				"city", "state", "zip code (5 digits)", "DOB (mmddyyyy)",
				"sex", "race"};
		// Strings are used for comparing input for different attributes
		String[] stateAbbrev = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN",
				"IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM",
				"NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV",
				"WI", "WY"};
		String[] sex = {"M", "F", "O"};
		String[] race = {"N", "A", "B", "H", "P", "W"};

		// Create HashSets for the different attributes
		HashSet<String> states = new HashSet<>(Arrays.asList(stateAbbrev));
		HashSet<String> sexes = new HashSet<>(Arrays.asList(sex));
		HashSet<String> races = new HashSet<>(Arrays.asList(race));
		String[] voterInfo = new String[11]; // Used to build string to add to database and voter.csv

		System.out.println("****VOTER INPUT****\n");

		// Loops through the 9 fields required to enter a new voter in the database
		for(int i = 0; i < 9; i++) {
				System.out.print("Enter " + fields[i] + ": ");

				/*
				If statements are used if the input is greater than the allowed max
				While loop continues until user enters in valid info for the attribute
				Another while loop is used to confirm valid input by prompting the user for a Y, N answer
				trim() is for leading whitespaces and toUpperCase() for capitalizing all characters
				 */

				// First name, last name and city
				if (i <= 1 || i == 3) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (voterInfo[i + 1].length() > 20 || !voterInfo[i + 1].matches("^[a-zA-Z_ ]*$")) {
							System.out.println("Invalid input, please re-enter " + fields[i].toLowerCase() + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// Street
				else if (i == 2) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (voterInfo[i + 1].length() > 40 || !voterInfo[i + 1].matches("^[a-zA-Z0-9_ ]*$")) {
							System.out.println("Invalid input, please re-enter " + fields[i].toLowerCase() + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// State
				else if (i == 4) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (!states.contains(voterInfo[i + 1])) {
							System.out.println("Invalid input, please re-enter " + fields[i] + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// Zipcode
				else if (i == 5) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (voterInfo[i + 1].length() != 5 || !voterInfo[i + 1].matches("^[0-9]*$")) {
							System.out.println("Invalid input, please re-enter " + fields[i] + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// DOB
				else if (i == 6) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (voterInfo[i + 1].length() != 8 || !voterInfo[i + 1].matches("^[0-9]*$")) {
							System.out.println("Invalid input, please re-enter " + fields[i] + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) {
							age = Voter.getAge(voterInfo[i + 1]); // Get the age from the entered birthdate
							break;
						}
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// Sex
				else if (i == 7) {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (!sexes.contains(voterInfo[i + 1])) {
							System.out.println("Invalid input, please re-enter " + fields[i] + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				// Race
				else {
					while (true) {
						answer = "";
						voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						while (!races.contains(voterInfo[i + 1])) {
							System.out.println("Invalid input, please re-enter " + fields[i] + ": ");
							voterInfo[i + 1] = scan.nextLine().trim().toUpperCase();
						}
						System.out.println("You entered: " + voterInfo[i + 1]);
						while (!answer.equals("Y") && !answer.equals("N")) {
							System.out.println("Is this correct (Y/N)?: ");
							answer = scan.nextLine().trim().toUpperCase();
						}
						if (answer.equals("Y")) break;
						System.out.print("Enter " + fields[i] + ": ");
					}
				}
				System.out.println();
		} // end of for loop

		// Check age based on entered DOB
		if (age >= 18) {
			String searchKey = Voter.getSearchKeyNAD(voterInfo); //gets string to search with
			long locInFile; //saves location in voterFile of voterInfo

			// Check for if the voter is already in the database
			if (Voter.checkKeyNAD(searchKey))
				System.out.println("\nVoter already database, registration not required");
			else {
				System.out.println("Voter not in database, registering voter");
				Voter.register(voterInfo); //registers voter, returns complete info
				locInFile = Voter.getKeyValNAD(searchKey); // Used for output at the end of else

				// Check to ensure the new voter info was added
				if (Voter.checkKeyNAD(searchKey)) {
					voterInfo = Voter.lookup(locInFile); // Pulls the location from the voters.csv file
					System.out.println("Voter now in database, registration successful\n");
					System.out.println("voterInfo: " + Arrays.toString(voterInfo) + "\n"); //test print
				}
			}
		}
		//  If not 18, then cannot register to vote
		else
		{
			System.out.println("Individual is not 18 years old, and cannot register to vote");
		}
	}
	
	public static void main(String[] args) {

		/*
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
		System.out.println("voterInfo: " + Arrays.toString(voterInfo) + "\n"); //test print
		
		if(!Candidate.isStateValid(voterInfo[5])) { //checks if voter is registered in this state
			System.out.println("State not valid"); //test print
		}
		
		if(Voter.isExpired(voterInfo[10])) { //checks expiration date of second test, because the test isn't expired, it shouldn't do anything, but it has been tested previously
			voterInfo = Voter.editLine(new String[] {Voter.genExpDate()}, 10, Voter.getKeyValNAD(searchKey));
			System.out.println("voterInfo: " + Arrays.toString(voterInfo)); //test print
		}
		Candidate.setStatesList(new String[] {"TX", "LS"}); //only will do two states for testing
		Candidate.addPres(new String[] {"President/Vice", "R", "James Robert and Jimmy Kriss", "L", "Elisha Bertha and Catherine Kate"});
		Candidate.addPos(new String[] {"Senator", "R", "Jeremy Bently", "R", "Helda Cameron"});
		
		if(0 < Candidate.getPosNum()) { //checks if there are any positions to cast ballot for
			Ballot.submit(new int[] {1, 2}, voterInfo); //votes for position 1 candidate 1 and position 2 candidate 2
			if(Ballot.checkIfBallots()) { //checks that there are ballots
				String[] ballotInfo = Ballot.lookup(Ballot.getKeyVal(Voter.getVUID(locInFile))); //uses saved searchKey for testing, admin would have to look this key up using Voter.lookup()
				System.out.println("ballot:");
				for(int i = 1; i < ballotInfo.length; i++) { //loops through ballot
					System.out.println(Arrays.toString(Candidate.lookup(ballotInfo[i], i)));
				}
				if(Candidate.isPresElec()) { //checks if it is a presidential election
					System.out.println("\n" + "demographics: " + Arrays.toString(Candidate.getDemoLine())); //test prints demographics
				}
			}
		}


		Candidate.clear(); //clears files for test so upon restart, candidates aren't readded
		Candidate.loadData("LS");
		Candidate.clear();
		 */

		loadAllData("TX"); //set data files
		registerNewVoter();
		
	}
}
