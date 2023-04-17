import { Button, Form, Input, Modal, Select, Space } from "antd";
import ISubscriptionPack from "../../../services/subscription_pack/types/ISubscriptionPack";
import { useDispatch } from "react-redux";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";

export interface AddEditSubscriptionPackModalProps {
    visible: boolean;
    title: string;
    onCancel: () => void;
    onOk: () => void;
    input?: ISubscriptionPack
}

const AddEditSubscriptionPackModal: React.FC<AddEditSubscriptionPackModalProps> = (props: AddEditSubscriptionPackModalProps) => {

    const { t } = useTranslation();
    const dispatch = useDispatch();
    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = React.useState(false);

    const handleOk = () => {
        console.log('OK', props);
    };

    const handleCancel = () => {
        props.onCancel();
        form.resetFields();
    };

    const onFinish = (values: any) => {
        console.log('Success:', values);

        if(props.input){ // edit

        }
        else { //add

        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

    useEffect(() => {
        form.resetFields();

        if(props.input ){
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
                    labelCol={{ span: 6 }}
                >
                    <Form.Item
                        label={t('subscription-pack.modal.title')}
                        name="title"
                        rules={[
                            { required: true, message: `${t('subscription-pack.modal.errors.required-title')}`},
                            { max: 255, message: `${t('subscription-pack.modal.errors.max-title')}`},
                        ]}
                    >
                        <Input placeholder={`${t('subscription-pack.modal.title')}`} />
                    </Form.Item>

                    <Form.Item
                        label={t('subscription-pack.modal.price')}
                        name="price"
                        rules={[
                            { required: true, message: `${t('subscription-pack.modal.errors.required-price')}`},
                            // { min: 10000, message: `${t('subscription-pack.modal.errors.min-price')}`},
                            // { max: 100000, message: `${t('subscription-pack.modal.errors.max-price')}`},
                        ]}

                    >
                        <Input type="number" placeholder={`${t('subscription-pack.modal.placeholders.price')}`}  suffix="vnd" />
                    </Form.Item>

                    <Form.Item
                        label={t('subscription-pack.modal.dateCount')}
                        name="dateCount"
                        rules={[{ required: true, message: `${t('subscription-pack.modal.errors.required-dateCount')}`}]}
                    >
                        <Select defaultValue="">
                            <Select.Option value="">{
                                t('subscription-pack.modal.placeholders.choose-month')
                            }</Select.Option>

                            {
                                months.map((month, index) => <>
                                    <Select.Option key={index} value={month*30}>
                                        {month} {t('subscription-pack.modal.month')}
                                    </Select.Option>

                                </>)
                            }

                        </Select>
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