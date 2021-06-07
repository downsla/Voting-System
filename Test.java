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
	
	public static String[] lookup(long l, File f) {
		String a = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			br.skip(l);
			a = br.readLine();
			br.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		String[] sa = a.split(",");
		String[] sr = new String[11];
		sr[0] = sa[0];
		int i;
		for(i = 1; i < 5; i++) {
			char[] ch = sa[i].toCharArray();
			StringBuilder sb = new StringBuilder();
			int j;
			for(j = 0; j < ch.length; j++) {
				if(ch[j] != ' ') {
					break;
				}
			}
			for(; j < ch.length; j++) {
				sb.append(ch[j]);
			}
			sr[i] = sb.toString();
		}
		for(; i < sr.length; i++) {
			sr[i] = sa[i];
		}
		return sr;
	}
	
	public static long register(String[] sa, Set<String> k, File f) {
		BigInteger bi;
		int[] b = new int[] {32, 29, 27, 25, 18, 16, 15, 14, 12, 9, 2, 1};
		while(true) {
			bi = new BigInteger("0");
			for(int i = 0; i < b.length; i++) {
				BigInteger random = new BigInteger(b[i], new Random());
				bi = bi.add(random);
			}
			if(!k.contains(bi.toString())) {
				break;
			}
	    }
	    String[] sr = new String[11];
	    sr[0] = String.format("%010d", bi);
	    for(int i = 1; i < (sr.length - 1); i++) {
	    	sr[i] = sa[i - 1];
	    }
	    Date d = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	    StringBuilder sbd = new StringBuilder(sdf.format(d));
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.YEAR, 1);
	    sbd.append(sdf.format(c.getTime()));
	    sr[sr.length - 1] = sbd.toString();
		long l = f.length();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
			bw.write(formatRow(sr));
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
		int i;
		for(i = 1; i < (p.length + 1); i++) {
			char[] ch = new char[p[i - 1] - sa[i].length()];
			Arrays.fill(ch, ' ');
			sb.append(ch).append(sa[i]).append(',');
		}
		for(; i < sa.length; i++) {
			sb.append(sa[i]).append(',');
		}
		sb.append('\n');
		return sb.toString();
	}

	public static void editRow(String[] sa, int c, long l, File f) {
		String[] sr = new String[11];
		String[] sc = lookup(l, f);
		for(int i = 0; i < sr.length; i++) {
			if(c <= i && i < (c + sa.length)) {
				sr[i] = sa[i - c];
			} else {
				sr[i] = sc[i];
			}
		}
		String s = formatRow(sr);
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
		
		String[] sArr = new String[] {lastName, firstName, street, city, state, zip, dob, sex, race};
		long l = register(sArr, map.keySet(), file);
		
		StringBuilder name = new StringBuilder(lastName).append(firstName);
		StringBuilder address = new StringBuilder(street).append(city).append(state).append(zip);
		StringBuilder sb = new StringBuilder(name).append(address);
		
		if(!map.containsKey(sb.toString())) {
			map.put(sb.toString(), l);
			saveHash(map, index);
		}
		
		System.out.println(Arrays.toString(lookup(map.get(sb.toString()), file)));
		
		String street2 = new String("5678 ROAD ST");
		String[] address2 = new String[] {street2, city, state, zip};
		editRow(address2, 3, map.get(sb.toString()), file);
		
		System.out.println(Arrays.toString(lookup(map.get(sb.toString()), file)));
		
	}

}
