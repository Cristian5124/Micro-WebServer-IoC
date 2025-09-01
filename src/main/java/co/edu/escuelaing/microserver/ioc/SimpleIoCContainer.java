package co.edu.escuelaing.microserver.ioc;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.escuelaing.microserver.annotations.GetMapping;
import co.edu.escuelaing.microserver.annotations.RequestParam;
import co.edu.escuelaing.microserver.annotations.RestController;
import co.edu.escuelaing.microserver.http.RequestDispatcher;

/**
 * Simple IoC container that manages REST controllers and handles method
 * invocation.
 * Uses Java reflection to discover and register components.
 */
public class SimpleIoCContainer implements RequestDispatcher {

    private final Map<String, ControllerMethod> mappings;
    private final Map<Class<?>, Object> instances;

    /**
     * Creates a new IoC container.
     */
    public SimpleIoCContainer() {
        this.mappings = new HashMap<>();
        this.instances = new HashMap<>();
    }

    /**
     * Registers a controller class by analyzing its annotations.
     *
     * @param controllerClass the controller class to register
     * @throws Exception if registration fails
     */
    public void registerController(Class<?> controllerClass) throws Exception {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("Class must be annotated with @RestController");
        }

        Object instance = controllerClass.getDeclaredConstructor().newInstance();
        instances.put(controllerClass, instance);

        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                String path = mapping.value();

                ControllerMethod controllerMethod = new ControllerMethod(instance, method);
                mappings.put(path, controllerMethod);

                System.out.println("Registered mapping: " + path + " -> " +
                        controllerClass.getSimpleName() + "." + method.getName());
            }
        }
    }

    /**
     * Registers a controller by class name.
     *
     * @param className the fully qualified class name
     * @throws Exception if registration fails
     */
    public void registerController(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        registerController(clazz);
    }

    /**
     * Scans the classpath for classes annotated with @RestController and registers
     * them.
     *
     * @param packageName the package to scan
     * @throws Exception if scanning fails
     */
    public void scanAndRegisterControllers(String packageName) throws Exception {
        List<Class<?>> classes = findClassesInPackage(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RestController.class)) {
                registerController(clazz);
            }
        }
    }

    /**
     * Finds all classes in a package using reflection.
     *
     * @param packageName the package name to scan
     * @return list of classes found
     * @throws Exception if scanning fails
     */
    private List<Class<?>> findClassesInPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource != null) {
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                findClassesInDirectory(directory, packageName, classes);
            }
        }

        return classes;
    }

    /**
     * Recursively finds classes in a directory.
     *
     * @param directory   the directory to search
     * @param packageName the package name
     * @param classes     the list to add found classes to
     * @throws Exception if class loading fails
     */
    private void findClassesInDirectory(File directory, String packageName, List<Class<?>> classes) throws Exception {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                }
            }
        }
    }

    @Override
    public String dispatch(String path, Map<String, String> queryParams) {
        ControllerMethod controllerMethod = mappings.get(path);
        if (controllerMethod == null) {
            return null;
        }

        try {
            Object result = invokeMethod(controllerMethod, queryParams);
            return result != null ? result.toString() : "";
        } catch (Exception e) {
            System.err.println("Error invoking method: " + e.getMessage());
            return "Internal Server Error";
        }
    }

    /**
     * Invokes a controller method with proper parameter binding.
     *
     * @param controllerMethod the method to invoke
     * @param queryParams      the query parameters
     * @return the method result
     * @throws Exception if invocation fails
     */
    private Object invokeMethod(ControllerMethod controllerMethod, Map<String, String> queryParams) throws Exception {
        Method method = controllerMethod.getMethod();
        Object instance = controllerMethod.getInstance();

        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = param.getAnnotation(RequestParam.class);
                String paramName = requestParam.value();
                String defaultValue = requestParam.defaultValue();

                String value = queryParams.get(paramName);
                if (value == null && !defaultValue.isEmpty()) {
                    value = defaultValue;
                }

                args[i] = value;
            } else {
                args[i] = null;
            }
        }

        return method.invoke(instance, args);
    }

    /**
     * Inner class to hold controller method information.
     */
    private static class ControllerMethod {
        private final Object instance;
        private final Method method;

        /**
         * Creates a new controller method.
         *
         * @param instance the controller instance
         * @param method   the method to invoke
         */
        public ControllerMethod(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }

        /**
         * Gets the controller instance.
         *
         * @return the instance
         */
        public Object getInstance() {
            return instance;
        }

        /**
         * Gets the method to invoke.
         *
         * @return the method
         */
        public Method getMethod() {
            return method;
        }
    }
}
