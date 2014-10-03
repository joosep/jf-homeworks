package ee.ut.jf2014.homework2;

import org.eclipse.jetty.server.Server;

import java.io.IOException;

/**
 * Created by joosep on 27.09.14.
 */
public class ChatServer {

    private final int TCP_PORT=8888;
    private final int HTTP_PORT=8080;

    public void start() throws Exception {
        MessageSenderService messageSender = new MessageSenderService(TCP_PORT);
        messageSender.start();
        Server server = new Server(HTTP_PORT);
        ChatMessageHandler messageHandler = new ChatMessageHandler(messageSender);
        server.setHandler(messageHandler);
        server.start();
        server.join();
    }
}
