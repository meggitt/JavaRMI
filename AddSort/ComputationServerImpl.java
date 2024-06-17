import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class ComputationServerImpl extends UnicastRemoteObject implements ComputationServer {

    protected ComputationServerImpl() throws RemoteException {
        super();
    }

    @Override
    public int add(int i, int j) throws RemoteException {
        return i + j;
    }

    @Override
    public int[] sort(int[] array) throws RemoteException {
        Arrays.sort(array);
        return array;
    }
}
