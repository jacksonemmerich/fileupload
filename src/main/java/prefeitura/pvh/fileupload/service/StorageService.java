package prefeitura.pvh.fileupload.service;

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
import java.util.Optional;


@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;
    @Autowired
    private FileDataRepository fileDataRepository;

   @Value("${file.upload-dir}")
    private String FOLDER_PATH;

    //private final String FOLDER_PATH="/ambiente-dev/my_storage/";

    public String uploadImage(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
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
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileData(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }


    @Transactional
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFileData();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
