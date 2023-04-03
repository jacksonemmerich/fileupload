package prefeitura.pvh.fileupload.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import prefeitura.pvh.fileupload.entity.FileData;
import prefeitura.pvh.fileupload.repository.FileDataRepository;
import prefeitura.pvh.fileupload.service.exception.ImageServiceException;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@Service
public class FileDataService {


    @Autowired
    private FileDataRepository fileDataRepository;

    @Value("${file.upload-dir}")
    private String FOLDER_PATH;


    public String uploadToFileSystem(MultipartFile file) throws IOException {
        String generatedName = generateFilename(file.getOriginalFilename());
        String filePath = FOLDER_PATH + generatedName;
        Path path = Paths.get(filePath);
        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(generatedName)
                .type(file.getContentType())
                .filePath(filePath).build());

        Files.write(path, file.getBytes());

        return fileData != null ? "file uploaded successfully : " + fileData.getFilePath() : null;
    }


    @Transactional
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
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
