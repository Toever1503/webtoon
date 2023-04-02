import { mangaAxios } from "./config/MangaAxios";

const basePath: String = "/tags";

interface TagFilter {
    s: string;
    page: number;
    size?: number;
    sort: string;
}

export interface TagInput {
    id?: number | string;
    tagName: string;
    slug?: string;
}

const filterTag = async (model: TagFilter) => mangaAxios.get(`${basePath}/filter?s=${model.s}&page=${model.page}&size=${model.size}&sort=${model.sort}`);

const addTag = async (model: TagInput) => mangaAxios.post(`${basePath}`, model);
const updateTag = async (model: TagInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const deleteTag = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const tagService = {
    filterTag,
    addTag,
    updateTag,
    deleteTag,
};

export default tagService;