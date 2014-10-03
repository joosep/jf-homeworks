package ee.ut.jf2014.homework2;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by joosep on 27.09.14.
 */
public class MessageSenderService implements Runnable, Closeable {
    private final ServerSocket server;
    private Thread serverThread;
    private List<ClientSocketHandler> clients;
    private volatile boolean isClosing = false;

    public MessageSenderService(int port) throws IOException {
        this.server = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    public void start() {
        this.serverThread = new Thread(this, "messageSender");
        this.serverThread.setDaemon(false);
        this.serverThread.start();
        System.out.println("MessageSender server started on port " + server.getLocalPort() + "!");

    }

    @Override
    public void run() {
        while (!isClosing && !Thread.currentThread().isInterrupted()) {
            System.out.println("Waiting for new connection");
            try {
                Socket s = server.accept();
                addClientSocketHandler(s);
            } catch (IOException e) {
                if (!isClosing) {
                    System.out.println("error while handling socket" + e.getMessage());
                } else {
                    System.out.println("error while handling socket during shutdown" + e.getMessage());
                }
            }
        }
    }

    private void addClientSocketHandler(Socket s) throws IOException {
        System.out.println("Accepted socket " + s);
        ClientSocketHandler client = new ClientSocketHandler(s);
        synchronized (clients) {
            clients.add(client);
            String message = client.getAuthor() + " joined the chat\n";
            tryToSendMessageToAll(message);
        }
        System.out.println("client with name " + client.getAuthor() + " created for socket " + s);
    }


    @Override
    public void close() throws IOException {
        this.isClosing = true;
        if (serverThread != null) serverThread.interrupt();
        if (server != null) {
            server.close();
        }
    }

    public void send(String author, String[] messageByLine) {
        for (String message : messageByLine) {
            String chatOutput = author + ": " + message + "\n";
            synchronized (clients) {
                tryToSendMessageToAll(chatOutput);
            }
        }

    }

    private void tryToSendMessageToAll(String output) {
        System.out.print("chat# " + output);
        Iterator<ClientSocketHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientSocketHandler client = iterator.next();
            try {
                client.write(output);
            } catch (Exception e) {
            //figured, that if socket is closed (as in client is disconnected), then throws exception, but don't do it.
            //need more time to look into disconnecting.
                System.out.println("something went bad: " + e.getMessage());
                closeAndRemoveClient(iterator, client);
            }
        }
    }

    private void closeAndRemoveClient(Iterator<ClientSocketHandler> iterator, ClientSocketHandler client) {
        try {
            client.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        iterator.remove();
        tryToSendMessageToAll(client.getAuthor()+" has left the chat");
    }
}
