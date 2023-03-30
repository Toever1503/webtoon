import React, { useEffect } from "react";
import { Button, Form, Input, Modal } from 'antd';
import { useDispatch, } from "react-redux";
import { addTag, updateTag } from "../../../stores/features/manga/tagSlice";
import tagService from "../../../services/TagService";
import { showNofification } from "../../../stores/features/notification/notificationSlice";


interface AddUpdateGenreModalProps {
    config: object;
}

const AddUpdateGenreModal: React.FC = ({ config }: AddUpdateGenreModalProps | any) => {


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
        setIsSubmitting(true);
        if (config.type === 'add') {
            tagService
                .addTag(values)
                .then((res) => {
                    console.log('add tag: ', res.data);
                    dispatch(addTag(res.data));
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Add tag successfully',
                    }));
                    handleCancel();
                })
                .finally(() => {
                    setIsSubmitting(false);
                });
        }
        else {
            tagService
                .updateTag({ ...values, id: config.record.id })
                .then((res) => {
                    console.log('update tag: ', res.data);
                    dispatch(updateTag(res.data));
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Edit tag successfully',
                    }));
                    handleCancel();
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
                labelCol={{ span: 4 }}
            >
                <Form.Item
                    label="Name"
                    name="tagName"
                    rules={[{ required: true, message: 'Please input your name!' }]}
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
                        Save
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};


export default AddUpdateGenreModal;