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
import java.util.Arrays;
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
	
	public static void resetBallot() { //clears files
		ballotFile.delete();
		indexB.delete();
		setData();
	}
	
	public static void setData() { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFile(new String[] {"ballots.csv", "indexB.txt"}); 
		ballotFile = fs[0];
		indexB = fs[1];
		mapB = loadHash(indexB);
		keysB = mapB.keySet();
	}
	
	public static void submit(String[] sa) { //need to save ballot info to tally in another file later
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

}
