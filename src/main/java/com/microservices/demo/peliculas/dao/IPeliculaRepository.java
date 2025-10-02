package com.microservices.demo.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.microservices.demo.peliculas.entities.Pelicula;

public interface IPeliculaRepository extends CrudRepository<Pelicula, Long> {
    
}
