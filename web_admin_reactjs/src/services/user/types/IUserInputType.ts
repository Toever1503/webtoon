import { IUserSex, IUserStatus } from "../../../pages/user/types/IUserType";

export default interface IUserInputType {
    id?: number | string;
    fullName: string;
    username: string;
    email: string;
    password?: string;
    phone?: string;
    sex: IUserSex;
    status: IUserStatus;
    authorities: number[] | string[]
    role: number | string;
}