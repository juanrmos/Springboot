package com.microservices.demo.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.microservices.demo.peliculas.entities.Genero;

public interface IGeneroRepository extends  CrudRepository<Genero, Long> {
        
}
