package prefeitura.pvh.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prefeitura.pvh.fileupload.entity.ImageData;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageData, Long> {


    Optional<ImageData> findByName(String fileName);


}