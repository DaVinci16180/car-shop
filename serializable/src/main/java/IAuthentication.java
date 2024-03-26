package src.main.java;

import javax.crypto.SecretKey;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IAuthentication extends Remote {
    String signIn(String username, String password) throws RemoteException;

    String signUp(String username, String password, String name) throws RemoteException;

    void signOut(String token) throws RemoteException;

    boolean validate(String token) throws RemoteException;

    boolean hasRole(String token, String[] hasAnyRole) throws RemoteException;

    SecretKey getHmac(String token) throws RemoteException;

    Map<String, String> getUserData(String token) throws RemoteException;
}
