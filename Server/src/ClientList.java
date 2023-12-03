import java.net.*;

// class for a client
class ClientInfo {
    private final int connectionId;
    private Socket socket;
    private String name;

    // Constructor
    public ClientInfo(Socket clientSocket, int connId, String clientName) {
        socket = clientSocket;
        connectionId = connId;
        name = clientName;
    }

    // Get Connection Id
    public int getConnectionId() {
        return connectionId;
    }

    // Get Client Name
    public String getName() {
        return name;
    }

    // Get Client Socket
    public Socket getSocket() {
        return socket;
    }

    // Set Client Name
    public void setName(String clientName) {
        name = clientName;
    }
}

// All Clients for server (Dynamic structure)
public class ClientList {
    private ClientInfo[] connectedClients; // Array holds all connected clients

    // Adds a client to the array (Dynamically)
    public boolean add(String name, Socket clientSocket) {
        // Program starts with length 0
        // The end index is null at all times
        try {
            ClientInfo[] clients = new ClientInfo[connectedClients.length + 1];
            for (int i = 0; i < connectedClients.length; i++) { // Copy current array into new created
                clients[i] = connectedClients[i];
            }

            // Next possible connection Id
            int connectionId = 0;
            if (connectedClients.length > 1) {
                connectionId = connectedClients[connectedClients.length - 2].getConnectionId() + 1;
            }

            clients[connectedClients.length - 1] = new ClientInfo(clientSocket, connectionId, name);

            connectedClients = clients;
            return true;
        } catch (Exception e) { // Error occurred
            return false;
        }
    }

    // Delete a client in the array (Dynamically)
    public boolean delete(int connectionId) {
        // Program starts with length 0
        // The end element is null at all times
        try {
            int index = getConnClientLocationById(connectionId);
            if (index < 0) {
                return false; // Client was not found to delete
            }
            ClientInfo[] clients = new ClientInfo[connectedClients.length - 1];
            // Copy current array into new created, without deleted element
            for (int i = 0; i < index; i++) {
                clients[i] = connectedClients[i];
            }
            for (int i = index + 1; i < connectedClients.length; i++) {
                clients[i-1] = connectedClients[i];
            }
            connectedClients = clients;
            return true;
        } catch (Exception e) { // Error occurred
            return false;
        }
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
                if (connectionId == connectedClients[currentIndex].getConnectionId()) {
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

    //Gets the client using its socket (inefficient: Linear Search)
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

