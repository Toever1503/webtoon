import { mangaAxios } from "../config/MangaAxios";

const basePath = "/statistics";

const sumRevenueThisMonth = async () => mangaAxios.get(`${basePath}/sum-revenue-this-month`);

const countTotalCompletedOrderThisMonth = async () => mangaAxios.get(`${basePath}/sum-completed-order-this-month`);
const countTotalNewRegisterThisMonth = async () => mangaAxios.get(`${basePath}/count-total-new-register-this-month`);
const countTotalRegisterTrialThisMonth = async () => mangaAxios.get(`${basePath}/count-total-register-trial-this-month`);


const sumRevenuePerDayByMonth = async (month: string) => mangaAxios.get(`${basePath}/sum-revenue-per-day-by-month?monthDate=${month}`);
const sumRevenuePerMonthByYear = async (year: number) => mangaAxios.get(`${basePath}/sum-revenue-per-month-by-year?year=${year}`);

const sumRevenuePerSubsPackByMonth = async (yearMonth: string) => mangaAxios.get(`${basePath}/sum-revenue-per-subsPack-by-month?monthDate=${yearMonth}`);
const countTotalPeoplePerSubsPackByMonth = async (yearMonth: string) => mangaAxios.get(`${basePath}/count-total-people-per-subsPack-by-month?monthDate=${yearMonth}`);

const countSubscriberStatusPerMonthByYear = async (year: number) => mangaAxios.get(`${basePath}/count-total-subscriber-status-per-month-by-year?year=${year}`);


const filterUserSubscriptionPackStatus = async (q: string = '', type: string = 'ALL', page: number = 0, size: number = 10) => mangaAxios.get(`${basePath}/filter-user-subscriptionPack-status?page=${page}&size=${size}&q=${q}&type=${type}`);


const statisticService = {
    sumRevenueThisMonth,
    countTotalCompletedOrderThisMonth,
    countTotalNewRegisterThisMonth,
    countTotalRegisterTrialThisMonth,

    sumRevenuePerDayByMonth,
    sumRevenuePerMonthByYear,

    sumRevenuePerSubsPackByMonth,
    countTotalPeoplePerSubsPackByMonth,

    countSubscriberStatusPerMonthByYear,

    filterUserSubscriptionPackStatus
};

export default statisticService;