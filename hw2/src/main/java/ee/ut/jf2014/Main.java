package ee.ut.jf2014;

import ee.ut.jf2014.homework2.ChatServer;

/* Implement a chat server that works with jf chat client: https://github.com/zeroturnaround/jf-chatclient
 * TCP socket endpoint, listen on port 8888
 * Functionality: receive name from client, send messages to client (name is only used for debugging)
 * ChatClient connects and prints a name
 * Use socket to send chat messages to clients
 * HTTP endpoint, listen on port 8080
 * Functionality: receive messages from client
 * ChatClient makes HTTP POST request
 * Author's name in HTTP header "author"
 * Message in request body
 * Server handles request, sends received message to all participants (via TCP)
 * Reading raw HTTP is not allowed, got to use some existing embeddable HTTP server (Jetty, Tomcat, HTTPComponents, Netty, etc)
 * Response messages pattern: author + ": " + message
 * When new user joins the chat, a message should be sent to all participants (including self)
 *    with the pattern: author + " joined the chat"
 * When server is distributing messages to clients, it should send all messages to all clients, always
 *   (there was a question if server should send the message back to the originating client).
 *
 *
 * Created by joosep on 27.09.14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ChatServer server = new ChatServer();
        server.start();
    }
}
