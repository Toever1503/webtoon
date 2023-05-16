import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import { Row, Card, Col, Statistic } from "antd";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import orderService from "../../../services/order/OrderService";
import formatVnCurrency from "../../../utils/formatVnCurrency";

const TopSection: React.FC = () => {

    const { t } = useTranslation();

    const [statisticData, setStatisticData] = React.useState<{
        totalRevenue: number,
        totalNewOrder: number,
        totalConfirmPending: number,
        totalCompleted: number,
        totalCanceled: number,
    }>({
        totalRevenue: 0,
        totalNewOrder: 0,
        totalConfirmPending: 0,
        totalCompleted: 0,
        totalCanceled: 0,
    });
    useEffect(() => {
        orderService.getDashboard().then((res) => {
            console.log('dashboardL ', res.data);
            setStatisticData(res.data)
        })
    }, []);
    return <>
        <div className="flex gap-[15px]">
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-[18px] text-center">
                        {t('dashboard.newOrderCount')}
                    </h3>}
                    value={statisticData.totalNewOrder}
                    valueStyle={{ textAlign: 'center' }}
                />
            </Card>
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-[18px] text-center">
                        {t('dashboard.totalRevenue')}
                    </h3>}
                    value={formatVnCurrency(statisticData.totalRevenue)}
                    valueStyle={{ textAlign: 'center' }}
                />
            </Card>

            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-[18px] text-center">
                        {t('dashboard.paymentPending')}
                    </h3>}
                    value={statisticData.totalConfirmPending}
                    valueStyle={{ textAlign: 'center' }}
                />
            </Card>

            {/* <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-[18px] text-center">
                        {t('dashboard.orderCompleted')}
                    </h3>}
                    value={statisticData.totalCompleted}
                    valueStyle={{ textAlign: 'center' }}
                />
            </Card> */}
            {/* <Card className="w-1/5">
                <Statistic
                    title={<h3 className="text-[18px] text-center">
                        {t('dashboard.orderCancelledCount')}
                    </h3>}
                    value={statisticData.totalCanceled}
                    valueStyle={{ textAlign: 'center' }}
                />
            </Card> */}

        </div>
    </>
};

export default TopSection;