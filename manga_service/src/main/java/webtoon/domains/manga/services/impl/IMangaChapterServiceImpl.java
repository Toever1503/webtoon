package webtoon.domains.manga.services.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import webtoon.domains.manga.dtos.MangaChapterDto;
import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.entities.MangaChapterEntity;
import webtoon.domains.manga.entities.MangaChapterImageEntity;
import webtoon.domains.manga.entities.MangaEntity;
import webtoon.domains.manga.entities.MangaVolumeEntity;
import webtoon.domains.manga.mappers.MangaChapterMapper;
import webtoon.domains.manga.models.MangaChapterFilterInput;
import webtoon.domains.manga.models.MangaChapterModel;
import webtoon.domains.manga.models.MangaUploadChapterInput;
import webtoon.domains.manga.repositories.IMangaChapterImageRepository;
import webtoon.domains.manga.repositories.IMangaChapterRepository;
import webtoon.domains.manga.repositories.IMangaVolumeRepository;
import webtoon.domains.manga.services.IMangaChapterService;
import webtoon.domains.manga.services.IMangaService;
import webtoon.storage.domain.dtos.FileDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class IMangaChapterServiceImpl implements IMangaChapterService {


    private final IMangaChapterRepository chapterRepository;

    private final IMangaService mangaService;

    private final IMangaVolumeRepository mangaVolumeRepository;

    private final IMangaChapterImageRepository chapterImageRepository;

    private final MangaChapterMapper chapterMapper;

    public IMangaChapterServiceImpl(IMangaChapterRepository chapterRepository, IMangaService mangaService,
                                    IMangaVolumeRepository mangaVolumeRepository, IMangaChapterImageRepository
                                            chapterImageRepository, MangaChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.mangaService = mangaService;
        this.mangaVolumeRepository = mangaVolumeRepository;
        this.chapterImageRepository = chapterImageRepository;
        this.chapterMapper = chapterMapper;
    }

    @Override
    public MangaChapterDto add(MangaChapterModel model) {
        MangaChapterEntity chapterEntity = MangaChapterEntity.builder().name(model.getName())
                .mangaVolume(model.getMangaVolumeId()).content(model.getContent()).chapterIndex(model.getChapterIndex())
                .requiredVip(model.getRequiredVip()).build();
        this.chapterRepository.saveAndFlush(chapterEntity);
        return MangaChapterDto.builder().name(chapterEntity.getName()).volumeId(chapterEntity.getMangaVolume())
                .chapterIndex(chapterEntity.getChapterIndex()).content(chapterEntity.getContent())
                .isRequiredVip(chapterEntity.getRequiredVip()).build();
    }

    @Override
    public MangaChapterDto update(MangaChapterModel model) {

        MangaChapterEntity entity = this.getById(model.getId());
        entity.setChapterIndex(model.getChapterIndex());
        entity.setContent(model.getContent());
        entity.setMangaVolume(model.getMangaVolumeId());
        entity.setName(model.getName());
        entity.setRequiredVip(model.getRequiredVip());
        chapterRepository.saveAndFlush(entity);
        return MangaChapterDto.builder().name(entity.getName()).content(entity.getContent())
                .chapterIndex(entity.getChapterIndex()).volumeId(entity.getMangaVolume())
                .isRequiredVip(entity.getRequiredVip()).build();
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
    public MangaChapterDto saveTextChapter(MangaUploadChapterInput input) {
        MangaEntity mangaEntity = this.mangaService.getById(input.getMangaID());
        MangaVolumeEntity volumeEntity = this.mangaVolumeRepository.findById(input.getVolumeId())
                .orElse(
                        MangaVolumeEntity.builder()
                                .name("Volume 1")
                                .manga(mangaEntity)
                                .volumeIndex(0)
                                .build()
                );
        this.mangaVolumeRepository.saveAndFlush(volumeEntity);

        MangaChapterEntity mangaChapterEntity = MangaChapterEntity.builder()
                .id(input.getId())
                .name(input.getName())
                .chapterIndex(this.chapterRepository.getLastChapterIndex(input.getMangaID()).orElse(-1L).intValue() + 1)
                .mangaVolume(volumeEntity)
                .content(input.getContent())
                .requiredVip(input.getIsRequiredVip())
                .build();

        this.chapterRepository.saveAndFlush(mangaChapterEntity);

        return MangaChapterDto.toDto(mangaChapterEntity);
    }

    @Override
    public MangaChapterDto saveImageChapter(MangaUploadChapterInput input, List<MultipartFile> multipartFiles) {


        try {
            MangaEntity mangaEntity = this.mangaService.getById(input.getMangaID());
            MangaVolumeEntity volumeEntity = this.mangaVolumeRepository.findById(input.getVolumeId())
                    .orElse(
                            MangaVolumeEntity.builder()
                                    .name("Volume 1")
                                    .manga(mangaEntity)
                                    .volumeIndex(0)
                                    .build()
                    );
            this.mangaVolumeRepository.saveAndFlush(volumeEntity);
            MangaChapterEntity mangaChapterEntity = MangaChapterEntity.builder()
                    .id(input.getId())
                    .name(input.getName())
                    .chapterIndex(this.chapterRepository.getLastChapterIndex(input.getMangaID()).orElse(-1L).intValue() + 1)
                    .mangaVolume(volumeEntity)
                    .requiredVip(input.getIsRequiredVip())
                    .build();
            this.chapterRepository.saveAndFlush(mangaChapterEntity);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body
                    = new LinkedMultiValueMap<>();
            multipartFiles.forEach(f -> body.add("files", f.getResource()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            String serverUrl = "http://localhost:8001/storage/mutations/upload-image-by-zip-file?folder=manga/1/";
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

            return MangaChapterDto.toDto(mangaChapterEntity);
        } catch (Exception e) {
            e.printStackTrace();
            // need remove image
            throw e;
        }

    }

    @Override
    public List<MangaChapterDto> getAllByVolumeId(Long id) {
        return this.chapterRepository.findByMangaVolumeId(id)
                .stream().map(MangaChapterDto::toDto).collect(Collectors.toList());
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

    @Override
    public MangaChapterImageEntity getImageForChapter(Long chapterId) {
        return null;
    }

    @Override
    public MangaChapterDto[] findNextPosts(Long chapterID, Long volumeId){
        MangaChapterDto[] chapterDtos = new MangaChapterDto[2];

        List<MangaChapterEntity> nextChapters = this.chapterRepository
                .findNextchapter(chapterID,volumeId,PageRequest.of(0,1));
        chapterDtos[1] = nextChapters.size() == 0 ? null : this.chapterMapper.toDto(nextChapters.get(0));

        List<MangaChapterEntity> prevChapters = this.chapterRepository
                .findPrevchapter(chapterID,volumeId,PageRequest.of(0,1,Sort.Direction.DESC,"id"));
        chapterDtos[0] = prevChapters.size() == 0 ? null : this.chapterMapper.toDto(prevChapters.get(0));

        return chapterDtos;
    }

    @Override
    public Page<MangaChapterDto> filterChapter(Pageable pageable, MangaChapterFilterInput input) {
        if (input.getQ() != null)
            input.setQ("%" + input.getQ() + "%");

        List<Specification> specs = new ArrayList<>();

        if (input.getQ() != null) {
            specs.add((root, query, cb) -> cb.like(root.get("name"), input.getQ()));
        }
        if (input.getVolumeId() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("mangaVolume").get("id"), input.getVolumeId()));
        }
        if (input.getChapterIndex() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("chapterIndex"), input.getChapterIndex()));
        }
        Specification<MangaChapterEntity> finalSpec = null;
        for (Specification spec : specs) {
            if (finalSpec == null) {
                finalSpec = spec;
            } else {
                finalSpec = finalSpec.and(spec);
            }
        }
        return this.chapterRepository.findAll(finalSpec, pageable).map(MangaChapterDto::toDto);
    }

}
