import React, { useEffect } from "react";
import { Button, Form, Input, Modal } from 'antd';
import { useDispatch, } from "react-redux";
import authorService from "../../../../services/manga/AuthorService";
import { addAuthor, updateAuthor } from "../../../../stores/features/manga/authorSlice";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";
import { useTranslation } from "react-i18next";


interface AddUpdateAuthorModalProps {
    config: object;
}

const AddUpdateAuthorModal: React.FC<AddUpdateAuthorModalProps> = ({ config }: AddUpdateAuthorModalProps | any) => {

    const { t } = useTranslation();
    const dispatch = useDispatch();
    const [form] = Form.useForm();
    const [isSubmitting, setIsSubmitting] = React.useState(false);

    const handleOk = () => {
        console.log('OK', config);
    };

    const handleCancel = () => {
        config.setVisible(false);
    };

    const onFinish = (values: any) => {
        console.log('Success:', values);

        if (isSubmitting) return;
        setIsSubmitting(true);
        values.name = values.name.trim();
        if (config.type === 'add') {
            authorService
                .addAuthor(values)
                .then((res) => {
                    console.log('add tag: ', res.data);
                    dispatch(addAuthor(res.data));
                    handleCancel();
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Thêm thành công!',
                    }))
                })
                .catch(err => {
                    console.log('err', err);

                    if (!err.response.data.code)
                        dispatch(showNofification({
                            type: 'error',
                            message: 'Thêm thất bại!'
                        }))
                    else {
                        dispatch(showNofification({
                            type: 'error',
                            message: t('response.errors.' + err.response.data.code)
                        }))
                    }
                })
                .finally(() => {
                    setIsSubmitting(false);
                });
        } else {
            authorService
                .updateAuthor({ ...values, id: config.record.id })
                .then((res) => {
                    console.log('update tag: ', res.data);
                    dispatch(updateAuthor(res.data));
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Sửa thành công!',
                    }))
                    handleCancel();
                })
                .catch(err => {
                    if (!err.response.data.code)
                        dispatch(showNofification({
                            type: 'error',
                            message: 'Sửa thất bại!'
                        }))
                    else {
                        dispatch(showNofification({
                            type: 'error',
                            message: t('response.errors.' + err.response.data.code)
                        }))
                    }
                })
                .finally(() => {
                    setIsSubmitting(false);
                });
        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    useEffect(() => {
        if (config.visible) {
            form.resetFields();
            if (config.type === 'update') {
                console.log('config.record', config.record);
                form.setFieldsValue(config.record);
            }
        }
    }, [config]);

    return (
        <Modal title={config.title} open={config.visible} footer={null} onOk={handleOk} onCancel={handleCancel}>
            <Form
                style={{ marginTop: 50 }}
                name="basic"
                form={form}
                initialValues={{ remember: true }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                autoComplete="off"
                labelCol={{ span: 5 }}
            >
                <Form.Item
                    label="Tên tác giả"
                    name="name"
                    rules={[{ required: true, message: 'Vui lòng nhập tên!' }]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Slug"
                    name="slug"
                >
                    <Input />
                </Form.Item>

                <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                    <Button type="primary" htmlType="submit" loading={isSubmitting}>
                        Lưu
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};


export default AddUpdateAuthorModal;