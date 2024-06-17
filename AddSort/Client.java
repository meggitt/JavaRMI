import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CompletableFuture;

public class Client {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Client <operation> <parameters>");
            return;
        }

        String operation = args[0];
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            ComputationServer server = (ComputationServer) registry.lookup("ComputationServer");

            switch (operation.toUpperCase()) {
                case "ADD":
                    if (args.length < 3) {
                        System.out.println("Usage: java Client ADD <operand1> <operand2>");
                        return;
                    }
                    int operand1 = Integer.parseInt(args[1]);
                    int operand2 = Integer.parseInt(args[2]);
                    int result = server.add(operand1, operand2);
                    System.out.println("Addition result: " + result);
                    break;

                case "SORT":
                    if (args.length < 2) {
                        System.out.println("Usage: java Client SORT <array>");
                        return;
                    }
                    int[] array = new int[args.length - 1];
                    for (int i = 1; i < args.length; i++) {
                        array[i - 1] = Integer.parseInt(args[i]);
                    }
                    CompletableFuture<int[]> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            return server.sort(array);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });

                    future.thenAccept(sortedArray -> {
                        if (sortedArray != null) {
                            System.out.print("Sorted array: ");
                            for (int num : sortedArray) {
                                System.out.print(num + " ");
                            }
                            System.out.println();
                        } else {
                            System.out.println("Failed to sort array.");
                        }
                    });

                    // Wait for asynchronous result
                    future.join();
                    break;

                default:
                    System.out.println("Invalid operation.");
            }

            // Cleanup
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
