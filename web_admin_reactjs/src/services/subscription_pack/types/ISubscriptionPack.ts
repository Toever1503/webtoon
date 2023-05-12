import IUserType from "../../../pages/user/types/IUserType";

export default interface ISubscriptionPack{
    id?: string | number;
    name: string;
    dateCount?: number;
    monthCount: number;
    price: number;

    createdAt?: String;
    updatedAt?: String;
    
    createdBy?: IUserType;
    updatedBy?: IUserType;
    status: boolean;
}