package com.microservices.demo.peliculas.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservices.demo.peliculas.entities.Genero;
import com.microservices.demo.peliculas.entities.Pelicula;
import com.microservices.demo.peliculas.services.IActorService;
import com.microservices.demo.peliculas.services.IGeneroService;
import com.microservices.demo.peliculas.services.PeliculaService;

import jakarta.validation.Valid;


@Controller
public class PeliculaController {


    private final PeliculaService peliculaService;
    private final IGeneroService generoService;
    private final IActorService actorService;
    
    public PeliculaController(PeliculaService peliculaService, IGeneroService generoService, IActorService actorService) {
        this.peliculaService = peliculaService;
        this.generoService = generoService;
        this.actorService = actorService;

    }

    @GetMapping({"/", "/home", "/index"})
    public String home(Model model) {
        model.addAttribute("peliculas", peliculaService.findAll());
        model.addAttribute("msj", "Catalago actualizado a 2025");
        model.addAttribute("tipoMsj", "success");
        return "home";  // Redirige directamente a pelicula.html
    }

    @GetMapping("/pelicula")
    public String crear(Model model) {
        Pelicula pelicula = new Pelicula();
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Nueva Película");
        
        List<Genero> generos = generoService.findAll();
        model.addAttribute("generos", generos);
        model.addAttribute("actores", actorService.findAll());
        
        return "pelicula";
    }
    
    @PostMapping("/pelicula")
    public String guardar(@Valid @ModelAttribute Pelicula pelicula,
                        BindingResult result,
                        @RequestParam(name="idActores", required = false) String idActores,
                        Model model) {

        // Validar errores ANTES de procesar
        if (result.hasErrors()) {
            model.addAttribute("titulo", pelicula.getId() != null ? "Editar Película" : "Nueva Película");
            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("actores", actorService.findAll());
            
            // Debug: Mostrar errores en consola
            result.getAllErrors().forEach(error -> {
                System.out.println("Error de validación: " + error.getDefaultMessage());
            });
            
            return "pelicula";
        }

        // Procesar actores seleccionados
        if (idActores != null && !idActores.trim().isEmpty()) {
            try {
                List<Long> ids = Arrays.stream(idActores.split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .map(Long::parseLong)
                                    .collect(Collectors.toList());

                if (!ids.isEmpty()) {
                    pelicula.setProtagonistas(actorService.findAllById(ids));
                } else {
                    pelicula.setProtagonistas(Collections.emptyList());
                }
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Error al procesar los actores seleccionados");
                model.addAttribute("titulo", pelicula.getId() != null ? "Editar Película" : "Nueva Película");
                model.addAttribute("generos", generoService.findAll());
                model.addAttribute("actores", actorService.findAll());
                return "pelicula";
            }
        } else {
            pelicula.setProtagonistas(Collections.emptyList());
        }

        // Guardar la película
        try {
            peliculaService.save(pelicula);
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la película: " + e.getMessage());
            model.addAttribute("titulo", pelicula.getId() != null ? "Editar Película" : "Nueva Película");
            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("actores", actorService.findAll());
            return "pelicula";
        }

        return "redirect:/pelicula";
    }
    
    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula == null) {
            return "redirect:/home";
        }
        
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Editar Película");
        
        List<Genero> generos = generoService.findAll();
        model.addAttribute("generos", generos);
        model.addAttribute("actores", actorService.findAll());
        
        return "pelicula";
    }
}