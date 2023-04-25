import { Modal, Tag, Descriptions } from "antd";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import IOrder from "../../../services/order/types/IOrder";
import { useEffect, useState } from "react";
import formatVnCurrency from "../../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../../utils/dateFormat";


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
        }
    }, [props]);


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
                    {order?.status}
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


            <Descriptions.Item label={t('order.table.modifiedBy')}>
                {
                    order?.modifiedBy?.fullName
                }
            </Descriptions.Item>

            <Descriptions.Item label={t('order.table.expireDate')}>
                {
                    dateTimeFormat(order?.expiredSubsDate)
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

    </Modal>
};

export default DetailOrderModal;