package com.travisspencer.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.unixsocket.client.HttpClientTransportOverUnixSockets;

public class Proxy
{
    private static final Logger _logger = LogManager.getLogger();

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        ConnectHandler proxy = new ConnectHandler();

        connector.setPort(8888);
        server.addConnector(connector);
        server.setHandler(proxy);

        ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);
        ServletHolder proxyServlet = new ServletHolder(new ProxyServlet()
        {
            @Override
            protected HttpClient newHttpClient()
            {
                return new HttpClient(new HttpClientTransportOverUnixSockets("/tmp/test.sock"), null);
            }
        });

        context.addServlet(proxyServlet, "/*");

        _logger.info("created server");
        server.start();

        _logger.info("started, now stopping");
        server.join();
    }
}
