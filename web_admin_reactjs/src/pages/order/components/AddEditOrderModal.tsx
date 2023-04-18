import {
    Modal,
    Form,
    Button,
    Input,
    Select
} from 'antd';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';
import IOrder from '../../../services/order/types/IOrder';
import ISubscriptionPack from '../../../services/subscription_pack/types/ISubscriptionPack';

export interface AddEditOrderModalProps {
    visible: boolean;
    onCancel: () => void;
    onOk: () => void;
    input?: IOrder,
    subscriptionPackList?: ISubscriptionPack[],

};

const AddEditOrderModal: React.FC<AddEditOrderModalProps> = (props: AddEditOrderModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const onCancelModal = () => {
        props.onCancel();
    };



    useEffect(() => {
        form.resetFields();

        if (props.input) {
            console.log('edit order: ', props.input);
            form.setFieldsValue({
                orderNumber: props.input.maDonHang,
                subscriptionPack: props.input?.subs_pack_id.id,
                price: props.input.finalPrice,
                paymentMethod: props.input.paymentMethod,
                status: props.input.status,
                createdBy: props.input?.createdBy?.id,
            });
        }
    }, [props]);

    return <Modal
        open={props.visible}
        title={t('order.modal.editTitle')}
        footer={null}
        onCancel={onCancelModal}
    >

        <Form
            style={{ marginTop: 50 }}
            name="basic"
            form={form}
            initialValues={{ remember: true }}
            // onFinish={onFinish}
            // onFinishFailed={onFinishFailed}
            autoComplete="off"
            labelCol={{ span: 10 }}
        >
            <Form.Item
                label={t('order.modal.form.labels.orderNumber')}
                name="orderNumber"
            >
                <Input readOnly disabled />
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.subscriptionPack')}
                name="subscriptionPack"
            >
                <Select
                    showSearch
                    disabled
                >
                    <Select.Option value="">{t('order.modal.form.labels.choosePack')}</Select.Option>

                    {
                        props.subscriptionPackList && props.subscriptionPackList.map((item: ISubscriptionPack) =>
                            <Select.Option value={item.id}>{item.name}</Select.Option>
                        )
                    }

                </Select>
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.price')}
                name="price"
            >
                <Input readOnly disabled />
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.paymentMethod')}
                name="paymentMethod"
            >
                <Select
                >
                    <Select.Option value="ATM">ATM</Select.Option>
                    <Select.Option value="VN_PAY">VNPAY</Select.Option>
                </Select>
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.status')}
                name="status"
            >
                <Select
                >
                    <Select.Option value="ATM">ATM</Select.Option>
                </Select>
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.createdBy')}
                name="createdBy"
            >
                <Input readOnly disabled />
            </Form.Item>



            <Form.Item className="mt-[10px]" wrapperCol={{ offset: 8, span: 16 }}>
                <Button type="primary" htmlType="submit" loading={isSubmitting}>
                    {t('subscription-pack.modal.saveBtn')}
                </Button>
            </Form.Item>
        </Form>

    </Modal>
};

export default AddEditOrderModal;