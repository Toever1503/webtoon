package webtoon.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webtoon.account.services.FilesStorageService;

import java.io.IOException;

@RestController
@RequestMapping(value = "files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FilesStorageService filesStorageService;

    @PostMapping("upload")
    public String save(@RequestParam(name = "multipartFile") MultipartFile multipartFile) throws IOException {
        this.filesStorageService.save(multipartFile);
        return "success";
    }

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = this.filesStorageService.load(filename);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .body(file);
    }
}
