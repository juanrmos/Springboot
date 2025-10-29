package com.microservices.demo.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.demo.peliculas.dao.IPeliculaRepository;
import com.microservices.demo.peliculas.entities.Pelicula;

@Service
public class PeliculaService implements IPeliculaService {

    @Autowired
    private IPeliculaRepository repo;

    @Override
    public void save(Pelicula pelicula) {
        repo.save(pelicula);
    }

    @Override
    public Pelicula findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Pelicula> findAll() {
        return (List<Pelicula>) repo.findAll();
    }

    // Implementación de nuevos métodos de búsqueda y filtrado
    
    @Override
    public List<Pelicula> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    public List<Pelicula> findByGenero(Long generoId) {
        if (generoId == null) {
            return findAll();
        }
        return repo.findByGeneroId(generoId);
    }

    @Override
    public List<Pelicula> findByYear(int year) {
        return repo.findByYear(year);
    }

    @Override
    public List<Pelicula> findByYearRange(int startYear, int endYear) {
        return repo.findByYearRange(startYear, endYear);
    }

    @Override
    public List<Pelicula> findByActor(Long actorId) {
        if (actorId == null) {
            return findAll();
        }
        return repo.findByActorId(actorId);
    }

    @Override
    public List<Pelicula> searchPeliculas(String nombre, Long generoId, Long actorId) {
        // Si todos los parámetros son nulos o vacíos, devolver todas las películas
        if ((nombre == null || nombre.trim().isEmpty()) && generoId == null && actorId == null) {
            return findAll();
        }
        
        String nombreBusqueda = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
        return repo.searchPeliculas(nombreBusqueda, generoId, actorId);
    }
}