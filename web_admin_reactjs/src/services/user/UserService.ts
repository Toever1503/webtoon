import { IUserStatus } from "../../pages/user/types/IUserType";
import { mangaAxios} from "../config/MangaAxios";
import IUserFilterInputType from "./types/IUserFilterInputType";
import IUserInputType from "./types/IUserInputType";


const basePath: String = "/users";

const findUserById = async (id: number | string) => mangaAxios.get(`${basePath}/${id}`);
const addNewUser = async (payload: IUserInputType) => mangaAxios.post(`${basePath}`, payload);
const updateUser = async (id: number | string, payload: IUserInputType) => mangaAxios.put(`${basePath}/${id}`, payload);
const deleteUserById = async (id: number | string) => mangaAxios.del(`${basePath}/${id}`);
const filterUser = async (payload: IUserFilterInputType, page: number, size: number) => mangaAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, payload);
const changeStatus = (id: number | string, status: IUserStatus) => mangaAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const getAllAuthorities = async () => mangaAxios.get(`${basePath}/all-authorities`);

const forgotPassword = async (email: string) => mangaAxios.post(`${basePath}/forgot-password?email=${email}`);
const signin = async (input: any) => mangaAxios.post(`${basePath}/signin`, input);

const userService = {
    findUserById,
    addNewUser,
    updateUser,
    deleteUserById,
    filterUser,
    changeStatus,
    getAllAuthorities,
    forgotPassword,
    signin
};

export default userService;