package com.riwi.m2w1;

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para VenueRepository.
 * Misma configuración que EventRepositoryTest (@DataJpaTest + H2 en memoria).
 */
@DataJpaTest
@DisplayName("VenueRepository - Pruebas de integración JPA")
class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    private Venue v1;
    private Venue v2;
    private Venue v3;

    @BeforeEach
    void setUp() {
        v1 = venueRepository.save(new Venue(null, "Teatro Principal",      "Cra. 65 #8B-91",  500));
        v2 = venueRepository.save(new Venue(null, "Centro de Convenciones","Cl. 16 #55-129",  1000));
        v3 = venueRepository.save(new Venue(null, "Teatro Metropolitano",  "Cl. 41 #57-30",   1500));
    }

    @Test
    @DisplayName("save() debe asignar ID automático")
    void shouldGenerateId() {
        assertThat(v1.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @DisplayName("findById() debe retornar el venue correcto")
    void shouldFindById() {
        Optional<Venue> found = venueRepository.findById(v2.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Centro de Convenciones");
    }

    @Test
    @DisplayName("existsById() debe retornar true para ID existente")
    void shouldConfirmExistence() {
        assertThat(venueRepository.existsById(v1.getId())).isTrue();
        assertThat(venueRepository.existsById(9999L)).isFalse();
    }

    @Test
    @DisplayName("deleteById() debe eliminar el venue y contar correctamente")
    void shouldDeleteVenue() {
        venueRepository.deleteById(v1.getId());

        assertThat(venueRepository.existsById(v1.getId())).isFalse();
        assertThat(venueRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("findAll(Pageable) debe paginar con metadatos correctos")
    void shouldPaginateVenues() {
        Page<Venue> page = venueRepository.findAll(PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0); // página actual = 0
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase() debe encontrar venues que contengan 'teatro'")
    void shouldFindVenuesByNameFragment() {
        Page<Venue> result = venueRepository.findByNameContainingIgnoreCase(
                "teatro", PageRequest.of(0, 10, Sort.by("name")));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Venue::getName)
                .containsExactlyInAnyOrder("Teatro Principal", "Teatro Metropolitano");
    }
}
