import { Dropdown, Input, MenuProps, Popconfirm, Space, Table, Tooltip } from "antd";
import { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import IUserType, { IUserStatus, USER_STATUS_LIST } from "./types/UserType";
import { DownOutlined } from "@ant-design/icons";



const UserPage: React.FC = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<IUserType[]>([
        {
            id: 1,
            avatar: '',
            email: 'fa@fa.ca',
            phone: '123456789',
            fullName: 'Nguyen Van A',
            sex: "FEMALE",
            status: "ACTIVED",
            username: 'nguyenvana',
            accountType: 'NORMAL'
        }
    ]);
    const [pageConfig, setPageConfig] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [userFilter, setUserFilter] = useState<any>({
        commentType: 'ALL'
    });
    const onSearch = () => {
    }



    const getAccountStatuses = (status: IUserStatus, record: IUserType): MenuProps => {
        return {
           items: USER_STATUS_LIST.filter((e: IUserStatus) => e !== status).map((e: IUserStatus) => {
                return {
                    key: e,
                    label: e,
                    onClick: () => {
                        console.log('e', e);
                        console.log('e', e);
                        setTableLoading(true);
                        // mangaService.changeReleaseStatus(record.id, e)
                        //     .then(() => {
                        //         dispatch(showNofification({
                        //             type: 'success',
                        //             message: 'Thay đổi trạng thái thành công!'
                        //         }));
                        //         setMangaData(mangaData.map((item: MangaInput) => {
                        //             if (item.id === record.id) {
                        //                 item.mangaStatus = e;
                        //             }
                        //             return item;
                        //         }));
                        //     })
                        //     .catch(err => {
                        //         console.log('err', err);
                        //         dispatch(showNofification({
                        //             type: 'error',
                        //             message: 'Thay đổi trạng thái thất bại!'
                        //         }));
                        //     })
                        //     .finally(() => setTableLoading(false))
                    }
                }
            })
        }
    };
    const columns: ColumnsType<IUserType> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('user.table.fullName'),
            dataIndex: 'fullName',
            key: 'fullName',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.username'),
            dataIndex: 'username',
            key: 'username',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.email'),
            dataIndex: 'email',
            key: 'email',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.phone'),
            dataIndex: 'phone',
            key: 'phone',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.sex'),
            dataIndex: 'sex',
            key: 'sex',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.accountType'),
            dataIndex: 'accountType',
            key: 'accountType',
            render: (text) => <>{text}</>,
        },
        {
            title: t('user.table.status'),
            dataIndex: 'status',
            key: 'status',
            render: (text, record) => <>
                <Dropdown menu={getAccountStatuses(text, record)} trigger={['click']}>
                    <a onClick={(e) => e.preventDefault()} className='text-[12px]'>
                        <span className='text-[12px] flex gap-x-[5px] items-center'>
                            <Tooltip title="Thay đổi trạng thái">
                                {text}
                            </Tooltip>
                            <DownOutlined />
                        </span>
                    </a>
                </Dropdown>
            </>,
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
                    {t('user.page-title')}
                </h1>
            </div>
            <div className="flex justify-end items-center">
                <Input.Search placeholder="input search text" value={userFilter.q} onChange={e => setUserFilter({ ...userFilter, q: e.target.value })} onSearch={onSearch} style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
        </div>
    </>)
}

export default UserPage;