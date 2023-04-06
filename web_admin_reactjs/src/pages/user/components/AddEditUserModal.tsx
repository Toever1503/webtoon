import { Modal, Form, Input, Checkbox, Button, Radio, Space } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import { RootState } from "../../../stores";
import { UserState } from "../../../stores/features/user/userSlice";
import userService from "../../../services/user/UserService";
import IUserType from "../types/IUserType";
import { AxiosResponse } from "axios";


export interface AddEditUserModalProps {
    visible: boolean;
    onOk: Function;
    cancel: Function;
    title: string;
    userInput?: any;
}
const AddEditUserModal: React.FC<AddEditUserModalProps> = (props: AddEditUserModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();


    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const onSubmit = () => {
        if (isSubmitting) return;

        setIsSubmitting(true);
        form.validateFields()
            .then((values: any) => {
                console.log('ok', values);
                userService
                .addNewUser({})
                .then((res: AxiosResponse<IUserType>) => {
                    console.log('add uew: ');
                    
                } )
            })
            .catch(err => {
                onFinishFailed(err);
            })
            .finally(() => setIsSubmitting(false));

    }

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
        dispatch(showNofification({
            type: 'error',
            message: `${t('user.form.errors.check-again')}`
        }));

    };

    const confirmPasswdValidator = (rule: any, value: string) => {
        if (!value)
            return Promise.reject(`${t('user.form.errors.confirm-password-required')}`);
        else if (value != form.getFieldValue('password'))
            return Promise.reject(`${t('user.form.errors.confirm-password-not-match')}`);

        return Promise.resolve();
    };
    const emailValidator = (rule: any, value: string) => {
        if (!value)
            return Promise.reject(`${t('user.form.errors.email-required')}`);
        else if (value.length > 255)
            return Promise.reject(`${t('user.form.errors.email-max')}`);
        else if (!value.match(/^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i))
            return Promise.reject(`${t('user.form.errors.email-invalid')}`);

        return Promise.resolve();
    };

    const phoneValidator = (rule: any, value: string) => {
        if (!value) Promise.resolve();
        else if (value?.length > 255)
            return Promise.reject(`${t('user.form.errors.phone-max')}`);
        else if (!value.match(/^[0-9]{10,11}$/))
            return Promise.reject(`${t('user.form.errors.phone-invalid')}`);
        return Promise.resolve();
    };

    useEffect(() => {
        form.resetFields();
        form.setFieldValue('sex', 'MALE');

        form.setFieldsValue({
            fullName: 'Nguyen Van A',
            username: 'nguyenvana' + (Math.random() * 1000000),
            email: (Math.random() * 1000000) + 'fa@fa.ca',
            password: 'fasfas',
            'confirm-password': 'fasfas',
        });

    }, []);

    return <>
        <Modal title={props.title} open={props.visible} onCancel={() => props.cancel()} footer={null}>
            <Form
                className="mt-[20px]"
                form={form}
                name="basic"
                style={{ maxWidth: 600 }}
                autoComplete="off"
                labelCol={{ span: 7 }}
            >
                <Form.Item
                    label={t('user.form.fullName')}
                    name="fullName"
                    rules={[
                        { required: true, message: `${t('user.form.errors.fullName-required')}` },
                        {
                            max: 255,
                            message: `${t('user.form.errors.fullName-max')}`
                        }
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label={t('user.form.username')}
                    name="username"
                    rules={[
                        { required: true, message: `${t('user.form.errors.username-required')}` },
                        {
                            max: 255,
                            message: `${t('user.form.errors.username-max')}`
                        }
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label={t('user.form.email')}
                    name="email"
                    rules={[
                        { required: true, validator: emailValidator },
                    ]}
                >
                    <Input type="email" />
                </Form.Item>

                <Form.Item
                    label={t('user.form.password')}
                    name="password"
                    rules={[
                        { required: true, message: `${t('user.form.errors.password-required')}` },
                        {
                            max: 30,
                            message: `${t('user.form.errors.password-max')}`
                        }
                    ]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item
                    label={t('user.form.confirm-password')}
                    name="confirm-password"
                    rules={[
                        { required: true, validator: confirmPasswdValidator },
                    ]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item
                    label={t('user.form.phone')}
                    name="phone"
                    rules={[
                        { validator: phoneValidator },
                    ]}
                >
                    <Input type='number' />
                </Form.Item>

                <Form.Item
                    label={t('user.form.sex')}
                    name="sex"
                >
                    <Radio.Group>
                        <Radio value="MALE"> {t('user.form.sex-radio.male')} </Radio>
                        <Radio value="FEMALE"> {t('user.form.sex-radio.female')} </Radio>
                    </Radio.Group>
                </Form.Item>

                <div className="flex justify-center space-x-3">
                    <Button htmlType="reset">
                        {t('user.form.reset-btn')}
                    </Button>
                    <Button htmlType="submit" loading={isSubmitting} onClick={onSubmit}>
                        {t('user.form.save-btn')}
                    </Button>
                </div>

            </Form>
        </Modal>
    </>;
};

export default AddEditUserModal;