import IUserType from "../../../pages/user/types/IUserType";
import ISubscriptionPack from "../../subscription_pack/types/ISubscriptionPack";

export default interface IOrder{
    id?: string | number;
    maDonHang: string;
    finalPrice: number;
    status: number;
    paymentMethod: PAYMENT_METHOD;
    created_at: string;
    updatedAt: string;
    expireDate: string;
    subs_pack_id: ISubscriptionPack;
    user_id: IUserType
}
 
export enum PAYMENT_METHOD {
    VN_PAY,
    ATM
}