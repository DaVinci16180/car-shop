package src.main.java;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CryptographyService {

    public static byte[] encryptAES(Object message, SecretKey key) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encode(cipher.doFinal(arr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object decryptAES(byte[] hash, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] message = cipher.doFinal(Base64.getDecoder().decode(hash));

            ByteArrayInputStream byteInStream = new ByteArrayInputStream(message);
            ObjectInput objectInput = new ObjectInputStream(byteInStream);

            Object result = objectInput.readObject();
            objectInput.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptRSA(Object message, PublicKey publicKey) {
        Cipher cipher;
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(arr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object decryptRSA(byte[] hash, PrivateKey privateKey) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] message = cipher.doFinal(hash);

            ByteArrayInputStream byteInStream = new ByteArrayInputStream(message);
            ObjectInput objectInput = new ObjectInputStream(byteInStream);

            Object result = objectInput.readObject();
            objectInput.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(Object message, SecretKey key) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);

            return Base64.getEncoder().encodeToString(mac.doFinal(arr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SecretKey generateKey(String algorithm) {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
