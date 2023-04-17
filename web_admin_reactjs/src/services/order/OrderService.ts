import { mangaAxios } from "../config/MangaAxios";
import IOrderFilterInput from "./types/IOrderFilterInput";


const basePath: String = "/order";

const filterOrder = async (input: IOrderFilterInput) => mangaAxios.post("/orders/filter", input);

const changeStatus = async (id: string, status: string) => mangaAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const orderService = {
    filterOrder,
    changeStatus
};
export default orderService;
