package application;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("webair")
public class RestApp extends ResourceConfig {

    // Change webair.sqlite path here
    public static String DB_PATH = "C:\\Users\\Nikola\\Documents\\IntelliJ projects\\Web programming - H7\\web\\webair.sqlite";

    public RestApp() {
        packages("application.ticket", "application.company");
    }
}