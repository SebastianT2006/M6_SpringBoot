package com.riwi.m2w1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: verifica que el contexto de Spring cargue correctamente.
 * @SpringBootTest levanta el contexto completo con H2.
 */
@SpringBootTest
class M2w1ApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto arranca sin errores, el test pasa
    }
}
