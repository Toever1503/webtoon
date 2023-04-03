import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { Area } from '@ant-design/plots';
import FlowChart from "./components/FlowChart";
import OrderDashboard from "./components/OrderDashboard";

const Dashboard: React.FC = () => {

    return <>
        <div className="grid grid-cols-10 gap-3 mt-3 ">
            <div className="col-span-7 w-full bg-white">
                <FlowChart />
            </div>
            <div className="col-span-3 bg-white">
                <OrderDashboard />
            </div>
        </div>
    </>
};

export default Dashboard;