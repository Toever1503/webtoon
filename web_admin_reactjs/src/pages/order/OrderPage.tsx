import { Button, Input, PaginationProps, Popconfirm, Select, Space, Table } from "antd";
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
import { IOrderState, addOrder, setOrderData, updateOrder } from "../../stores/features/order/orderSlice";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import AddEditOrderModal, { AddEditOrderModalProps } from "./components/AddEditOrderModal";
import subscriptionPackService from "../../services/subscription_pack/subscriptionPackService";
import debounce from "../../utils/debounce";
import IUserType from "../user/types/IUserType";
import formatVnCurrency from "../../utils/formatVnCurrency";
import { dateTimeFormat } from "../../utils/dateFormat";

const OrderPage: React.FC = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const orderState: IOrderState = useSelector((state: RootState) => state.order);
    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<IOrder[]>();
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
            width: 200,
            render: (text) => <>{text}</>,
        },
        {
            title: t('order.table.subscription'),
            dataIndex: 'subs_pack_id',
            key: 'subscription',
            render: (text: ISubscriptionPack) => <>{text?.name}</>,
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
            title: t('order.table.status'),
            dataIndex: 'status',
            key: 'status',
            render: (text) => <>{
                t('order.eStatus.' + text)
            }</>,
        },
        {
            title: t('order.table.expireDate'),
            dataIndex: 'expiredSubsDate',
            key: 'expireDate',
            render: (_, record: IOrder) => <span>
                {
                    dateTimeFormat(record.expiredSubsDate)
                }
            </span>,
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
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => showEditModal(record)}>{t('order.upgradeSubs')}</a>
                    <a onClick={() => showEditModal(record)}>{t('buttons.edit')}</a>
                    <Popconfirm
                        title={t('manga.form.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <a onClick={e => e.stopPropagation()} className="text-red-400 hover:text-red-500">{
                            t('buttons.delete')
                        }</a>
                    </Popconfirm>

                </Space>
            ),
        },
    ];

    const [filterInput, setFilterInput] = useState<IOrderFilterInput>({
        status: '',
    });


    const FINAL_STATUSES: EORDER_STATUS[] = [
        'PENDING_PAYMENT',
        'PAID',
        'CANCELED',
        'COMPLETED',
        'REFUND_CONFIRM_PENDING',
        'REFUNDING',
        'REFUNDED',
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

    const showEditModal = (record: IOrder) => {
        addEditOrderModal.input = record;
        setAddEditOrderModal({
            ...addEditOrderModal,
            visible: true,
            title: t('order.modal.editTitle'),
        });
    }
    // end edit modal

    const onSearch = debounce(() => {
        onCallApi();
    });

    const onCallApi = () => {
        if (tableLoading) return;
        setTableLoading(true);
        orderService
            .filterOrder({
                q: filterInput.q ? filterInput.q : undefined,
                status: filterInput.status ? filterInput.status : undefined,
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
    }

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
                <Button onClick={() => setAddEditOrderModal({
                    ...addEditOrderModal,
                    visible: true
                })}>
                    {t('order.addBtn')}
                </Button>
            </div>

            <div className="flex justify-between items-center">
                <div className="flex items-center space-x-3">
                    <label className="font-bold">{t('order.table.status')}: </label>
                    <Select className="min-w-[150px]"
                        onChange={val => {
                            filterInput.status = val;
                            setFilterInput(filterInput);
                            onCallApi();
                        }}
                        value={filterInput.status}>
                        <Select.Option value="">
                            {t('order.eStatus.ALL')}
                        </Select.Option>

                        {
                            FINAL_STATUSES.map((status, index) =>
                                <Select.Option key={index} value={status} >
                                    {t('order.eStatus.' + status)}
                                </Select.Option>)
                        }
                    </Select>
                </div>
                <Input.Search placeholder={`${t('placeholders.search')}`}
                    value={filterInput.q} onChange={e => setFilterInput({ ...filterInput, q: e.target.value })}
                    onSearch={onSearch}
                    style={{ width: 200 }} />
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
    </>)
};


export default OrderPage;