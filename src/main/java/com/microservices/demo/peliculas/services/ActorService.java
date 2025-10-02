package com.microservices.demo.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.demo.peliculas.dao.IActorRepository;
import com.microservices.demo.peliculas.entities.Actor;


@Service
public class ActorService implements IActorService {

    @Autowired
    private IActorRepository actorRepository;

    @Override
    public List<Actor> findAll() {
        return (List<Actor>) actorRepository.findAll();
    }

    @Override
    public List<Actor> findByIdIn(List<Long> ids) {
        return (List<Actor>) actorRepository.findAllById(ids);
    }
    
}
