import { mangaAxios } from "../config/MangaAxios";
const basePath: String = "/manga/chapter";

export interface ChapterFilterInput {
    volumeId: number | string | null;
    mangaId: number | string | null;
    q?: string;
    chapterIndex?: number;
    displayType: 'CHAP' | 'VOL'
}

const findById = (id: number | string) => mangaAxios.get(`${basePath}/${id}`);
const filterChapter = (input: ChapterFilterInput, page: number, size: number) => mangaAxios.post(`${basePath}/filter/?page=${page}&size=${size}&sort=id,desc`, input);
const getLastChapterIndex = (mangaId: number | string) => mangaAxios.get(`${basePath}/get-last-chapter-index/${mangaId}`);

const deleteById = (id: number | string) => mangaAxios.del(`${basePath}/${id}`);

const chapterService = {
    filterChapter,
    findById,
    getLastChapterIndex,
    deleteById
};
export default chapterService;