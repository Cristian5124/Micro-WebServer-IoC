package co.edu.escuelaing.microserver.ioc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import co.edu.escuelaing.microserver.examples.GreetingController;
import co.edu.escuelaing.microserver.examples.HelloController;

/**
 * Unit tests for the SimpleIoCContainer.
 */
public class SimpleIoCContainerTest {
    
    private SimpleIoCContainer container;
    
    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        container = new SimpleIoCContainer();
    }
    
    /**
     * Tests controller registration and method dispatch.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testControllerRegistration() throws Exception {
        container.registerController(HelloController.class);
        
        Map<String, String> params = new HashMap<>();
        String response = container.dispatch("/api", params);
        
        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain Micro WebServer", response.contains("Micro WebServer"));
    }
    
    /**
     * Tests request parameter handling.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testRequestParameters() throws Exception {
        container.registerController(GreetingController.class);
        
        Map<String, String> params = new HashMap<>();
        params.put("name", "TestUser");
        
        String response = container.dispatch("/greeting", params);
        
        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain name", response.contains("TestUser"));
    }
    
    /**
     * Tests default parameter values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testDefaultParameters() throws Exception {
        container.registerController(GreetingController.class);
        
        Map<String, String> params = new HashMap<>();
        String response = container.dispatch("/greeting", params);
        
        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain default value", response.contains("World"));
    }
    
    /**
     * Tests non-existent path handling.
     */
    @Test
    public void testNonExistentPath() {
        Map<String, String> params = new HashMap<>();
        String response = container.dispatch("/nonexistent", params);
        
        assertNull("Response should be null for non-existent path", response);
    }
}
