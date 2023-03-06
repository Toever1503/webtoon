package webtoon.services;

import webtoon.dtos.MangaDto;
import webtoon.models.MangaModel;

public interface IMangaService {

	MangaDto add(MangaModel model);

}
