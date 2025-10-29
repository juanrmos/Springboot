package com.microservices.demo.peliculas.services;

import java.util.List;

import com.microservices.demo.peliculas.entities.Pelicula;

public interface IPeliculaService {

    public void save(Pelicula pelicula);
    public Pelicula findById(Long id);
    public void delete(Long id);
    public List<Pelicula> findAll();
    
    // Nuevos métodos de búsqueda y filtrado
    public List<Pelicula> searchByNombre(String nombre);
    public List<Pelicula> findByGenero(Long generoId);
    public List<Pelicula> findByYear(int year);
    public List<Pelicula> findByYearRange(int startYear, int endYear);
    public List<Pelicula> findByActor(Long actorId);
    public List<Pelicula> searchPeliculas(String nombre, Long generoId, Long actorId);
}