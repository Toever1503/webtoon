import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Line } from '@ant-design/plots';
import { useTranslation } from 'react-i18next';
import dayjs from 'dayjs';
import orderService from '../../../services/order/OrderService';
import formatVnCurrency from '../../../utils/formatVnCurrency';

const FlowChart: React.FC = () => {
    const { t } = useTranslation();

    const [config, setConfig] = useState<any>({
        data: [],
        xField: 'date',
        yField: 'doanh thu',
        label: {},
        point: {
            size: 5,
            shape: 'diamond',
            style: {
                fill: 'white',
                stroke: '#5B8FF9',
                lineWidth: 2,
            },
        },
        tooltip: {
            showMarkers: false,
        },
        state: {
            active: {
                style: {
                    shadowBlur: 4,
                    stroke: '#000',
                    fill: 'red',
                },
            },
        },
        interactions: [
            {
                type: 'marker-active',
            },
        ],
    });;


    let last7Days: Map<any, any> = new Map();

    const [hasInit, setHasInit] = useState(false);
    useEffect(() => {
        last7Days.set(dayjs().format('YYYY-MM-DD'), formatVnCurrency(0));
        for (let i = 0; i < 6; i++) {
            last7Days.set(dayjs().subtract(i + 1, 'day').format('YYYY-MM-DD'), formatVnCurrency(0));
        }

        console.log('last 7 days: ', last7Days);

        if (hasInit) return;
        orderService
            .sumRevenueInLast7Days()
            .then(res => {
                res.data.forEach((item: any) => {
                    last7Days.set(item[0], formatVnCurrency(item[1]));
                });


                const newChartData: any[] = [];
                last7Days.forEach((value, key) => {
                    newChartData.push({
                        date: key,
                        'doanh thu': value
                    });
                });

                config.data = newChartData.reverse();
                setConfig(config);
            })
            .finally(() => setHasInit(true));

    }, [])



    return <div className='p-[10px]'>
        <h3 className='mb-[50px] text-xl'>
            {t('dashboard.recentRevenueIn7Days')}
        </h3>
        {
            hasInit &&
            <Line {...config} />
        }
    </div>;

}

export default FlowChart;