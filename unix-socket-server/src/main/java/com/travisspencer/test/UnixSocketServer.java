package com.travisspencer.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.unixsocket.UnixSocketConnector;
import org.eclipse.jetty.util.IO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnixSocketServer
{
    private static final Logger _logger = LogManager.getLogger();

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        UnixSocketConnector connector = new UnixSocketConnector(server);
        connector.setAcceptQueueSize(128);
        connector.setUnixSocket("/tmp/test.sock");
        server.addConnector(connector);

        ServletContextHandler handler = new ServletContextHandler(null, "/");
        handler.setServer(server);
        handler.addServlet(new ServletHolder(new HttpServlet()
        {
            protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
            {
                res.setContentType("text/html");

                PrintWriter writer = res.getWriter();
                writer.println("<html>");
                writer.println("<head><title>Hello World Servlet</title></head>");
                writer.println("<body>Hello World! How are you doing?</body>");
                writer.println("</html>");
                writer.close();

                res.flushBuffer();
            }
        }), "/*");
        server.setHandler(handler);

        server.setHandler(handler);

        _logger.info("created server");
        server.start();

        _logger.info("started, now stopping");
        server.join();
    }
}
