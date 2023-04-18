import { Button, Form, Input, Modal, Select, Space } from "antd";
import ISubscriptionPack from "../../../services/subscription_pack/types/ISubscriptionPack";
import { useDispatch } from "react-redux";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import subscriptionPackService from "../../../services/subscription_pack/subscriptionPackService";
import { AxiosResponse } from "axios";

export interface AddEditSubscriptionPackModalProps {
    visible: boolean;
    title: string;
    onCancel: () => void;
    onOk: (record: ISubscriptionPack) => void;
    input?: ISubscriptionPack
}

const AddEditSubscriptionPackModal: React.FC<AddEditSubscriptionPackModalProps> = (props: AddEditSubscriptionPackModalProps) => {

    const { t } = useTranslation();
    const dispatch = useDispatch();
    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = React.useState(false);

    const [dateCountChooseType, setDateCountChooseType] = useState<'CUSTOM' | 'SELECT'>('SELECT');

    const handleCancel = () => {
        props.onCancel();
        form.resetFields();
    };

    const onFinish = (values: ISubscriptionPack) => {
        console.log('Success:', values);

        setIsSubmitting(true);
        if (props.input) { // edit
            subscriptionPackService
                .updateSubscriptionPack(props.input.id || 0, values)
                .then((res: AxiosResponse<ISubscriptionPack>) => {
                    console.log('edit ok', res.data);
                    props.onOk(res.data);
                    dispatch(showNofification({
                        type: 'success', message: `${t('subscription-pack.modal.form.edit-success')}`
                    }));
                })
                .catch((err: any) => {
                    console.log(err);
                    dispatch(showNofification({
                        type: 'error', message: `${t('subscription-pack.modal.form.edit-failed')}`
                    }));
                })
                .finally(() => {
                    setIsSubmitting(false);
                });
        }
        else { //add
            subscriptionPackService
                .addSubscriptionPack(values)
                .then((res: AxiosResponse<ISubscriptionPack>) => {
                    console.log('add ok', res.data);
                    props.onOk(res.data);
                    dispatch(showNofification({
                        type: 'success', message: `${t('subscription-pack.modal.form.add-success')}`
                    }));
                })
                .catch((err: any) => {
                    console.log(err);
                    dispatch(showNofification({
                        type: 'error', message: `${t('subscription-pack.modal.form.add-failed')}`
                    }));
                })
                .finally(() => {
                    setIsSubmitting(false);
                });
        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
        dispatch(showNofification({
            type: 'warning', message: `${t('subscription-pack.modal.errors.check-input-again')}`
        }))
    };


    const discountPriceValidator = (rule: any, value: string) => {
        if (!form.getFieldValue('originalPrice'))
            return Promise.reject(`${t('subscription-pack.modal.errors.required-price')}`);
        else if (Number(value) > Number(form.getFieldValue('originalPrice')))
            return Promise.reject(`${t('subscription-pack.modal.errors.exceed-discount-price')}`);

        return Promise.resolve();
    };

    const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

    useEffect(() => {
        form.resetFields();

        if (props.input) {
            form.setFieldsValue({
                ...props.input
            });
        }
    }, [props]);

    return (
        <>
            <Modal title={props.title} open={props.visible} onCancel={handleCancel} footer={null}>
                <Form
                    style={{ marginTop: 50 }}
                    name="basic"
                    form={form}
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete="off"
                    labelCol={{ span: 8 }}
                >
                    <Form.Item
                        label={t('subscription-pack.modal.name')}
                        name="name"
                    >
                        <Input readOnly disabled placeholder={`${t('subscription-pack.modal.name')}`} />
                    </Form.Item>

                    <Form.Item
                        label={t('subscription-pack.modal.price')}
                        name="price"
                        rules={[
                            { required: true, message: `${t('subscription-pack.modal.errors.required-price')}` },
                            { min: 5, message: `${t('subscription-pack.modal.errors.min-price')}` },
                            { max: 10, message: `${t('subscription-pack.modal.errors.max-price')}` },
                        ]}

                    >
                        <Input type="number" placeholder={`${t('subscription-pack.modal.placeholders.price')}`} suffix="vnd" />
                    </Form.Item>

                    <Form.Item
                        label={t('subscription-pack.modal.dateCount')}
                        name="monthCount"
                        rules={[{ required: true, message: `${t('subscription-pack.modal.errors.required-dateCount')}` }]}
                    >
                        {
                            dateCountChooseType === 'SELECT' ?
                                <Space>
                                    <Select defaultValue={props.input?.monthCount || ""} onChange={val => {
                                        form.setFieldValue('monthCount', val);
                                        form.setFieldValue('name', t('subscription-pack.modal.name') + ' ' + Number(val) + ' ' + t('subscription-pack.modal.month'));
                                    }}

                                    >
                                        <Select.Option value="">{
                                            t('subscription-pack.modal.placeholders.choose-month')
                                        }</Select.Option>

                                        {
                                            months.map((month) => 
                                                <Select.Option key={month} value={month}>
                                                    {month} {t('subscription-pack.modal.month')}
                                                </Select.Option>
                                            )
                                        }

                                    </Select>
                                    <a onClick={() => {
                                        setDateCountChooseType('CUSTOM');
                                        form.setFieldValue('monthCount', "13");
                                    }}>Nhập thủ công</a>
                                </Space>
                                :
                                <Space >
                                    <Input type="number" defaultValue={13} min={13} max={60} onChange={e => {
                                        form.setFieldValue('name', t('subscription-pack.modal.name') + ' ' + Number(e.target.value) + ' ' + t('subscription-pack.modal.month'));
                                    }} suffix={t('subscription-pack.modal.month')} />
                                    <a onClick={() => {
                                        setDateCountChooseType('SELECT');
                                    }}>{t('subscription-pack.modal.resetBtn')}</a>
                                </Space>
                        }

                    </Form.Item>



                    <Form.Item className="mt-[10px]" wrapperCol={{ offset: 8, span: 16 }}>

                        <Button className="mr-2" type="primary" htmlType="button" loading={isSubmitting}>
                            {t('subscription-pack.modal.resetBtn')}
                        </Button>

                        <Button type="primary" htmlType="submit" loading={isSubmitting}>
                            {t('subscription-pack.modal.saveBtn')}
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default AddEditSubscriptionPackModal;