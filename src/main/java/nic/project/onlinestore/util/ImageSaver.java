package nic.project.onlinestore.util;

import lombok.extern.slf4j.Slf4j;
import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import nic.project.onlinestore.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
public class ImageSaver {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public void saveImage(MultipartFile file, String finalPath, String fileName) {
        Path targetPath = Paths.get(finalPath, fileName);
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении изображения: " + e.getMessage());
            throw new ImageUploadException("Ошибка при загрузке изображения");
        }
    }

    public void createFolder(String requiredPath, String requiredName) {
        Path directoryPath = Paths.get(requiredPath, requiredName);
        try {
            if (!Files.exists(directoryPath)) Files.createDirectory(directoryPath);
        } catch (IOException e) {
            logger.error("Ошибка при создании папки: " + e.getMessage());
            throw new ImageUploadException("Ошибка при загрузке изображения");
        }
    }

    public void deleteFolder(String path) {
        try {
            Path pth = Paths.get(path);
            if(Files.exists(pth)) {
                Files.walk(pth)
                        .sorted((p1, p2) -> -p1.compareTo(p2))
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                logger.error("Ошибка при удалении папки: " + p);
                                throw new RuntimeException("Ошибка при удалении папки");
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении папки");
        }
    }

}
