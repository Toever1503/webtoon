import { Dayjs } from "dayjs";
import { mangaAxios } from "./config/MangaAxios";

const basePath: String = "/manga";

export interface MangaInput {
    id?: number | string;
    title: string;
    alternativeTitle?: string;
    description: string;
    excerpt: string;
    mangaName: string;
    status: MangaStatus;
    mangaStatus: ReleaseStatus;
    releaseYear: Dayjs;
    mangaType: MangaType;
    genres: string[];
    authors: string[];
    tags: string[];
    featureImage: string;
}

export type MangaStatus = 'PUBLISHED' | 'DELETED' | 'DRAFTED';
export type ReleaseStatus = 'COMING' | 'GOING' | 'STOPPED' | 'CANCELLED' | 'COMPLETED';
export type MangaType =  'UNSET' | 'IMAGE' | 'TEXT';


const addMangaInfo = async (model: MangaInput) => mangaAxios.post(`${basePath}`, model);
const updateMangaInfo = async (model: MangaInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const deleteMangaInfo = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const mangaService = {
    addMangaInfo,
    updateMangaInfo,
    deleteMangaInfo,
};

export default mangaService;