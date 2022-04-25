package src;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class ServerMain {
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        String baseUrl = ( args.length > 0 ) ? args[0] : "http://localhost:4000";

        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create( baseUrl ), new ResourceConfig( UserResource.class ), false );
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
            @Override
            public void run() {
                server.shutdownNow();
            }
        } ) );
        server.start();

        /*System.out.println( String.format( "\nGrizzly-HTTP-Server gestartet mit der URL: %s\n"
                        + "Stoppen des Grizzly-HTTP-Servers mit:      Strg+C\n",
                baseUrl) );*/

        Thread.currentThread().join();
    }

}
