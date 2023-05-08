import { Button, Card, Col, Divider, Row, Space, Statistic } from "antd";
import { useTranslation } from "react-i18next";
import BarChart from "./components/BarChart";


const RevenueStatsPage: React.FC = () => {
    const { t } = useTranslation();

    return <div className="py-3">

        <h1 className="text-[23px] font-[400] m-0">
            {t('statistic.revenue.title')}
        </h1>

        <div className="flex space-x-[15px] mt-[15px]">
            <div className="w-[620px]">
                <Card bordered={false} style={{ width: '100%' }}>
                    <Col span={12}>
                        <Statistic title={t('statistic.revenue.totalRevenueThisMonth')} value={112893} />

                    </Col>
                </Card>

                <Card bordered={false} style={{ width: '100%', height: '140px', marginTop: '15px' }}>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Statistic title={t('statistic.revenue.totalSubscriber')} value={112893} />

                        </Col>
                        <Col span={12}>

                            <Statistic title={t('statistic.revenue.totalSubscriberOnTrial')} value={112893} />
                        </Col>
                    </Row>
                </Card>

                <Card bordered={false} style={{ width: '100%', marginTop: '15px' }}>
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
            </div>

            <div className="w-[800px] grid space-y-[15px]">

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
            </div>
        </div>
    </div>
};

export default RevenueStatsPage;