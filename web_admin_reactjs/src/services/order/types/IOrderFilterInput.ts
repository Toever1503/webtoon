export default interface IOrderFilterInput{
    q?: string;
    status?: string[];
    paymentMethod?: string;
    fromDate?: string;
    toDate?: string;
}