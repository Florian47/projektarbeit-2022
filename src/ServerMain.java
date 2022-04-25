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
        ResourceConfig config = new ResourceConfig( UserResource.class );
        config.register(new FilterCors());
        config.register(FilterCors.class);

        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create( baseUrl ), config, false );
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
            @Override
            public void run() {
                server.shutdownNow();
            }
        } ) );
        server.start();
        Thread.currentThread().join();
    }

}
