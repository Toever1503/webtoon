import { mangaAxios } from "../config/MangaAxios";
import { EORDER_STATUS } from "./types/IOrder";
import IOrderFilterInput from "./types/IOrderFilterInput";
import IOrderInput from "./types/IOrderInput";
import IUpgradeOrderInput from "./types/IUpgradeOrderInput";


const basePath: String = "/orders";

const filterOrder = async (input: IOrderFilterInput) => mangaAxios.post(`${basePath}/filter?sort=id,desc`, input);



const changeStatus = async (id: string | number, status: EORDER_STATUS) => mangaAxios.patch(`${basePath}/${id}/change-status?status=${status}`);
const getDetailOrderById = async (id: string | number) => mangaAxios.get(`${basePath}/${id}`);

const addOrder = async (input: IOrderInput) => mangaAxios.post(`${basePath}`, input);
const updateOrder = async (input: IOrderInput) => mangaAxios.put(`${basePath}/${input.id}`, input);

const upgradeOrder = async (input: IUpgradeOrderInput) => mangaAxios.post(`${basePath}/upgrade-order`, input);
const deleteById = async (id: string | number) => mangaAxios.del(`${basePath}/${id}`);


const getDashboard = async () => mangaAxios.del(`${basePath}/dashboard`);
const sumRevenueInLast7Days = async () => mangaAxios.del(`${basePath}/sum-revenue-in-last-7-days`);

const sendMailRenewSubscriptionPack = async (id: number | string) => mangaAxios.get(`${basePath}/send-mail-renew-subscription-pack/${id}`);

const orderService = {
    getDetailOrderById,
    filterOrder,
    changeStatus,
    addOrder,
    updateOrder,
    upgradeOrder,
    deleteById,
    getDashboard,
    sumRevenueInLast7Days,
    sendMailRenewSubscriptionPack
};
export default orderService;
