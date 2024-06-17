import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            // Create and bind the server object to the RMI registry
            ComputationServerImpl server = new ComputationServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            if(UnicastRemoteObject.toStub(server) == null) {
                ComputationServer stub1 = (ComputationServer) UnicastRemoteObject.exportObject(server, 0);
            }
            registry.bind("ComputationServer", server);

            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
