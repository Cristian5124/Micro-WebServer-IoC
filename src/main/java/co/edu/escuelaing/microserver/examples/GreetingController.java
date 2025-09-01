package co.edu.escuelaing.microserver.examples;

import java.util.concurrent.atomic.AtomicLong;

import co.edu.escuelaing.microserver.annotations.GetMapping;
import co.edu.escuelaing.microserver.annotations.RequestParam;
import co.edu.escuelaing.microserver.annotations.RestController;

/**
 * Greeting controller that demonstrates @RequestParam usage.
 */
@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    /**
     * Returns a personalized greeting message.
     *
     * @param name the name to greet
     * @return personalized greeting
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        long count = counter.incrementAndGet();
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Saludo</title>" +
                "<link rel='icon' type='image/x-icon' href='/Pictures/Logo.jpg'>" +
                "<link rel='stylesheet' href='/styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Servicio de Saludo</h1>" +
                "<p><strong>¡Hola " + name + "!</strong></p>" +
                "<p>Request #" + count + "</p>" +
                "<form onsubmit='greetUser(); return false;'>" +
                "<input type='text' id='nameInput' placeholder='Tu nombre' value='" + name + "'>" +
                "<button type='submit'>Saludar</button>" +
                "</form>" +
                "<p><a href='/'>Inicio</a> | <a href='/counter'>Contador</a></p>" +
                "</div>" +
                "<script>" +
                "function greetUser() {" +
                "const name = document.getElementById('nameInput').value || 'World';" +
                "window.location.href = '/greeting?name=' + name;" +
                "}" +
                "</script>" +
                "</body></html>";
    }

    /**
     * Returns the current counter value.
     *
     * @return counter information
     */
    @GetMapping("/counter")
    public String counter() {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Contador</title>" +
                "<link rel='icon' type='image/x-icon' href='/Pictures/Logo.jpg'>" +
                "<link rel='stylesheet' href='/styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Contador de Requests</h1>" +
                "<p>Total de solicitudes: <strong>" + counter.get() + "</strong></p>" +
                "<p><a href='javascript:location.reload()'>Actualizar</a> | <a href='/'>Inicio</a></p>" +
                "</div>" +
                "</body></html>";
    }
}
