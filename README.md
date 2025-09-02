# 🚀 Micro WebServer IoC - AREP

Un servidor web ligero con framework IoC construido en Java utilizando reflexión y anotaciones personalizadas. Implementa capacidades similares a Spring Boot pero de manera minimalista y educativa.

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)

## Características

**Servidor HTTP**: Implementación sin dependencias externas

**Framework IoC**: Contenedor de inversión de control usando Java Reflection API

**Anotaciones Personalizadas**: `@RestController`, `@GetMapping`, `@RequestParam`

**Descubrimiento Automático**: Escaneo automático de componentes en el classpath

**Servicio de Archivos Estáticos**: HTML, CSS, JavaScript, imágenes PNG y JPEG

**Parámetros de Consulta**: Soporte completo con valores por defecto

**Inyección de Dependencias**: Carga dinámica de POJOs como controladores

## Arquitectura

El framework sigue una arquitectura modular con clara separación de responsabilidades:

```
┌─────────────────────┐    ┌─────────────────┐    ┌──────────────────┐
│  MicroWebServerIoC  │───▶│   HttpServer   │───▶│ RequestDispatcher│
│   (Entry Point)     │    │ (Socket Layer)  │    │ (Route Handling) │
└─────────────────────┘    └─────────────────┘    └──────────────────┘
                                    │
                                    ▼
                          ┌─────────────────-┐    ┌──────────────────┐
                          │SimpleIoCContainer│───▶│   Annotations   │
                          │ (IoC Framework)  │    │ (@RestController)│
                          └─────────────────-┘    └──────────────────┘
```

## Inicio Rápido

### Prerrequisitos

Antes de ejecutar el proyecto, asegúrate de tener:

