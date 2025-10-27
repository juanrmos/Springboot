package com.microservices.demo.peliculas.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoService implements IArchivoService {

    private final String DIRECTORIO_ARCHIVOS = "archivos";

    @Override
    public String guardar(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("El archivo está vacío");
        }

        // Crear directorio si no existe
        File directorio = new File(DIRECTORIO_ARCHIVOS);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Generar nombre único para el archivo
        String nombreOriginal = file.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        String nombreUnico = UUID.randomUUID().toString() + extension;

        // Guardar archivo
        Path rutaArchivo = Paths.get(DIRECTORIO_ARCHIVOS).resolve(nombreUnico).toAbsolutePath();
        Files.copy(file.getInputStream(), rutaArchivo);

        return nombreUnico;
    }

    @Override
    public boolean eliminar(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty() || nombreArchivo.equals("_default.jpg")) {
            return false;
        }

        try {
            Path rutaArchivo = Paths.get(DIRECTORIO_ARCHIVOS).resolve(nombreArchivo).toAbsolutePath();
            Files.deleteIfExists(rutaArchivo);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}