import { Modal, Form, Input, Checkbox, Button, Radio, Space, Select } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import { RootState } from "../../../stores";
import { UserState } from "../../../stores/features/user/userSlice";
import userService from "../../../services/user/UserService";
import IUserType from "../types/IUserType";
import { AxiosError, AxiosResponse } from "axios";


export interface AddEditUserModalProps {
    visible: boolean;
    onOk: Function;
    cancel: Function;
    title: string;
    userInput?: IUserType;
    authorityOptions: {
        id: number | string;
        authorityName: string;
    }[];
}
const AddEditUserModal: React.FC<AddEditUserModalProps> = (props: AddEditUserModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();


    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const [passwordVal, setPasswordVal] = useState<string>('');

    const onSubmit = () => {
        if (isSubmitting) return;

        setIsSubmitting(true);
        const authorities: any = [];
        form.getFieldValue('authorities').forEach((e: string | {
            label: string;
            value: string | number;
        }) => {
            if (typeof e === 'string')
                authorities.push(e);
            else
                authorities.push(e.value);
        });

        form.validateFields()
            .then((values: any) => {
                console.log('ok', values);
                if (!props.userInput) // for add
                    userService
                        .addNewUser({
                            ...values,
                            authorities: authorities
                        })
                        .then((res: AxiosResponse<IUserType>) => {
                            console.log('add new user: ', res.data);
                            props.onOk(res.data);
                            dispatch(showNofification({
                                type: 'success',
                                message: `${t('user.form.add-success')}`
                            }));
                        })
                        .catch((err: AxiosError<{
                            code: string;
                        }>) => {
                            console.log('err: ', err?.response?.data);
                            dispatch(showNofification({
                                type: 'error',
                                message: err?.response?.data ? `${t(`response.errors.${err?.response?.data.code}`)}` : `${t('user.form.add-failed')}`
                            }));
                        })
                        .finally(() => setIsSubmitting(false));
                else // for edit
                    userService
                        .updateUser(props.userInput.id || 1, {
                            ...values,
                            authorities: authorities
                        })
                        .then((res: AxiosResponse<IUserType>) => {
                            console.log('edit new user: ', res.data);
                            props.onOk(res.data);
                            dispatch(showNofification({
                                type: 'success',
                                message: `${t('user.form.edit-success')}`
                            }));
                        })
                        .catch((err: AxiosError<{
                            code: string;
                        }>) => {
                            console.log('err: ', err?.response?.data);
                            dispatch(showNofification({
                                type: 'error',
                                message: err?.response?.data ? `${t(`response.errors.${err?.response?.data.code}`)}` : `${t('user.form.add-failed')}`
                            }));
                        })
                        .finally(() => setIsSubmitting(false));
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

        // form.setFieldsValue({
        //     fullName: 'Nguyen Van A',
        //     username: 'nguyenvana' + (Math.random() * 1000000),
        //     email: (Math.random() * 1000000) + 'fa@fa.ca',
        //     password: 'fasfas',
        //     'confirm-password': 'fasfas',
        // });
        if (props.visible && props.userInput) {
            console.log('edit: ', props.userInput);
            form.setFieldsValue({
                fullName: props.userInput.fullName,
                username: props.userInput.username,
                email: props.userInput.email,
                authorities: props.userInput.authorities.map(e => {
                    return {
                        label: e.authorityName,
                        value: e.id
                    };
                })
            });
        }

    }, [props.visible]);

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
                    rules={!props.userInput ? [
                        { required: true, message: `${t('user.form.errors.password-required')}` },
                        {
                            max: 30,
                            message: `${t('user.form.errors.password-max')}`
                        }
                    ] : []}
                >
                    <Input.Password onChange={e => setPasswordVal(e.target.value)} />
                </Form.Item>

                <Form.Item
                    label={t('user.form.confirm-password')}
                    name="confirm-password"
                    rules={!props.userInput && passwordVal ? [
                        { required: true, validator: confirmPasswdValidator },
                    ] : []}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item
                    label={t('user.form.authority')}
                    name="authorities"
                    rules={[
                        { required: true, message: `${t('user.form.errors.authority-required')}` },
                    ]}
                >
                    <Select mode="multiple"
                        showArrow>
                        {
                            props.authorityOptions.map(au => (
                                <Select.Option key={au.id.toString()} value={au.id.toString()}>{t(`user.authority.${au.authorityName}`)} </Select.Option>
                            ))
                        }
                    </Select>
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