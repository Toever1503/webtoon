package webtoon.domains.manga.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaChapterImageEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.models.MangaChapterModel;
import webtoon.domains.manga.models.MangaUploadChapterInput;
import webtoon.domains.manga.repositories.IMangaChapterImageRepository;
import webtoon.domains.manga.repositories.IMangaChapterRepository;
import webtoon.domains.manga.repositories.IMangaVolumeRepository;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.domains.manga.services.IMangaService;
import webtoon.storage.domain.dtos.FileDto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class IMangaChapterServiceImpl implements IMangaChapterService {

    @Autowired
    private IMangaChapterRepository chapterRepository;
    @Autowired
    private IMangaService mangaService;
    @Autowired
    private IMangaVolumeRepository mangaVolumeRepository;
    @Autowired
    private IMangaChapterImageRepository chapterImageRepository;

    @Override
    public MangaChapterDto add(MangaChapterModel model) {
        MangaChapterEntity chapterEntity = MangaChapterEntity.builder().name(model.getName())
                .mangaVolume(model.getMangaId()).content(model.getContent()).chapterIndex(model.getChapterIndex())
                .requiredVip(model.getRequiredVip()).build();
        this.chapterRepository.saveAndFlush(chapterEntity);
        return MangaChapterDto.builder().name(chapterEntity.getName()).mangaId(chapterEntity.getMangaVolume())
                .chapterIndex(chapterEntity.getChapterIndex()).content(chapterEntity.getContent())
                .requiredVip(chapterEntity.getRequiredVip()).build();
    }

    @Override
    public MangaChapterDto update(MangaChapterModel model) {

        MangaChapterEntity entity = this.getById(model.getId());
        entity.setChapterIndex(model.getChapterIndex());
        entity.setContent(model.getContent());
        entity.setMangaVolume(model.getMangaId());
        entity.setName(model.getName());
        entity.setRequiredVip(model.getRequiredVip());
        chapterRepository.saveAndFlush(entity);
        return MangaChapterDto.builder().name(entity.getName()).content(entity.getContent())
                .chapterIndex(entity.getChapterIndex()).mangaId(entity.getMangaVolume())
                .requiredVip(entity.getRequiredVip()).build();
    }

    @Override
    public boolean deleteById(java.lang.Long id) {
        try {
            this.chapterRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    @Override
    public Page<MangaChapterDto> filter(Pageable pageable, Specification<MangaChapterEntity> specs) {
        return chapterRepository.findAll(specs, pageable).map(MangaChapterDto::toDto);
    }

    @Override
    public void createTextChapter(MangaUploadChapterInput input) {
        MangaEntity mangaEntity = this.mangaService.getById(input.getMangaID());
        MangaVolumeEntity volumeEntity = this.mangaVolumeRepository.findById(input.getVolumeID())
                .orElse(
                        MangaVolumeEntity.builder()
                                .name("Volume 1")
                                .manga(mangaEntity)
                                .volumeIndex(0)
                                .build()
                );
        this.mangaVolumeRepository.saveAndFlush(volumeEntity);
        MangaChapterEntity mangaChapterEntity = MangaChapterEntity.builder()
                .name(input.getChapterName())
                .chapterIndex(input.getChapterIndex())
                .mangaVolume(volumeEntity)
                .content(input.getChapterContent())
                .requiredVip(input.getIsRequiredVip())
                .build();
        this.chapterRepository.saveAndFlush(mangaChapterEntity);
    }

    @Override
    public void createImageChapter(MangaUploadChapterInput input, List<MultipartFile> multipartFiles) {


        try {
            MangaEntity mangaEntity = this.mangaService.getById(input.getMangaID());
            MangaVolumeEntity volumeEntity = this.mangaVolumeRepository.findById(input.getVolumeID())
                    .orElse(
                            MangaVolumeEntity.builder()
                                    .name("Volume 1")
                                    .manga(mangaEntity)
                                    .volumeIndex(0)
                                    .build()
                    );
            this.mangaVolumeRepository.saveAndFlush(volumeEntity);
            MangaChapterEntity mangaChapterEntity = MangaChapterEntity.builder()
                    .name(input.getChapterName())
                    .chapterIndex(input.getChapterIndex())
                    .mangaVolume(volumeEntity)
                    .requiredVip(input.getIsRequiredVip())
                    .build();
            this.chapterRepository.saveAndFlush(mangaChapterEntity);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body
                    = new LinkedMultiValueMap<>();
            body.add("file", multipartFiles.get(0).getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            String serverUrl = "http://localhost:8080/storage/mutations/upload-image-by-zip-file?folder=manga/1/";
            ResponseEntity<List<FileDto>> response = restTemplate.exchange(
                    serverUrl,
                    HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            List<FileDto> imageList = response.getBody();
            AtomicInteger imageIndex = new AtomicInteger();
            List<MangaChapterImageEntity> mangaChapterImages = imageList.stream().map(file -> MangaChapterImageEntity.builder()
                    .mangaChapter(mangaChapterEntity)
                    .image(file.getUrl())
                    .imageIndex(imageIndex.getAndIncrement())
                    .build()).collect(Collectors.toList());
            this.chapterImageRepository.saveAllAndFlush(mangaChapterImages);

        } catch (Exception e) {
            e.printStackTrace();
            // need remove image
            throw e;
        }

    }

    private List<MangaChapterImageEntity> getImagesFromZipFile(String mangaName, MultipartFile file) throws IOException {
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            if (zipEntry.getName().endsWith(".png") ||
                    zipEntry.getName().endsWith(".jpg") ||
                    zipEntry.getName().endsWith(".jpeg")) {
                System.out.println("zipEntry.getName(): " + zipEntry.getName());
            }
//            File newFile = new File("F:\\uploads\\unzip\\" + zipEntry.getName());
//
//            if (!zipEntry.isDirectory()) {
//                // write file content
//                FileOutputStream fos = new FileOutputStream(newFile);
//                fos.write(zis.readAllBytes());
//                fos.close();
//            }

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        return null;
    }

    public MangaChapterEntity getById(java.lang.Long id) {
        return this.chapterRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

}
