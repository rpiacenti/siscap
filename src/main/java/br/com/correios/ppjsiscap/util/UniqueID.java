package br.com.correios.ppjsiscap.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueID {

	public String UniqueID() {
		// TODO Auto-generated constructor stub
		espera();
		Date date = new Date();
		String uniqueID = null;
		
		try {
			uniqueID = createOrderNumber(date).trim();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueID;

	}

	public static String createOrderNumber(Date orderDate)
			throws NoSuchAlgorithmException {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String datestring = df.format(orderDate).toString();
		// System.out.println("datestring="+datestring);
		// System.out.println("datestring size="+datestring.length());
		String hash = makeHashString(datestring);// creates SHA1 hash of 16
													// digits
		// System.out.println("hash="+hash);
		// System.out.println("hash size="+hash.length());
		int datestringlen = datestring.length();
		String ordernum = hash.substring(12, 20);
		ordernum = ordernum.substring(1, ordernum.length());
		// System.out.println("ordernum size="+ordernum.length());
		return ordernum;
	}

	private static String makeHashString(String plain)
			throws NoSuchAlgorithmException {
		final int MD_PASSWORD_LENGTH = 16;
		final String HASH_ALGORITHM = "SHA1";
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
			md.update(plain.getBytes());
			BigInteger hashint = new BigInteger(1, md.digest());
			hash = hashint.toString(MD_PASSWORD_LENGTH);
		} catch (NoSuchAlgorithmException nsae) {
			throw (nsae);
		}
		return hash;
	}
    
	public static void espera() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}