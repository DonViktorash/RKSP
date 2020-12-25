package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            StringBuilder httpRequest = new StringBuilder();
            while((inputLine = in.readLine()).length() != 0){
                httpRequest.append(inputLine).append("\n");
            }
            // read content of POST request
            if (in.ready()) httpRequest.append("\r\n");
            while(in.ready()){
                httpRequest.append((char)in.read());
            }

            new HttpReadWriter(out, httpRequest.toString()).sendResponse();

            in.close();
            out.close();
            clientSocket.close();

            System.out.println(" > client connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
