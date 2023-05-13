import IUserType from "../../../pages/user/types/IUserType";

export default interface ISubscriptionPack{
    id?: string | number;
    name: string;
    monthCount: number;
    price: number;

    description?: String;
    updatedAt?: String;
    
    updatedBy?: IUserType;
    status: boolean;
}