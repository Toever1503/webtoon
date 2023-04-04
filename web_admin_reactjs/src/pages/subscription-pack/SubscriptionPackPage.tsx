import { Input, PaginationProps, Popconfirm, Space, Table } from "antd";
import { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import ISubscriptionPackType from "./types/ISubscriptionPackType";

const SubscriptionPackPage: React.FC = () => {
    const { t } = useTranslation();

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<any[]>();
    const [pageConfig, setPageConfig] = useState<PaginationProps>({
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: false
    });

    const columns: ColumnsType<ISubscriptionPackType> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('subscription-pack.table.name'),
            dataIndex: 'name',
            key: 'name',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.desc'),
            dataIndex: 'desc',
            key: 'desc',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.price'),
            dataIndex: 'price',
            key: 'price',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.limitedDayCount'),
            dataIndex: 'dayCount',
            key: 'dayCount',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.createdAt'),
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.modifiedAt'),
            dataIndex: 'modifiedAt',
            key: 'modifiedAt',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.createdBy'),
            dataIndex: 'createdBy',
            key: 'createdBy',
            render: (text) => <>{text}</>,
        },
        {
            title: t('subscription-pack.table.modifiedBy'),
            dataIndex: 'modifiedBy',
            key: 'modifiedBy',
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
                    {t('subscription-pack.page-title')}
                </h1>
            </div>
            <div className="flex justify-end items-center">
                <Input.Search placeholder="input search text" style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
        </div>
    </>)
};

export default SubscriptionPackPage;