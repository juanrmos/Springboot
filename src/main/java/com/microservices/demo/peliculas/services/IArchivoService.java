package com.microservices.demo.peliculas.services;

import org.springframework.web.multipart.MultipartFile;

public interface IArchivoService {
    
    public String guardar(MultipartFile file) throws Exception;
    
    public boolean eliminar(String nombreArchivo);
}