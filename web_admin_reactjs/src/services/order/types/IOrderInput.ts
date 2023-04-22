import { EORDER_STATUS, PAYMENT_METHOD } from "./IOrder";

export default interface IOrderInput {
    id?: string | number;
    paymentMethod: PAYMENT_METHOD;
    status: EORDER_STATUS;
    subscriptionPack: string | number;
    user_id: string | number;
}