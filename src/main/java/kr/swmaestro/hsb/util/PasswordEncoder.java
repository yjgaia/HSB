package kr.swmaestro.hsb.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA1 암호화를 담당하는 유틸리티 클래스
 * 
 * @author 심영재
 */
public class PasswordEncoder {

	private static final Charset charset = Charset.forName("UTF-8");
	private static MessageDigest SHA1;

	static {
		try {
			SHA1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}

	private static String toHexString(byte[] b) {
		if (b == null)
			return null;
		if (b.length == 0)
			return "";

		StringBuffer r = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			String x = Integer.toHexString(b[i] & 0xFF).toLowerCase();
			if (x.length() < 2)
				r.append('0');
			r.append(x);
		}
		return r.toString();
	}

	public static String encodePassword(String password) {
		if (password == null) {
			return null;
		} else if (password.isEmpty()) {
			return "";
		}

		byte[] b = password.getBytes(charset);
		byte[] s = SHA1.digest(b); // stage1
		s = SHA1.digest(s); // stage2

		return toHexString(s);
	}

}
