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

export interface UpgradeOrderModalProps {
    visible: boolean;
    onCancel: () => void;
    onOk: (record: IOrder) => void;
    input?: IOrder,
    subscriptionPackList: ISubscriptionPack[],
};

const UpgradeOrderModal: React.FC<UpgradeOrderModalProps> = (props: UpgradeOrderModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const [selectedUpgradeSubscriptionPack, setSelectedUpgradeSubscriptionPack] = useState<number | string>();
    const [upgradeSubscriptionPack, setUpgradeSubscriptionPack] = useState<ISubscriptionPack>();
    const [paymentMethodVal, setPaymentMethodVal] = useState<'ATM' | 'VN_PAY'>('ATM');

    const createUpgradeOrder = () => {
        if (isSubmitting) return;
    }

    useEffect(() => {
        if (props.visible) {
            setSelectedUpgradeSubscriptionPack(undefined);
            setUpgradeSubscriptionPack(undefined);
            setPaymentMethodVal('ATM');
        }
    }, [props]);

    return <Modal
        open={props.visible}
        title={t('order.upgradeModal.title')}
        onCancel={props.onCancel}
        footer={null}
    >

        <Space size="large">
            <div className='mt-[20px]'>
                <h3>
                    {t('order.upgradeModal.currentSubscriptionPack')}
                </h3>

                <Select className='min-w-[200px]' value={props.input?.subs_pack_id.id} disabled>

                    {
                        props.input && <Select.Option value={props.input.subs_pack_id.id} key={props.input.subs_pack_id.id}>
                            <Space align='baseline'>
                                <span>{props.input.subs_pack_id.name}</span>
                                -
                                <span>{
                                    formatVnCurrency(props.input.subs_pack_id.price)
                                }</span>
                            </Space>
                        </Select.Option>
                    }
                </Select>
            </div>

            <div className='mt-[20px]'>
                <h3>
                    {t('order.upgradeModal.chooseUpgradeSubscriptionPack')}
                </h3>
                {
                    props.visible &&
                    <Select className='min-w-[200px]' value={selectedUpgradeSubscriptionPack} onChange={val => {
                        const subscriptionPack = props.subscriptionPackList?.find(item => item.id === val);
                        setSelectedUpgradeSubscriptionPack(val);
                        setUpgradeSubscriptionPack(subscriptionPack);
                    }}>
                        {
                            props.subscriptionPackList && props.subscriptionPackList
                                .filter((item: ISubscriptionPack) => item.monthCount > (props.input ? props.input.subs_pack_id.monthCount : 0))
                                .map((item: ISubscriptionPack) =>
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
                }
            </div>
        </Space>


        {
            upgradeSubscriptionPack && <>
                <div className='mt-[10px]'>

                    <p className='text-base font-medium'>
                        {t('order.upgradeModal.needPriceToPay')}
                    </p>
                    <p>
                        {formatVnCurrency(upgradeSubscriptionPack.price)} ({t('order.upgradeModal.newPack')})
                        <span className='mx-[5px]'>-</span>
                        {
                            formatVnCurrency(props.input?.subs_pack_id.price)
                        } ({t('order.upgradeModal.oldPack')})
                        <span className='mx-[5px]'>=</span>
                        {
                            formatVnCurrency(upgradeSubscriptionPack.price - (props.input ? props.input.subs_pack_id.price : 0))
                        }
                    </p>
                </div>

                <div className='mt-[10px] grid'>
                    <label className='text-base font-medium'>
                        {t('order.upgradeModal.paymentMethod')}
                    </label>
                    <Select
                        className='w-[100px] mt-[5px]'
                        value={paymentMethodVal} onChange={val => setPaymentMethodVal(val)}
                    >
                        <Select.Option value="ATM">ATM</Select.Option>
                        <Select.Option value="VN_PAY">VNPAY</Select.Option>
                    </Select>
                </div>

                <div className='mt-[10px] flex justify-center'>
                    <Button loading={isSubmitting} onClick={createUpgradeOrder}>
                        {t('order.upgradeModal.createOrder')}
                    </Button>
                </div>
            </>
        }

    </Modal>
};

export default UpgradeOrderModal;