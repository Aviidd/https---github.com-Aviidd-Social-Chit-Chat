import java.io.*;
import java.net.*;

public class Client {

    Socket socket;

    BufferedReader br; // used for reading
    PrintWriter out; // used for writing

    // initializer constructor
    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("192.168.1.39", 7777);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // TODO: handle exception
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

                    // if client get end from server than it will close the reader socket / thread
                    if (msg.equals("end")) {
                        System.out.println("Server Ended the chat");
                        socket.close();
                        break;
                    }
                    ;
                    System.out.println("Server : " + msg);
                }

            } catch (Exception e) {
                System.out.println("Connection is closed"); // this will print the exact line error on terminal
            }

        };
        new Thread(r1).start();

    }

    // method which will write the input from client and sent to server
    public void startWriting() {
        // make thread which will write data and sent to server

        System.out.println("Writer Started");

        Runnable r2 = () -> {

            try {
                while (!socket.isClosed()) {

                    // this line will take input from server
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    // if client write end than it will close the write socket / thread
                    if (content.equals("end")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is client");

        new Client(); // calling the constructor
    }
}