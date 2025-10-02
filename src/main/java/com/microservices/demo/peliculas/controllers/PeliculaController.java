package com.microservices.demo.peliculas.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.microservices.demo.peliculas.entities.Genero;
import com.microservices.demo.peliculas.entities.Pelicula;
import com.microservices.demo.peliculas.services.IGeneroService;
import com.microservices.demo.peliculas.services.PeliculaService;
import com.microservices.demo.peliculas.services.IActorService;

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
    public String guardar(@ModelAttribute Pelicula pelicula, Model model) {
        // Guardar la película
        peliculaService.save(pelicula);
        return "redirect:/pelicula";
    }
    
    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula == null) {
            pelicula = new Pelicula();
        }
        
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Editar Película");
        
        // Cargar géneros para el select
        List<Genero> generos = generoService.findAll();
        model.addAttribute("generos", generos);
        
        return "pelicula";
    }
    
    @GetMapping("/")
    public String home() {
        return "home";  
    }
    
    @GetMapping("/home")
    public String homeAlternative() {
        return "home";  
    }
}