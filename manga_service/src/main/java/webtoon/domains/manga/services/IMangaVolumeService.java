package webtoon.domains.manga.services;

import webtoon.domains.manga.dtos.MangaVolumeDto;
import webtoon.domains.manga.models.MangaVolumeModel;

public interface IMangaVolumeService {

	boolean deleteById(Long id);

	MangaVolumeDto update(MangaVolumeModel model);

	MangaVolumeDto add(MangaVolumeModel model);

}
