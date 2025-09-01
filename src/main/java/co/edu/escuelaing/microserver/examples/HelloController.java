package co.edu.escuelaing.microserver.examples;

import co.edu.escuelaing.microserver.annotations.GetMapping;
import co.edu.escuelaing.microserver.annotations.RestController;

/**
 * Simple Hello World controller for testing the framework.
 */
@RestController
public class HelloController {

    /**
     * Returns a simple greeting message.
     *
     * @return greeting message
     */
    @GetMapping("/api")
    public String index() {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Micro WebServer IoC</title>" +
                "<link rel='icon' type='image/x-icon' href='/Pictures/Logo.jpg'>" +
                "<link rel='stylesheet' href='/styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                "<img src='/Pictures/Logo.jpg' alt='Logo' style='max-width: 100px; height: auto;'>" +
                "</div>" +
                "<h1>Micro WebServer IoC</h1>" +
                "<div class='status'>Framework funcionando correctamente</div>" +
                "<p>Servidor web con framework IoC usando Java Reflection y Annotations.</p>" +
                "<div class='service'>" +
                "<h3><a href='/greeting'>Servicio de Saludo</a></h3>" +
                "<p>Demuestra @RequestParam: <a href='/greeting?name=Estudiante'>/greeting?name=Estudiante</a></p>" +
                "</div>" +
                "<div class='service'>" +
                "<h3><a href='/info'>Información del Framework</a></h3>" +
                "<p>Detalles sobre las anotaciones soportadas</p>" +
                "</div>" +
                "<div class='service'>" +
                "<h3><a href='/counter'>Contador de Requests</a></h3>" +
                "<p>Número de solicitudes procesadas</p>" +
                "</div>" +
                "</div>" +
                "<script src='/script.js'></script>" +
                "</body></html>";
    }

    /**
     * Returns information about the framework.
     *
     * @return framework information
     */
    @GetMapping("/info")
    public String info() {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Info Framework</title>" +
                "<link rel='icon' type='image/x-icon' href='/Pictures/Logo.jpg'>" +
                "<link rel='stylesheet' href='/styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Framework Information</h1>" +
                "<p>Construido con Java Reflection y Annotations</p>" +
                "<h3>Características soportadas:</h3>" +
                "<div class='service'>" +
                "<strong>@RestController:</strong> Marca clases como componentes web" +
                "</div>" +
                "<div class='service'>" +
                "<strong>@GetMapping:</strong> Mapea métodos HTTP GET a URIs" +
                "</div>" +
                "<div class='service'>" +
                "<strong>@RequestParam:</strong> Vincula parámetros de consulta" +
                "</div>" +
                "<div class='service'>" +
                "<strong>Archivos Estáticos:</strong> Sirve HTML, CSS, JS y PNG" +
                "</div>" +
                "<p><a href='/'>Inicio</a> | <a href='/greeting'>Saludo</a></p>" +
                "</div>" +
                "</body></html>";
    }
}
