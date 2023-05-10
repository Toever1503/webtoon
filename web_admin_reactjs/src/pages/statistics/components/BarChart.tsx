import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Column } from '@ant-design/plots';



type BarDataType = {
  name: string,
  value: number,
};

type BarChartProps = {
  data: [BarDataType]
}
const BarChart: React.FC<BarChartProps> = (props: BarChartProps) => {

  const config = {
    data: props.data || [],
    xField: 'name',
    yField: 'value',
    xAxis: {
      label: {
        autoHide: true,
        autoRotate: false,
      },
    },
    meta: {
      type: {
        alias: '类别',
      },
      sales: {
        alias: '销售额',
      },
    },
    minColumnWidth: 20,
    maxColumnWidth: 20,
  };
  return <Column {...config} />;
};


export default BarChart;