package webtoon.account.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init();

    String save(MultipartFile multipartFile) throws IOException;

    ResponseEntity<ByteArrayResource> get(String fileName);

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();
}
