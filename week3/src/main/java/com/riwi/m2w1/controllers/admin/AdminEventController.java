package com.riwi.m2w1.controllers.admin;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador MVC de VISTA para Eventos.
 *
 * GET  /admin/events       → listado (admin/events/list)
 * GET  /admin/events/new   → formulario vacío (admin/events/form)
 * POST /admin/events/save  → guardar y redirigir (PRG)
 */
@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String listEvents(Model model) {
        List<Event> events = eventService
                .findAll(PageRequest.of(0, 100, Sort.by("id").ascending()))
                .getContent();
        model.addAttribute("events", events);
        return "admin/events/list";
    }

    @GetMapping("/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/events/form";
    }

    /**
     * Patrón Post-Redirect-Get: guarda y redirige al listado.
     * Evita el reenvío del formulario al refrescar la página.
     */
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/admin/events";
    }
}
