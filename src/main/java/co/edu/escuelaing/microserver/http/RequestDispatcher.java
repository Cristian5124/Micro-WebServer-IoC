package co.edu.escuelaing.microserver.http;

import java.util.Map;

/**
 * Interface for dispatching HTTP requests to appropriate handlers.
 */
public interface RequestDispatcher {
    
    /**
     * Dispatches a request to the appropriate handler.
     *
     * @param path the request path
     * @param queryParams the query parameters
     * @return the response content, or null if no handler found
     */
    String dispatch(String path, Map<String, String> queryParams);
}
