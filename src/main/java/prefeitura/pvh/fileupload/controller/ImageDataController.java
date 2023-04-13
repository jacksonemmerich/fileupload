package prefeitura.pvh.fileupload.controller;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import prefeitura.pvh.fileupload.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageDataController {
	@Autowired
	private ImageService service;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
		String uploadImage = service.uploadImage(file);
		return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
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
		return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(imageData);

	}

}
