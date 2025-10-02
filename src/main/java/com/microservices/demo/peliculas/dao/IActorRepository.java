package com.microservices.demo.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.microservices.demo.peliculas.entities.Actor;

public interface IActorRepository extends CrudRepository<Actor, Long> {
    
}
