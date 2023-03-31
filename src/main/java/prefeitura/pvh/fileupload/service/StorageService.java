package prefeitura.pvh.fileupload.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import prefeitura.pvh.fileupload.entity.FileData;
import prefeitura.pvh.fileupload.entity.ImageData;
import prefeitura.pvh.fileupload.repository.FileDataRepository;
import prefeitura.pvh.fileupload.repository.StorageRepository;
import prefeitura.pvh.fileupload.util.ImageUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;
    @Autowired
    private FileDataRepository fileDataRepository;

    @Value("${file.upload-dir}")
    private String FOLDER_PATH;

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

    public String uploadToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + generateFilename(file.getOriginalFilename());
        Path path = Paths.get(filePath);
        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileData(filePath).build());

        Files.write(path, file.getBytes());

        return fileData != null ? "file uploaded successfully : " + fileData.getFileData() : null;
    }

    @Transactional
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        if (fileData.isPresent()) {
            String filePath = FOLDER_PATH + fileData.get().getFileData();
            byte[] images = Files.readAllBytes(new File(filePath).toPath());
            return images;
        } else {
            return new byte[0];
        }
    }

    /*@Transactional
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        //String filePath = fileData.get().getFileData();

        String filePath = FOLDER_PATH + fileData.get().getFileData();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }*/

   /* @Transactional
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {

        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = FOLDER_PATH + fileData.map(FileData::getFileData).orElse(null);
        return Files.readAllBytes(Paths.get(filePath));
    }*/
    /*public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.map(FileData::getFileData).orElse(null);
        if (filePath != null) {
            File file = new File(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(file.length());
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(fileName).build());
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    private String generateFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        if (extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg")) {
            return UUID.randomUUID().toString() + "." + extension;
        } else {
            throw new RuntimeException("Tipo de arquivo inválido");
        }
    }
}
