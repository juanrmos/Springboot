package com.microservices.demo.peliculas.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.microservices.demo.peliculas.services.IArchivoService;

@Controller
public class ArchivoController {

    private final IArchivoService archivoService;

    public ArchivoController(IArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            String nombreArchivo = archivoService.guardar(file);
            return nombreArchivo;
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> verArchivo(@PathVariable String filename) {
        try {
            Path rutaArchivo = Paths.get("archivos").resolve(filename).toAbsolutePath();
            Resource recurso = new UrlResource(rutaArchivo.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                        .body(recurso);
            } else {
                throw new RuntimeException("No se puede leer el archivo: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el archivo: " + filename);
        }
    }
}