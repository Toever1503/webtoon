import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { Button, Form, Input, Row, Card, Checkbox } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ForgotPasswordModal from "./components/ForgotPasswordModal";
import NotificationComponent from "../../components/NotificationComponent";
import { useDispatch } from "react-redux";
import { showNofification } from "../../stores/features/notification/notificationSlice";
import userService from "../../services/user/UserService";
import { AxiosResponse } from "axios";
import { hasAnyAuths, setCookie } from "../../plugins/cookieUtil";
import { useNavigate } from "react-router-dom";


const FormItem = Form.Item

const LoginPage: React.FC = () => {

    const { t } = useTranslation();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [form] = Form.useForm();

    const [loading, setLoading] = useState<boolean>(false);

    const [forgotPasswordModalVisible, setForgotPasswordModalVisible] = useState<boolean>(false);


    const onFinish = (payload: {
        username: string,
        password: string,
        rememberMe: boolean
    }): void => {
        setLoading(true);
        console.log('username: ', payload);

        userService.signin(payload)
            .then((res: AxiosResponse<{
                token: string,
                validTimeIn: number,
                auths: string[]
            }>) => {
                console.log('login success: ', res.data);
                localStorage.setItem('auths', JSON.stringify(res.data.auths));

                if (!hasAnyAuths(['ADMIN', 'EMP'])) {
                    dispatch(showNofification({
                        type: 'error',
                        message: 'Vui lòng sử dụng tài khoản admin để đăng nhập!'
                    }));
                    navigate('/signin');
                    return;
                }

                setCookie('token', res.data.token, res.data.validTimeIn);
                dispatch(showNofification({
                    type: 'success',
                    message: t('login.errors.login-success')
                }));
                navigate('/');
            })
            .catch((err: any) => {
                console.log('login failed: ', err);

                dispatch(showNofification({
                    type: 'error',
                    message: t('login.errors.login-failed')
                }));
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
        form.setFieldsValue({
            username: 'admin',
            password: 123456,
            rememberMe: false
        });
    }, []);

    return (
        <div className="bg-[#c4e3ff] w-full h-full flex items-center">
            <NotificationComponent />
            <ForgotPasswordModal visible={forgotPasswordModalVisible} onClose={() => setForgotPasswordModalVisible(false)} />
            <div className="min-w-[350px] mx-auto">
                <Card >
                    <div className='{styles.logo}'>
                        <img alt="logo" src='{config.logoPath}' />
                        <h3 className="text-center text-xl">   Webtoon Admin </h3>
                    </div>
                    <Form
                        autoComplete="off"
                        form={form}
                        onFinish={onFinish}
                        onFinishFailed={onFailed}
                    >
                        <FormItem name="username"
                            rules={[{ required: true }]} >
                            <Input prefix={<>
                                <UserOutlined />
                            </>}
                                placeholder={`${t('login.placeholders.username')}`}
                            />
                        </FormItem>

                        <FormItem name="password" rules={[{ required: true }]}>
                            <Input prefix={<LockOutlined />} type='password' required placeholder={`${t('login.placeholders.password')}`} />
                        </FormItem>

                        <Form.Item name="rememberMe">
                            <Checkbox onChange={e => form.setFieldValue('rememberMe', e.target.checked)}>{t('login.placeholders.remember')}</Checkbox>

                            <a className="login-form-forgot float-right" onClick={() => setForgotPasswordModalVisible(true)}>
                                {t('login.placeholders.forgot')}
                            </a>
                        </Form.Item>

                        <Row>
                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={loading}
                            >
                                {t('login.buttons.login')}
                            </Button>
                        </Row>
                    </Form>
                </Card>
            </div>
        </div>
    )
};

export default LoginPage;