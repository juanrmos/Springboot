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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microservices.demo.peliculas.entities.Pelicula;
import com.microservices.demo.peliculas.services.IActorService;
import com.microservices.demo.peliculas.services.IArchivoService;
import com.microservices.demo.peliculas.services.IGeneroService;
import com.microservices.demo.peliculas.services.PeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculaController {

    private final PeliculaService peliculaService;
    private final IGeneroService generoService;
    private final IActorService actorService;
    private final IArchivoService archivoService;
    
    public PeliculaController(PeliculaService peliculaService, IGeneroService generoService, 
                            IActorService actorService, IArchivoService archivoService) {
        this.peliculaService = peliculaService;
        this.generoService = generoService;
        this.actorService = actorService;
        this.archivoService = archivoService;
    }

    @GetMapping({"/", "/home", "/index"})
    public String home(Model model) {
        model.addAttribute("peliculas", peliculaService.findAll());
        model.addAttribute("titulo", "Catálogo de Películas");
        model.addAttribute("msj", "Catálogo actualizado a 2025");
        model.addAttribute("tipoMsj", "success");
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        return "home";
    }

    // NUEVA FUNCIONALIDAD: Búsqueda de películas
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "generoId", required = false) Long generoId,
            @RequestParam(name = "actorId", required = false) Long actorId,
            Model model) {
        
        List<Pelicula> peliculas;
        
        // Si no hay parámetros de búsqueda, mostrar todas
        if ((nombre == null || nombre.trim().isEmpty()) && generoId == null && actorId == null) {
            peliculas = peliculaService.findAll();
            model.addAttribute("msj", "Mostrando todas las películas");
        } else {
            peliculas = peliculaService.searchPeliculas(nombre, generoId, actorId);
            
            // Construir mensaje de búsqueda
            StringBuilder mensaje = new StringBuilder("Resultados de búsqueda");
            if (nombre != null && !nombre.trim().isEmpty()) {
                mensaje.append(" - Nombre: '").append(nombre).append("'");
            }
            if (generoId != null) {
                mensaje.append(" - Género seleccionado");
            }
            if (actorId != null) {
                mensaje.append(" - Actor seleccionado");
            }
            
            model.addAttribute("msj", mensaje.toString() + " (" + peliculas.size() + " resultados)");
        }
        
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("titulo", "Búsqueda de Películas");
        model.addAttribute("tipoMsj", "info");
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        
        // Mantener valores de búsqueda en el formulario
        model.addAttribute("nombreBusqueda", nombre);
        model.addAttribute("generoIdBusqueda", generoId);
        model.addAttribute("actorIdBusqueda", actorId);
        
        return "home";
    }

    // NUEVA FUNCIONALIDAD: Filtro por año
    @GetMapping("/filtrar-por-ano")
    public String filtrarPorAno(
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "startYear", required = false) Integer startYear,
            @RequestParam(name = "endYear", required = false) Integer endYear,
            Model model) {
        
        List<Pelicula> peliculas;
        String mensaje;
        
        if (year != null) {
            // Filtrar por un año específico
            peliculas = peliculaService.findByYear(year);
            mensaje = "Películas estrenadas en " + year + " (" + peliculas.size() + " resultados)";
        } else if (startYear != null && endYear != null) {
            // Filtrar por rango de años
            peliculas = peliculaService.findByYearRange(startYear, endYear);
            mensaje = "Películas estrenadas entre " + startYear + " y " + endYear + 
                     " (" + peliculas.size() + " resultados)";
        } else {
            // Sin filtro, mostrar todas
            peliculas = peliculaService.findAll();
            mensaje = "Mostrando todas las películas";
        }
        
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("titulo", "Películas por Año");
        model.addAttribute("msj", mensaje);
        model.addAttribute("tipoMsj", "info");
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        
        // Mantener valores del filtro
        model.addAttribute("yearFiltro", year);
        model.addAttribute("startYearFiltro", startYear);
        model.addAttribute("endYearFiltro", endYear);
        
        return "filtro-ano";
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("peliculas", peliculaService.findAll());
        model.addAttribute("titulo", "Administrar Películas");
        return "listado";
    }

    @GetMapping("/pelicula")
    public String crear(Model model) {
        Pelicula pelicula = new Pelicula();
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Nueva Película");
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        return "pelicula";
    }
    
    @PostMapping("/pelicula")
    public String guardar(@Valid @ModelAttribute Pelicula pelicula,
                        BindingResult result,
                        @RequestParam(name="idActores", required = false) String idActores,
                        @RequestParam(name="file", required = false) MultipartFile file,
                        Model model,
                        RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", pelicula.getId() != null ? "Editar Película" : "Nueva Película");
            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("actores", actorService.findAll());
            return "pelicula";
        }

        // Procesar actores
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
                flash.addFlashAttribute("error", "Error al procesar los actores seleccionados");
                return "redirect:/pelicula";
            }
        } else {
            pelicula.setProtagonistas(Collections.emptyList());
        }

        // Procesar imagen
        if (file != null && !file.isEmpty()) {
            try {
                // Si está editando y tiene imagen anterior, eliminarla
                if (pelicula.getId() != null) {
                    Pelicula peliculaExistente = peliculaService.findById(pelicula.getId());
                    if (peliculaExistente != null && peliculaExistente.getImagenUrl() != null) {
                        archivoService.eliminar(peliculaExistente.getImagenUrl());
                    }
                }
                
                String nombreArchivo = archivoService.guardar(file);
                pelicula.setImagenUrl(nombreArchivo);
            } catch (Exception e) {
                flash.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
                return "redirect:/pelicula";
            }
        } else {
            // Si no se sube imagen y es nueva, asignar imagen por defecto
            if (pelicula.getId() == null) {
                pelicula.setImagenUrl("_default.jpg");
            } else {
                // Si es edición, mantener la imagen existente
                Pelicula peliculaExistente = peliculaService.findById(pelicula.getId());
                if (peliculaExistente != null) {
                    pelicula.setImagenUrl(peliculaExistente.getImagenUrl());
                }
            }
        }

        try {
            peliculaService.save(pelicula);
            flash.addFlashAttribute("success", pelicula.getId() != null ? 
                "Película actualizada correctamente" : "Película guardada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la película: " + e.getMessage());
            return "redirect:/pelicula";
        }

        return "redirect:/listado";
    }
    
    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula == null) {
            return "redirect:/listado";
        }
        
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("titulo", "Editar Película");
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        
        return "pelicula";
    }

    @GetMapping("/pelicula/eliminar/{id}")
    public String eliminar(@PathVariable(name = "id") Long id, RedirectAttributes flash) {
        Pelicula pelicula = peliculaService.findById(id);
        
        if (pelicula != null) {
            // Eliminar imagen si no es la por defecto
            if (pelicula.getImagenUrl() != null && !pelicula.getImagenUrl().equals("_default.jpg")) {
                archivoService.eliminar(pelicula.getImagenUrl());
            }
            
            peliculaService.delete(id);
            flash.addFlashAttribute("success", "Película eliminada correctamente");
        } else {
            flash.addFlashAttribute("error", "La película no existe");
        }
        
        return "redirect:/listado";
    }
}