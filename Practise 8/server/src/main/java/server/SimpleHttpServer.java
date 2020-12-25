package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    public static final int PORT = 8080;
    public static List<Book> books = new ArrayList<>();

    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" > server socket created");

            // sample of book
            books.add(new Book("0", "Война и мир", "Л. Н. Толстой", "1865"));

            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                executorService.execute(new ClientHandler(client));
                System.out.println(" > connection accepted");
            }

            executorService.shutdown();
        } catch (IOException e) {
            System.out.println(" > accept() failed");
            e.printStackTrace();
        }
    }
}
