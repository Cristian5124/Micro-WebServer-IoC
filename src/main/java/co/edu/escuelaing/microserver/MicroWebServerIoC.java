package co.edu.escuelaing.microserver;

import java.io.IOException;

import co.edu.escuelaing.microserver.http.HttpServer;
import co.edu.escuelaing.microserver.ioc.SimpleIoCContainer;

/**
 * Main class for the Micro WebServer IoC framework.
 * Provides a simple way to start a web server with IoC capabilities.
 */
public class MicroWebServerIoC {

    private static final int DEFAULT_PORT = 8080;

    /**
     * Main entry point for the framework.
     *
     * @param args command line arguments - first argument should be the controller
     *
     */
    public static void main(String[] args) {
        try {
            SimpleIoCContainer container = new SimpleIoCContainer();

            if (args.length > 0) {
                container.registerController(args[0]);
                System.out.println("Registered controller: " + args[0]);
            } else {
                container.scanAndRegisterControllers("co.edu.escuelaing.microserver.examples");
                System.out.println("Scanned and registered controllers from examples package");
            }

            HttpServer server = new HttpServer(DEFAULT_PORT, container);

            System.out.println("Starting Micro WebServer IoC on port " + DEFAULT_PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\nShutting down server...");
                    server.stop();
                } catch (IOException e) {
                    System.err.println("Error stopping server: " + e.getMessage());
                }
            }));

            server.start();

        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
