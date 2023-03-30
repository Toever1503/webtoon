import { Dayjs } from "dayjs";
import { VolumeForm } from "../../pages/manga/component/manga-form/MangaAddEditVolumeModal";
import { ChapterType } from "../../pages/manga/component/manga-form/MangaVolumeInfoItem";
import { mangaAxios } from "../config/MangaAxios";

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

// export type MangaChapterInput = {
//     id?: number | string;
//     mangaID: number | string;
//     volumeID: number | string;
//     chapterName: string;
//     chapterIndex: number;
//     isRequiredVip: boolean;
//     chapterContent?: string;
//     chapterImages?: string[];
// }

// for manga
export type MangaFilterInput = {
    status: MangaStatus;
    q: string;
}

const filterManga = (input: MangaFilterInput, page: number, size: number) => mangaAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, input);
const addMangaInfo = async (model: MangaInput) => mangaAxios.post(`${basePath}`, model);
const updateMangaInfo = async (model: MangaInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const setMangaType = async (id: number | string, type: MangaType) => mangaAxios.patch(`${basePath}/set-type/${id}?type=${type}`);
const deleteMangaInfo = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const createTextChapter = async (model: ChapterType, mangaId: number | string) => mangaAxios.post(`${basePath}/chapter/save-text-chapter?mangaId=${mangaId}`, model);
const createImageChapter = async (model: FormData, mangaId: number | string) => mangaAxios.post(`${basePath}/chapter/save-image-chapter?mangaId=${mangaId}`, model);

const reIndexChapter = async (chapterId: number | string, index: number) => mangaAxios.patch(`${basePath}/chapter/re-index-chapter/${chapterId}?index=${index}`);

const getVolumeForManga = (mangaId: number | string) => mangaAxios.get(`${basePath}/volume/get-all-for-manga/${mangaId}`);

type MangaVolumeInput = {
    id?: number | string;
    mangaId: number | string;
    name: string;
    volumeIndex: number;
}
const createNewVolume = async (input: MangaVolumeInput) => mangaAxios.post(`${basePath}/volume`, input);
const updateVolume = async (id: number | string, input: VolumeForm) => mangaAxios.post(`${basePath}/volume/${id}`, input);


// for chapter
const getChapterByVolumeId = async (volumeId: number | string) => mangaAxios.get(`${basePath}/chapter/get-all-by-volume/${volumeId}`);



const mangaService = {
    filterManga,
    addMangaInfo,
    updateMangaInfo,
    setMangaType,
    deleteMangaInfo,
    createTextChapter,
    createImageChapter,
    createNewVolume,
    updateVolume,
    getVolumeForManga,
    getChapterByVolumeId
};

export default mangaService;