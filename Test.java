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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Test {
	
	public static String genVUID(Random r, Set<String> k) {
		BigInteger bi;
		int[] b = new int[] {32, 29, 27, 25, 18, 16, 15, 14, 12, 9, 2, 1};
		while(true) {
			bi = new BigInteger("0");
			for(int i = 0; i < b.length; i++) {
				BigInteger random = new BigInteger(b[i], r);
				bi = bi.add(random);
			}
			if(!k.contains(bi.toString())) {
				break;
			}
	    }
	    return String.format("%010d", bi);
	}
	
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
	
	public static String[] getLookup(String s, HashMap<String, Long> hm, File f) {
		String a = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			br.skip(hm.get(s));
			a = br.readLine();
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return a.split(",");
	}
	
	public static long register(String s, File f) {
		long l = f.length();
		try {
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
	
	public static HashMap<String, Long> loadHash(File f) {
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
	
	public static void saveHash(HashMap<String, Long> hm, File f) {
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
	
	public static String formatRow(String[] sa) {
		StringBuilder sb = new StringBuilder(sa[0]).append(',');
		int[] p = new int[] {20, 20, 20, 20};
		for(int i = 1; i < p.length; i++) {
			char[] ch = new char[p[i] - sa[i].length()];
			Arrays.fill(ch, ' ');
			sb.append(ch).append(sa[i]).append(',');
		}
		for(int i = (p.length + 1); i < sa.length; i++) {
			sb.append(sa[i]).append(',');
		}
		sb.append('\n');
		return sb.toString();
	}
	
	public static String formatCell(String s) {
		char[] ch = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		int i;
		for(i = 0; i < ch.length; i++) {
			if(ch[i] != ' ') {
				break;
			}
		}
		for(; i < ch.length; i++) {
			sb.append(ch[i]);
		}
		return sb.toString();
	}
	
	public static void editRow(String[] sa, String s, HashMap<String, Long> hm, File f) { //not tested
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.seek(hm.get(s));
			raf.write(Arrays.toString(sa).getBytes());
			raf.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		File file = new File("test.csv");
		if(!file.exists()) {
            try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
		
		File index = new File("index.txt");
		if(!index.exists()) {
            try {
				index.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
        }
		
		HashMap<String, Long> map = loadHash(index);
		
		String id = new String(genVUID(new Random(), map.keySet()));
		String firstName = new String("JOHN");
		String lastName = new String("DOE");
		String street = new String("1234 ROAD ST");
		String city = new String("CITY");
		String state = new String("TX");
		String zip = new String("123456");
		String dob = new String("01022003");
		String sex = new String("M");
		String race = new String("W");
		String valid = new String("0101202012312021");
		
		String[] sArr = new String[] {id, lastName, firstName, street, city, state, zip, dob, sex, race, valid};
		String s = formatRow(sArr);
		long l = register(s, file);
		
		StringBuilder name = new StringBuilder(lastName).append(firstName);
		StringBuilder address = new StringBuilder(street).append(city).append(state).append(zip);
		StringBuilder sb = new StringBuilder(name).append(address);
		
		if(!map.containsKey(sb.toString())) {
			map.put(sb.toString(), l);
			saveHash(map, index);
		}
		
		for(int i = 0; i < 10; i++) {
			System.out.println(formatCell(getLookup(sb.toString(), map, file)[i]));
		}
		
	}

}
