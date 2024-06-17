import java.nio.file.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject; 
import java.rmi.RemoteException;

public class Server {
    private static final String SERVER_FOLDER_PATH = "../server_folder";

    public static void main(String[] args) {
        try {
            System.out.println("Server is booting....");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Registry registry = LocateRegistry.createRegistry(1099);
            Path serverFolderPath = Paths.get(SERVER_FOLDER_PATH);
            if (!Files.exists(serverFolderPath)) {
                Files.createDirectories(serverFolderPath);
            }

            ProductImpl fileSync = new ProductImpl(serverFolderPath);
            if(UnicastRemoteObject.toStub(fileSync) == null) {
                Product stub1 = (Product) UnicastRemoteObject.exportObject(fileSync, 0);
            }
            registry.rebind("FileSync", fileSync);

            ProductImpl p1 = new ProductImpl(serverFolderPath);

            if(UnicastRemoteObject.toStub(p1) == null) {
                Product stub1 = (Product) UnicastRemoteObject.exportObject(p1, 0);
            }
            registry.rebind("product", p1);

            System.out.println("Server is ready.");
            while (true) {
                Thread.sleep(1000); // Sleep for a while to prevent the main thread from exiting
            }

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
