import { mangaAxios } from "../config/MangaAxios";
import ISubscriptionPack from "./types/ISubscriptionPack";
import ISubscriptionPackFilterInput from "./types/ISubscriptionPackFilterInput";

const basePath = "/subscription-packs";

const filterSubscriptionPack = async (input: ISubscriptionPackFilterInput, page: number = 0, size: number = 10) => mangaAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, input);

const addSubscriptionPack = async (input: ISubscriptionPack) => mangaAxios.post(`${basePath}`, input);

const updateSubscriptionPack = async (id: string | number, input: ISubscriptionPack) => mangaAxios.put(`${basePath}/${id}`, input);

const deleteSubscriptionPack = async (id: string) => mangaAxios.del(`${basePath}/${id}`);

const subscriptionPackService = {
    filterSubscriptionPack,
    addSubscriptionPack,
    updateSubscriptionPack,
    deleteSubscriptionPack
};

export default subscriptionPackService;