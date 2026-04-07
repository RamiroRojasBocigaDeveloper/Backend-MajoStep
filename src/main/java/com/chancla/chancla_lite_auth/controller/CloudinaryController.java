package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
@Tag(name = "Cloudinary", description = "Endpoints para la gestión de imágenes con Cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(summary = "Subir imagen", description = "Sube una imagen a Cloudinary y devuelve los datos de la subida, incluyendo la URL.")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = cloudinaryService.upload(file);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar imagen", description = "Elimina una imagen de Cloudinary por su ID público.")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("id") String id) {
        try {
            Map<String, Object> result = cloudinaryService.delete(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
