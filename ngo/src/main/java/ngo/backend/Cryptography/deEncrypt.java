package ngo.backend.Cryptography;

public class deEncrypt {
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getSecretKey(secret));
            return jakarta.xml.bind.DatatypeConverter.printBase64Binary(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getSecretKey(secret));
            return new String(cipher.doFinal(jakarta.xml.bind.DatatypeConverter.parseBase64Binary(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private static javax.crypto.SecretKey getSecretKey(String myKey) {
        java.security.MessageDigest sha = null;
        try {
            byte[] key = myKey.getBytes("UTF-8");
            sha = java.security.MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = java.util.Arrays.copyOf(key, 16);
            return new javax.crypto.spec.SecretKeySpec(key, "AES");
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
