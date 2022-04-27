package src;

import org.apache.derby.iapi.jdbc.AutoloadedDriver;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerMain {
    public static void main( String[] args ) throws IOException, InterruptedException, SQLException {
        //cache db connection on startup
        DBConnection.get().setAutoCommit(true);
        DBConnection.dropDatabaseContent();
        DBConnection.fillWithTestData();




        String baseUrl = ( args.length > 0 ) ? args[0] : "http://localhost:4000";
        ResourceConfig config = new ResourceConfig( UserResource.class );
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
