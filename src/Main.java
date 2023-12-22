import java.net.*;
import java.io.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) {
        ClientList clientList = new ClientList();
        ServerToClient[] client = new ServerToClient[1000];
        int clientCount = 0;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(5004);
        } catch (IOException e) {
            System.out.println("Failed to create server socket");
            return;
        }
        while (true) {
            Socket clientSocketCatched = null;
            System.out.println("Listening for clients...");
            try {
                clientSocketCatched = serverSocket.accept();
                ClientInfo currentClient = clientList.add("Client1", clientSocketCatched);
                client[clientCount] = new ServerToClient(currentClient, clientList);
                client[clientCount].start();
                clientCount++;

                // Update all client lists for each client on server
                for (int i=0; i<client.length; i++) {
                    try {
                        client[i].updateClientList(clientList);
                    } catch (Exception e) {
                        System.out.println("Client " + i + " needs to be deleted!!!");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client initial connection error");
            }
        }
    }
}