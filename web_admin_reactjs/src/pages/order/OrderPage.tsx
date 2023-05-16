import { Button, DatePicker, Input, PaginationProps, Popconfirm, Select, Space, Table, Tag } from "antd";
import { ColumnsType } from "antd/es/table";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import IOrderFilterInput from "../../services/order/types/IOrderFilterInput";
import orderService from "../../services/order/OrderService";
import { AxiosResponse } from "axios";
import IOrder, { EORDER_STATUS } from "../../services/order/types/IOrder";
import { reIndexTbl } from "../../utils/indexData";
import { RootState } from "../../stores";
import { useDispatch, useSelector } from "react-redux";
import { IOrderState, addOrder, deleteOrderById, setOrderData, updateOrder } from "../../stores/features/order/orderSlice";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import AddEditOrderModal, { AddEditOrderModalProps } from "./components/AddEditOrderModal";
import subscriptionPackService from "../../services/subscription_pack/subscriptionPackService";
import debounce from "../../utils/debounce";
import IUserType from "../user/types/IUserType";
import formatVnCurrency from "../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../utils/dateFormat";
import UpgradeOrderModal, { UpgradeOrderModalProps } from "./components/UpgradeOrderModal";
import { showNofification } from "../../stores/features/notification/notificationSlice";
import DetailOrderModal, { DetailOrderModalProps } from "./components/DetailOrderModal";
import { Dayjs } from "dayjs";

