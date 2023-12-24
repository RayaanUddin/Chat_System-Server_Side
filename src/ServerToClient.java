/* Class inherited from thread. Every single client connected has a running thread */

import java.io.*;

public class ServerToClient extends Thread {
    final boolean debug = true;

    // This clients details
    private final ClientInfo thisClient;

    // All clients connected (Needs Updating every time a client connects)
    ClientList clientList;

    // Constructor
    public ServerToClient(ClientInfo client, ClientList initialClientList) {
        thisClient = client;
        clientList = initialClientList;
    }

    // Updates clientList
    public void updateClientList(ClientList updatedClientList) {
        clientList = updatedClientList;
    }

    // Receive input from client (waiting)
    private boolean awaitingResponse(BufferedReader inputStream) {
        String line;

        try {
            line = inputStream.readLine();
            if (debug) {
                System.out.println(line);
            }
            if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                thisClient.getSocket().close();
                System.out.println("Disconnected client " + thisClient.getClientDetails().getName());
                return false; // Client has quit
            } else {
                try {
                    if (line.substring(0, 4).equalsIgnoreCase("all ")) {
                        Packet packet = new Packet(line.substring(4), thisClient.getClientDetails());
                        clientList.broadcastString(packet); // Send message to all clients (Broadcasting)
                    } else if (line.substring(0,8).equalsIgnoreCase("private ")) {
                        try {
                            int connId;
                            String connId_str = "";
                            String message = "";
                            for (int i=8;i<line.length();i++) {
                                if ((line.charAt(i) == ' ')) {
                                    message = line.substring(i + 1);
                                    break;
                                } else {
                                    connId_str = connId_str + line.charAt(i);
                                }
                            }
                            if (!message.isEmpty()) {
                                connId = Integer.parseInt(connId_str);
                                if (clientList.sendMessageToClient(message, connId, thisClient.getClientDetails())) {
                                    System.out.println("Sending private message..");
                                }
                            }
                        } catch (Exception ignored) {}

                    }
                } catch (Exception ignored) {
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Running program
    public void run() {
        BufferedReader inputStream;
        try {
            inputStream = new BufferedReader(new InputStreamReader(thisClient.getSocket().getInputStream()));
        } catch (IOException e) {
            return; // Connection lost from client
        }
        while (true) {
            if (!awaitingResponse(inputStream)) {
                return;
            }
        }
    }
}
