import { Button, Form, Input, Modal } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import userService from "../../../services/user/UserService";


export interface ForgotPasswordModalProps {
    visible: boolean;
    onClose: () => void;
}

const FormItem = Form.Item;

const ForgotPasswordModal: React.FC<ForgotPasswordModalProps> = (props: ForgotPasswordModalProps) => {

    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [loading, setLoading] = useState<boolean>(false);
    const [form] = Form.useForm();


    const onFinish = ({ email }: {
        email: string
    }) => {
        if (loading) return;
        setLoading(true);

        userService.forgotPassword(email)
            .then(() => {
                dispatch(showNofification({
                    type: 'success',
                    message: t('login.modal.forgot-success')
                }));
                props.onClose();
            })
            .catch(err => {
                console.log('forgot err: ', err.response.data);

                if (!err.response.data.code)
                    dispatch(showNofification({
                        type: 'error',
                        message: t('login.modal.forgot-failed')
                    }))
                else {
                    dispatch(showNofification({
                        type: 'error',
                        message: t('response.errors.' + err.response.data.code)
                    }))
                }
            })
            .finally(() => setLoading(false));

    };

    const onFailed = () => {
        dispatch(showNofification({
            type: 'warning',
            message: t('login.modal.errors.check-again')
        }))
    };

    useEffect(() => {
        console.log('props.visible', props.visible);
        form.resetFields();
    }, [props]);


    return <Modal
        open={props.visible}
        onCancel={props.onClose}
        title={`${t('login.modal.title')}`}
        footer={null}>
        <Form
            form={form}
            autoComplete="off"
            onFinish={onFinish}
            onFinishFailed={onFailed}
        >

            <span>
                {t('login.modal.form.labels.email')}
            </span>
            <FormItem name="email"
                rules={[{ required: true, message: `${t('login.modal.errors.email')}` }]} >
                <Input
                    type="email"
                    allowClear
                    placeholder={`${t('login.placeholders.email')}`}
                />
            </FormItem>


            <FormItem>
                <Button htmlType="submit" loading={loading}>
                    {t('login.buttons.send')}
                </Button>
            </FormItem>
        </Form>
    </Modal>
};

export default ForgotPasswordModal;