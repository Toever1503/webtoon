import { EyeOutlined } from "@ant-design/icons";
import React from "react"
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const OrderDashboard: React.FC = () => {
    const { t } = useTranslation();

    return <>
        <div>
            <div className="p-4">
                <h3 className="text-xl">{t('dashboard.orderLatest.title')}</h3>
                <div className="flex flex-col">
                    <div className="overflow-x-auto ">
                        <div className="py-2 inline-block min-w-full">
                            <div className="overflow-hidden">
                                <table className="min-w-full">
                                    <thead className="bg-gray-50 border-b">
                                        <tr>
                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                #
                                            </th>
                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                {t('dashboard.orderLatest.table.createdBy')}
                                            </th>
                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                {t('dashboard.orderLatest.table.subscriptionPack')}
                                            </th>
                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                {t('dashboard.orderLatest.table.price')}
                                            </th>
                                            <th className="">
                                                <div className="w-3 h-3 rounded-full bg-gray-200 mx-4"></div>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr className="bg-white border-b border-gray-600 transition duration-300 ease-in-out hover:bg-gray-100" >
                                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">1</td>
                                            <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                Mark
                                            </td>
                                            <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                Otto
                                            </td>
                                            <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                @mdo
                                            </td>
                                            <td>
                                                <Link to='/' className="text-center flex items-center justify-center">
                                                    <EyeOutlined/>
                                                </Link>
                                            </td>
                                        </tr>
                                  
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>
}

export default OrderDashboard;