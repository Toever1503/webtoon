import { EPAYMENT_METHOD } from "./IOrder";

export default interface IUpgradeOrderInput {
    id?: string | number;
    originalOrderId: string | number;
    subscriptionPackId: string | number;
    paymentMethod: EPAYMENT_METHOD;
}