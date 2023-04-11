import IUserType from "../../user/types/UserType";

export default interface ISubscriptionPackType{
    id: number;
    name: string;
    desc: string;
    price: number;
    dayCount: number;
    createdAt: string | Date;
    modifiedAt: string | Date;
    createdBy: IUserType;
    modifiedBy: IUserType;
}