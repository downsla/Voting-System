import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TestB extends TestF{ //test ballot class

	private static File ballotFile;
	private static File indexB;
	private static HashMap<String, Long> mapB;
	private static Set<String> keysB;
	
	public static boolean checkKeysB(String s) { //accessor method
		return keysB.contains(s);
	}
	
	public static long getPointerB(String s) { //accessor method
		return mapB.get(s);
	}
	
	public static boolean checkIfBallots() { //checks if there are any ballots cast
		return (0 < ballotFile.length());
	}
	
	public static void resetBallot(String s) { //clears files
		ballotFile.delete();
		indexB.delete();
		setData(s);
	}
	
	public static void setData(String s) { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFile(new String[] {"ballots", "indexB"}, s); 
		ballotFile = fs[0];
		indexB = fs[1];
		mapB = loadHash(indexB);
		keysB = mapB.keySet();
	}
	
	public static void submit(String[] sa, String v[]) { //need to save ballot info to tally in another file later, takes voter info
		if (TestC.checkIfDemo()) { //if there is a demographics line, update it
			TestC.incrementFirstLine(getDemo(v), Integer.valueOf(sa[1])); //updates demographics using presidential candidate and the voter's demographic
		}
		for(int i = 0; i < sa.length; i++) { //formats single digits to two
			sa[i] = String.format("%02d", sa[i]);
		}
		long l = writeFile(formatRow(sa), ballotFile); //saves pointer from new registered info
		mapB.put(sa[0], l); //generates hash maps as well as updates the serialization
		saveHash(mapB, indexB);
		keysB = mapB.keySet();
	}
	
	public static String[] lookup(long l) { //returns line at pointer in file
		return readFile(l, ballotFile);
	}

	public static int getAge(String s) { //returns age given DOB string
		String c = new String(new SimpleDateFormat("MMddyyyy").format(new Date())); //current date
		char[] chB = s.toCharArray(); //separates to characters
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
	
	public static boolean[] getDemo(String[] sa) { //returns boolean array containing only demographic info from voter info
		boolean[] b = new boolean[12];
		int a = getAge(sa[7]);
		if(18 <= a && a <= 35) {
			b[0] = true;
		} else if(36 <= a && a <= 65) {
			b[1] = true;
		} else if(66 <= a) {
			b[2] = true;
		}
		if(sa[8] == "M") {
			b[3] = true;
		} else if(sa[8] == "W") {
			b[4] = true;
		} else if(sa[8] == "O") {
			b[5] = true;
		}
		if(sa[9] == "N") {
			b[6] = true;
		} else if(sa[9] == "A") {
			b[7] = true;
		} else if(sa[9] == "B") {
			b[8] = true;
		} else if(sa[9] == "H") {
			b[9] = true;
		} else if(sa[9] == "P") {
			b[10] = true;
		} else if(sa[9] == "W") {
			b[11] = true;
		}
		return b;
	}
	
}
