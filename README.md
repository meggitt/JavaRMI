# FileUpload and AddSort using JAVA RMI

## Table of Contents

- [Project Description](#project-description)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Scenarios](#scenarios)
- [Contributions](#contributions)

## Project Description

This project includes two main parts:

1. **File Upload/Download System**: A client-server application that allows clients to upload, download, rename, delete, and synchronize files with a server.
2. **Computation Server**: A client-server application that provides basic computation services like addition and sorting of integers.

Both parts leverage Java RMI (Remote Method Invocation) to facilitate communication between the client and the server.

## Features

### Part 1: File Upload/Download System

- Upload files from client to server.
- Download files from server to client.
- Rename files on the server.
- Delete files on the server.
- Synchronize files between client and server.

### Part 2: Computation Server

- Add two integers.
- Sort a list of integers.

## Requirements

- Latest JDK (Java Development Kit). [Download here](https://www.oracle.com/java/technologies/downloads/)
- (Optional) Any IDE (Integrated Development Environment) for Java development.

## Installation

### Verify JDK Installation

1. Download and install the latest JDK.
2. Verify the installation by running the following command in Command Prompt:

   ```sh
   java --version
   ```

   The output should resemble:

   ```sh
   java 21.0.2 2024-01-16 LTS
   Java(TM) SE Runtime Environment (build 21.0.2+13-LTS-58)
   Java HotSpot(TM) 64-Bit Server VM (build 21.0.2+13-LTS-58, mixed mode, sharing)
3. Clone the repository:
   ```sh
   git clone https://github.com/meggitt/JavaRMI.git
   cd movie-search-engine
   ```


### (Optional) Configure IDE

1. Install an IDE such as Eclipse or IntelliJ IDEA.
2. Configure the IDE for Java development.

## Running the Application

### Execution Through CMD

#### Part 1: File Upload/Download System

1. **Compile and Run Server**
   - Navigate to the `Server` directory inside `FileUpload`.
   - Execute the following commands to compile and run the server:

     ```sh
     javac Product.java
     javac ProductImpl.java
     javac Server.java
     java Server
     ```

2. **Compile and Run Client**
   - Navigate to the `Client` directory inside `FileUpload`.
   - Execute the following commands to compile the client:

     ```sh
     javac Product.java
     javac Client.java
     ```

3. **Client Operations**
   - Open a new command prompt tab.
   - Navigate to the `Client` directory and execute the following commands:

     ```sh
     java Client UPLOAD example.txt     # Uploads example.txt to server_folder
     java Client DOWNLOAD example.txt   # Downloads example.txt to client_folder (downloaded_example.txt)
     java Client                        # Synchronizes files (needs further inputs)
     java Client RENAME example.txt exampleNew.txt   # Renames example.txt to exampleNew.txt on server
     ```

#### Part 2: Computation Server

1. **Compile and Run Server**
   - Navigate to the `AddSort` directory.
   - Execute the following commands to compile and run the server:

     ```sh
     javac ComputationServer.java
     javac ComputationServerImpl.java
     javac Server.java
     java Server
     ```

2. **Compile and Run Client**
   - Navigate to the `AddSort` directory.
   - Execute the following command to compile the client:

     ```sh
     javac Client.java
     ```

3. **Client Operations**
   - Open a new command prompt tab.
   - Navigate to the `AddSort` directory and execute the following commands:

     ```sh
     java Client ADD 4 5                # Adds integers 4 and 5
     java Client SORT 3 6 1 33 35       # Sorts integers 3, 6, 1, 33, 35
     ```

### Using Visual Studio Code (VSC)

1. **Install and Configure Java**
   - Install Java support in Visual Studio Code.

2. **Run Server and Client**
   - Open the respective folders in VSC.
   - Click on `Terminal -> New Terminal` for each of the server and client directories.
   - Execute the same commands as mentioned above for CMD execution.


## Scenarios

### File Upload/Download System

- **UPLOAD**: Uploads a file from the client to the server.
- **DOWNLOAD**: Downloads a file from the server to the client.
- **RENAME**: Renames a file on the server.
- **DELETE**: Deletes a file on the server.
- **SYNC**: Synchronizes files between the client and the server.

### Computation Server

- **ADD**: Adds two integers.
- **SORT**: Sorts a list of integers.

## Contributions

Contributions are welcome! Please create an issue or submit a pull request with your changes.
