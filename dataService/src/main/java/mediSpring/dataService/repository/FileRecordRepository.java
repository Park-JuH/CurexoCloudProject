package mediSpring.dataService.repository;

import mediSpring.dataService.domain.FileRecord;
import mediSpring.dataService.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {

    Member save(Member member);
    Optional<FileRecord> findById(Long id);
    Optional<FileRecord> findByFilename(String filename);
    List<FileRecord> findAll();
}
