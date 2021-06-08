import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Test {
	
	public static File voterFile;
	public static File indexNA;
	public static File indexVD;
	public static HashMap<String, Long> mapNA;
	public static HashMap<String, Long> mapVD;
	public static Set<String> keysNA;
	public static Set<String> keysVD;
	
	public static String hashFunction(String data) { //unused
		String hash = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			byte[] digest = md.digest();
			BigInteger bi = new BigInteger(digest);
			hash = bi.toString(16);
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static String[] lookup(long l) { //takes pointer and returns all voter info
		String s = new String(); //puts line from file into string
		try {
			BufferedReader br = new BufferedReader(new FileReader(voterFile));
			br.skip(l); //skips using pointer
			s = br.readLine();
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		String[] sa = s.split(","); //converts to array, prepares to format
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
	
	public static String[] register(String[] sa) { //registers voter using array with all necessary info to be provided (includes null spaces to be filled) and returns all voter info
		BigInteger bi; //generates random VUID as string of 10 digits
		int[] b = new int[] {32, 29, 27, 25, 18, 16, 15, 14, 12, 9, 2, 1}; //bits who's max is 9999999999
		while(true) { //loops until new VUID generated
			bi = new BigInteger("0");
			for(int i = 0; i < b.length; i++) { //adds random bits together
				BigInteger random = new BigInteger(b[i], new Random());
				bi = bi.add(random);
			}
			if(!keysNA.contains(bi.toString())) { //checks if VUID already exists
				break;
			}
	    }
	    String[] sr = new String[11];
	    sr[0] = String.format("%010d", bi); //formats with leading zeros to be 10 digits long
	    for(int i = 1; i < (sr.length - 1); i++) { //adds inputed array of info
	    	sr[i] = sa[i];
	    }
	    sr[sr.length - 1] = genExpDate(); //adds expiration date
		long l = voterFile.length(); //saves pointer to the new registered info
		try { //appends info to the end of the voter file
			BufferedWriter bw = new BufferedWriter(new FileWriter(voterFile, true));
			bw.write(formatRow(sr));
			bw.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		mapNA.put(getSearchNA(sr), l); //generates both NA and VD hash maps as well as updates the serialization
		mapVD.put(getSearchVD(sr), l);
		saveHash(mapNA, indexNA);
		saveHash(mapVD, indexVD);
		return sr;
	}
	
	public static String genExpDate() { //generates expiration date string
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    c.add(Calendar.YEAR, 1);
	    return new String(new SimpleDateFormat("MMddyyyy").format(c.getTime()));
	}
	
	public static boolean checkExpDate(String s) { //checks given expiration date against current date
		String c = new String(new SimpleDateFormat("MMddyyyy").format(new Date())); //current date
		char[] chE = s.toCharArray(); //separates to characters
		char[] chC = c.toCharArray();
		if(Integer.valueOf(String.copyValueOf(chC, 4, 4)) <= Integer.valueOf(String.copyValueOf(chE, 4, 4))) { //checks year
			if(Integer.valueOf(String.copyValueOf(chC, 0, 2)) <= Integer.valueOf(String.copyValueOf(chE, 0, 2))) { //checks month
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
	
	public static HashMap<String, Long> loadHash(File f) { //reads hash map from file
		HashMap<String, Long> hm = new HashMap<String, Long>();
		if(f.length() != 0) {
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				hm = (HashMap<String, Long>) inStream.readObject();
				inStream.close();
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return hm;
	}
	
	public static void saveHash(HashMap<String, Long> hm, File f) { //saves hash map to file
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(f));
			outStream.writeObject(hm);
			outStream.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String formatRow(String[] sa) { //formats and returns array of all info to be written to voter file
		StringBuilder sb = new StringBuilder(sa[0]).append(',');
		int[] p = new int[] {20, 20, 20, 20}; //max character length for pertaining indexes
		int i;
		for(i = 1; i < (p.length + 1); i++) { //puts correct number of empty space before string
			char[] ch = new char[p[i - 1] - sa[i].length()];
			Arrays.fill(ch, ' ');
			sb.append(ch).append(sa[i]).append(','); //inserts string as well as delimiter (,)
		}
		for(; i < sa.length; i++) { //adds rest of array
			sb.append(sa[i]).append(',');
		}
		sb.append('\n'); //adds the next line character (\n)
		return sb.toString();
	}

	public static void editRow(String[] sa, int c, long l) { //edits an exist voter info using array of new info, the index to start overwriting, and the pointer to the voter file
		String[] sr = new String[11];
		String[] sc = lookup(l); //gets current all voter info
		for(int i = 0; i < sr.length; i++) { //inserts new info
			if(c <= i && i < (c + sa.length)) {
				sr[i] = sa[i - c];
			} else {
				sr[i] = sc[i];
			}
		}
		String sn = formatRow(sr); //formats all new info to write to voter file
		try { //overwrites existing line in voter file
			RandomAccessFile raf = new RandomAccessFile(voterFile, "rw");
			raf.seek(l);
			raf.write(sn.getBytes());
			raf.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		mapNA.put(getSearchNA(sr), l); //adds new key with preexisting value to hash map that searches using updated info (VD does not need to be updated as VUID and DOB never change)
		mapNA.remove(getSearchNA(sc)); //removes old key from NA search string using old saved lookup
		saveHash(mapNA, indexNA); //saves updated hash map
	}
	
	public static void setData() { //loads necessary global variable files and creates their hash maps and key sets
		voterFile = new File("voters.csv");
		if(!voterFile.exists()) { //only creates file if file does not exist
            try {
            	voterFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
		indexNA = new File("indexNA.txt");
		if(!indexNA.exists()) {
            try {
				indexNA.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
		indexVD = new File("indexVD.txt");
		if(!indexVD.exists()) {
            try {
				indexVD.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
		mapNA = loadHash(indexNA);
		mapVD = loadHash(indexVD);
		keysNA = mapNA.keySet();
		keysVD = mapVD.keySet();
	}
	
	public static String getSearchNA(String[] sa) { //pulls string necessary to search hash map NA from array of all voter info
		String[] temp = new String[7];
		System.arraycopy(sa, 1, temp, 0, temp.length);
		return String.join("", temp);
	}
	
	public static String getSearchVD(String[] sa) { //pulls string necessary to search hash map VD from array of all voter info
		String[] temp = new String[] {sa[0], sa[7]};
		return String.join("", temp);
	}
	
	public static void main(String[] args) { //driver file has a test case for not registered (or registered if you run twice without deleting data files) and editing users voter info		
		setData(); //set data files
		
		String[] sArr = new String[11]; //creates array and populates with data for registered user
		sArr[1] = new String("JOHN");
		sArr[2] = new String("DOE");
		sArr[3] = new String("1234 ROAD ST");
		sArr[4] = new String("CITY");
		sArr[5] = new String("TX");
		sArr[6] = new String("123456");
		sArr[7] = new String("01022003");
		String s = getSearchNA(sArr); //gets string to search with
		
		if(!keysNA.contains(s)) { //if user isn't registered, gets other necessary info to register
			sArr[8] = new String("M");
			sArr[9] = new String("W");
			sArr = register(sArr); //registers voter
		} else { //otherwise overwrite user inputed info with array containing all stored info
			sArr = lookup(mapNA.get(s));
		}
		System.out.println(Arrays.toString(sArr)); //test print
		
		String[] sAAlt = new String[11]; //creates array for editing address, asking only for VUID and DOB
		sAAlt[0] = new String(sArr[0]);
		sAAlt[7] = new String(sArr[7]);
		String sAlt = getSearchVD(sAAlt); //gets corresponding search string
		if(keysVD.contains(sAlt)) { //if they are registered, then allows them to update address
			sAAlt = lookup(mapVD.get(sAlt)); //gets array of all preexisting voter info
			String streetNew = new String("5678 ROAD ST"); //get new address
			String[] addressNew = new String[] {streetNew, sAAlt[4], sAAlt[5], sAAlt[6]}; //for simplicity sake, only changed street and copied other info from previous test
			editRow(addressNew, 3, mapVD.get(sAlt)); //sends data to be edited in voter file. 3 corresponds to the index that the address begins. also sends the pointer (potentially could use NA to update gender this way if editRow() is tweaked)
			System.out.println(Arrays.toString(lookup(mapVD.get(sAlt)))); //test print
		}
		
		if (checkExpDate(sAAlt[10])) { //checks expiration date of second test, because the test isn't expired, it doesn't do anything, but it has been tested by modifying genExpDate()
			editRow(new String[] {genExpDate()}, 10, mapVD.get(sAlt));
			System.out.println(Arrays.toString(lookup(mapVD.get(sAlt))));
		}
		
	}

}