- **Java 11+** - [Descargar aquí](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven 3.8+** - [Descargar aquí](https://maven.apache.org/)

Verifica tu instalación:

```cmd
java -version
mvn -version
```

### Instalación y Ejecución

1. **Construir el proyecto**:
   ```cmd
   mvn clean compile package
   ```

2. **Ejecutar con controlador específico**:
   ```cmd
   java -cp target/classes co.edu.escuelaing.microserver.MicroWebServerIoC co.edu.escuelaing.microserver.examples.HelloController
   ```

3. **Ejecutar con descubrimiento automático**:
   ```cmd
   java -cp target/classes co.edu.escuelaing.microserver.MicroWebServerIoC
   ```

4. **Usar Maven exec plugin**:
   ```cmd
   mvn exec:java
   ```

5. **Abrir el navegador** y navegar a:
   ```
   http://localhost:8080
   ```

## Ejemplos de Uso

### Controlador Básico

```java
@RestController
public class HelloController {
    
    @GetMapping("/")
    public String index() {
        return "Greetings from Micro WebServer IoC!";
    }
    
    @GetMapping("/api")
    public String api() {
        return "Welcome to our API!";
    }
}
```

### Controlador con Parámetros

```java
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name + "! Request #" + counter.incrementAndGet();
    }
    
    @GetMapping("/info")
    public String info(@RequestParam(value = "format", defaultValue = "text") String format) {
        return "Framework: Micro WebServer IoC | Format: " + format;
    }
}
```

## API Endpoints

El framework viene con varios endpoints preconfigurados para demostración:

| Método | Endpoint | Descripción | Ejemplo |
|--------|----------|-------------|---------|
| GET | `/` | Página principal del framework | `http://localhost:8080/` |
| GET | `/api` | Interfaz de API principal | `http://localhost:8080/api` |
| GET | `/greeting` | Servicio de saludo básico | `http://localhost:8080/greeting` |
| GET | `/greeting?name=Cristian` | Saludo personalizado | `http://localhost:8080/greeting?name=Cristian` |
| GET | `/info` | Información del framework | `http://localhost:8080/info` |
| GET | `/counter` | Contador de requests | `http://localhost:8080/counter` |
| GET | `/static/*` | Archivos estáticos | `http://localhost:8080/test.html` |

## Testing

### Ejecutar Tests Unitarios

Ejecuta la suite de tests:

```cmd
mvn test
```

### Testing Manual con Navegador

1. Inicia el servidor
2. Navega a `http://localhost:8080`
3. Usa la interfaz interactiva para probar todos los endpoints
4. Revisa la consola del desarrollador para respuestas detalladas

### Testing con Postman

Importa los siguientes endpoints en Postman:

**Requests GET:**
```
GET http://localhost:8080/
GET http://localhost:8080/api
GET http://localhost:8080/greeting
GET http://localhost:8080/greeting?name=Cristian
GET http://localhost:8080/info
GET http://localhost:8080/counter
```

## Estructura del Proyecto

```
Micro-WebServer-IoC/
├── src/
│   ├── main/
│   │   ├── java/co/edu/escuelaing/microserver/
│   │   │   ├── MicroWebServerIoC.java      # Clase principal
│   │   │   ├── annotations/                # Anotaciones personalizadas
│   │   │   │   ├── RestController.java     # Marca controladores
│   │   │   │   ├── GetMapping.java         # Mapeo de rutas GET
│   │   │   │   └── RequestParam.java       # Parámetros de request
│   │   │   ├── http/                       # Servidor HTTP
│   │   │   │   ├── HttpServer.java         # Servidor base
│   │   │   │   └── RequestDispatcher.java  # Despachador de requests
│   │   │   ├── ioc/                        # Framework IoC
│   │   │   │   └── SimpleIoCContainer.java # Contenedor IoC
│   │   │   └── examples/                   # Controladores de ejemplo
│   │   │       ├── HelloController.java    # Controlador simple
│   │   │       └── GreetingController.java # Controlador con parámetros
│   │   └── resources/
│   │       └── static/                     # Archivos web estáticos
│   │           ├── index.html              # Página principal
│   │           ├── script.js               # JavaScript
│   │           ├── styles.css              # Estilos CSS
│   │           └── Pictures/               # Imágenes
│   └── test/
│       └── java/                           # Tests unitarios
├── target/                                 # Clases compiladas
├── pom.xml                                # Configuración Maven
└── README.md                              # Este archivo
```

## Tecnologías Utilizadas

**[Java 11+](https://openjdk.org/)** - Lenguaje de programación con características modernas

**[Maven](https://maven.apache.org/)** - Gestión de dependencias y automatización de build

**[JUnit 4](https://junit.org/junit4/)** - Framework de testing unitario

**Java Reflection API** - Para descubrimiento dinámico de componentes

**Java Annotations** - Para configuración declarativa

**Socket Programming** - Para implementación del servidor HTTP

## Despliegue

### Desarrollo Local
```cmd
mvn exec:java -Dexec.mainClass="co.edu.escuelaing.microserver.MicroWebServerIoC"
```

### Empaquetado para Producción
```cmd
REM Construir el JAR
mvn clean package

REM Ejecutar el servidor
java -cp target/micro-webserver-ioc-1.0.0.jar co.edu.escuelaing.microserver.MicroWebServerIoC
```

## Configuración

El framework puede ser personalizado:

```java
// Cambiar paquete de escaneo de controladores
container.scanAndRegisterControllers("com.mi.paquete.controllers");

// Registrar controlador específico
container.registerController("com.mi.paquete.MiControlador");

// Archivos estáticos se sirven desde src/main/resources/static/
```

## Patrones de Diseño Implementados

**Dependency Injection**: Framework IoC para gestión automática de dependencias

**Single Responsibility**: Cada clase tiene una responsabilidad específica

**Factory Pattern**: Creación dinámica de instancias de controladores

**Strategy Pattern**: Dispatching de requests a handlers apropiados

**Observer Pattern**: Detección automática de componentes anotados

## Uso de Java Reflection

El framework hace uso extensivo de la API de Reflection de Java:

**Escaneo de clases**: Búsqueda automática de clases anotadas

**Introspección de métodos**: Análisis de métodos anotados con `@GetMapping`

**Invocación dinámica**: Llamada de métodos de controlador en tiempo de ejecución

**Análisis de parámetros**: Procesamiento automático de `@RequestParam`

## Desarrollo de Nuevos Controladores

Para agregar nuevos controladores:

1. **Crear una clase** anotada con `@RestController`:
   ```java
   @RestController
   public class MiControlador {
   }
   ```

2. **Agregar métodos** anotados con `@GetMapping`:
   ```java
   @GetMapping("/mi-ruta")
   public String miMetodo() {
       return "Mi respuesta";
   }
   ```

3. **Usar parámetros** con `@RequestParam`:
   ```java
   @GetMapping("/parametros")
   public String conParametros(@RequestParam(value = "param", defaultValue = "default") String param) {
       return "Parámetro recibido: " + param;
   }
   ```

4. **El framework los descubrirá automáticamente** al arrancar


## Razón de Ser

Este proyecto fue desarrollado como parte del curso AREP (Arquitecturas Empresariales) para demostrar:

Uso avanzado de Java Reflection API
Implementación de anotaciones personalizadas
Creación de un framework IoC básico
Patrones de diseño empresariales
Programación con sockets a bajo nivel

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Autor

**Cristian David Polo Garrido** - Desarrollador - [GitHub](https://github.com/Cristian5124)
