import React, { useState, useEffect, useRef } from 'react';
import ReactDOM from 'react-dom';
import { Column } from '@ant-design/plots';



export type BarDataType = {
  name: string,
  value: number,
};

type BarChartProps = {
  data: BarDataType[]
}
const BarChart: React.FC<BarChartProps> = (props: BarChartProps) => {

  const [config, setConfig] = useState<any>({
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
  });

  const ref = useRef(null);

  useEffect(() => {
    config.data = props.data;
    if (ref.current) {
      // @ts-ignore
      ref.current.getChart().update({ data: props.data })
    }

  }, [props]);

  return <>
    {<Column {...config} ref={ref} />}
  </>;
};


export default BarChart;