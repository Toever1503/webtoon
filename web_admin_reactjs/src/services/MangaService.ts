import { Dayjs } from "dayjs";
import { mangaAxios } from "./config/MangaAxios";

const basePath: String = "/manga";

export interface MangaInput {
    id: number | string;
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
export type MangaType = 'UNSET' | 'IMAGE' | 'TEXT';

export type MangaChapterInput = {
    id?: number | string;
    mangaID: number | string;
    volumeID: number | string;
    chapterName: string;
    chapterIndex: number;
    isRequiredVip: boolean;
    chapterContent?: string;
    chapterImages?: string[];
}

const addMangaInfo = async (model: MangaInput) => mangaAxios.post(`${basePath}`, model);
const updateMangaInfo = async (model: MangaInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const setMangaType = async (id: number | string, type: MangaType) => mangaAxios.patch(`${basePath}/set-type/${id}?type=${type}`);
const deleteMangaInfo = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const createTextChapter = async (model: MangaChapterInput) => mangaAxios.post(`${basePath}/chapter/create-text-chapter`, model);
const createImageChapter = async (model: MangaChapterInput) => mangaAxios.post(`${basePath}/chapter/create-image-chapter`, model);


type MangaVolumeInput = {
    id?: number | string;
    mangaID: number | string;
    name: string;
    volumeIndex: number;
}
const createNewVolume = async (input: MangaVolumeInput) => mangaAxios.post(`${basePath}/volume`, input);


const mangaService = {
    addMangaInfo,
    updateMangaInfo,
    setMangaType,
    deleteMangaInfo,
    createTextChapter,
    createImageChapter,
    createNewVolume
};

export default mangaService;