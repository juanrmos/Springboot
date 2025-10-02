package com.microservices.demo.peliculas.services;

import java.util.List;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.demo.peliculas.dao.IGeneroRepository;
import com.microservices.demo.peliculas.entities.Genero;

@Service
public class GeneroService implements IGeneroService {

    @Autowired
    private IGeneroRepository generoRepository;

    @Override
    public void save (Genero genero){
        generoRepository.save(genero);
    }

    @Override
    public Genero findById (Long id){
        return generoRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        generoRepository.deleteById(id);
    }

    @Override
    public List<Genero> findAll() {
        return (List<Genero>)generoRepository.findAll();
    }

}
