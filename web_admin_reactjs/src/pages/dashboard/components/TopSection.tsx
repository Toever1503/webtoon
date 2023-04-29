import { ArrowDownOutlined, ArrowUpOutlined } from "@ant-design/icons";
import { Row, Card, Col, Statistic } from "antd";
import React from "react";
import { useTranslation } from "react-i18next";

const TopSection: React.FC = () => {

    const { t } = useTranslation();

    return <>
        <div className="flex gap-[15px]">
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-xl">
                        {t('dashboard.newOrderCount')}
                    </h3>}
                    value={11.28}
                    precision={2}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ArrowUpOutlined />}
                    suffix="%"
                />
            </Card>
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-xl">
                        {t('dashboard.totalRevenue')}
                    </h3>}
                    value={11.28}
                    precision={2}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ArrowUpOutlined />}
                    suffix="%"
                />
            </Card>
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-xl">
                        {t('dashboard.paymentPending')}
                    </h3>}
                    value={11.28}
                    precision={2}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ArrowUpOutlined />}
                    suffix="%"
                />
            </Card>
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-xl">
                        {t('dashboard.orderCompleted')}
                    </h3>}
                    value={11.28}
                    precision={2}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ArrowUpOutlined />}
                    suffix="%"
                />
            </Card>
            <Card className="w-1/5" bordered={false}>
                <Statistic
                    title={<h3 className="text-xl">
                        {t('dashboard.orderCancelledCount')}
                    </h3>}
                    value={11.28}
                    precision={2}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ArrowUpOutlined />}
                    suffix="%"
                />
            </Card>

        </div>
    </>
};

export default TopSection;