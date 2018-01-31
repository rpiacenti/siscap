package br.com.correios.ppjsiscap.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniqueKey {
    
	
	
	public UniqueKey() {
			// TODO Auto-generated constructor stub
			Date date = new Date();
			try {
				System.out.println("Numero Pedido:" + createOrderNumber());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	public static String createOrderNumber()
			throws NoSuchAlgorithmException {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date orderDate = new Date();
		String datestring = df.format(orderDate).toString();
		// System.out.println("datestring="+datestring);
		// System.out.println("datestring size="+datestring.length());
		String hash = makeHashString(datestring);// creates SHA1 hash of 16
													// digits
		// System.out.println("hash="+hash);
		// System.out.println("hash size="+hash.length());
		int datestringlen = datestring.length();
		String ordernum = hash.substring(10, 20);
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

	

}

