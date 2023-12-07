import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client extends Thread {
    final boolean debug = true;
    class Packet {
        ClientInfo sender;
        String message;
    }

    // This clients details
    private final ClientInfo thisClient;

    // All clients connected (Needs Updating every time a client connects)
    ClientList clientList;

    // Constructor
    public Client(ClientInfo client, ClientList initialClientList) {
        thisClient = client;
        clientList = initialClientList;
    }

    // Updates clientList
    public void updateClientList(ClientList updatedClientList) {
        clientList = updatedClientList;
    }

    // Receive input from client (waiting)
    private boolean awaitingResponse(BufferedReader inputStream, DataOutputStream outputStream) {
        String line;

        Packet packet = new Packet();
        packet.sender = thisClient;

        try {
            line = inputStream.readLine();
            if (debug) {
                System.out.println(line);
            }
            if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                thisClient.getSocket().close();
                System.out.println("Disconnected client " + thisClient.getName());
                return false; // Client has quit
            } else {
                if (line.substring(0,3).equalsIgnoreCase("all")) {
                    packet.message = line.substring(3);
                    sendMessageToAll(packet);
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    // Send message to all clients (Broadcasting)
    public void sendMessageToAll(Packet packet) {
        clientList.broadcastString(packet.sender.getName() + ": \n" + packet.message + "\n");
    }

    // Send message to this client
    public boolean sendMessageToThisClient(Packet packet) {
        try {
            DataOutputStream outputStream = new DataOutputStream(thisClient.getSocket().getOutputStream());
            // Send message
            outputStream.writeBytes(packet.sender.getName() + " (Private): \n" + packet.message + "\n");
            outputStream.flush();
            return true;
        } catch (IOException e) {
            if (debug) {
                System.out.println("Connection Lost");
            }
            return false;
        }
    }

    // Running program
    public void run() {
        BufferedReader inputStream = null;
        DataOutputStream outputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader(thisClient.getSocket().getInputStream()));
            outputStream = new DataOutputStream(thisClient.getSocket().getOutputStream());
        } catch (IOException e) {
            return; // Connection lost from client
        }
        String line;
        while (true) {
            if (!awaitingResponse(inputStream, outputStream)) {
                return;
            }
        }
    }
}