const OrderPage: React.FC = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const orderState: IOrderState = useSelector((state: RootState) => state.order);
    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<IOrder[]>([]);
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
            title: t('order.table.orderNumber'),
            dataIndex: 'maDonHang',
            key: 'orderNumber',
            width: 100,
            render: (text) => <>{text}</>,
        },
        {
            title: t('order.table.subscription'),
            dataIndex: 'subs_pack_id',
            key: 'subscription',
            render: (text: ISubscriptionPack, record: IOrder) => <>
                {text?.name} <br />
                {
                    record.orderType === 'UPGRADE' && <span className="text-red-400"> ({t('order.table.upgrade')})</span>
                }

                {
                    record.orderType === 'RENEW' && <span className="text-red-400"> ({t('order.table.renew')})</span>
                }
            </>,
        },
        {
            title: t('order.table.finalPrice'),
            dataIndex: 'finalPrice',
            key: 'finalPrice',
            render: (text) => <>{
                formatVnCurrency(text)
            }</>,
        },
        {
            title: t('order.table.paymentMethod'),
            dataIndex: 'paymentMethod',
            key: 'paymentMethod',
            render: (text) => <>{text}</>,
        },
        {
            title: 'Số điện thoại',
            dataIndex: 'email',
            key: 'email',
            render: (_, record: IOrder) => <>{
                record.user_id.phone
            }</>,
        },
        {
            title: t('order.table.status'),
            dataIndex: 'status',
            key: 'status',
            render: (text: EORDER_STATUS) => <>
                <Tag color={getColorForStatus(text)}>{t('order.eStatus.' + text)}</Tag>
            </>,
        },

        {
            title: t('order.table.createdBy'),
            dataIndex: 'createdBy',
            key: 'createdBy',
            render: (_, record: IOrder) => <>{
                record.user_id.fullName
            }</>,
        },
        {
            title: t('order.table.modifiedBy'),
            dataIndex: 'modifiedBy',
            key: 'modifiedBy',
            render: (text: IUserType) => <>{
                text ? text.fullName : '-'
            }</>,
        },
        {
            title: t('order.table.createdAt'),
            dataIndex: 'created_at',
            key: 'createdAt',
            render: (text) => <>{dateTimeFormat(text)}</>,
        },
        {
            title: t('order.table.updatedAt'),
            dataIndex: 'modifiedAt',
            key: 'updatedAt',
            render: (text) => <>{dateTimeFormat(text)}</>,
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (_, record: IOrder) => (
                <>
                    <Space size="small">
                        {
                            <a onClick={() => viewDetailOrder(record)}>{t('order.table.viewDetail')}</a>
                        }
                        <Popconfirm
                            title={t('manga.form.sure-delete')}
                            onConfirm={(e) => {
                                e?.stopPropagation();
                                handleDeleteOrder(record);
                            }}
                            okText={t('confirm-yes')}
                            cancelText={t('confirm-no')}
                        >
                            <a onClick={e => e.stopPropagation()} className="text-red-400 hover:text-red-500">{
                                t('buttons.delete')
                            }</a>
                        </Popconfirm>

                    </Space>
                    <br />
                    {
                        record.status === 'USER_CONFIRMED_BANKING' &&
                        <Link to={`/orders/handle/${record.id}`}>
                            Xử lý TT
                        </Link>
                    }
                </>
            ),
            width: 150
        },
    ];

    const getColorForStatus = (status: EORDER_STATUS) => {
        if (status === 'COMPLETED') return '#1677ff';
        if (status === 'PENDING_PAYMENT') return '#ff5722';
        if (status === 'USER_CONFIRMED_BANKING') return '#ff5722';
        if (status === 'CANCELED') return 'red';
    }

    const [filterInput, setFilterInput] = useState<{
        q?: string,
        status: string,
        paymentMethod: 'ALL',
        timeRange: Dayjs[],
    }>({
        status: 'ALL',
        paymentMethod: 'ALL',
        timeRange: []
    });


    const FINAL_STATUSES: EORDER_STATUS[] = [
        'PENDING_PAYMENT',
        'USER_CONFIRMED_BANKING',
        'COMPLETED',
        'CANCELED'
    ];

    // begin edit modal
    const [subscriptionPackList, setSubscriptionPackList] = useState<ISubscriptionPack[]>([]);
    const [addEditOrderModal, setAddEditOrderModal] = useState<AddEditOrderModalProps>({
        visible: false,
        title: t('order.modal.addTitle'),
        onCancel: () => {
            addEditOrderModal.visible = false;
            addEditOrderModal.title = t('order.modal.addTitle');
            addEditOrderModal.input = undefined;
            setAddEditOrderModal(addEditOrderModal);
        },
        onOk: (record: IOrder) => {
            console.log('on ok: ', addEditOrderModal);

            if (addEditOrderModal.input) {// for edit
                dispatch(updateOrder(record));
            }
            else {// for add
                dispatch(addOrder({
                    data: record,
                    pageSize: pageConfig.pageSize,
                }));
            }
            addEditOrderModal.onCancel();
        }
    });

    const openEditModal = (record: IOrder) => {
        addEditOrderModal.input = record;
        setAddEditOrderModal({
            ...addEditOrderModal,
            visible: true,
            title: t('order.modal.editTitle'),
        });
    }
    // end edit modal


    // begin upgrade modal
    const [upgradeOrderModal, setUpgradeOrderModal] = useState<UpgradeOrderModalProps>({
        visible: false,
        onCancel: () => {
            upgradeOrderModal.visible = false;
            upgradeOrderModal.input = undefined;
            setUpgradeOrderModal(upgradeOrderModal);
        },
        onOk: (record: IOrder) => {
            dispatch(addOrder({
                data: record,
                pageSize: pageConfig.pageSize,
            }));

            const orders: IOrder[] = dataSource.map<IOrder>((item: IOrder) => {
                if (item.id === upgradeOrderModal.input?.id) {
                    item.hasUpgradingOrder = true;
                }
                return item;
            });
            setDataSource(orders);
            upgradeOrderModal.onCancel();
        },
        input: undefined,
        subscriptionPackList: []
    });

    const showUpgradeModal = (record: IOrder) => {
        upgradeOrderModal.input = record;
        setUpgradeOrderModal({
            ...upgradeOrderModal,
            visible: true,
        });
    }

    // end upgrade modal

    const handleDeleteOrder = (record: IOrder) => {
        if (tableLoading) return;

        setTableLoading(true);
        orderService.deleteById(record.id || 0)
            .then(() => {
                dispatch(deleteOrderById({
                    id: record.id || 0,
                }));

                dispatch(showNofification({
                    type: 'success',
                    message: t('order.delete-success'),
                }));
            })
            .catch(err => {
                console.log('delete order failed: ', err);

                dispatch(showNofification({
                    type: 'error',
                    message: t('order.delete-failed'),
                }));
            })
            .finally(() => setTableLoading(false));
    };


    // begin detail modal
    const [detailOrderModal, setDetailOrderModal] = useState<DetailOrderModalProps>({
        visible: false,
        onCancel: () => {
            detailOrderModal.visible = false;
            setDetailOrderModal(detailOrderModal);
        },
    });
    const showEditModal = (record: IOrder) => {
        console.log('edit');
        openEditModal(record);
    };
    const viewDetailOrder = (record: IOrder) => {
        detailOrderModal.input = record;
        setDetailOrderModal({
            ...detailOrderModal,
            visible: true,
        });
    };

    const onSearch = debounce(() => {
        onCallApi();
    });

    const onCallApi = () => {
        if (tableLoading) return;
        setTableLoading(true);
        orderService
            .filterOrder({
                q: filterInput.q ? filterInput.q : undefined,
                status: filterInput.status !== 'ALL' ? [filterInput.status] : undefined,
                paymentMethod: filterInput.paymentMethod === 'ALL' ? undefined : filterInput.paymentMethod,
                fromDate: filterInput.timeRange.length > 0 ? filterInput.timeRange[0].format('YYYY-MM-DD') : undefined,
                toDate: filterInput.timeRange.length > 0 ? filterInput.timeRange[1].format('YYYY-MM-DD') : undefined,
            })
            .then((res: AxiosResponse<{
                content: IOrder[],
                totalElements: number
            }>) => {
                console.log('order data: ', res.data);
                dispatch(setOrderData(res.data.content));
            })
            .catch(err => {
                console.log('filter error: ', err);
            })
            .finally(() => setTableLoading(false));
    };

    const [hasInitialized, setHasInitialized] = useState(false);
    useEffect(() => {
        if (!hasInitialized) {
            onCallApi();
            subscriptionPackService.getAllSubscriptionPack()
                .then((res: AxiosResponse<ISubscriptionPack[]>) => {
                    setSubscriptionPackList(res.data);
                });
            setHasInitialized(true);
        }

        setDataSource(reIndexTbl(pageConfig.current || 1, pageConfig.pageSize || 0, orderState.data));
    }, [orderState]);

    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <h1 className="text-[23px] font-[400] m-0">
                    {t('order.page-title')}
                </h1>
                {/* <Button onClick={() => setAddEditOrderModal({
                    ...addEditOrderModal,
                    visible: true
                })}>
                    {t('order.addBtn')}
                </Button> */}
            </div>

            <div className="grid lg:flex items-center gap-3 bg-white px-[15px] py-[15px]">
                <Space>
                    <label className="font-bold">{t('order.table.status')}: </label>
                    <Select className="min-w-[200px]"
                        onChange={val => {
                            filterInput.status = val;
                            setFilterInput(filterInput);
                            onCallApi();
                        }}
                        value={filterInput.status}>
                        <Select.Option value="ALL">
                            {t('order.eStatus.ALL')}
                        </Select.Option>

                        {
                            FINAL_STATUSES.map((status, index) =>
                                <Select.Option key={index} value={status} >
                                    {t('order.eStatus.' + status)}
                                </Select.Option>)
                        }
                    </Select>
                </Space>

                <Space>
                    <label className="font-bold">{t('order.table.paymentMethod')}: </label>
                    <Select className="min-w-[200px]"
                        onChange={val => {
                            filterInput.paymentMethod = val;
                            setFilterInput(filterInput);
                            onCallApi();
                        }}
                        value={filterInput.paymentMethod}>
                        <Select.Option value="ALL">
                            {t('order.eStatus.ALL')}
                        </Select.Option>

                        <Select.Option value={'ATM'} >
                            {t('order.ePaymentMethod.ATM')}
                        </Select.Option>
                        <Select.Option value={'VN_PAY'} >
                            {t('order.ePaymentMethod.VN_PAY')}
                        </Select.Option>
                    </Select>
                </Space>

                <Space>
                    <label className="font-bold">{t('order.table.timeRange')}: </label>
                    <DatePicker.RangePicker onChange={(val: any) => {
                        console.log('val: ', val);

                        if (!val)
                            filterInput.timeRange = [];
                        else
                            filterInput.timeRange = [val[0], val[1]];

                        setFilterInput(filterInput);
                        onCallApi();
                    }} />
                </Space>

                <Space>
                    <label className="font-bold">{t('order.table.keyword')}: </label>
                    <Input.Search placeholder={`${t('placeholders.search')}`}
                        value={filterInput.q} onChange={e => setFilterInput({ ...filterInput, q: e.target.value })}
                        onSearch={onSearch}
                        style={{ width: 200 }} />
                </Space>
            </div>


            <Table columns={columns} loading={tableLoading} dataSource={dataSource} pagination={pageConfig} />
            <AddEditOrderModal
                visible={addEditOrderModal.visible}
                title={addEditOrderModal.title}
                onCancel={addEditOrderModal.onCancel}
                onOk={addEditOrderModal.onOk}
                input={addEditOrderModal.input}
                subscriptionPackList={subscriptionPackList}
            />
        </div>

        <UpgradeOrderModal visible={upgradeOrderModal.visible}
            onCancel={upgradeOrderModal.onCancel}
            onOk={upgradeOrderModal.onOk} subscriptionPackList={subscriptionPackList}
            input={upgradeOrderModal.input} />

        <DetailOrderModal
            visible={detailOrderModal.visible}
            onCancel={detailOrderModal.onCancel}
            input={detailOrderModal.input}
        />
    </>)
};


export default OrderPage;