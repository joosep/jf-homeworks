package ee.ut.jf2014.homework2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by joosep on 27.09.14.
 */
public class ClientSocketHandler{
    private Socket socket = null;
    private String author;
    private Scanner scanner;
    private PrintWriter printWriter;

    public ClientSocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        open();
        author = scanner.nextLine();
    }

    private void open() throws IOException {
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(socket.getInputStream());
    }


    public void write(String message) {
            printWriter.write(message);
            printWriter.flush();
    }

    public void close() throws IOException {
        if (printWriter != null) printWriter.close();
        if (scanner != null) scanner.close();
    }

    public String getAuthor() {
        return author;
    }
}