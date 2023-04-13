package prefeitura.pvh.fileupload.controller;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import prefeitura.pvh.fileupload.FileuploadApplication;
import prefeitura.pvh.fileupload.service.FileDataService;

@RestController
@RequestMapping("/fileSystem")
public class FileDataController {
	
	@Autowired
    private FileDataService fileDataService;
	
	@PostMapping
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = fileDataService.uploadToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
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
