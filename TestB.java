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
	
	public static void setData(String s) { //loads necessary global variable files and creates their hash maps and key sets
		File[] fs = setFile(new String[] {"ballot.csv", "indexB.txt"}); 
		ballotFile = fs[0];
		indexB = fs[1];
		mapB = loadHash(indexB);
		keysB = mapB.keySet();
	}
	
	public static void submit(String[] sa) { //need to save ballot info to tally in another file later
		long l = writeFile(formatRow(sa), ballotFile); //saves pointer from new registered info
		mapB.put(sa[0], l); //generates hash maps as well as updates the serialization
		saveHash(mapB, indexB);
	}

}
