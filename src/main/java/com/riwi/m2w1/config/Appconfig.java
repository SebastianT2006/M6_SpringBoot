package com.riwi.m2w1.config;



import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.repositories.EventRepository;
import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.VenueRepository;



// mplementar una clase `@Configuration` que utilice la anotación `@Bean` para cargar datos iniciales (Seeders) en el arranque de la aplicación.
@Configuration
public class Appconfig {
    @Bean
    public CommandLineRunner seedData(EventRepository eventRepository) {
        return args -> {
            eventRepository.getAllEvents().add(new Event(1, "Concierto de Rock", "2024-07-15", "Un concierto de rock con bandas locales."));
            eventRepository.getAllEvents().add(new Event(2, "Feria de Comida", "2024-08-20", "Una feria gastronómica con comida de todo el mundo."));
            eventRepository.getAllEvents().add(new Event(3, "Exposición de Arte", "2024-09-10", "Una exposición de arte contemporáneo con artistas emergentes."));
        };
    }

    @Bean
    public CommandLineRunner seedVenues(VenueRepository venueRepository) {
        return args -> {
            venueRepository.getAllVenues().add(new Venue(1, "Teatro Principal", "Cra. 65 #8B-91, Guayabal, Medellín, Guayabal, Medellín, Antioquia", 500));
            venueRepository.getAllVenues().add(new Venue(2, "Centro de Convenciones", "Cl. 16 #55-129, Guayabal, Medellín, Guayabal, Medellín, Antioquia.", 1000));
            venueRepository.getAllVenues().add(new Venue(3, "Parque Central", "Cl 10 #50-267, Guayabal, Medellín, Guayabal, Medellín, Antioquia.", 2000));
        };
    }

}
