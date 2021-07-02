import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Voter extends Database{
	
	private static File voterFile;
	private static File indexNAD;
	private static File indexVD;
	private static HashMap<String, Long> mapNAD;
	private static HashMap<String, Long> mapVD;
	private static Set<String> keysNAD;
	private static Set<String> keysVD;
	private static int[] spacing = new int[] {20, 20, 40, 20};

	public static boolean checkKeyNAD(String key) { //true if key exists
		return keysNAD.contains(key);
	}

	public static boolean checkKeyVD(String key) { //true if key exists
		return keysVD.contains(key);
	}

	public static long getKeyValNAD(String key) { //gets pointer in file from key
		return mapNAD.get(key);
	}

	public static long getKeyValVD(String key) { //gets pointer in file from key
		return mapVD.get(key);
	}
	
	public static String[] lookup(long l) { //takes pointer and returns all voter info
		String[] sa = readFile(l, voterFile); //puts line from file into string array, prepares to format
		String[] sr = new String[11];
		sr[0] = sa[0]; //adds VUID index
		int i;
		for(i = 1; i < 5; i++) { //removes leading empty characters from pertaining indexes
			char[] ch = sa[i].toCharArray(); 
			StringBuilder sb = new StringBuilder();
			int j;
			for(j = 0; j < ch.length; j++) { //only removes leading empty space
				if(ch[j] != ' ') {
					break;
				}
			}
			for(; j < ch.length; j++) { //adds characters after leading space
				sb.append(ch[j]);
			}
			sr[i] = sb.toString(); //returns formatted index
		}
		for(; i < sr.length; i++) { //adds rest of indexes to array
			sr[i] = sa[i];
		}
		return sr;
	}
	
	public static String getVUID(long l) { //returns only the VUID from file (will be used by admin)
		return getFirstPartOfLine(10, l, voterFile);
	}

	public static String[] register(String[] voterInfo) { //registers voter using array with all necessary info to be provided (includes null spaces to be filled) and returns all voter info
		BigInteger bi; //generates random VUID as string of 10 digits
		int[] b = new int[] {32, 29, 27, 25, 18, 16, 15, 14, 12, 9, 2, 1}; //bits who's max is 9999999999
		while(true) { //loops until new VUID generated
			bi = new BigInteger("0");
			for(int i = 0; i < b.length; i++) { //adds random bits together
				BigInteger random = new BigInteger(b[i], new Random());
				bi = bi.add(random);
			}
			if(!keysNAD.contains(bi.toString())) { //checks if VUID already exists
				break;
			}
	    }
	    String[] sa = new String[11];
	    sa[0] = String.format("%010d", bi); //formats with leading zeros to be 10 digits long
	    for(int i = 1; i < (sa.length - 1); i++) { //adds inputed array of info
	    	sa[i] = voterInfo[i];
	    }
	    sa[sa.length - 1] = genExpDate(); //adds expiration date
		long l = writeFile(formatLine(sa, spacing, 1), voterFile); //saves pointer from the new registered info
		mapNAD.put(getSearchKeyNAD(sa), l); //generates both NA and VD hash maps as well as updates the serialization
		mapVD.put(getSearchKeyVD(sa), l);
		saveHash(mapNAD, indexNAD);
		saveHash(mapVD, indexVD);
		keysNAD = mapNAD.keySet();
		keysVD = mapVD.keySet();
		return sa;
	}
	
	public static String genExpDate() { //generates expiration date string
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    c.add(Calendar.YEAR, 1);
	    return new String(new SimpleDateFormat("MMddyyyy").format(c.getTime()));
	}

	public static boolean isExpired(String expDate) { //checks given expiration date against current date
		String c = new String(new SimpleDateFormat("MMddyyyy").format(new Date())); //current date
		char[] chE = expDate.toCharArray(); //separates to characters
		char[] chC = c.toCharArray();
		Integer yC = Integer.valueOf(String.copyValueOf(chC, 4, 4));
		Integer yE = Integer.valueOf(String.copyValueOf(chE, 4, 4));
		Integer mC = Integer.valueOf(String.copyValueOf(chC, 0, 2));
		Integer mE = Integer.valueOf(String.copyValueOf(chE, 0, 2));
		if(yC < yE) { //checks year
			return false;
		} else if(yC == yE) { //if year is the same
			if(mC < mE) { // checks month
				return false;
			} else if(mC == mE) { //if month is the same
				if(Integer.valueOf(String.copyValueOf(chC, 2, 2)) <= Integer.valueOf(String.copyValueOf(chE, 2, 2))) { //checks day
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public static int getAge(String dob) { //returns age given DOB string
		String c = new String(new SimpleDateFormat("MMddyyyy").format(new Date())); //current date
		char[] chB = dob.toCharArray(); //separates to characters
		char[] chC = c.toCharArray();
		int a = Integer.valueOf(String.copyValueOf(chC, 4, 4)) - Integer.valueOf(String.copyValueOf(chB, 4, 4)); //subtracts to get year
		int mB = Integer.valueOf(String.copyValueOf(chB, 0, 2)); //saves month for ifs
		int mC = Integer.valueOf(String.copyValueOf(chC, 0, 2));
		if(mC < mB) { //checks month
			return (a - 1);
		} else if(mC == mB) {
			if(Integer.valueOf(String.copyValueOf(chC, 2, 2)) < Integer.valueOf(String.copyValueOf(chB, 2, 2))) { //checks day
				return (a - 1);
			} else {
				return a;
			}
		} else {
			return a;
		}
	}

	public static boolean[] getDemo(String[] voterInfo) { //returns boolean array containing only demographic info from voter info
		boolean[] b = new boolean[12];
		int a = getAge(voterInfo[7]);
		if(18 <= a && a <= 35) {
			b[0] = true;
		} else if(36 <= a && a <= 65) {
			b[1] = true;
		} else if(66 <= a) {
			b[2] = true;
		}
		if(voterInfo[8].equals("M")) {
			b[3] = true;
		} else if(voterInfo[8].equals("F")) {
			b[4] = true;
		} else if(voterInfo[8].equals("O")) {
			b[5] = true;
		}
		if(voterInfo[9].equals("N")) {
			b[6] = true;
		} else if(voterInfo[9].equals("A")) {
			b[7] = true;
		} else if(voterInfo[9].equals("B")) {
			b[8] = true;
		} else if(voterInfo[9].equals("H")) {
			b[9] = true;
		} else if(voterInfo[9].equals("P")) {
			b[10] = true;
		} else if(voterInfo[9].equals("W")) {
			b[11] = true;
		}
		return b;
	}
	
	public static String[] editLine(String[] newInfo, int startIndex, long l) { //edits an exist voter info using array of new info, the index to start overwriting, and the pointer to the voter file
		String[] sa = new String[11];
		String[] sc = lookup(l); //gets current all voter info
		for(int i = 0; i < sa.length; i++) { //inserts new info
			if(startIndex <= i && i < (startIndex + newInfo.length)) {
				sa[i] = newInfo[i - startIndex];
			} else {
				sa[i] = sc[i];
			}
		}
		String sn = formatLine(sa, spacing, 1); //formats all new info to write to voter file
		overwriteFile(sn, l, voterFile);
		mapNAD.put(getSearchKeyNAD(sa), l); //adds new key with preexisting value to hash map that searches using updated info (VD does not need to be updated as VUID and DOB never change)
		mapNAD.remove(getSearchKeyNAD(sc)); //removes old key from NA search string using old saved lookup
		saveHash(mapNAD, indexNAD); //saves updated hash map
		keysNAD = mapNAD.keySet();
		return sa;
	}
	
	public static void loadData() { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFile(new String[] {"voters.csv", "indexNA.txt", "indexVD.txt"});
		voterFile = fs[0];
		indexNAD = fs[1];
		indexVD = fs[2];
		mapNAD = loadHash(indexNAD);
		mapVD = loadHash(indexVD);
		keysNAD = mapNAD.keySet();
		keysVD = mapVD.keySet();
	}
	//
	public static String getSearchKeyNAD(String[] voterInfo) { //pulls string necessary to search hash map NA from array of all voter info
		String[] temp = new String[7];
		System.arraycopy(voterInfo, 1, temp, 0, temp.length);
		return String.join("", temp);
	}

	public static String getSearchKeyVD(String[] voterInfo) { //pulls string necessary to search hash map VD from array of all voter info
		String[] temp = new String[] {voterInfo[0], voterInfo[7]};
		return String.join("", temp);
	}

}