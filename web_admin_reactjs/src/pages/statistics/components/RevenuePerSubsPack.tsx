import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Pie } from '@ant-design/plots';

const RevenuePerSubsPack = () => {
  const data = [
    {
      name: 'fsa',
      value: 27,
    },

    {
      name: '5gg',
      value: 52,
    },

  ];
  const config = {
    showTitle: true,
    title: 'Revenue per subscription pack',
    appendPadding: 10,
    data,
    angleField: 'value',
    colorField: 'name',
    radius: 0.8,
    label: {
      type: 'outer',
      content: '{name} {value}',
    },
    interactions: [
      {
        type: 'pie-legend-active',
      },
      {
        type: 'element-active',
      },
    ],
  };
  return <Pie {...config} />;
};

export default RevenuePerSubsPack;
