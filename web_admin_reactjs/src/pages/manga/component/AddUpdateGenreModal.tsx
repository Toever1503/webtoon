import React, { useEffect } from "react";
import { Button, Form, Input, Modal } from 'antd';
import { useDispatch, } from "react-redux";
import { addGenre, updateGenre } from "../../../stores/features/manga/genreSlice";


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
        if(config.type === 'add')
            dispatch(addGenre(values));
        else
            dispatch(updateGenre(values));
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    useEffect(() => {
        if (config.visible)
            form.resetFields();
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
                    name="name"
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