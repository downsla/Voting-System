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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Test {
	
	public static String getVUID(Random r, Set<String> keys) {
		BigInteger id;
		int[] bits = new int[] {32, 29, 27, 25, 18, 16, 15, 14, 12, 9, 2, 1};
		while(true) {
			id = new BigInteger("0");
			for(int i = 0; i < bits.length; i++) {
				BigInteger random = new BigInteger(bits[i], r);
				id = id.add(random);
			}
			if(!keys.contains(hashFunction(id.toString()))) {
				break;
			}
	    }
	    return String.format("%010d", id);
	}
	
	public static String hashFunction(String data) {
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
	
	public static String[] lookUp(String h, HashMap<String, Long> m, File f) {
		String data = new String();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			reader.skip(m.get(h));
			data = reader.readLine();
			reader.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return data.split(",");
	}
	
	public static long register(StringBuilder d, File f) {
		long l = f.length();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
			writer.write(d.toString());
			writer.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static HashMap<String, Long> loadHash(File f) {
		HashMap<String, Long> m = new HashMap<String, Long>();
		if(f.length() != 0) {
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				m = (HashMap<String, Long>) inStream.readObject();
				inStream.close();
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public static void saveHash(HashMap<String, Long> m, File f) {
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(f));
			outStream.writeObject(m);
			outStream.close();
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
		
		String s = new String(getVUID(new Random(), map.keySet()));
		StringBuilder string = new StringBuilder(s);
		string.append(",\"hello\",");
		string.append("\"world\",");
		string.append('\n');
		
		long len = register(string, file);	
		
		map.put(hashFunction(s), len);
		
		saveHash(map, index);
		
		System.out.println(Arrays.toString(lookUp(hashFunction(s), map, file)));
		
	}

}
