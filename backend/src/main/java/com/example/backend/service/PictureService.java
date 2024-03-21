package com.example.backend.service;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.model.Picture;
import com.example.backend.repository.PictureRepository;

@Service
public class PictureService {

    @Autowired
    private PictureRepository imageRepository;
    private static final String PICTURE_PATH = "backend/src/main/resources/static/images";

    public void loadPicturesFromFolder(String pathFolder) throws SerialException, SQLException {
        File folder = new File(pathFolder);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        byte[] data = Files.readAllBytes(file.toPath());
                        String name = StringUtils.cleanPath(file.getName());
                        Picture image = new Picture(null);
                        image.setName(name);
                        image.setContent(Files.probeContentType(file.toPath()));
                        image.setData(data);
                        imageRepository.save(image);
                    } catch (IOException e) {

                    }
                }
            }
        }
    }

    public void savePictures(List<Picture> imagees) {
        for (Picture image : imagees) {
            savePicture(image);
        }

    }

    public void savePicture(Picture image) {
        String nameImagen = image.getName();
        String rutaCompleta = Paths.get(PICTURE_PATH, nameImagen).toString();
        try (FileOutputStream fos = new FileOutputStream(rutaCompleta)) {
            fos.write(image.getData());
            fos.flush();
            fos.getFD().sync();
        } catch (IOException e) {

        }
    }

    public boolean verifyPictureExistance(String namefile) {
        Path rutaCompleta = Paths.get(PICTURE_PATH, namefile);
        return Files.exists(rutaCompleta) && Files.isRegularFile(rutaCompleta);
    }
    public void delete(Picture image){
        imageRepository.delete(image);
    }
    public void save(Picture image){
        imageRepository.save(image);
    }
    public Picture newPicture(MultipartFile imageFile) throws IOException{
        byte[] datosImage = imageFile.getBytes();

					Picture image= new Picture(null);
					image.setContent(imageFile.getContentType());
					image.setName(imageFile.getOriginalFilename());
					image.setData(datosImage);
					savePicture(image);
                    save(image);
                    return image;
					
    }
   
}
