import { Dayjs } from "dayjs";
import { VolumeForm } from "../../pages/manga/component/modal/MangaAddEditVolumeModal";
import { mangaAxios } from "../config/MangaAxios";
import { ChapterType } from "../../pages/manga/component/manga-form-new/ChapterSetting";

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
    releaseYear: string | undefined;
    mangaType: MangaType;
    genres: string[];
    authors: string[];
    tags: string[];
    featuredImage: string;
    featuredImageFile?: File;
    isFree: boolean;
    displayType: 'VOL' | 'CHAP';
}

export type MangaStatus = 'ALL' | 'PUBLISHED' | 'DELETED' | 'HIDDEN' | 'DRAFTED';
export type ReleaseStatus = 'COMING' | 'ONGOING' | 'ON_STOPPING' | 'CANCELLED' | 'COMPLETED';
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
    genre: string;
    author: string;
    status: MangaStatus;
    q?: string;
    releaseStatus?: ReleaseStatus;
    timeRange?: Dayjs[];
}
const findById = async (id: number | string) => mangaAxios.get(`${basePath}/${id}`);

const filterManga = (input: MangaFilterInput, page: number, size: number) => mangaAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, input);
const addMangaInfo = async (model: FormData) => mangaAxios.post(`${basePath}`, model, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});
const updateMangaInfo = async (model: MangaInput) => mangaAxios.put(`${basePath}/${model.id}`, model);
const setMangaTypeAndDisplayType = async (id: number | string, mangaTye: MangaType,  displayType: 'VOL' | 'CHAP') => mangaAxios.patch(`${basePath}/set-manga-type-and-display-type/${id}?mangaType=${mangaTye}&displayType=${displayType}`);
const resetMangaType = async (id: number | string) => mangaAxios.patch(`${basePath}/reset-manga-type/${id}`);
const changeStatus = async (id: number | string, status: MangaStatus) => mangaAxios.patch(`${basePath}/change-status/${id}?status=${status}`);
const changeReleaseStatus = async (id: number | string, status: ReleaseStatus) => mangaAxios.patch(`${basePath}/change-release-status/${id}?mangaSTS=${status}`);
const calculateTotalMangaEachStatus = async () => mangaAxios.get(`${basePath}/calc-total-manga-each-status`);

const deleteById = async (id: number | string) => mangaAxios.del(`${basePath}/${id}`);
const deleteMangaInfo = async (ids: Array<number | string>) => mangaAxios.del(`${basePath}/bulk-del?ids=${ids}`);

const createTextChapter = async (model: ChapterType, mangaId: number | string) => mangaAxios.post(`${basePath}/chapter/save-text-chapter?mangaId=${mangaId}`, model);
const createImageChapter = async (model: FormData, mangaId: number | string) => mangaAxios.post(`${basePath}/chapter/save-image-chapter?mangaId=${mangaId}`, model);


export interface VolumeFilterInput {
    mangaId: number | string;
    q?: string;
    volumeIndex?: number | null;
}

const filterVolume = (input: VolumeFilterInput, page: number, size: number) => mangaAxios.post(`${basePath}/volume/filter?page=${page}&size=${size}&sort=id,desc`, input);
const getLastVolIndex = (mangaId: number | string) => mangaAxios.get(`${basePath}/volume/get-last-vol-index/${mangaId}`);

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
    setMangaTypeAndDisplayType,
    resetMangaType,
    deleteMangaInfo,
    createTextChapter,
    createImageChapter,
    createNewVolume,
    updateVolume,
    filterVolume,
    getChapterByVolumeId,
    getLastVolIndex,
    findById,
    deleteById,
    changeStatus,
    changeReleaseStatus,
    calculateTotalMangaEachStatus,

};

export default mangaService;