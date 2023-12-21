import java.io.Serializable;

public class ClientDetails implements Serializable {
    private final int connectionId;

    private String name;

    // Constructor
    public ClientDetails(int connectionId, String name) {
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

    // Set Client Name
    public void setName(String clientName) {
        name = clientName;
    }
}