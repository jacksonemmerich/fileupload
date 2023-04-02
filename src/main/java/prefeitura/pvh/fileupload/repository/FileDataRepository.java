package prefeitura.pvh.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prefeitura.pvh.fileupload.entity.FileData;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(String filename);

}