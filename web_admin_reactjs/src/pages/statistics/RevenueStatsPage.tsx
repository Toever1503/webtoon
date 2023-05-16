import { Button, Card, Col, DatePicker, Divider, Row, Space, Statistic } from "antd";
import { useTranslation } from "react-i18next";
import BarChart, { BarDataType } from "./components/BarChart";
import LineChart, { LineDataType } from "./components/LineChart";
import { useEffect, useState } from "react";
import formatVnCurrency from "../../utils/formatVnCurrency";
import statisticService from "../../services/stats/StatisticService";
import dayjs, { Dayjs } from "dayjs";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import subscriptionPackService from "../../services/subscription_pack/subscriptionPackService";
import React from "react";
import TopSection from "../dashboard/components/TopSection";


const RevenueStatsPage: React.FC = () => {
    const { t } = useTranslation();


    const [registrationRateData, setRegistrationRateData] = useState<LineDataType[]>([]);
    let year = 1;
    for (let i = 0; i < 31; i++) {
        registrationRateData.push({
            name: 'Tháng ' + String(year),
            "value": Math.floor(Math.random() * 100),
            "category": "Số người mua mới"
        });

        registrationRateData.push({
            name: 'Tháng ' + String(year),
            "value": Math.floor(Math.random() * 100),
            "category": "Số người gia hạn"
        });
        year++;
    }


    const [totalRevenueThisMonth, setTotalRevenueThisMonth] = useState<number>(0);
    const [totalSubscriber, setTotalSubscriber] = useState<number>(0);
    const [totalSubscriberOnTrial, setTotalSubscriberOnTrial] = useState<number>(0);


    const [revenueByDayDate, setRevenueByDayDate] = useState<Dayjs | null>(dayjs());
    const [revenueByDayData, setRevenueByDayData] = useState<LineDataType[]>([]);
    const callApiGetRevenueByDay = (date: Dayjs) => {
        console.log(date.format('YYYY-MM-DD'));

        let daysInMonth: number = date.daysInMonth();
        let dataMap = new Map<String, number>();

        for (let i = 1; i <= daysInMonth; i++) {
            dataMap.set(date.format('YYYY-MM-') + (i < 10 ? `0${i}` : i), 0);
        }

        console.log('data: ', dataMap);
        statisticService
            .sumRevenuePerDayByMonth(date.format('YYYY-MM'))
            .then((res) => {
                res.data.
                    forEach((item: [String, number]) => {
                        console.log('item: ', item);
                        dataMap.set(item[0], item[1]);
                    });

                let data: LineDataType[] = [];
                dataMap.forEach((value, key) => {
                    data.push({
                        name: String(key.substring(8)),
                        value: value,
                        category: 'Doanh thu từng ngày'
                    });
                });
                setRevenueByDayData(data);
            });
    }

    const [monthlyRevenueDate, setMonthlyRevenueDate] = useState<Dayjs | null>(dayjs());
    const [monthlyRevenueData, setMonthlyRevenueData] = useState<LineDataType[]>([]);

    const onCallApiGetMonthlyRevenue = (date: Dayjs) => {
        console.log('year: ', date.year());

        let year: number = date.year();
        let dataMap = new Map<String, number>();
        for (let i = 1; i <= 12; i++) {
            dataMap.set(year + '-' + (i < 10 ? `0${i}` : i), 0);
        }

        console.log('data: ', dataMap);
        statisticService
            .sumRevenuePerMonthByYear(year)
            .then(res => {
                res.data.forEach((item: [String, number]) => {
                    dataMap.set(item[0], item[1]);
                });


                console.log('monthlyRevenueData: ', dataMap);
                let data: LineDataType[] = [];
                dataMap.forEach((value, key) => {
                    data.push({
                        name: String(key.substring(5)),
                        value: value,
                        category: 'Doanh thu của từng tháng'
                    });
                });
                setMonthlyRevenueData(data);
            });
    };

    const [revenuePerSubDate, setRevenuePerSubDate] = useState<Dayjs | null>(dayjs());
    const [revenuePerSubData, setRevenuePerSubData] = useState<BarDataType[]>([]);
    const [subscriptionPackData, setSubscriptionPackData] = useState<ISubscriptionPack[]>([]);
    const [forceInitializeRevenuePerSubs, setForceInitializeRevenuePerSubs] = useState<boolean>(false);

    const onCallApiGetRevenuePerSub = (date: Dayjs, subsPack: ISubscriptionPack[]) => {

        let dataMap = new Map<number | string, number>();

        statisticService.sumRevenuePerSubsPackByMonth(date.format('YYYY-MM'))
            .then(res => {
                res.data.forEach((item: [number, string, number]) => {
                    dataMap.set(item[0], item[2]);
                });

                let data = subsPack.map((item: ISubscriptionPack) => {
                    return ({
                        name: item.name,
                        value: dataMap.get(item.id || 0) || 0,
                    })
                });

                setRevenuePerSubData(data);
            });

    };

    const [rateSubscriberDate, setRateSubscriberDate] = useState<Dayjs | null>(dayjs());
    const [rateSubscriberData, setRateSubscriberData] = useState<LineDataType[]>([]);
    const onCallApiGetRateSubscriber = (date: Dayjs) => {
        let year: number = date.year();
        statisticService.countSubscriberStatusPerMonthByYear(year).then((res) => {
            let data: LineDataType[] = res.data.map((item: [number, number, String]) => ({
                name: Number(item[0]) < 10 ? `0${item[0]}` : String(item[0]),
                value: String(item[1]),
                category: String(item[2]),
            }));
            setRateSubscriberData([...data]);

            console.log('res111: ', res.data[0] < 0);
        });

    };


    useEffect(() => {

        statisticService.sumRevenueThisMonth().then((res) => {
            setTotalRevenueThisMonth(res.data);
        });
        statisticService.countTotalRegisterThisMonth().then((res) => {
            setTotalSubscriber(res.data);
        });
        statisticService.countTotalRegisterTrialThisMonth().then((res) => {
            setTotalSubscriberOnTrial(res.data);
        });

        subscriptionPackService.getAllSubscriptionPack()
            .then((res) => {
                setSubscriptionPackData(res.data);
                onCallApiGetRevenuePerSub(revenuePerSubDate || dayjs(), res.data)
            });

        callApiGetRevenueByDay(revenueByDayDate || dayjs());
        onCallApiGetMonthlyRevenue(monthlyRevenueDate || dayjs());
        onCallApiGetRateSubscriber(rateSubscriberDate || dayjs());

    }, []);

    return <div className="py-3">

        <h1 className="text-[23px] font-[400] m-0">
            {t('statistic.revenue.title')}
        </h1>

        <div>
            <div className="flex gap-[15px] mt-[15px]">

                <Card className="w-1/4" bordered={false}>
                    <Statistic
                        title={<h3 className="font-semibold text-xl">
                            {t('statistic.revenue.totalRevenueThisMonth')}
                        </h3>}
                        value={formatVnCurrency(totalRevenueThisMonth)}
                    />
                </Card>

                <Card className="w-1/4" bordered={false}>
                    <Statistic
                        title={<h3 className="text-[18px] text-center">
                            {t('dashboard.orderCompleted')}
                        </h3>}
                        value={totalSubscriber}
                        valueStyle={{ textAlign: 'center' }}
                    />
                </Card>
{/* 
                <Card className="w-1/4" bordered={false}>
                    <Statistic
                        title={<h3 className="font-semibold text-xl">
                            {t('statistic.revenue.totalSubscriber')}
                        </h3>}
                        value={totalSubscriber}
                    />
                </Card>

                <Card className="w-1/4" bordered={false}>
                    <Statistic
                        title={<h3 className="font-semibold text-xl">
                            {t('statistic.revenue.totalSubscriberOnTrial')}
                        </h3>}
                        value={totalSubscriberOnTrial}
                    />
                </Card> */}
            </div>
        </div>

        <div className="grid grid-cols-2 gap-[15px] mt-[15px]">
            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.revenueByDay')}
                    </h3>
                    <DatePicker picker="month" value={revenueByDayDate} onChange={e => {
                        setRevenueByDayDate(e);
                        callApiGetRevenueByDay(e || dayjs());
                    }} />

                </Space>

                <LineChart data={revenueByDayData} />
            </Card>

            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.monthlyRevenue')}
                    </h3>

                    <DatePicker picker="year" value={monthlyRevenueDate} onChange={e => {
                        setMonthlyRevenueDate(e);
                        onCallApiGetMonthlyRevenue(e || dayjs());
                    }} />
                </Space>

                <LineChart data={monthlyRevenueData} />
            </Card>

            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.totalRevenueBySubsPack')}
                    </h3>

                    <DatePicker picker="month" value={revenuePerSubDate} onChange={e => {
                        setRevenuePerSubDate(e);
                        onCallApiGetRevenuePerSub(e || dayjs(), subscriptionPackData);
                    }} />
                </Space>

                <BarChart data={revenuePerSubData} />

            </Card>

            <Card bordered={false} style={{ width: '100%', height: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.registrationRate.title')}
                    </h3>
                    <DatePicker picker="year" onChange={e => {
                        setRateSubscriberDate(e);
                        onCallApiGetRateSubscriber(e || dayjs());
                    }} value={rateSubscriberDate} />
                </Space>

                <LineChart data={rateSubscriberData} />
            </Card>

        </div>
    </div>
};

export default RevenueStatsPage;