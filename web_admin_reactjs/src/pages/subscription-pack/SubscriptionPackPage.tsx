import { Button, Input, PaginationProps, Popconfirm, Space, Table } from "antd";
import { ColumnsType } from "antd/es/table";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import AddEditSubscriptionPackModal, { AddEditSubscriptionPackModalProps } from "./components/AddEditSubscriptionPackModal";
import { SubscriptionPackState, addSubscriptionPack, deleteSubscriptionPackById, setSubscriptionPackData, updateSubscriptionPack } from "../../stores/features/subscription-pack/subscriptionPackSlice";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../stores";
import subscriptionPackService from "../../services/subscription_pack/subscriptionPackService";
import { AxiosResponse } from "axios";
import { reIndexTbl } from "../../utils/indexData";
import formatVnCurrency from "../../utils/formatVnCurrency";
import ISubscriptionPackFilterInput from "../../services/subscription_pack/types/ISubscriptionPackFilterInput";
import debounce from "../../utils/debounce";
import { showNofification } from "../../stores/features/notification/notificationSlice";

const SubscriptionPackPage: React.FC = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const subscriptionPackState: SubscriptionPackState = useSelector((state: RootState) => state.subscriptionPack);

    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<any[]>();
    const [pageConfig, setPageConfig] = useState<PaginationProps>({
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: false
    });
    const columns: ColumnsType<ISubscriptionPack> = [
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
            title: t('subscription-pack.table.price'),
            dataIndex: 'price',
            key: 'price',
            render: (text, record) => <>
                {
                    record.discountPrice ?
                        <>
                            <div>
                                {formatVnCurrency(record.discountPrice)}
                            </div>
                            {
                                <del>
                                    {formatVnCurrency(record.originalPrice)}
                                </del>
                            }
                        </>
                        :
                        formatVnCurrency(record.originalPrice)
                }
            </>,
        },
        {
            title: t('subscription-pack.table.limitedDayCount'),
            dataIndex: 'monthCount',
            key: 'monthCount',
            render: (text) => <>
                {text} {t('subscription-pack.modal.month')}
            </>,
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
                    <a onClick={() => showAddEditModal(record)}>Edit</a>
                    <Popconfirm
                        title={t('subscription-pack.table.sure-delete')}
                        onConfirm={() => onDelete(record.id || 0)}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <a onClick={e => e.stopPropagation()} className="text-red-400 hover:text-red-500">Delete</a>
                    </Popconfirm>

                </Space>
            ),
        },
    ];


    const onTblChange = (pagination: PaginationProps) => {
        pageConfig.current = pagination.current;
        setPageConfig({
            ...pageConfig,
        });
        onCallApiFilter();
    };
    const [addEditSubscriptionPackModal, setAddEditSubscriptionPackModal] = useState<AddEditSubscriptionPackModalProps>({
        visible: false,
        title: '',
        onCancel: () => {
            console.log('Cancel');

            setAddEditSubscriptionPackModal({
                ...addEditSubscriptionPackModal,
                visible: false,
                title: t('subscription-pack.modal.addTitle'),
                input: undefined
            })
        },
        onOk: (record: ISubscriptionPack) => {
            if (addEditSubscriptionPackModal.input) { // for edit
                dispatch(updateSubscriptionPack(record));
            } else // for add
                dispatch(addSubscriptionPack({
                    data: record,
                    pageSize: pageConfig.pageSize || 10,
                }));
            addEditSubscriptionPackModal.onCancel();
        },
        input: undefined
    });

    const showAddEditModal = (input?: ISubscriptionPack) => {
        addEditSubscriptionPackModal.input = input;

        setAddEditSubscriptionPackModal({
            ...addEditSubscriptionPackModal,
            visible: true,
            title: t('subscription-pack.modal.editTitle'),
            input: input
        });
    };

    const onDelete = (id: number | string) => {
        if (tableLoading) return;
        setTableLoading(true);

        subscriptionPackService.deleteSubscriptionPack(id)
            .then(() => {
                dispatch(showNofification({
                    type: 'success',
                    message: t('subscription-pack.table.delete-success')
                }));
                dispatch(deleteSubscriptionPackById({ id }));
            })
            .catch(err => {
                console.log('delete subs pack failed: ', err);

                dispatch(showNofification({
                    type: 'success',
                    message: t('subscription-pack.table.delete-failed')
                }))
            })
            .finally(() => setTableLoading(false));

    }

    const onCallApiFilter = () => {
        if (tableLoading) return;
        setTableLoading(true);
        subscriptionPackService.filterSubscriptionPack({
            q: undefined
        }, (pageConfig.current || 1) - 1, pageConfig.pageSize)
            .then((res: AxiosResponse<{
                content: ISubscriptionPack[],
                totalElements: number
            }>) => {
                console.log('get all subscriptions: ', res.data);

                dispatch(setSubscriptionPackData(
                    res.data.content
                ));
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements
                });
            })
            .catch(err => {
                console.log('err: ', err);
            })
            .finally(() => setTableLoading(false))
    };

    const [hasInitialized, setHasInitialized] = useState(false);
    useEffect(() => {
        if (!hasInitialized) {
            onCallApiFilter();

            setHasInitialized(true);
        };


        setDataSource(reIndexTbl(pageConfig.current || 1, pageConfig.pageSize || 0, subscriptionPackState.data))

    }, [subscriptionPackState]);


    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <h1 className="text-[23px] font-[400] m-0">
                    {t('subscription-pack.page-title')}
                </h1>
                <Button type="primary" className="flex items-center" onClick={() => {
                    setAddEditSubscriptionPackModal({
                        ...addEditSubscriptionPackModal,
                        visible: true,
                        title: t('subscription-pack.modal.addTitle'),
                        input: undefined
                    })
                }}>
                    <span className="mr-2">{t('subscription-pack.addBtn')}</span>
                </Button>
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} onChange={onTblChange} pagination={pageConfig} />
            <AddEditSubscriptionPackModal visible={addEditSubscriptionPackModal.visible} title={addEditSubscriptionPackModal.title}
                onCancel={addEditSubscriptionPackModal.onCancel}
                onOk={addEditSubscriptionPackModal.onOk}
                input={addEditSubscriptionPackModal.input} />
        </div>
    </>)
};

export default SubscriptionPackPage;