export default interface ISubscriptionPack{
    id?: string | number;
    name: string;
    dateCount?: number;
    monthCount: number;
    originalPrice: number;
    discountPrice?: number;

    createdAt?: String;
    updatedAt?: String;
    
    createdBy?: String;
    updatedBy?: String;
}