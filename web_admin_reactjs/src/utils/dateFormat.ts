import dayjs from "dayjs";

const DatePattern = {
    dateTime: 'YYYY-MM-DD HH:mm:ss',
    date: 'YYYY-MM-DD',
    time: 'HH:mm:ss',
    month: 'YYYY-MM',
    year: 'YYYY',
    monthDay: 'MM-DD',
    monthDayTime: 'MM-DD HH:mm:ss',
};

const dateTimeFormat = (date: string | Date) => {
    return dayjs(date).format(DatePattern.dateTime);
};
const dateCustomFormat = (date: string | Date, pattern: string) => dayjs(date).format(pattern);
export {
    dateTimeFormat,
    dateCustomFormat,
};


