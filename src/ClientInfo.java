/* Class to store a single client info */

import java.net.Socket;

public class ClientInfo {
    private final int connectionId;
    private final Socket socket;
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
