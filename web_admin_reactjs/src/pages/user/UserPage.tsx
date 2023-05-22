import { Dropdown, Input, MenuProps, Popconfirm, Space, Table, Tooltip, Button, Select } from "antd";
import { ColumnsType, TablePaginationConfig } from "antd/es/table";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import IUserType, { IUserStatus, USER_STATUS_LIST } from "./types/IUserType";
import { DownOutlined } from "@ant-design/icons";
import AddEditUserModal, { AddEditUserModalProps } from "./components/AddEditUserModal";
import { UserState, addNewUser, deleteUser, editUser, setUserData } from "../../stores/features/user/userSlice";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../stores";
import { reIndexTbl } from "../../utils/indexData";
import userService from "../../services/user/UserService";
import { AxiosResponse } from "axios";
import { showNofification } from "../../stores/features/notification/notificationSlice";



const UserPage: React.FC = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const userState: UserState = useSelector((state: RootState) => state.user);

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<IUserType[]>([]);
    const [pageConfig, setPageConfig] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: false,
    });
    const [userFilter, setUserFilter] = useState<any>({
        role: 'ALL',
    });
    const onSearch = () => {
        onCallApiFilterUser();
    }

    const getAccountStatuses = (status: IUserStatus, record: IUserType): MenuProps => {
        return {
            items: USER_STATUS_LIST.filter((e: IUserStatus) => e !== status).map((e: IUserStatus) => {
                return {
                    key: e,
                    label: t(`user.table.status-item-select.${e}`),
                    onClick: () => {
                        console.log('e', e);
                        console.log('e', e);
                        setTableLoading(true);
                        if (record.id)
                            userService.changeStatus(record.id, e)
                                .then(() => {
                                    dispatch(showNofification({
                                        type: 'success',
                                        message: t('user.table.change-status-success')
                                    }));
                                    dispatch(setUserData(dataSource.map((item: IUserType) => {
                                        if (item.id === record.id) {
                                            item.status = e;
                                        }
                                        return item;
                                    })))
                                })
                                .catch(err => {
                                    console.log('err', err);
                                    dispatch(showNofification({
                                        type: 'error',
                                        message: t('user.table.change-status-failed')
                                    }));
                                })
                                .finally(() => setTableLoading(false))
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
            render: (text) => <>{text ? text : '-'}</>,
        },
        {
            title: t('user.table.sex'),
            dataIndex: 'sex',
            key: 'sex',
            render: (text) => <>{text && t(`user.form.sex-radio.${text.toLowerCase()}`)}</>,
        },
        {
            title: t('user.table.role'),
            dataIndex: 'role',
            key: 'role',
            render: (text) => <>{t(`user.table.role-item.${text?.roleName}`)}</>,
        },
        {
            title: t('user.table.accountType'),
            dataIndex: 'accountType',
            key: 'accountType',
            render: (text) => <>{t(`user.table.accountType-item.${text}`)}</>,
        },
        {
            title: t('user.table.status'),
            dataIndex: 'status',
            key: 'status',
            width: 150,
            render: (text, record) => <>
                <Dropdown menu={getAccountStatuses(text, record)} trigger={['click']}>
                    <a onClick={(e) => e.preventDefault()} className='text-[12px]'>
                        <span className='text-[12px] flex gap-x-[5px] items-center'>
                            <Tooltip title="Thay đổi trạng thái">
                                {t(`user.table.status-item.${text}`)}
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
            width: 150,
            render: (_, record) => (
                <Space size="small">
                    <Button size="small" onClick={() => showEditUserModal(record)}>{t('user.form.edit-btn')}</Button>
                    <Popconfirm
                        title={t('user.form.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                            if (record.id)
                                onDeleleUser(record.id);
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <Button size="small" onClick={e => e.stopPropagation()} >{t('user.form.delete-btn')}</Button>
                    </Popconfirm>

                </Space>
            ),
        },
    ];


    // begin add edit user modal
    const [addEditUserModal, setAddEditUserModal] = useState<AddEditUserModalProps>({
        title: 'Add new user',
        visible: false,
        cancel: () => {
            setAddEditUserModal({ ...addEditUserModal, visible: false, userInput: undefined })
        },
        onOk: (data: IUserType) => {
            console.log('modal prop: ', addEditUserModal);

            dispatch(addNewUser({
                data,
                pageSize: pageConfig.pageSize
            }));
            addEditUserModal.cancel();
        },
        authorityOptions: []
    });
    const [authorityOptions, setAuthorityOptions] = useState<{
        id: number | string;
        authorityName: string;
    }[]>([]);

    const showEditUserModal = (record: IUserType) => {
        console.log('edit user: ', record);

        setAddEditUserModal({
            ...addEditUserModal,
            visible: true,
            title: t('user.modal.edit-title'),
            userInput: record,
        });
    };

    const onDeleleUser = (id: number | string) => {
        setTableLoading(true);

        userService
            .deleteUserById(id)
            .then(() => {
                dispatch(showNofification({
                    type: 'success',
                    message: t('user.form.delete-success')
                }))
                dispatch(deleteUser(Number(id)));
            })
            .catch(err => {
                console.log('delete user err', err);
                dispatch(showNofification({
                    type: 'success',
                    message: t('user.form.delete-failed')
                }))
            })
            .finally(() => setTableLoading(false))
    }
    // end add edit user modal

    const onTblChange = (pagination: TablePaginationConfig) => {
        pageConfig.current = pagination.current || 1;
        setPageConfig(pageConfig);
        onCallApiFilterUser();
    };
    const onCallApiFilterUser = () => {
        setTableLoading(true);
        userService.filterUser({
            q: userFilter.q,
            role: userFilter.role === 'ALL' ? undefined : userFilter.role,
        }, pageConfig.current - 1, pageConfig.pageSize)
            .then((res: AxiosResponse<{
                content: IUserType[],
                totalElements: number,
            }>) => {
                console.log('res', res.data);
                dispatch(setUserData(
                    res.data.content
                ));
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements,
                });
            })
            .finally(() => setTableLoading(false));
    }

    const [hasInitialized, setHasInitialized] = useState(false);
    useEffect(() => {
        if (!hasInitialized) {
            onCallApiFilterUser();

            userService.getAllAuthorities()
                .then((res: AxiosResponse<any>) => {
                    console.log('get all authorities: ', res.data);
                    setAuthorityOptions(res.data);
                })
                .catch(err => {
                    console.log('err: ', err);
                })

            setHasInitialized(true);
        }


        setDataSource(reIndexTbl(pageConfig.current, pageConfig.pageSize || 0, userState.data))
    }, [userState]);

    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <h1 className="text-[23px] font-[400] m-0">
                    {t('user.page-title')}
                </h1>
                <Button className="font-medium" onClick={() => {
                    setAddEditUserModal({
                        ...addEditUserModal,
                        visible: true,
                        title: t('user.modal.add-title'),
                    });
                }}>
                    {t('user.add-btn')}
                </Button>

            </div>

            <div className="grid lg:flex items-center justify-between gap-3 bg-white px-[15px] py-[15px]">
                <Space>
                    <label className="font-bold">
                        Loại tài khoản: </label>
                    <Select className="min-w-[200px]"
                        onChange={val => {
                            userFilter.role = val;
                            setUserFilter(userFilter);
                            onCallApiFilterUser();
                        }}
                        value={userFilter.role}>
                        <Select.Option value="ALL">
                            {t('order.eStatus.ALL')}
                        </Select.Option>

                        {/* <Select.Option value='1' >
                            Quản lý
                        </Select.Option> */}

                        <Select.Option value='2' >
                            Nhân viên
                        </Select.Option>

                        <Select.Option value='3' >
                            Khách hàng
                        </Select.Option>
                    </Select>
                </Space>

                <Space>
                    <label className="font-bold">Từ khóa: </label>
                    <Input.Search placeholder={`${t('placeholders.search')}`} value={userFilter.q} onChange={e => setUserFilter({ ...userFilter, q: e.target.value })} onSearch={onSearch} style={{ width: 200 }} />

                </Space>

            </div>
            <AddEditUserModal visible={addEditUserModal.visible} authorityOptions={authorityOptions} cancel={addEditUserModal.cancel} onOk={addEditUserModal.onOk} title={addEditUserModal.title} userInput={addEditUserModal.userInput} />
            <div className="flex justify-end items-center">
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} onChange={onTblChange} rowKey={(() => Math.random())} pagination={pageConfig} />
        </div>
    </>)
}

export default UserPage;