import { mangaAxios } from "../config/MangaAxios";

const basePath = "/statistics";

const sumRevenueThisMonth = async () => mangaAxios.get(`${basePath}/sum-revenue-this-month`);
const countTotalRegisterThisMonth = async () => mangaAxios.get(`${basePath}/count-total-register-this-month`);
const countTotalRegisterTrialThisMonth = async () => mangaAxios.get(`${basePath}/count-total-register-trial-this-month`);


const sumRevenuePerDayByMonth = async (month: string) => mangaAxios.get(`${basePath}/sum-revenue-per-day-by-month?monthDate=${month}`);
const sumRevenuePerMonthByYear = async (year: number) => mangaAxios.get(`${basePath}/sum-revenue-per-month-by-year?year=${year}`);
const sumRevenuePerSubsPackByMonth = async (month: string) => mangaAxios.get(`${basePath}/sum-revenue-per-subsPack-by-month?monthDate=${month}`);


const statisticService = {
    sumRevenueThisMonth,
    countTotalRegisterThisMonth,
    countTotalRegisterTrialThisMonth,

    sumRevenuePerDayByMonth,
    sumRevenuePerMonthByYear,
    sumRevenuePerSubsPackByMonth,
};

export default statisticService;