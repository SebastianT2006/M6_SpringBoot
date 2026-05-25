package com.riwi.m2w1.controllers.admin;

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.service.VenueService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador MVC de VISTA para Venues.
 *
 * GET  /admin/venues       → listado (admin/venues/list)
 * GET  /admin/venues/new   → formulario vacío (admin/venues/form)
 * POST /admin/venues/save  → guardar y redirigir (PRG)
 */
@Controller
@RequestMapping("/admin/venues")
public class AdminVenueController {

    private final VenueService venueService;

    public AdminVenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public String listVenues(Model model) {
        List<Venue> venues = venueService
                .findAll(PageRequest.of(0, 100, Sort.by("id").ascending()))
                .getContent();
        model.addAttribute("venues", venues);
        return "admin/venues/list";
    }

    @GetMapping("/new")
    public String newVenueForm(Model model) {
        model.addAttribute("venue", new Venue());
        return "admin/venues/form";
    }

    @PostMapping("/save")
    public String saveVenue(@ModelAttribute Venue venue) {
        venueService.createVenue(venue);
        return "redirect:/admin/venues";
    }
}
