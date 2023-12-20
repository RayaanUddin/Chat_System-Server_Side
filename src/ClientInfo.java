/* Class to store a single client info */

import java.io.Serializable;
import java.net.Socket;

public class ClientInfo implements Serializable {
    private final int connectionId;
    private final Socket socket;
    private String name;

    // Constructor
    public ClientInfo(Socket socket, int connectionId, String name) {
        this.socket = socket;
        this.connectionId = connectionId;
        this.name = name;
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
