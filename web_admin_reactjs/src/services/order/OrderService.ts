import { mangaAxios } from "../config/MangaAxios";
import IOrderFilterInput from "./types/IOrderFilterInput";
import IOrderInput from "./types/IOrderInput";


const basePath: String = "/orders";

const filterOrder = async (input: IOrderFilterInput) => mangaAxios.post(`${basePath}/filter?sort=id,desc`, input);

const changeStatus = async (id: string, status: string) => mangaAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const addOrder = async (input: IOrderInput) => mangaAxios.post(`${basePath}`, input);
const updateOrder = async (input: IOrderInput) => mangaAxios.put(`${basePath}/${input.id}`, input);

const orderService = {
    filterOrder,
    changeStatus,
    addOrder,
    updateOrder
};
export default orderService;
