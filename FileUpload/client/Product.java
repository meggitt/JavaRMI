import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Product extends Remote {
    void uploadFile(byte[] fileData, String fileName) throws RemoteException;
    byte[] downloadFile(String fileName) throws RemoteException;
    boolean deleteFile(String fileName) throws RemoteException;
    boolean renameFile(String oldFileName, String newFileName) throws RemoteException;
    void syncFile(String filename, byte[] content) throws RemoteException;
}
