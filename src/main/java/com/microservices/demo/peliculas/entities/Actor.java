package com.microservices.demo.peliculas.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="actores")
public class Actor implements Serializable {
    
    private static final long serialVersionUID = -5993984638673000666L; 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Cambiado de long a Long
    
    private String nombre;
    
    @Column(name="url_imagen")
    private String urlImagen;

    public String getUrlImagen() {
        return this.urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getId() {  // Cambiado de long a Long
        return this.id;
    }

    public void setId(Long id) {  // Cambiado de long a Long
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}