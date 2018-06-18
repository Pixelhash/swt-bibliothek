package de.fhl.swtlibrary.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class VerificationUtil {

  private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

  public static String getRandomString(int length) {
    SecureRandom rnd = new SecureRandom();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      builder.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
    }
    return builder.toString();
  }

  public static String hashSha256(String message) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(message.getBytes());
      return DatatypeConverter.printHexBinary(hash).toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String doHmacSha256AsHex(String key, String message) {
    try {
      Mac hasher = Mac.getInstance("HmacSHA256");
      hasher.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
      byte[] hash = hasher.doFinal(message.getBytes());
      return DatatypeConverter.printHexBinary(hash).toLowerCase();
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      e.printStackTrace();
    }
    return null;
  }

}
