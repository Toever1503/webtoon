import { mangaAxios } from "../config/MangaAxios";
const basePath: String = "/manga";

export interface ChapterFilterInput {
    volumeId: number | string;
    q?: string;
    chapterIndex?: number;
}

const filterChapter = (input: ChapterFilterInput, page: number, size: number) => mangaAxios.post(`${basePath}/chapter/filter?page=${page}&size=${size}&sort=id,desc`, input);

const chapterService = {
    filterChapter
};
export default chapterService;