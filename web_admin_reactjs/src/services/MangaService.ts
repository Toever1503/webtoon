import { Dayjs } from "dayjs";
import { mangaAxios } from "./config/MangaAxios";

const basePath: String = "/tags";

export interface MangaInput {
    id?: number | string;
    title: string;
    alternativeTitle?: string;
    content: string;
    excerpt: string;
    postName: string;
    mangaStatus: MangaStatus;
    releaseStatus: ReleaseStatus;
    releaseYear: Dayjs;
    mangaType: MangaType;
    genres: string[];
    authors: string[];
    tags: string[];
    featureImage: string;

    commentCount?: number;
    viewCount?: number;
    rating?: number;
}

export type MangaStatus = 'PUBLISHED' | 'DELETED' | 'DRAFTED';
export type ReleaseStatus = 'COMING' | 'GOING' | 'STOPED' | 'CANCELED' | 'FINISHED';
export type MangaType = 'UNSET' | 'IMAGE' | 'TEXT';


const addMangaInfo = async (model: MangaInput) => mangaAxios.post(`${basePath}`, model);
const updateMangaInfo = async (model: MangaInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const deleteMangaInfo = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const tagService = {
    addMangaInfo,
    updateMangaInfo,
    deleteMangaInfo,
};

export default tagService;