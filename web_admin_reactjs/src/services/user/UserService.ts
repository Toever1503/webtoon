import { IUserStatus } from "../../pages/user/types/IUserType";
import { userAxios } from "../config/UserAxios";
import IUserFilterInputType from "./types/IUserFilterInputType";
import IUserInputType from "./types/IUserInputType";


const basePath: String = "/users";

const findUserById = async (id: number | string) => userAxios.get(`${basePath}/${id}`);
const addNewUser = async (payload: IUserInputType) => userAxios.post(`${basePath}`, payload);
const updateUser = async (id: number | string, payload: IUserInputType) => userAxios.put(`${basePath}/${id}`, payload);
const deleteUserById = async (id: number | string) => userAxios.del(`${basePath}/${id}`);
const filterUser = async (payload: IUserFilterInputType, page: number, size: number) => userAxios.post(`${basePath}/filter?page=${page}&size=${size}&sort=id,desc`, payload);
const changeStatus = (id: number | string, status: IUserStatus) => userAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const getAllAuthorities = async () => userAxios.get(`${basePath}/all-authorities`);

const forgotPassword = async (email: string) => userAxios.post(`${basePath}/forgot-password?email=${email}`);
const signin = async (input: any) => userAxios.post(`${basePath}/signin`, input);

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