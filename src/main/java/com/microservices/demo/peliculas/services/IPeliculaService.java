package com.microservices.demo.peliculas.services;

import java.util.List;

import com.microservices.demo.peliculas.entities.Pelicula;


public interface IPeliculaService {

    public void save (Pelicula pelicula);
    public Pelicula findById (Long id);
    public void delete (Long id);
    public List<Pelicula> findAll();

}
