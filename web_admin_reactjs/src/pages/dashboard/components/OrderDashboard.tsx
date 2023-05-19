import { EyeOutlined } from "@ant-design/icons";
import React, { useEffect } from "react"
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import IOrder from "../../../services/order/types/IOrder";
import orderService from "../../../services/order/OrderService";
import formatVnCurrency from "../../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../../utils/dateFormat";
import { hasAnyAuths } from "../../../plugins/cookieUtil";

const OrderDashboard: React.FC = () => {
    const { t } = useTranslation();

    const [latestOrder, setLatestOrder] = React.useState<IOrder[]>([]);


    useEffect(() => {
        orderService.filterOrder({
            status: ['USER_CONFIRMED_BANKING', 'PENDING_PAYMENT'],
        })
            .then((res) => {
                console.log('latest order: ', res.data);

                setLatestOrder(res.data.content);
            })
    }, []);

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
                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                {t('dashboard.orderLatest.table.createdAt')}
                                            </th>

                                            <th scope="col"
                                                className="text-sm font-medium text-gray-900 px-6 py-4 text-left">
                                                {t('dashboard.orderLatest.table.action')}
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            latestOrder.map((order: IOrder, index: number) =>
                                                <tr key={index} className="bg-white border-b border-gray-600 transition duration-300 ease-in-out hover:bg-gray-100" >
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{index + 1}</td>
                                                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                        {
                                                            order.user_id.fullName
                                                        }
                                                    </td>
                                                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                        {
                                                            order.subs_pack_id.name
                                                        }
                                                        {
                                                            order.orderType === 'UPGRADE' && <span className="text-red-400"> ({t('order.table.upgrade')})</span>
                                                        }

                                                        {
                                                            order.orderType === 'RENEW' && <span className="text-red-400"> ({t('order.table.renew')})</span>
                                                        }
                                                    </td>
                                                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                                                        {
                                                            formatVnCurrency(order.finalPrice)
                                                        }
                                                    </td>
                                                    <td>
                                                        {
                                                            dateTimeFormat(order.created_at)
                                                        }
                                                    </td>
                                                    <td>
                                                        {
                                                            order.status === 'USER_CONFIRMED_BANKING' ?
                                                                <Link to={`/orders/handle/${order.id}`}>
                                                                    Xử lý TT
                                                                </Link>
                                                                : '-'
                                                        }
                                                    </td>
                                                </tr>
                                            )
                                        }
                                        {
                                            latestOrder.length <= 0 &&
                                            <tr>
                                                <td colSpan={5} className="px-6 py-4 whitespace-nowrap text-sm text-center font-medium text-gray-500">
                                                    Chưa có đơn hàng nào!
                                                </td>
                                            </tr>
                                        }

                                        {
                                            hasAnyAuths(['ROLE_MANAGE_ORDER']) && <tr>
                                                <td colSpan={5} className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                    <Link to='/orders'>
                                                        Xem tất cả</Link>
                                                </td>
                                            </tr>
                                        }


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