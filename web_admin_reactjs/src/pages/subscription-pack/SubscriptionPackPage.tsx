import { Button, Input, PaginationProps, Popconfirm, Space, Table } from "antd";
import { ColumnsType } from "antd/es/table";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import AddEditSubscriptionPackModal, { AddEditSubscriptionPackModalProps } from "./components/AddEditSubscriptionPackModal";
import { SubscriptionPackState, addSubscriptionPack, setSubscriptionPackData } from "../../stores/features/subscription-pack/subscriptionPackSlice";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../stores";
import subscriptionPackService from "../../services/subscription_pack/subscriptionPackService";
import { AxiosResponse } from "axios";
import { reIndexTbl } from "../../utils/indexData";

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
        setAddEditSubscriptionPackModal({
            ...addEditSubscriptionPackModal,
            visible: true,
            title: t('subscription-pack.modal.editTitle'),
            input: input
        })
    };

    const columns: ColumnsType<ISubscriptionPack> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('subscription-pack.table.title'),
            dataIndex: 'title',
            key: 'title',
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

    const onCallApiFilter = () => {

    };

    const [hasInitialized, setHasInitialized] = useState(false);
    useEffect(() => {
        if (!hasInitialized) {
            onCallApiFilter();

            subscriptionPackService.filterSubscriptionPack({
                q: undefined
            })
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
            setHasInitialized(true);
        };


        // setDataSource(reIndexTbl(pageConfig.current || 1, pageConfig.pageSize || 0, subscriptionPackState.data))
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
            <div className="flex justify-end items-center">
                <Input.Search placeholder="input search text" style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
            <AddEditSubscriptionPackModal visible={addEditSubscriptionPackModal.visible} title={addEditSubscriptionPackModal.title}
                onCancel={addEditSubscriptionPackModal.onCancel}
                onOk={addEditSubscriptionPackModal.onOk}
                input={addEditSubscriptionPackModal.input} />
        </div>
    </>)
};

export default SubscriptionPackPage;