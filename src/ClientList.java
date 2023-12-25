/* Class for a dynamic data structure of all clients connected to server */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientList {

    private static class Client {
        public ClientInfo clientInfo; // Array holds all connected clients
        public ServerToClient clientThread; // Running communications between server to individual client
    }

    private Client[] client; // Holds thread + client info to generate a list

    // Broadcast message to all
    public void broadcastString(Packet packet) {
        ObjectOutputStream outputStream;
        System.out.println("Broadcast: " + packet.getMessage());
        for (int i=0; i<client.length; i++) {
            try {
                if (client[i].clientInfo.getClientDetails().getConnectionId() != packet.getClientDetails().getConnectionId()) {
                    outputStream = new ObjectOutputStream(client[i].clientInfo.getSocket().getOutputStream());
                    outputStream.writeObject(packet);
                }
            } catch (IOException e) {
                System.out.println("Error occurred when broadcasting");
            }
        }
    }

    // Send message to a client
    public boolean sendMessageToClient(String message, int clientConnId, ClientDetails senderDetails) {
        try {
            ClientInfo client = getClientById(clientConnId);
            if (client == null) {
                System.out.println("Client not found");
                return false;
            }
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getSocket().getOutputStream());
            Packet packet = new Packet(message, senderDetails);
            outputStream.writeObject(packet);
            System.out.println("Private message sent");
            return true;
        } catch (IOException e) {
            System.out.println("Client not found");
            return false;
        }
    }

    // Adds a client to the array (Dynamically), returns client added info
    public void add(String name, Socket clientSocket) {
        try {
            Client[] client_new = new Client[client.length + 1];

            // Copy current array into new created
            for (int i=0;i<client.length; i++) {
                client_new[i] = client[i];
            }

            // Next possible connection Id
            int connectionId = 0;
            if (client.length > 0) {
                connectionId = client[client.length - 1].clientInfo.getClientDetails().getConnectionId() + 1; // the connection id must always be greater than the socket before
            }

            client_new[client_new.length-1] = new Client();

            // Setting client info
            ClientInfo currentClientInfo = new ClientInfo(clientSocket, connectionId, name);
            client_new[client_new.length-1].clientInfo = currentClientInfo;

            // Setting thread
            ServerToClient currentClient = new ServerToClient(currentClientInfo, this);
            currentClient.start();
            client_new[client_new.length-1].clientThread = currentClient;

            client = client_new;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    // Delete a client in the array (Dynamically)
    public boolean delete(int connectionId) {
        // Clients Connected List: Starts from 0
        int index = getConnClientLocationById(connectionId);
        if (index < 0) {
            return false; // Client was not found to delete
        }
        Client[] client_new = new Client[client.length - 1];

        // Copy current array into new created, without deleted element
        for (int i=0; i<index; i++) {
            client_new[i] = client[i];
        }
        for (int i=index+1; i<client.length; i++) {
            client_new[i-1] = client[i];
        }

        client = client_new;
        return true;
    }

    // Gets the clients info by its connection id
    public ClientInfo getClientById(int connectionId) {
        int index = getConnClientLocationById(connectionId);
        if (index < 0) {
            return null;
        } else {
            return client[getConnClientLocationById(connectionId)].clientInfo;
        }
    }

    // Gets the clients index location within the connectedClients array by connection id
    private int getConnClientLocationById(int connectionId) {
        // Relation between connectedClients index and client id (ONLY WHEN CREATED), id = index
        // The clients connection id >= index location, within connectedClients
        try {
            int currentIndex = connectionId;
            while (currentIndex >= 0) {
                if (connectionId == client[currentIndex].clientInfo.getClientDetails().getConnectionId()) {
                    return currentIndex;
                } else {
                    currentIndex --;
                }
            }
        } catch (Exception e) {
            return -1; // Not found, Array out of bound
        }
        return -1; // Not found, Client has disconnected
    }

    // Constructor
    public ClientList() {
        client = new Client[0];
    }
}

