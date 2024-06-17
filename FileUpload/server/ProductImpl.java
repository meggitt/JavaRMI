import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProductImpl extends UnicastRemoteObject implements Product {
    private Path serverFolderPath;
    public ProductImpl() throws RemoteException {
        super();
    }
    public ProductImpl(Path serverFolderPath)throws RemoteException{
        //System.out.println("at constructor setting serverfolderpath");
        super();
        this.serverFolderPath = serverFolderPath;
        //System.out.println("it is"+this.serverFolderPath);
    }


    @Override
    public synchronized void uploadFile(byte[] fileData, String fileName) throws RemoteException {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(fileData);
            fos.close();
            System.out.println("File " + fileName + " uploaded successfully.");
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public synchronized byte[] downloadFile(String fileName) throws RemoteException {
        try {
            Path filePath = Paths.get(fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public synchronized boolean deleteFile(String fileName) throws RemoteException {
        try {
            Path filePath = Paths.get(fileName);
            Files.deleteIfExists(filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public synchronized boolean renameFile(String oldFileName, String newFileName) throws RemoteException {
        try {
            Path oldFilePath = Paths.get(oldFileName);
            Path newFilePath = Paths.get(newFileName);
            Files.move(oldFilePath, newFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error renaming file: " + e.getMessage());
            return false;
        }
    }

   
    @Override
    public void syncFile(String filename, byte[] content) throws RemoteException {
       // System.out.println("here at"+filename);
        Path filePath = serverFolderPath.resolve(filename);
       // System.out.println("here at the path"+filePath);
        try {
            Files.write(filePath, content);
            System.out.println("File " + filename + " synchronized.");
        } catch (IOException e) {
            System.err.println("Error synchronizing file " + filename + ": " + e.getMessage());
        }
    }
}
