package nic.project.onlinestore.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Component
public class ImageValidator {

    @Value("${max_images_in_review}")
    private int MAX_IMAGES_IN_REVIEW;

    public void validateImages(List<MultipartFile> files, Map<String, String> errors) {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.getContentType() != null) {
                    if (!file.getContentType().startsWith("image"))
                        errors.put("files", "Загруженный файл не является изображением");
                }
            }
            if (files.size() > MAX_IMAGES_IN_REVIEW) errors.put("files", "Не более 5 изображений");
        }
    }

}
