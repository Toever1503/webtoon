import { Modal, Tag, Descriptions, Space, Button, Popconfirm, Tooltip } from "antd";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import IOrder from "../../../services/order/types/IOrder";
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
    showEditModal: () => void;
}
const DetailOrderModal: React.FC<DetailOrderModalProps> = (props: DetailOrderModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [order, setOrder] = useState<IOrder>();

    useEffect(() => {
        if (props.input) {
            setOrder(props.input);
        }
    }, [props]);


    function markCompletedOrder(input: IOrder) {
        orderService
            .changeStatus(input.id || 0, 'COMPLETED')
            .then(() => {
                dispatch(changeOrderStatus({
                    id: input.id || 0,
                    status: 'COMPLETED',
                }));

                if (order) {
                    order.status = 'COMPLETED';
                    setOrder({ ...order });
                }
                dispatch(showNofification(({
                    type: 'success',
                    message: t('order.modal.actions.update-status-success'),
                })));
            })
            .catch((err) => {
                console.log('error: ', err);

                dispatch(showNofification(({
                    type: 'error',
                    message: t('order.modal.actions.update-status-failed'),
                })));
            });
    }

    return <Modal
        width={'80%'}
        open={props.visible}
        onCancel={props.onCancel}
        title={t('order.detailModal.title')}
        footer={null}
    >
        <Descriptions bordered >
            <Descriptions.Item label={t('order.table.orderNumber')}>{order?.maDonHang}</Descriptions.Item>

            <Descriptions.Item label={t('order.table.subscription')}>
                {order?.subs_pack_id?.name} <br />
                {
                    order?.orderType === 'UPGRADE' && <span className="text-red-400"> ({t('order.table.upgrade')})</span>
                }
            </Descriptions.Item>

            <Descriptions.Item label={t('order.table.status')}>
                <Tag>
                    {t('order.eStatus.' + order?.status)}
                </Tag>
                {
                    order && order.status === 'USER_CONFIRMED_BANKING' &&

                    <Tooltip title={t('order.modal.form.labels.mark-complete')}>
                        <Popconfirm
                            title={t('order.modal.form.labels.sure-complete')}
                            onConfirm={(e) => {
                                e?.stopPropagation();
                                markCompletedOrder(order);
                            }}
                            okText={t('confirm-yes')}
                            cancelText={t('confirm-no')}
                        >
                            <span className="text-blue-400 hover:text-blue-500 cursor-pointer"><CheckOutlined /></span>
                        </Popconfirm>
                    </Tooltip>
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


            <Descriptions.Item label={t('order.table.modifiedBy')}>
                {
                    order?.modifiedBy?.fullName
                }
            </Descriptions.Item>

            {
                order?.fromOrder && <Descriptions.Item label={t('order.table.originalOrderNumber')}>
                    {
                        order?.fromOrder?.maDonHang
                    }
                </Descriptions.Item>
            }
        </Descriptions>

        <Space className="mt-[15px]">
            {order && order.status !== 'COMPLETED' && order.status !== 'CANCELED' && <Button type="primary">
                <a onClick={() => props.showEditModal()}>{t('buttons.edit')}</a>
            </Button>
            }
        </Space>
    </Modal>
};

export default DetailOrderModal;