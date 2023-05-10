import { Button, Card, Col, Divider, Row, Space, Statistic } from "antd";
import { useTranslation } from "react-i18next";
import BarChart from "./components/BarChart";
import LineChart, { LineDataType } from "./components/LineChart";
import { useState } from "react";


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

    return <div className="py-3">

        <h1 className="text-[23px] font-[400] m-0">
            {t('statistic.revenue.title')}
        </h1>

        <div className="grid grid-cols-3 gap-[15px] mt-[15px]">
            <Card bordered={false}>
                <Statistic
                    title={<h3 className="font-semibold text-xl">
                        {t('statistic.revenue.totalRevenueThisMonth')}
                    </h3>}
                    value={11.28}
                />
            </Card>
            <Card bordered={false}>
                <Statistic
                    title={<h3 className="font-semibold text-xl">
                        {t('statistic.revenue.totalSubscriber')}
                    </h3>}
                    value={9.3}
                />
            </Card>

            <Card bordered={false}>
                <Statistic
                    title={<h3 className="font-semibold text-xl">
                        {t('statistic.revenue.totalSubscriberOnTrial')}
                    </h3>}
                    value={9.3}
                />
            </Card>
        </div>

        <div className="grid grid-cols-2 gap-[15px] mt-[15px]">
            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.revenueByDay')}
                    </h3>
                    <Button>
                        {t('buttons.filter')}
                    </Button>

                </Space>

                <BarChart data={[{
                    name: 'fsa',
                    value: 27,
                }]} />
            </Card>

            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.monthlyRevenue')}
                    </h3>
                    <Button>
                        {t('buttons.filter')}
                    </Button>
                </Space>

                <BarChart data={[{
                    name: 'fsa',
                    value: 27,
                }, {
                    name: 'fsafff',
                    value: 517,
                }]} />
            </Card>

            <Card bordered={false} style={{ width: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                        {t('statistic.revenue.totalRevenueBySubsPack')}
                    </h3>
                    <Button>
                        {t('buttons.filter')}
                    </Button>
                </Space>

                <BarChart data={[{
                    name: 'fsa',
                    value: 27,
                }]} />

            </Card>

            <Card bordered={false} style={{ width: '100%', height: '100%' }}>
                <Space className="mb-[15px]">
                    <h3>
                    {t('statistic.registrationRate.title')}
                    </h3>
                    <Button>
                        {t('buttons.filter')}
                    </Button>
                </Space>

                <LineChart data={registrationRateData} />
            </Card>

        </div>
    </div>
};

export default RevenueStatsPage;