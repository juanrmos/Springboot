package com.microservices.demo.peliculas.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.microservices.demo.peliculas.entities.Pelicula;

public interface IPeliculaRepository extends CrudRepository<Pelicula, Long> {
    
    // Búsqueda por nombre (case insensitive)
    List<Pelicula> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por género
    List<Pelicula> findByGeneroId(Long generoId);
    
    // Búsqueda por año de estreno
    @Query("SELECT p FROM Pelicula p WHERE YEAR(p.fechaEstreno) = :year")
    List<Pelicula> findByYear(@Param("year") int year);
    
    // Búsqueda por rango de años
    @Query("SELECT p FROM Pelicula p WHERE YEAR(p.fechaEstreno) BETWEEN :startYear AND :endYear")
    List<Pelicula> findByYearRange(@Param("startYear") int startYear, @Param("endYear") int endYear);
    
    // Búsqueda por actor
    @Query("SELECT DISTINCT p FROM Pelicula p JOIN p.protagonistas a WHERE a.id = :actorId")
    List<Pelicula> findByActorId(@Param("actorId") Long actorId);
    
    // Búsqueda combinada: nombre, género y/o actor
    @Query("SELECT DISTINCT p FROM Pelicula p LEFT JOIN p.protagonistas a " +
           "WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:generoId IS NULL OR p.genero.id = :generoId) " +
           "AND (:actorId IS NULL OR a.id = :actorId)")
    List<Pelicula> searchPeliculas(
        @Param("nombre") String nombre,
        @Param("generoId") Long generoId,
        @Param("actorId") Long actorId
    );
}