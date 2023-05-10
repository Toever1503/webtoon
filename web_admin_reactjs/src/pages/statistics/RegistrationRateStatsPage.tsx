import { Card } from "antd";
import LineChart, { LineDataType } from "./components/LineChart";
import { useTranslation } from "react-i18next";
import { useState } from "react";

const RegistrationRateStatsPage: React.FC = () => {
    const {t} = useTranslation();


    const [registrationRateData, setRegistrationRateData] = useState<LineDataType[]>([]);
     let year = 1;
    for(let i = 0; i < 31; i++){
        registrationRateData.push({
            name: 'Tháng ' + String(year),
            "value": Math.floor(Math.random() * 100),
            "category": "Số người mua mới"
        });

        registrationRateData.push({
            name:  'Tháng ' + String(year),
            "value": Math.floor(Math.random() * 100),
            "category": "Số người gia hạn"
        });
        year++;
    }

    return <div className="py-3">
        <div className="flex space-x-3 mb-[15px]">
            <h1 className="text-[23px] font-[400] m-0">
                {t('statistic.registrationRate.title')}
            </h1>
        </div>

        <Card>
            <LineChart data={registrationRateData}/>
        </Card>
    </div>
};

export default RegistrationRateStatsPage;