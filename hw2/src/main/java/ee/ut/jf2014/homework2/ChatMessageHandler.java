package ee.ut.jf2014.homework2;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by joosep on 27.09.14.
 */
public class ChatMessageHandler extends
        AbstractHandler {
    private final MessageSenderService messageSender;

    public ChatMessageHandler(MessageSenderService messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        String requestMethod = baseRequest.getMethod();
       if (requestMethod.equals("POST")) {
           handleMessage(request, response);
       } else {
           handleBadRequest(response);
       }
        baseRequest.setHandled(true);
    }

    private void handleBadRequest(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("Bad Request: You can only make POST request with author header.");
    }

    private void handleMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String author = request.getHeader("author");
        if (author != null) {
            String[] messageByLine = request.getReader().lines().toArray(size -> new String[size]);
            messageSender.send(author, messageByLine);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("message accepted");
        } else {
            handleBadRequest(response);
        }
    }
}