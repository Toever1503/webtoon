import { Modal, Tag, Descriptions, Space, Button, Popconfirm, Tooltip } from "antd";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import IOrder, { EORDER_STATUS } from "../../../services/order/types/IOrder";
import { useEffect, useState } from "react";
import formatVnCurrency from "../../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../../utils/dateFormat";
import { CheckCircleOutlined, CheckOutlined } from "@ant-design/icons";
import orderService from "../../../services/order/OrderService";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import { changeOrderStatus } from "../../../stores/features/order/orderSlice";


export interface DetailOrderModalProps {
    visible: boolean;
    onCancel: () => void;
    input?: IOrder,
}
const DetailOrderModal: React.FC<DetailOrderModalProps> = (props: DetailOrderModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [order, setOrder] = useState<IOrder>();

    useEffect(() => {
        if (props.input) {
            setOrder(props.input);

            console.log('view detail order: ', props.input);

        }
    }, [props]);

    const getColorForStatus = (status: EORDER_STATUS) => {
        if (status === 'COMPLETED') return '#1677ff';
        if (status === 'PENDING_PAYMENT') return '#ff5722';
        if (status === 'USER_CONFIRMED_BANKING') return '#ff5722';
        if (status === 'CANCELED') return 'red';
    }


    return <Modal
        width={'80%'}
        open={props.visible}
        onCancel={props.onCancel}
        title={t('order.detailModal.title')}
        footer={null}
    >
        {
            order && <Descriptions bordered labelStyle={{fontWeight: '600'}}>
                <Descriptions.Item label={t('order.table.orderNumber')}>{order?.maDonHang}</Descriptions.Item>

                <Descriptions.Item label={t('order.table.subscription')}>
                    {order?.subs_pack_id?.name} <br />
                    {
                        order?.orderType === 'UPGRADE' && <span className="text-red-400"> ({t('order.table.upgrade')})</span>
                    }
                </Descriptions.Item>

                <Descriptions.Item label={t('order.table.status')}>
                    <Tag color={getColorForStatus(order?.status)}>
                        {t('order.eStatus.' + order?.status)}
                    </Tag>

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


                <Descriptions.Item span={2} label={t('order.table.modifiedBy')}>
                    {
                        order?.modifiedBy?.fullName
                    }
                </Descriptions.Item>

                {
                    order.paymentMethod === 'VN_PAY'
                    && order.status === 'COMPLETED'
                    && order.paymentDto && <>
                        <Descriptions.Item label='Mã giao dịch VNPAY'>
                            {
                                order.paymentDto.transNo
                            }
                        </Descriptions.Item>

                        <Descriptions.Item label='Mã giao dịch ngân hàng'>
                            {
                                order.paymentDto.bankTranNo
                            }
                        </Descriptions.Item>
                    </>
                }

            </Descriptions>
        }


    </Modal>
};

export default DetailOrderModal;