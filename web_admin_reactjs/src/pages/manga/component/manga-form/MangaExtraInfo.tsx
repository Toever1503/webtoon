import { Form, Input } from "antd";

const MangaExtraInfo: React.FC = () => {
    const [form] = Form.useForm();
    const onFinish = (values: any) => {
        console.log('Success:', values);
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };
    return (
        <div className="">
            <p className='text-[15px] font-bold p-[10px] m-0'>Manga extra info</p>

            <div>
                {/* <Form
                    name="basic"
                    form={form}
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    layout="vertical"
                    autoComplete="off"
                >
                    <Form.Item
                        label="Alternative name"
                        name="alternativeName"
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Status"
                        name="status"
                    >
                        <Input />
                    </Form.Item>
                </Form> */}
            </div>
        </div>)
};


export default MangaExtraInfo;