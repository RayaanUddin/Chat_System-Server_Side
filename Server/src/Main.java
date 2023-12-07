import java.net.*;
import java.io.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ClientList clientList = new ClientList();
        Client[] client = new Client[2];
        ServerSocket serverSocket = new ServerSocket(5004);
        int count = 0;
        while (true) {
            Socket clientSocketCatched = null;
            System.out.println("Listening for clients...");
            try {
                clientSocketCatched = serverSocket.accept();
                ClientInfo currentClient = clientList.add("Client1", clientSocketCatched);
                client[count] = new Client(currentClient, clientList);
                client[count].start();
                count++;
            } catch (Exception e) {

            }
        }
    }
}

class ConnectedClient extends Thread {
    private final Socket socket;

    public ConnectedClient(Socket clientSocket, ClientList currentConnectedClients, int id) {
        socket = clientSocket;
    }

    ClientList clientList;

    // Test method
    public int add(int x, int y) {return x+y;}

    public void updateConnectedClients(ClientList clients) {
        clientList = clients;
    }

    public void run() {
        BufferedReader inputStream = null;
        DataOutputStream outputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return; // Connection lost from client
        }
        String line;
        while (true) {
            try {
                line = inputStream.readLine();
                System.out.println(line);
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    System.out.println("Disconnect");
                    return; // Client has quit
                } else {
                    outputStream.writeBytes(line + "\n\r");
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}