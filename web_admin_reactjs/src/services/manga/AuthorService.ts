import { mangaAxios } from "../config/MangaAxios";

const basePath: String = "/author";

interface AuthorFilter {
    s: string;
    page: number;
    size?: number;
    sort: string;
}

export interface AuthorInput {
    id: number | string;
    name: string;
    slug: string;
}

const filterAuthor = async (model: AuthorFilter) => mangaAxios.get(`${basePath}/filter?s=${model.s}&page=${model.page}&size=${model.size}&sort=${model.sort}`);

const addAuthor = async (model: AuthorInput) => mangaAxios.post(`${basePath}`, model);
const updateAuthor = async (model: AuthorInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const deleteAuthor= async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const authorService = {
    filterAuthor,
    addAuthor,
    updateAuthor,
    deleteAuthor,
};

export default authorService;