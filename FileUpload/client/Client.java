import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

public class Client {
    private static final String CLIENT_FOLDER_PATH = "../client_folder";
    private static final String SERVER_FOLDER_PATH = "../server_folder";

    public static void main(String[] args) {
        if (args.length < 2) {
            SyncFile();
            System.out.println("Usage: java Client <command> <file_path>");
            return;
        }

        String command = args[0];
        String filePath = args[1];

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            Product product = (Product) registry.lookup("product");

            switch (command.toUpperCase()) {
                case "UPLOAD":
                    uploadFile(product, filePath);
                    break;
                case "DOWNLOAD":
                    downloadFile(product, filePath);
                    break;
                case "DELETE":
                    deleteFile(product, filePath);
                    break;
                case "RENAME":
                    if (args.length != 3) {
                        System.out.println("Usage: java Client RENAME <old_file_name> <new_file_name>");
                        return;
                    }
                    String newFileName = args[2];
                    renameFile(product, filePath, newFileName);
                    break;
                default:
                    System.out.println("Invalid command.");
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void SyncFile() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Product fileSync = (Product) registry.lookup("product");
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path clientFolderPath = Paths.get(CLIENT_FOLDER_PATH);
            clientFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            Scanner scanner = new Scanner(System.in);
            System.out.println(
                    "Enter the filename to synchronize (or type all to synchronize all files fromclient_folder): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("all")) {
                try {
                    uploadExistingFiles(fileSync, clientFolderPath);
                    Files.walk(clientFolderPath)
                            .filter(Files::isRegularFile)
                            .forEach(path -> {
                                try {
                                    byte[] content = Files.readAllBytes(path);
                                    String filename = clientFolderPath.relativize(path).toString();
                                    System.out.println("Synchronized file: " + filename);
                                    fileSync.syncFile(filename, content);
                                } catch (IOException e) {
                                    System.err.println("Error reading file: " + e.getMessage());
                                }
                            });

                    // Continuous check for changes
                    while (true) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            if (kind == StandardWatchEventKinds.OVERFLOW) {
                                continue;
                            }

                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path filename = ev.context();
                            Path fullPath = clientFolderPath.resolve(filename);

                            if (Files.isDirectory(fullPath)) {
                                continue; // Ignore directories
                            }

                            if (kind == StandardWatchEventKinds.ENTRY_CREATE
                                    || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                // Upload the created or modified file to the server
                                byte[] content = Files.readAllBytes(fullPath);
                                String relativePath = clientFolderPath.relativize(fullPath).toString();
                                fileSync.syncFile(relativePath, content);
                            }
                        }
                        key.reset();
                    }
                } catch (IOException e) {
                    System.err.println("Error synchronizing files: " + e.getMessage());
                } catch (InterruptedException e) {
                    System.err.println("Watch service interrupted: " + e.getMessage());
                }
            } else {
                String[] filenames = input.split("\\s+");

                // Synchronize each file specified by the user
                for (String filename : filenames) {
                    Path clientFilePath = clientFolderPath.resolve(filename);
                    if (Files.exists(clientFilePath) && !Files.isDirectory(clientFilePath)) {
                        byte[] content = Files.readAllBytes(clientFilePath);
                        System.out.println("Synchronized File name:" + filename);
                        fileSync.syncFile(filename, content);
                    } else {
                        System.out.println("File " + filename + " does not exist in the client folder.");
                    }
                }

                // Continuous check for changes
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        Path fullPath = clientFolderPath.resolve(filename);

                        if (Files.isDirectory(fullPath)) {
                            continue; // Ignore directories
                        }

                        if (kind == StandardWatchEventKinds.ENTRY_CREATE
                                || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            // Upload the created or modified file to the server
                            byte[] content = Files.readAllBytes(fullPath);
                            fileSync.syncFile(filename.toString(), content);
                        }
                    }
                    key.reset();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in File Synchronization Client: " +
                    e.getMessage());
        }
    }

    private static void uploadExistingFiles(Product fileSync, Path folderPath) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path entry : stream) {
                if (!Files.isDirectory(entry)) {
                    byte[] content = Files.readAllBytes(entry);
                    fileSync.syncFile(entry.getFileName().toString(), content);
                }
            }
        }
    }

    private static void uploadFile(Product product, String filePath) throws Exception {
        File file = new File("../client_folder/" + filePath);
        byte[] fileData = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(fileData);
        fis.close();
        String serverFilePath = "../server_folder/" + file.getName(); // Assuming the server_folder is located at
                                                                      // "../server_folder"

        product.uploadFile(fileData, serverFilePath);

        System.out.println("File uploaded successfully.");
    }

    private static void downloadFile(Product product, String filePath) throws Exception {
        byte[] fileData = product.downloadFile("../server_folder/" + filePath);
        if (fileData != null) {
            // Modify the file path to save downloaded files to the server_folder
            String serverFilePath = "../client_folder/downloaded_" + filePath;
            File file = new File(serverFilePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileData);
            fos.close();
            System.out.println("File downloaded successfully.");
        } else {
            System.out.println("File not found on server.");
        }
    }

    private static void deleteFile(Product product, String filePath) throws Exception {
        boolean success = product.deleteFile("../server_folder/" + filePath);
        if (success) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File not found on server.");
        }
    }

    private static void renameFile(Product product, String oldFileName, String newFileName) throws Exception {
        boolean success = product.renameFile("../server_folder/" + oldFileName, "../server_folder/" + newFileName);
        if (success) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("File not found on server.");
        }
    }
}
