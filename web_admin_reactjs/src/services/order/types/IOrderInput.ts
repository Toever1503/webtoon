import { EORDER_STATUS, EPAYMENT_METHOD } from "./IOrder";

export default interface IOrderInput {
    id?: string | number;
    paymentMethod: EPAYMENT_METHOD;
    status: EORDER_STATUS;
    subscriptionPack: string | number;
    user_id: string | number;
}