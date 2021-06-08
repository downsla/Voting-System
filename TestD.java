import java.util.Arrays;

public class TestD { //test driver class

	public static void main(String[] args) { //driver file has a test case for not registered (or registered if you run twice without deleting data files) and editing users voter info	
		Test.setData(); //set data files
		
		String[] sArr = new String[11]; //creates array and populates with data for registered user
		sArr[1] = new String("DOE");
		sArr[2] = new String("JOHN");
		sArr[3] = new String("1234 ROAD ST");
		sArr[4] = new String("CITY");
		sArr[5] = new String("TX");
		sArr[6] = new String("123456");
		sArr[7] = new String("01022003");
		String s = Test.getSearchNA(sArr); //gets string to search with
		
		if(!Test.checkKeysNA(s)) { //if user isn't registered, gets other necessary info to register
			sArr[8] = new String("M");
			sArr[9] = new String("W");
			sArr = Test.register(sArr); //registers voter
		} else { //otherwise overwrite user inputed info with array containing all stored info
			sArr = Test.lookup(Test.getPointerNA(s));
		}
		System.out.println(Arrays.toString(sArr)); //test print
		
		String[] sAAlt = new String[11]; //creates array for editing address, asking only for VUID and DOB
		sAAlt[0] = new String(sArr[0]);
		sAAlt[7] = new String(sArr[7]);
		String sAlt = Test.getSearchVD(sAAlt); //gets corresponding search string
		if(Test.checkKeysVD(sAlt)) { //if they are registered, then allows them to update address
			sAAlt = Test.lookup(Test.getPointerVD(sAlt)); //gets array of all preexisting voter info
			String streetNew = new String("5678 ROAD ST"); //get new address
			String[] addressNew = new String[] {streetNew, sAAlt[4], sAAlt[5], sAAlt[6]}; //for simplicity sake, only changed street and copied other info from previous test
			Test.editRow(addressNew, 3, Test.getPointerVD(sAlt)); //sends data to be edited in voter file. 3 corresponds to the index that the address begins. also sends the pointer (potentially could use NA to update gender this way if editRow() is tweaked)
			System.out.println(Arrays.toString(Test.lookup(Test.getPointerVD(sAlt)))); //test print
		}
		
		if (Test.checkExpDate(sAAlt[10])) { //checks expiration date of second test, because the test isn't expired, it doesn't do anything, but it has been tested by modifying genExpDate()
			Test.editRow(new String[] {Test.genExpDate()}, 10, Test.getPointerVD(sAlt));
			System.out.println(Arrays.toString(Test.lookup(Test.getPointerVD(sAlt))));
		}

	}

}
