package prefeitura.pvh.fileupload.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import prefeitura.pvh.fileupload.entity.ImageData;
import prefeitura.pvh.fileupload.repository.ImageRepository;
import prefeitura.pvh.fileupload.service.exception.ImageServiceException;
import prefeitura.pvh.fileupload.util.ImageUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = repository.save(ImageData.builder()
                .name(generateFilename(file.getOriginalFilename()))
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + repository.findByName(imageData.getName()).get().getName();
        }
        return null;
    }

    @Transactional
    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    private String generateFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        if (extension.equalsIgnoreCase("pdf") ||
                extension.equalsIgnoreCase("png") ||
                extension.equalsIgnoreCase("jpg")||
                extension.equalsIgnoreCase("jpeg")) {
            return UUID.randomUUID().toString() + "." + extension;
        } else {
            throw new ImageServiceException("Tipo de arquivo inválido");
        }
    }
}
