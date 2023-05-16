import { ArrowLeftOutlined, LoadingOutlined } from "@ant-design/icons";
import { Button, Card, DatePicker, Descriptions, Input, PaginationProps, Popconfirm, Select, Space, Spin, Table, Tabs, Tag, Timeline, Tooltip } from "antd";

import { useTranslation } from "react-i18next";

import { useDispatch, useSelector } from "react-redux";
import formatVnCurrency from "../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../utils/dateFormat";
import { useEffect, useState } from "react";
import IOrder from "../../services/order/types/IOrder";
import orderService from "../../services/order/OrderService";
import { useNavigate, useParams, useRoutes } from "react-router-dom";
import { showNofification } from "../../stores/features/notification/notificationSlice";


const OrderHandlePage: React.FC = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const navigate = useNavigate();

    const [order, setOrder] = useState<IOrder>();
    const params = useParams()


    const [hasCompleted, setHasCompleted] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const onConfirmCompleted = () => {
        if (isLoading) return;
        setIsLoading(true);

        if (params.id) {
            orderService.changeStatus(params.id, 'COMPLETED')
                .then(() => {
                    setHasCompleted(true);
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Xác nhận hoàn tất đơn hàng thành công!'
                    }));
                })
                .catch((err) => {
                    console.log('mark completed order failed: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: 'Xác nhận hoàn tất đơn hàng thất bại!'
                    }));
                })
                .finally(() => {
                    setIsLoading(false);
                })
        }
    };
    useEffect(() => {
        if (params.id)
            orderService.getDetailOrderById(params.id)
                .then((res) => {
                    setOrder(res.data);
                    if (res.data.status === 'COMPLETED') setHasCompleted(true);
                });

        console.log('params', params.id);

    }, []);

    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
            <Tooltip title="Quay lại">
                    <ArrowLeftOutlined onClick={() => navigate(-1)} />
                </Tooltip>
                <h1 className="text-[23px] font-[400] m-0">
                    Xử lý đơn hàng
                </h1>

            </div>

            <Card>
                <div className="flex gap-[50px]">

                    <div className="min-w-[200px]">
                        <h3 className="mb-[15px]">Trạng thái đơn hàng: </h3>

                        <Timeline
                            mode="left"
                            items={[
                                {
                                    color: 'green',
                                    children: 'Chờ kiểm tra thanh toán',
                                },
                                {
                                    color: `${hasCompleted ? 'green' : 'gray'}`,
                                    children: 'Hoàn tất',
                                },
                            ]}
                        />

                        {
                            !hasCompleted &&
                            <Button size="small" type="primary" onClick={onConfirmCompleted} loading={isLoading} className="mt-3">Xác nhận hoàn tất</Button>
                        }

                    </div>


                    <div>
                        <h3 className="mb-[15px]">Thông tin đơn hàng: </h3>

                        {
                            order && <Descriptions bordered labelStyle={{fontWeight: '600'}}>
                                <Descriptions.Item label={t('order.table.orderNumber')}>{order?.maDonHang}</Descriptions.Item>

                                <Descriptions.Item label={t('order.table.subscription')}>
                                    {order?.subs_pack_id?.name} <br />
                                    {
                                        order?.orderType === 'UPGRADE' && <span className="text-red-400"> ({t('order.table.upgrade')})</span>
                                    }
                                </Descriptions.Item>

                                <Descriptions.Item label={t('order.table.finalPrice')}>
                                    {formatVnCurrency(order?.finalPrice)}
                                </Descriptions.Item>

                                <Descriptions.Item label={t('order.table.paymentMethod')}>{
                                    order?.paymentMethod
                                }</Descriptions.Item>

                                <Descriptions.Item label={t('order.table.createdBy')}>
                                    {
                                        order?.user_id?.fullName
                                    }
                                </Descriptions.Item>

                                <Descriptions.Item label={t('order.table.createdAt')}>{
                                    dateTimeFormat(order?.created_at)
                                }</Descriptions.Item>

                                <Descriptions.Item label={t('order.table.updatedAt')}>
                                    {
                                        dateTimeFormat(order?.updatedAt)
                                    }
                                </Descriptions.Item>


                                <Descriptions.Item  span={2} label={t('order.table.modifiedBy')}>
                                    {
                                        order?.modifiedBy?.fullName
                                    }
                                </Descriptions.Item>

                            </Descriptions>
                        }
                    </div>
                </div>
            </Card>





        </div>
    </>)
};


export default OrderHandlePage;