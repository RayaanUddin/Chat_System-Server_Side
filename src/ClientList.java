/* Class for a dynamic data structure of all clients connected to server */

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientList {
    private ClientInfo[] connectedClients; // Array holds all connected clients

    // Broadcast message to all
    public void broadcastString(Packet packet) {
        ObjectOutputStream outputStream;
        for (int i=0; i<connectedClients.length; i++) {

            try {
                outputStream = new ObjectOutputStream(connectedClients[i].getSocket().getOutputStream());
                outputStream.writeObject(packet);
                System.out.println("Broadcast: " + packet.getMessage());
            } catch(IOException e) {
                System.out.println("Error occurred when broadcasting");
            }
        }
    }

    // Send message to a client
    public boolean sendMessageToClient(String message, int clientConnId) {
        try {
            ClientInfo client = getClientById(clientConnId);
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getSocket().getOutputStream());
            Packet packet = new Packet(message, client.getClientDetails());
            outputStream.writeObject(packet);
            return true;
        } catch (IOException e) {
            System.out.println("Client not found");
            return false;
        }
    }

    // Adds a client to the array (Dynamically), returns client added info
    public ClientInfo add(String name, Socket clientSocket) {
        // Program starts with length 0
        ClientInfo[] clients = new ClientInfo[connectedClients.length + 1];

        // Copy current array into new created
        for (int i=0;i<connectedClients.length; i++) {
            clients[i] = connectedClients[i];
        }

        // Next possible connection Id
        int connectionId = 0;
        if (connectedClients.length > 0) {
            connectionId = connectedClients[connectedClients.length - 1].getClientDetails().getConnectionId() + 1; // the connection id must always be greater than the socket before
        }

        ClientInfo currentClient = new ClientInfo(clientSocket, connectionId, name);
        clients[clients.length-1] = currentClient;

        connectedClients = clients;
        return currentClient;
    }

    // Delete a client in the array (Dynamically)
    public boolean delete(int connectionId) {
        // Program starts with length 0
        int index = getConnClientLocationById(connectionId);
        if (index < 0) {
            return false; // Client was not found to delete
        }
        ClientInfo[] clients = new ClientInfo[connectedClients.length - 1];

        // Copy current array into new created, without deleted element
        for (int i=0; i<index; i++) {
            clients[i] = connectedClients[i];
        }
        for (int i=index+1; i<connectedClients.length; i++) {
            clients[i-1] = connectedClients[i];
        }
        connectedClients = clients;
        return true;
    }

    // Gets the clients info by its connection id
    public ClientInfo getClientById(int connectionId) {
        try {
            return connectedClients[getConnClientLocationById(connectionId)];
        } catch (Exception e) { // getConnClientLocationById returned -1, Client not found
            return null; // Not found
        }
    }

    // Gets the clients index location within the connectedClients array by connection id
    private int getConnClientLocationById(int connectionId) {
        // Relation between connectedClients index and client id (ONLY WHEN CREATED), id = index
        // The clients connection id >= index location, within connectedClients
        try {
            int currentIndex = connectionId;
            while (currentIndex >= 0) {
                if (connectionId == connectedClients[currentIndex].getClientDetails().getConnectionId()) {
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

    //Gets the client using its socket (inefficient: Linear Search) DEPRECIATED
    public ClientInfo getClientBySocket(Socket clientSocket) {
        for (int i=0; i<connectedClients.length; i++) {
            if (connectedClients[i].getSocket() == clientSocket) {
                return connectedClients[i];
            }
        }
        return null; // Not found
    }

    // Constructor
    public ClientList() {
        connectedClients = new ClientInfo[0];
    }
}

