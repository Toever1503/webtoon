import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Pie } from '@ant-design/plots';

type PieData = {
    name: string,
    value: number
}
type PieChartProps = {
    data: PieData[]
};

const PieChart = (props: PieChartProps) => {
    const config = {
        appendPadding: 10,
        data: props.data,
        angleField: 'value',
        colorField: 'name',
        radius: 0.9,
        label: {
            type: 'inner',
            offset: '-30%',
            content: ({ percent }: any) => `${(percent * 100).toFixed(2)}%`,
            style: {
                fontSize: 14,
                textAlign: 'center',
            },
        },
        interactions: [
            {
                type: 'element-active',
            },
        ],
    };

    useEffect(() => {
        
    }, [props.data]);
    return <Pie {...config} />;
};

export default PieChart;