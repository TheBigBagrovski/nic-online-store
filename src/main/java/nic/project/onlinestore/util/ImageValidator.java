package nic.project.onlinestore.util;

import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageValidator {

    @Value("${max_images_in_review}")
    private int MAX_IMAGES_IN_REVIEW;

    public void validateImages(List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                if (file.getContentType() != null && !file.getContentType().startsWith("image")) {
                    throw new ImageUploadException("Загруженный файл не является изображением");
                }
            });
            if (files.size() > MAX_IMAGES_IN_REVIEW) {
                throw new ImageUploadException("Не более 5 изображений");
            }
        }
    }

}
