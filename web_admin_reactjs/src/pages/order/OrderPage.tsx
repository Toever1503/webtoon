import { Input, PaginationProps, Popconfirm, Space, Table } from "antd";
import { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const OrderPage: React.FC = () => {
    const { t } = useTranslation();

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<any[]>();
    const [pageConfig, setPageConfig] = useState<PaginationProps>({
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: false,
    });
    const columns: ColumnsType<any> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: 'Name',
            dataIndex: 'title',
            key: 'name',
            render: (text) => <>{text}</>,
        },
        {
            title: 'Manga Type',
            dataIndex: 'mangaType',
            key: 'mangaType',
            render: (text) => <>{text}</>,
        },
        
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <Link to={`/mangas/edit/${record.id}`}>Edit</Link>
                    <Popconfirm
                        title={t('manga.form.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <a onClick={e => e.stopPropagation()} className="text-red-400 hover:text-red-500">Delete</a>
                    </Popconfirm>

                </Space>
            ),
        },
    ];


    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <h1 className="text-[23px] font-[400] m-0">
                    {t('order.page-title')}
                </h1>
            </div>
            <div className="flex justify-end items-center">
                <Input.Search placeholder="input search text" style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
        </div>
    </>)
};


export default OrderPage;