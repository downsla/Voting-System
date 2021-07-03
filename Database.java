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
import java.util.Arrays;
import java.util.HashMap;

public class Database {

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
	//
	public static File[] setFiles(String[] fileNames, String stateAbbr) { //appends state to file name
		StringBuilder[] sba = new StringBuilder[] {new StringBuilder(fileNames[0]), new StringBuilder(fileNames[1])}; //makes file in accordance with state
		sba[0].append("-" + stateAbbr + ".csv");
		sba[1].append("-" + stateAbbr + ".txt");
		return setFile(new String[] {sba[0].toString(), sba[1].toString()});
	}
	//public state File[] setFiles(String[] fileNames) {
	public static File[] setFile(String[] sa) { //loads files from input array
		File[] fs = new File[sa.length];
		for(int i = 0; i < sa.length; i++) {
			fs[i] = new File(sa[i]);
			if(!fs[i].exists()) { //only creates file if file does not exist
	            try {
	            	fs[i].createNewFile();
				} catch(IOException e) {
					e.printStackTrace();
				}
	        }
		}
		return fs;
	}

	public static String formatLine(String[] info) { //formats string array for writing to csv file
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < info.length; i++) { //adds rest of array
			sb.append(info[i]).append(',');
		}
		sb.append('\n'); //adds the next line character (\n)
		return sb.toString();
	}

	public static String formatLine(String[] info, int[] maxCharLength, int startIndex) { //formats and returns array of all info to be written to file using array of max char length starting at index
		StringBuilder sb = new StringBuilder();
		int i;
		for(i = 0; i < startIndex; i++) { //puts beginning of array to start of format
			sb.append(info[i]).append(',');
		}
		for(; i < (maxCharLength.length + startIndex); i++) { //puts correct number of empty space before string
			char[] ch = new char[maxCharLength[i - startIndex] - info[i].length()];
			Arrays.fill(ch, ' ');
			sb.append(ch).append(info[i]).append(','); //inserts string as well as delimiter (,)
		}
		for(; i < info.length; i++) { //adds rest of array
			sb.append(info[i]).append(',');
		}
		sb.append('\n'); //adds the next line character (\n)
		return sb.toString();
	}

	public static long writeFile(String s, File f) { //writes given string to given file and returns pointer
		long l = f.length(); //saves pointer to the new submitted info
		try { //appends info to the end of the ballot file
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
			bw.write(s);
			bw.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static String[] readFile(long l, File f) { //reads line from given file at given pointer and returns array
		String s = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			br.skip(l); //skips using pointer
			s = br.readLine();
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return s.split(",");
	}
	
	public static void overwriteFile(String s, long l, File f) { //overwrites existing line in given file at given pointer with given string
		try { 
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.seek(l);
			raf.write(s.getBytes());
			raf.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFirstPartOfLine(int length, long l, File f) { //returns chars from beginning of line in file.
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			br.skip(l); //skips using pointer
			for(int i = 0; i < length; i++) { //reads chars
				sb.append((char)br.read());
			}
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
}