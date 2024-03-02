package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.model.Picture;
import com.example.demo.repository.PictureRepository;

@Service
public class ImagenService {

    @Autowired
    private PictureRepository imagenRepository;
    private static final String RUTA_IMAGENES = "demo/src/main/resources/static/images";

    public void cargarImagenesDesdeCarpeta(String rutaCarpeta) throws SerialException, SQLException {
        File carpeta = new File(rutaCarpeta);
        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    try {
                        byte[] datos = Files.readAllBytes(archivo.toPath());
                        String nombre = StringUtils.cleanPath(archivo.getName());
                        Picture imagen = new Picture(null);
                        imagen.setName(nombre);
                        imagen.setContent(Files.probeContentType(archivo.toPath()));
                        imagen.setDatos(datos);
                        imagenRepository.save(imagen);
                    } catch (IOException e) {

                    }
                }
            }
        }
    }

    public void guardarImagenes(List<Picture> imagenes) {
        for (Picture imagen : imagenes) {
            guardarImagen(imagen);
        }

    }

    public void guardarImagen(Picture imagen) {
        String nombreImagen = imagen.getName();
        String rutaCompleta = Paths.get(RUTA_IMAGENES, nombreImagen).toString();
        try (FileOutputStream fos = new FileOutputStream(rutaCompleta)) {
            fos.write(imagen.getData());
            fos.flush();
            fos.getFD().sync();
        } catch (IOException e) {

        }
    }

    public boolean verificarExistenciaImagen(String nombreArchivo) {
        Path rutaCompleta = Paths.get(RUTA_IMAGENES, nombreArchivo);
        return Files.exists(rutaCompleta) && Files.isRegularFile(rutaCompleta);
    }
}
