package webtoon.account.services.impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import webtoon.account.services.FilesStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final String PATH = "uploads";

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            Files.copy(
                    file.getInputStream(),
                    this.root.resolve(
                            Objects.requireNonNull(file.getOriginalFilename())
                    )
            );
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return "Success";
    }

    @Override
    public ResponseEntity<ByteArrayResource> get(String fileName) {
        try {
            Path path = Paths.get(this.PATH, fileName);
            byte[] buffer = Files.readAllBytes(path);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok().contentLength(buffer.length).contentType(MediaType.parseMediaType("image/png")).body(byteArrayResource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(
                            this.root,
                            1
                    )
                    .filter(
                            path -> !path.equals(this.root)
                    )
                    .map(this.root::relativize
                    );
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
