import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Line } from '@ant-design/plots';



export type LineDataType = {
    name: string;
    value: number;
    category: string;
}

type LineChartProps = {
    data: any;
}
const LineChart: React.FC<LineChartProps> = (props: LineChartProps) => {
    // const [data, setData] = useState<any>([]);

   


    const config = {
        data: props.data || [{
            name: '0',
            "value": 0,
            "category": "Empty"
        }],
        xField: 'name',
        yField: 'value',
        seriesField: 'category',
        yAxis: {
            label: {
                // 数值格式化为千分位
                formatter: (v: any) => `${v}`.replace(/\d{1,3}(?=(\d{3})+$)/g, (s) => `${s},`),
            },
        },
        color: ['#1979C9', '#FAA219'],
    };

    return <Line {...config} />;
};

export default LineChart;