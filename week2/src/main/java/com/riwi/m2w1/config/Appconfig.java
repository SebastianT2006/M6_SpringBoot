package com.riwi.m2w1.config;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.EventRepository;
import com.riwi.m2w1.repositories.VenueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeder de datos iniciales.
 *
 * Solo inserta datos si las tablas están vacías (idempotente).
 * Con H2 persistente, tras el primer arranque ya no vuelve a insertar.
 */
@Configuration
public class Appconfig {

    @Bean
    public CommandLineRunner seedEvents(EventRepository eventRepository) {
        return args -> {
            // Solo sembrar si la tabla está vacía
            if (eventRepository.count() == 0) {
                eventRepository.save(new Event(null, "Concierto de Rock",    "2024-07-15", "Un concierto de rock con bandas locales."));
                eventRepository.save(new Event(null, "Feria de Comida",      "2024-08-20", "Una feria gastronómica con comida de todo el mundo."));
                eventRepository.save(new Event(null, "Exposición de Arte",   "2024-09-10", "Una exposición de arte contemporáneo con artistas emergentes."));
                System.out.println("✅ Eventos de prueba insertados.");
            }
        };
    }

    @Bean
    public CommandLineRunner seedVenues(VenueRepository venueRepository) {
        return args -> {
            if (venueRepository.count() == 0) {
                venueRepository.save(new Venue(null, "Teatro Principal",      "Cra. 65 #8B-91, Guayabal, Medellín",  500));
                venueRepository.save(new Venue(null, "Centro de Convenciones","Cl. 16 #55-129, Guayabal, Medellín",  1000));
                venueRepository.save(new Venue(null, "Parque Central",        "Cl 10 #50-267, Guayabal, Medellín",   2000));
                System.out.println("✅ Venues de prueba insertados.");
            }
        };
    }
}
