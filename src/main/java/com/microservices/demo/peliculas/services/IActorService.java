package com.microservices.demo.peliculas.services;

import java.util.List;

import com.microservices.demo.peliculas.entities.Actor;

public interface IActorService {
    public List<Actor> findAll();
    public List<Actor> findAllById(List<Long> ids);
    
}
