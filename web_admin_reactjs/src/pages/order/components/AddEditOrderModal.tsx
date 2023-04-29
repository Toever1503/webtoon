import {
    Modal,
    Form,
    Button,
    Input,
    Select,
    Space
} from 'antd';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';
import IOrder, { EORDER_STATUS } from '../../../services/order/types/IOrder';
import ISubscriptionPack from '../../../services/subscription_pack/types/ISubscriptionPack';
import formatVnCurrency from '../../../utils/formatVnCurrency';
import debounce from '../../../utils/debounce';
import IUserType from '../../user/types/IUserType';
import userService from '../../../services/user/UserService';
import { AxiosResponse } from 'axios';
import { showNofification } from '../../../stores/features/notification/notificationSlice';
import orderService from '../../../services/order/OrderService';

export interface AddEditOrderModalProps {
    visible: boolean;
    title: string;
    onCancel: () => void;
    onOk: (record: IOrder) => void;
    input?: IOrder,
    subscriptionPackList?: ISubscriptionPack[],

};

const FINAL_STATUSES: EORDER_STATUS[] = [
    'PAID',
    'COMPLETED',
    'REFUND_CONFIRM_PENDING',
    'REFUNDING',
    'REFUNDED',
];


// STATUS that can change subscription pack
const ALLOW_CHANGE_SUBS_PACK: EORDER_STATUS[] = [
    'PENDING_PAYMENT',
];




const AddEditOrderModal: React.FC<AddEditOrderModalProps> = (props: AddEditOrderModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const onCancelModal = () => {
        props.onCancel();
    };


    const [userOptions, setUserOptions] = useState<IUserType[]>([]);
    const [isSearchingUser, setIsSearchingUser] = useState<boolean>(false);
    const onSearchUser = debounce((val: string) => {
        onCallSearchUser(val);
    });
    const onCallSearchUser = (val: string) => {
        if (isSearchingUser) return;

        setIsSearchingUser(true);
        console.log('search user: ', val);
        setUserOptions([]);
        userService
            .filterUser({
                q: val,
            }, 0, 5)
            .then((res: AxiosResponse<{
                content: IUserType[],
            }>) => {
                console.log('user data: ', res.data);
                if (props.input) {
                    const userId = props.input?.user_id?.id;
                    setUserOptions([props.input.user_id, ...res.data.content.filter((item: IUserType) => item.id !== userId)]);
                }
                else
                    setUserOptions(res.data.content);
            })
            .finally(() => setIsSearchingUser(false));
    };


    const onFinish = (payload: any) => {
        console.log('on finish: ', payload);
        if (isSubmitting) return;

        setIsSubmitting(true);
        payload.orderType = 'NEW';
        if (!props.input) // for add new  
            orderService.addOrder(payload)
                .then((res: AxiosResponse<IOrder>) => {
                    console.log('add order res: ', res);
                    dispatch(showNofification({
                        type: 'success',
                        message: t('order.modal.actions.add-success'),
                    }));
                    props.onOk(res.data);
                })
                .catch(err => {
                    console.log('err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: t('order.modal.actions.add-failed'),
                    }));
                })
                .finally(() => setIsSubmitting(false));

        else {
            payload.id = props.input.id;
            payload.orderType = props.input.orderType;
            orderService.updateOrder(payload)
                .then((res: AxiosResponse<IOrder>) => {
                    console.log('EDIT order res: ', res);
                    dispatch(showNofification({
                        type: 'success',
                        message: t('order.modal.actions.edit-success'),
                    }));
                    props.onOk(res.data);
                })
                .catch(err => {
                    console.log('err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: t('order.modal.actions.edit-failed'),
                    }));
                })
                .finally(() => setIsSubmitting(false));
        }
    };

    const onFinishFailed = (err: any) => {
        console.log('on finish failed: ', err);

        dispatch(showNofification({
            type: 'warning',
            message: t('order.modal.errors.check-again'),
        }));
    };

    useEffect(() => {
        onCallSearchUser('');
        form.resetFields();

        form.setFieldsValue({
            paymentMethod: 'ATM',
            status: FINAL_STATUSES[0],
        })
        if (props.input) {
            console.log('edit order: ', props.input);
            form.setFieldsValue({
                orderNumber: props.input.maDonHang,
                subscriptionPack: props.input?.subs_pack_id.id,
                price: props.input.finalPrice,
                paymentMethod: props.input.paymentMethod,
                status: props.input.status,
                user_id: props.input?.user_id?.id,
            });

        }
    }, [props]);

    return <Modal
        open={props.visible}
        title={props.title}
        footer={null}
        onCancel={onCancelModal}
        width={'800px'}
    >

        <Form
            className='w-full'
            style={{ marginTop: 50 }}
            name="basic"
            form={form}
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
            labelCol={{ span: 6 }}
        >
            {
                props.input &&
                <Form.Item
                    label={t('order.modal.form.labels.orderNumber')}
                    name="orderNumber"
                >
                    <Input readOnly disabled />
                </Form.Item>
            }

            <Form.Item
                label={t('order.modal.form.labels.subscriptionPack')}
                name="subscriptionPack"
                rules={[{ required: true, message: `${t('order.modal.errors.required-subs')}` }]}
            >
                <Select
                    disabled={props.input && !ALLOW_CHANGE_SUBS_PACK.includes(props.input.status)}
                    showSearch
                    placeholder={t('order.modal.form.labels.choosePack')}
                    onChange={(val) => {
                        console.log('item: ', val);

                        props.subscriptionPackList?.find((item: ISubscriptionPack) => {
                            if (item.id === val) {
                                form.setFieldsValue({
                                    price: item.price,
                                })
                            }
                        })
                    }}
                >
                    {
                        props.subscriptionPackList && props.subscriptionPackList.map((item: ISubscriptionPack) =>
                            <Select.Option value={item.id} key={item.id}>
                                <Space align='baseline'>
                                    <span>{item.name}</span>
                                    -
                                    <span>{
                                        formatVnCurrency(item.price)
                                    }</span>
                                </Space>
                            </Select.Option>
                        )
                    }

                </Select>
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.price')}
                name="price"
            >
                <Input readOnly disabled suffix="vnd" />
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
                    {
                        FINAL_STATUSES.map((status, index) =>
                            <Select.Option key={index} value={status} >
                                {t('order.eStatus.' + status)}
                            </Select.Option>)
                    }
                </Select>
            </Form.Item>

            <Form.Item
                label={t('order.modal.form.labels.createdBy')}
                name="user_id"
                rules={[{ required: true, message: `${t('order.modal.errors.required-user')}` }]}
            >
                <Select
                    showSearch
                    placeholder={t('order.modal.form.labels.chooseUser')}
                    onSearch={onSearchUser}
                    allowClear
                    filterOption={false}
                >
                    {
                        userOptions.map((item: IUserType) =>
                            <Select.Option value={item.id} key={item.id}>
                                <Space align='baseline'>
                                    <h5 className='text-xl m-0'>{item.fullName}</h5>
                                    <span>{item.email}</span>
                                    <span>- {item.phone}</span>
                                </Space>
                            </Select.Option>
                        )
                    }

                </Select>
            </Form.Item>



            <Form.Item className="mt-[10px]" wrapperCol={{ offset: 8, span: 16 }}>
                <Space>
                    <Button type="primary" htmlType="reset">
                        {t('buttons.reset')}
                    </Button>

                    <Button type="primary" htmlType="submit" loading={isSubmitting}>
                        {t('subscription-pack.modal.saveBtn')}
                    </Button>
                </Space>
            </Form.Item>
        </Form>

    </Modal>
};

export default AddEditOrderModal;