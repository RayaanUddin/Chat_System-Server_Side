/* Class to store a single client info */

import java.io.Serializable;
import java.net.Socket;

public class ClientInfo {
    private final ClientDetails clientDetails;
    private final Socket socket;

    // Constructor
    public ClientInfo(Socket socket, int connectionId, String name) {
        this.socket = socket;
        this.clientDetails = new ClientDetails(connectionId, name);
    }

    // Get clientDetails
    public ClientDetails getClientDetails() {
        return this.clientDetails;
    }

    // Get Client Socket
    public Socket getSocket() {
        return socket;
    }
}
