import { mangaAxios } from "../config/MangaAxios";
import ISubscriptionPack from "./types/ISubscriptionPack";
import ISubscriptionPackFilterInput from "./types/ISubscriptionPackFilterInput";

const basePath = "/subscription-packs";

const getAllSubscriptionPack = async () => mangaAxios.get(`${basePath}/get-all`);
const filterSubscriptionPack = async (input: ISubscriptionPackFilterInput, page: number = 0, size: number = 10) => mangaAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, input);

const addSubscriptionPack = async (input: ISubscriptionPack) => mangaAxios.post(`${basePath}`, input);

const updateSubscriptionPack = async (id: string | number, input: ISubscriptionPack) => mangaAxios.put(`${basePath}/${id}`, input);

const deleteSubscriptionPack = async (id: string | number) => mangaAxios.del(`${basePath}/${id}`);

const subscriptionPackService = {
    getAllSubscriptionPack,
    filterSubscriptionPack,
    addSubscriptionPack,
    updateSubscriptionPack,
    deleteSubscriptionPack
};

export default subscriptionPackService;