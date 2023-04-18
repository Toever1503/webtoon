import { mangaAxios } from "../config/MangaAxios";
import IOrderFilterInput from "./types/IOrderFilterInput";


const basePath: String = "/orders";

const filterOrder = async (input: IOrderFilterInput) => mangaAxios.post(`${basePath}/filter`, input);

const changeStatus = async (id: string, status: string) => mangaAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const orderService = {
    filterOrder,
    changeStatus
};
export default orderService;
