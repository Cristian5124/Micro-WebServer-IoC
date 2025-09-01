package co.edu.escuelaing.microserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple HTTP server that handles GET requests and serves static files.
 * Supports HTML and PNG file types.
 */
public class HttpServer {
    
    private final int port;
    private final RequestDispatcher requestDispatcher;
    private ServerSocket serverSocket;
    
    /**
     * Creates a new HTTP server on the specified port.
     *
     * @param port the port to listen on
     * @param requestDispatcher the dispatcher to handle dynamic requests
     */
    public HttpServer(int port, RequestDispatcher requestDispatcher) {
        this.port = port;
        this.requestDispatcher = requestDispatcher;
    }
    
    /**
     * Starts the HTTP server and begins listening for requests.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on http://localhost:" + port);
        
        while (!serverSocket.isClosed()) {
            try (Socket clientSocket = serverSocket.accept()) {
                handleRequest(clientSocket);
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Error handling request: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Stops the HTTP server.
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
    
    /**
     * Handles an individual HTTP request.
     *
     * @param clientSocket
     * @throws IOException
     */
    private void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(out);
        
        String requestLine = in.readLine();
        if (requestLine == null) {
            return;
        }
        
        System.out.println("Request: " + requestLine);
        
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            sendErrorResponse(writer, out, 400, "Bad Request");
            return;
        }
        
        String method = parts[0];
        String uri = parts[1];
        
        Map<String, String> queryParams = parseQueryParameters(uri);
        String path = uri.split("\\?")[0];
        
        if (!"GET".equals(method)) {
            sendErrorResponse(writer, out, 405, "Method Not Allowed");
            return;
        }
        
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
        }
        
        String response = requestDispatcher.dispatch(path, queryParams);
        if (response != null) {
            sendResponse(writer, out, 200, "text/html", response.getBytes());
            return;
        }
        
        if (serveStaticFile(writer, out, path)) {
            return;
        }
        
        sendErrorResponse(writer, out, 404, "Not Found");
    }
    
    /**
     * Parses query parameters from the URI.
     *
     * @param uri the request URI
     * @return a map of parameter names to values
     */
    private Map<String, String> parseQueryParameters(String uri) {
        Map<String, String> params = new HashMap<>();
        
        if (!uri.contains("?")) {
            return params;
        }
        
        String query = uri.substring(uri.indexOf("?") + 1);
        String[] pairs = query.split("&");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        
        return params;
    }
    
    /**
     * Attempts to serve a static file.
     *
     * @param writer the response writer
     * @param out the output stream
     * @param path the file path
     * @return true if the file was served, false otherwise
     * @throws IOException if an I/O error occurs
     */
    private boolean serveStaticFile(PrintWriter writer, OutputStream out, String path) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }
        
        Path filePath = Paths.get("src/main/resources/static" + path);
        
        if (!Files.exists(filePath)) {
            return false;
        }
        
        String contentType = getContentType(path);
        byte[] content = Files.readAllBytes(filePath);
        
        sendResponse(writer, out, 200, contentType, content);
        return true;
    }
    
    /**
     * Gets the content type for a file based on its extension.
     *
     * @param path the file path
     * @return the content type
     */
    private String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "text/plain";
    }
    
    /**
     * Sends an HTTP response.
     *
     * @param writer the response writer
     * @param out the output stream
     * @param statusCode the HTTP status code
     * @param contentType the content type
     * @param content the response content
     * @throws IOException if an I/O error occurs
     */
    private void sendResponse(PrintWriter writer, OutputStream out, int statusCode, String contentType, byte[] content) throws IOException {
        writer.println("HTTP/1.1 " + statusCode + " OK");
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();
        
        out.write(content);
        out.flush();
    }
    
    /**
     * Sends an HTTP error response.
     *
     * @param writer the response writer
     * @param out the output stream
     * @param statusCode the HTTP status code
     * @param message the error message
     * @throws IOException if an I/O error occurs
     */
    private void sendErrorResponse(PrintWriter writer, OutputStream out, int statusCode, String message) throws IOException {
        String content = "<html><body><h1>" + statusCode + " " + message + "</h1></body></html>";
        sendResponse(writer, out, statusCode, "text/html", content.getBytes());
    }
}
