import { IUserStatus } from "../../pages/user/types/IUserType";
import { userAxios } from "../config/UserAxios";
import IUserInputType from "./types/IUserInputType";


const basePath: String = "/users";

const findUserById = async (id: number | string) => userAxios.get(`${basePath}/${id}`);
const addNewUser = async (payload: IUserInputType) => userAxios.post(`${basePath}`, payload);
const updateUser = async (id: number | string, payload: IUserInputType) => userAxios.put(`${basePath}/${id}`, payload);
const deleteUserById = async (id: number | string) => userAxios.del(`${basePath}/${id}`);
const filterUser = async (payload: IUserInputType) => userAxios.post(`${basePath}`, payload);
const changeStatus = (id: number | string, status: IUserStatus) => userAxios.patch(`${basePath}/${id}/change-status?status=${status}`);

const userService = {
    findUserById,
    addNewUser,
    updateUser,
    deleteUserById,
    filterUser,
    changeStatus,
};

export default userService;