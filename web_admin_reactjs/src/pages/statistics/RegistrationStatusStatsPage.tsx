import { Dropdown, Input, MenuProps, Popconfirm, Space, Table, Tooltip, Button, Segmented } from "antd";
import { ColumnsType, TablePaginationConfig } from "antd/es/table";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import { reIndexTbl } from "../../utils/indexData";
import { AxiosResponse } from "axios";
import { showNofification } from "../../stores/features/notification/notificationSlice";
import IUserType from "../user/types/IUserType";
import ISubscriptionPack from "../../services/subscription_pack/types/ISubscriptionPack";
import statisticService from "../../services/stats/StatisticService";
import { useNavigate } from "react-router-dom";



interface IUserRegistrationStatus {
    user?: IUserType;
    subscriptionPack: ISubscriptionPack;
}

const RegistrationStatusStatsPage: React.FC = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const dispatch = useDispatch();


    const [tableLoading, setTableLoading] = useState<boolean>(false);
    const [dataSource, setDataSource] = useState<IUserRegistrationStatus[]>([
        {
            user: {
                id: 1,
                email: 'fas',
                fullName: 'fas',
                phone: 'fas',
                accountType: "FB",
                sex: "FEMALE",
                authorities: [],
                username: 'fas',
                status: "ACTIVED"
            },
            subscriptionPack: {
                name: 'fas',
                price: 1,
                monthCount: 1,
            },
        }
    ]);
    const [pageConfig, setPageConfig] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [userFilter, setUserFilter] = useState<{
        type: string;
        q?: string;
    }>({
        type: 'EXPIRING'
    });
    const onSearch = () => {
        onCallApiFilterUserSubscriptionStatus();
    }


    const columns: ColumnsType<IUserRegistrationStatus> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('statistic.registrationStatus.table.user'),
            dataIndex: 'user',
            key: 'user',
            render: (_, record: IUserRegistrationStatus) => <>{record?.user?.fullName}</>,
        },
        {
            title: t('statistic.registrationStatus.table.subscriptionPack'),
            dataIndex: 'subscriptionPack',
            key: 'subscriptionPack',
            render: (_, record: IUserRegistrationStatus) => <>{record?.subscriptionPack?.name}</>,
        },
        {
            title: t('statistic.registrationStatus.table.expiredDate'),
            dataIndex: 'expiredDate',
            key: 'expiredDate',
            render: (text) => <>{text}</>,
        },

        {
            title: t('comment.table.action'),
            key: 'action',
            width: 150,
            render: (_, record) => (
                <Space size="small">
                    {
                        userFilter.type == 'EXPIRING' ?
                            <Button size="small">{
                                t('statistic.registrationStatus.table.action.sendEmail')
                            }</Button>
                            : '-'
                    }
                </Space>
            ),
        },
    ];



    const onTblChange = (pagination: TablePaginationConfig) => {
        pageConfig.current = pagination.current || 1;
        setPageConfig(pageConfig);
        onCallApiFilterUserSubscriptionStatus();
    };

    const onCallApiFilterUserSubscriptionStatus = () => {
        setTableLoading(true);
        statisticService
            .filterUserSubscriptionPackStatus(
                userFilter.q,
                userFilter.type,
                pageConfig.current - 1,
                pageConfig.pageSize,
            )
            .then((res: AxiosResponse<{
                content: IUserRegistrationStatus[];
                totalElements: number;
            }>) => {
                console.log('user registration status', res.data);
                setDataSource(reIndexTbl(pageConfig.current, pageConfig.pageSize || 0, res.data.content));
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements,
                })
            })
            .catch((err) => {
                console.log('err', err);
                dispatch(showNofification({ type: 'error', message: t('notifications.getDataFailed') }));
            })
            .finally(() => setTableLoading(false));

    }

    const [hasInitialized, setHasInitialized] = useState(false);
    useEffect(() => {
        if (!hasInitialized) {
            onCallApiFilterUserSubscriptionStatus();
            setHasInitialized(true);
        }
    }, []);


    const FILTER_STATUS = [
        {
            'label': 'Sắp hết hạn',
            'value': 'EXPIRING'
        },
        {
            'label': 'Đã hết hạn',
            'value': 'EXPIRED'
        }
    ];

    const onChangeStatus = (val: string) => {
        userFilter.type = val;
        setUserFilter(userFilter);
        onCallApiFilterUserSubscriptionStatus();
    };

    return (<>
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <h1 className="text-[23px] font-[400] m-0">
                    {t('statistic.registrationStatus.title')}
                </h1>
                <Button>
                    Gửi email gia hạn cho tất cả
                </Button>

            </div>
            <div className="flex justify-between items-center">
                <Segmented options={FILTER_STATUS} value={userFilter.type} onChange={((val) => onChangeStatus(String(val)))} />
                <Input.Search placeholder={`${t('placeholders.search')}`} value={userFilter.q} onChange={e => setUserFilter({ ...userFilter, q: e.target.value })} onSearch={onSearch} style={{ width: 200 }} />
            </div>

            <Table columns={columns} loading={tableLoading} dataSource={dataSource} onChange={onTblChange} rowKey={(() => Math.random())} pagination={pageConfig} />
        </div>
    </>)
}

export default RegistrationStatusStatsPage;