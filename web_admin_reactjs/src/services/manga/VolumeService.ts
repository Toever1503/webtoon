import { mangaAxios } from "../config/MangaAxios";
const basePath: String = "/manga/volume";


const deleteById = (id: number | string) => mangaAxios.del(`${basePath}/${id}`);

const volumeService = {
    deleteById
};
export default volumeService;