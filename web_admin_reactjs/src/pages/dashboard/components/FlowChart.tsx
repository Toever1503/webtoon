import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Area } from '@ant-design/plots';

const FlowChart: React.FC = () => {
    const [data, setData] = useState([
        {
            "country": "demo1",
            "date": 1965,
            "value": 1390.5
        },
        {
            "country": "demo1",
            "date": 1966,
            "value": 1069.5
        },
        {
            "country": "demo1",
            "date": 1967,
            "value": 1521.7
        },
        {
            "country": "demo1",
            "date": 1968,
            "value": 1215.9
        },
        {
            "country": "中南美",
            "date": 1965,
            "value": 1009.2
        },
        {
            "country": "中南美",
            "date": 1966,
            "value":805.7
        },
        {
            "country": "中南美",
            "date": 1967,
            "value": 700.5
        },
        {
            "country": "中南美",
            "date": 1968,
            "value": 1280
        }

    ]);

    const config = {
        data,
        xField: 'date',
        yField: 'value',
        seriesField: 'country'
    };

    return <Area {...config} />;

}

export default FlowChart;