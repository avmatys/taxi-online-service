package by.bsuir.matys_rozin_zarudny;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import by.bsuir.matys_rozin_zarudny.layer.controllers.rest.AccountController;
import by.bsuir.matys_rozin_zarudny.layer.controllers.rest.AuthenticationController;
import by.bsuir.matys_rozin_zarudny.layer.controllers.rest.BookingController;
import by.bsuir.matys_rozin_zarudny.layer.controllers.rest.TaxiController;
import by.bsuir.matys_rozin_zarudny.layer.security.AuthenticationFilter;

/**
 * Application configuration.
*/
@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        // RESTful Controllers
        register(AccountController.class);
        register(AuthenticationController.class);
        register(BookingController.class);
        register(TaxiController.class);
      
        
        // Filters        
        register(RolesAllowedDynamicFeature.class);
        register(AuthenticationFilter.class);
       
        
    }
}
