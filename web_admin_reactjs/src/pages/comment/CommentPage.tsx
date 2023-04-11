import { Button, Input, Popconfirm, Select, Space } from "antd";
import Table, { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import IUserType from "../user/types/UserType";


type CommentFilter = {
    q?: string;
    commentType: CommentType;
}
type CommentType = 'POST' | 'MANGA' | 'ALL';

interface CommentInputType {
    index: number;
    id: number | string;
    content: string;
    createdAt: string;
    createdBy?: IUserType;
    commentType: CommentType;
};

const CommentPage: React.FC = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<CommentInputType[]>([
        {
            index: 1,
            id: 1,
            content: '1',
            createdAt: '1',
            commentType: 'POST'

        }
    ]);
    const [pageConfig, setPageConfig] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [commentFilter, setCommentFilter] = useState<CommentFilter>({
        commentType: 'ALL'
    });
    const onSearch = () => {
    }

    const columns: ColumnsType<CommentInputType> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('comment.table.user'),
            dataIndex: 'createdBy',
            key: 'createdBy',
            render: (text) => <>{
                text ? text.fullName : '-'
            }</>,
        },
        {
            title: t('comment.table.content'),
            dataIndex: 'content',
            key: 'content',
            render: (text) => <>{text}</>,
        },
        {
            title: t('comment.table.createdAt'),
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (text) => <>{text}</>,
        },

        {
            title: t('comment.table.action'),
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <Link to={`/mangas/edit/${record.id}`}>Edit</Link>
                    <Popconfirm
                        title={t('manga.form.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                            // onDeleleManga(record.id);
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
                    {t('comment.page-title')}
                </h1>
            </div>
            <div className="flex justify-between items-center">
                <div className="flex space-x-2 items-center">
                    <span className="text-lg">{t('comment.comment-type-title')}</span>
                    <Select value={commentFilter.commentType} onChange={(val: CommentType) => setCommentFilter({ ...commentFilter, commentType: val })}>
                        <Select.Option value="ALL">Tất cả</Select.Option>
                        <Select.Option value="POST">1</Select.Option>
                        <Select.Option value="MANGA">2</Select.Option>
                    </Select>
                </div>

                <Input.Search placeholder="input search text" value={commentFilter.q} onChange={e => setCommentFilter({ ...commentFilter, q: e.target.value })} onSearch={onSearch} style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
        </div>
    </>)
}

export default CommentPage;