package prefeitura.pvh.fileupload;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prefeitura.pvh.fileupload.service.FileDataService;
import prefeitura.pvh.fileupload.service.ImageService;

import java.io.IOException;

@SpringBootApplication
@RestController
@RequestMapping("/image")
public class FileuploadApplication {

    @Autowired
    private ImageService service;
    @Autowired
    private FileDataService fileDataService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = service.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = service.downloadImage(fileName);
        MediaType mediaType = null;

        String extension = FilenameUtils.getExtension(fileName);
        if ("png".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_PNG;
        } else if ("jpeg".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if ("pdf".equalsIgnoreCase(extension)) {
            mediaType = MediaType.APPLICATION_PDF;
        }

        // arquivos com MIME types diferentes de png, jpeg e pdf não são suportados
        if (mediaType == null) {
            throw new IOException("Tipo de mídia inválido para arquivo: " + fileName);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(mediaType)
                .body(imageData);

    }

    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = fileDataService.uploadToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException, InvalidMediaTypeException {
        byte[] imageData = fileDataService.downloadImageFromFileSystem(fileName);
        MediaType mediaType = null;

        String extension = FilenameUtils.getExtension(fileName);
        if ("png".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_PNG;
        } else if ("jpeg".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if ("pdf".equalsIgnoreCase(extension)) {
            mediaType = MediaType.APPLICATION_PDF;
        }

        // arquivos com MIME types diferentes de png, jpeg e pdf não são suportados
        if (mediaType == null) {
            throw new IOException("Tipo de mídia inválido para arquivo: " + fileName);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(mediaType)
                .body(imageData);
    }


    public static void main(String[] args) {
        SpringApplication.run(FileuploadApplication.class, args);
    }

}