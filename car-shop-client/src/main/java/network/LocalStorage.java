package network;

import src.main.java.CryptographyService;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;

public class LocalStorage {

    private static final class InstanceHolder {
        private static final LocalStorage instance = new LocalStorage();
    }

    public static LocalStorage getInstance() {
        return LocalStorage.InstanceHolder.instance;
    }

    private LocalStorage() {}

    // Keys
    public KeyPair keyPair = CryptographyService.generateKeyPair();
    public PublicKey serverPublicKey;
    public SecretKey hmac;

    // Session
    public String token = null;
    public String userName;
    public String userRole;

    // Error
    public boolean error = false;
    public String errorMessage;

    public void clearSession() {
        this.token = null;
        this.userName = null;
    }

    public void resetError() {
        error = false;
        errorMessage = "";
    }

    public void reset() {
        clearSession();
        serverPublicKey = null;
    }

}
