import IUserType from "../../../pages/user/types/IUserType";
import ISubscriptionPack from "../../subscription_pack/types/ISubscriptionPack";

export default interface IOrder{
    id?: string | number;
    maDonHang: string;
    finalPrice: number;
    status: EORDER_STATUS;
    orderType: ORDER_TYPE;
    paymentMethod: PAYMENT_METHOD;
    created_at: string;
    updatedAt: string;
    expiredSubsDate: string;
    subs_pack_id: ISubscriptionPack;
    user_id: IUserType
    modifiedBy: IUserType
}
 
export type EORDER_STATUS = 'PENDING_PAYMENT' | 'PAID' | 'CANCELED' | 'COMPLETED' | 'REFUND_CONFIRM_PENDING' | 'REFUNDING' | 'REFUNDED';


export enum ORDER_TYPE {
    NEW,
    RENEW,
    EXTEND,
    UPGRADE
}
export enum PAYMENT_METHOD {
    VN_PAY,
    ATM
}