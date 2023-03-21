import { mangaAxios } from "./config/AccountAxios";

const basePath: String = "/tags";

interface TagFilter {
    s: string;
    page: number;
    size: number;
    sort: string;
}

interface TagInput {
    id: number | string;
    name: string;
    slug: string;
}

const filterTag = async (model: TagFilter) => mangaAxios.get(`${basePath}/filter?${model.s}&page=${model.page}&size=${model.size}&sort=${model.sort}`);

const addTag = async (model: TagInput) => mangaAxios.post(`${basePath}`, model);
const updateTag = async (model: TagInput) => mangaAxios.post(`${basePath}/${model.id}`, model);
const deleteTag = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/buck-del`, ids);

const tagService = {
    filterTag,
    addTag,
    updateTag,
    deleteTag,
};