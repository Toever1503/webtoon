import { mangaAxios } from "../config/MangaAxios";

const basePath: String = "/genre";

interface GenreFilter {
    s: string | null;
    page: number;
    size?: number;
    sort: string;
}

export interface GenreInput {
    id?: number | string;
    name: string;
    slug?: string;
}

const filterGenre = async (model: GenreFilter) => mangaAxios.get(`${basePath}/filter?s=${model.s}&page=${model.page}&size=${model.size}&sort=${model.sort}`);

const addGenre = async (model: GenreInput) => mangaAxios.post(`${basePath}`, model);
const updateGenre = async (model: GenreInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const deleteGenre = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const genreService = {
    filterGenre,
    addGenre,
    updateGenre,
    deleteGenre,
};

export default genreService;