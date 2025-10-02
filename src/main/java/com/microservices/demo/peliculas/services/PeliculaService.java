package com.microservices.demo.peliculas.services;

import java.util.List;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.demo.peliculas.dao.IPeliculaRepository;
import com.microservices.demo.peliculas.entities.Pelicula;

@Service
public class PeliculaService implements IPeliculaService {

    @Autowired
    private IPeliculaRepository repo;

    @Override
    public void save (Pelicula pelicula){
        repo.save(pelicula);
    }

    @Override
    public Pelicula findById (Long id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Pelicula> findAll() {
        return (List<Pelicula>)repo.findAll();
    }

}
