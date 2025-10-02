package com.microservices.demo.peliculas.services;

import java.util.List;

import com.microservices.demo.peliculas.entities.Genero;


public interface IGeneroService {

    public void save (Genero genero);
    public Genero findById (Long id);
    public void delete (Long id);
    public List<Genero> findAll();

}
