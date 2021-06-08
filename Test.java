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

public class Test extends TestF{ //test voter class
	
	private static File voterFile;
	private static File indexNA;
	private static File indexVD;
	private static HashMap<String, Long> mapNA;
	private static HashMap<String, Long> mapVD;
	private static Set<String> keysNA;
	private static Set<String> keysVD;
	
	public static boolean checkKeysNA(String s) { //accessor method
		return keysNA.contains(s);
	}
	
	public static boolean checkKeysVD(String s) { //accessor method
		return keysVD.contains(s);
	}
	
	public static long getPointerNA(String s) { //accessor method
		return mapNA.get(s);
	}
	
	public static long getPointerVD(String s) { //accessor method
		return mapVD.get(s);
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
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(voterFile));
			br.skip(l); //skips using pointer
			for(int i = 0; i < 10; i++) { //reads 10 chars (VUID length)
				sb.append((char)br.read());
			}
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
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
		long l = writeFile(formatRow(sr, new int[] {20, 20, 20, 20}, 1), voterFile); //saves pointer from the new registered info
		mapNA.put(getSearchNA(sr), l); //generates both NA and VD hash maps as well as updates the serialization
		mapVD.put(getSearchVD(sr), l);
		saveHash(mapNA, indexNA);
		saveHash(mapVD, indexVD);
		keysNA = mapNA.keySet();
		keysVD = mapVD.keySet();
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
		String sn = formatRow(sr, new int[] {20, 20, 20, 20}, 1); //formats all new info to write to voter file
		overwriteFile(sn, l, voterFile);
		mapNA.put(getSearchNA(sr), l); //adds new key with preexisting value to hash map that searches using updated info (VD does not need to be updated as VUID and DOB never change)
		mapNA.remove(getSearchNA(sc)); //removes old key from NA search string using old saved lookup
		saveHash(mapNA, indexNA); //saves updated hash map
	}
	
	public static void setData() { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFile(new String[] {"voters.csv", "indexNA.txt", "indexVD.txt"});
		voterFile = fs[0];
		indexNA = fs[1];
		indexVD = fs[2];
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

}