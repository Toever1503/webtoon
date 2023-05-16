import IUserType from "../../../pages/user/types/IUserType";
import ISubscriptionPack from "../../subscription_pack/types/ISubscriptionPack";

export default interface IOrder {
    id?: string | number;
    maDonHang: string;
    finalPrice: number;
    status: EORDER_STATUS;
    orderType: EORDER_TYPE;
    paymentMethod: EPAYMENT_METHOD;
    created_at: string;
    updatedAt: string;
    expiredSubsDate: string;
    subs_pack_id: ISubscriptionPack;
    user_id: IUserType;
    modifiedBy: IUserType;
    hasUpgradingOrder: boolean;
    paymentDto: IPaymentDto;
}

type IPaymentDto = {
    id: string;
    bankTranNo: string;
    transNo: string;
}
export type EORDER_STATUS =  'PENDING_PAYMENT' | 'CANCELED' | 'COMPLETED' | 'USER_CONFIRMED_BANKING';


export type EORDER_TYPE = 'NEW' | 'UPGRADE' | 'RENEW';
export type EPAYMENT_METHOD = 'VN_PAY' | 'ATM';