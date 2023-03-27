package webtoon.domains.manga.resources2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import webtoon.domains.manga.models.MangaUploadChapterInput;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.storage.domain.dtos.FileDto;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("api/manga/chapter")
public class MangaChapterResource2 {

    private final IMangaChapterService mangaChapterService;

    public MangaChapterResource2(IMangaChapterService mangaChapterService) {
        this.mangaChapterService = mangaChapterService;
    }

    @PostMapping("create-text-chapter")
    public void createTextChapter(@RequestBody MangaUploadChapterInput input) {
        this.mangaChapterService.createTextChapter(input);
    }

    @PostMapping("create-image-chapter")
    public void createImageChapter(MangaUploadChapterInput input, @RequestPart List<MultipartFile> multipartFiles) throws IOException {
        if (multipartFiles.size() == 0)
            throw new RuntimeException("File is required");


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", multipartFiles.get(0).getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = "http://localhost:8001/storage/mutations/upload-image-by-zip-file?folder=manga/1/";
        ResponseEntity<List> response = restTemplate.postForEntity(serverUrl, requestEntity, List.class);

        this.mangaChapterService.createImageChapter(input, multipartFiles);
    }

    public static void main(String[] args) throws IOException {
        String fileZip = "F:\\uploads\\uploads.zip";

        FileInputStream inputStream = new FileInputStream(fileZip);
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();

//        while (zipEntry != null) {
//            if (zipEntry.getName().endsWith(".png") ||
//                    zipEntry.getName().endsWith(".jpg") ||
//                    zipEntry.getName().endsWith(".jpeg")) {
//                System.out.println("zipEntry.getName(): " + zipEntry.getName());
//            }
////            File newFile = new File("F:\\uploads\\unzip\\" + zipEntry.getName());
////
////            if (!zipEntry.isDirectory()) {
////                // write file content
////                FileOutputStream fos = new FileOutputStream(newFile);
////                fos.write(zis.readAllBytes());
////                fos.close();
////            }
//
//
//            zipEntry = zis.getNextEntry();
//        }

        zis.closeEntry();
        zis.close();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class UploadFileInput {
        private byte[] bytes;
        private String fileName;
    }

    static FileDto uploadImageByZipFile(MultipartFile file) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", file);


        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = "http://localhost:8001/storage/mutations/upload-image-by-zip-file?folder=manga/1";
        ResponseEntity<FileDto> response = restTemplate.postForEntity(serverUrl, requestEntity, FileDto.class);
        return null;
    }
}
