import React, { useState, useEffect } from 'react';

import FlowChart from "./components/FlowChart";
import OrderDashboard from "./components/OrderDashboard";
import TopSection from './components/TopSection';
import dayjs from 'dayjs';

const Dashboard: React.FC = () => {


    return <>
        <h1 className='mt-[15px] text-2xl'>
            Hôm nay {
                dayjs().format('DD/MM/YYYY')
            }
        </h1>

        <div className='pt-[15px] grid gap-[15px]'>
            <OrderDashboard />
            {/* <TopSection />
            <div className="grid grid-cols-10 gap-[15px]">
                <div className="col-span-6 w-full bg-white">
                    <FlowChart />
                </div>
                <div className="col-span-4 bg-white">
                    <OrderDashboard />
                </div>
            </div> */}
        </div>
    </>
};

export default Dashboard;