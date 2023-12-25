import java.net.*;
import java.io.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) {
        ClientList clientList = new ClientList();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(5004);
        } catch (IOException e) {
            System.out.println("Failed to create server socket");
            return;
        }
        try {
            while (true) {
                Socket clientSocketCaught;
                System.out.println("Listening for clients...");
                try {
                    clientSocketCaught = serverSocket.accept();
                    clientList.add("Client1", clientSocketCaught);
                } catch (IOException e) {
                    System.out.println("Client initial connection error");
                }
            }
        } catch (Exception e) {
            System.out.println("Server loop has been interrupted!\nEnding program...");
        }
    }
}