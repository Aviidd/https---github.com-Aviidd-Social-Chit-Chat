import java.net.*;
import java.io.*;

public class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br; // used for reading
    PrintWriter out; // used for writing

    // initializing Constructor
    public Server() {
        // try catch will catch the excpetion
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Server is waiting");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method which will reading the input from client
    public void startReading() {
        // make thread which will read data

        Runnable r1 = () -> {
            System.out.println("Reader is started");

            try {
                while (true) {
                    String msg = br.readLine();

                    // if server read end from client than it will close the socket / reader thread.
                    if (msg.equals("end")) {
                        System.out.println("Client Ended the chat");
                        socket.close();
                        break;
                    }
                    ;
                    System.out.println("Client : " + msg);
                }

            } catch (Exception e) {
                System.out.println("Connection is Closed");
                e.printStackTrace(); // this will print the exact line error on terminal
            }

        };
        new Thread(r1).start();

    }

    // method which will write the input from server and sent to client
    public void startWriting() {
        // make thread which will write data and sent to client

        System.out.println("Writer Started");

        Runnable r2 = () -> {
            try {
                while (!socket.isClosed()) {

                    // this line will take input from user
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    // if Server write end than it will close the socket / write thread
                    if (content.equals("end")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is server");
        new Server();
    }
}
