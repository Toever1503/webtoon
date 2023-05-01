import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, Dropdown, MenuProps, Popconfirm, Tooltip } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import mangaService, { MangaFilterInput, MangaInput, MangaStatus, ReleaseStatus } from '../../services/manga/MangaService';
import { Link, useNavigate } from 'react-router-dom';
import { AxiosResponse } from 'axios';
import { TagInput } from '../../services/TagService';
import { AuthorInput } from '../../services/manga/AuthorService';
import { GenreInput } from '../../services/manga/GenreService';
import { DeleteOutlined, DownOutlined } from '@ant-design/icons';
import debounce from '../../utils/debounce';
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';
import { showNofification } from '../../stores/features/notification/notificationSlice';


const { Search } = Input;


const mangaStatus: MangaStatus[] = [
    'PUBLISHED', 'DRAFTED', 'DELETED'
];

const releaseStatus: ReleaseStatus[] = [
    'COMING', 'ONGOING',  'ON_STOPPING', 'CANCELLED', 'COMPLETED'
];






const MangaPage: React.FC = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [mangaStatusCalc, setMangaStatusCalc] = React.useState<{
        'PUBLISHED': number,
        'DRAFTED': number,
        'DELETED': number,
        'ALL': number | undefined
    }>({
        'PUBLISHED': 0,
        'DRAFTED': 0,
        'DELETED': 0,
        'ALL': 0
    });

    const columns: ColumnsType<MangaInput> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: t('manga.table.title'),
            dataIndex: 'title',
            key: 'title',
            render: (text) => <>{text}</>,
        },
        {
            title: t('manga.table.mangaType'),
            dataIndex: 'mangaType',
            key: 'mangaType',
            render: (text) => <>{t(`manga.eMangaType.${text}`)}</>,
        },
        {
            title: t('manga.table.releaseStatus'),
            dataIndex: 'mangaStatus',
            key: 'mangaStatus',
            render: (text: ReleaseStatus, record) => <>
                <Dropdown menu={getReleaseStatus(text, record)} trigger={['click']}>
                    <a onClick={(e) => e.preventDefault()} className='text-[12px]'>
                        <span className='text-[12px] flex gap-x-[5px] items-center'>
                            <Tooltip title="Thay đổi trạng thái">
                                {
                                    t(`manga.eReleaseStatus.${text}`)
                                }
                            </Tooltip>
                            <DownOutlined />
                        </span>
                    </a>
                </Dropdown>

            </>,
        },
        {
            title: t('manga.table.genre'),
            dataIndex: 'genres',
            key: 'genres',
            render: (_, record) => (
                <>
                    {
                        record?.genres.length > 0 ?
                            // @ts-ignore
                            record.genres.map((genre: GenreInput) => {
                                return <Tag key={genre.id}>{genre.name}</Tag>
                            })
                            : '-'
                    }
                </>
            ),
            width: 200,
        },
        {
            title: t('manga.table.author'),
            dataIndex: 'authors',
            key: 'authors',
            render: (_, record) => (
                <>
                    {
                        record?.authors.length > 0 ?
                            // @ts-ignore
                            record.authors.map((author: AuthorInput) => {
                                return <Tag key={author.id}>{author.name}</Tag>
                            })
                            : '-'
                    }
                </>
            ),
            width: 200,
        },
        {
            title: t('manga.table.tag'),
            key: 'tags',
            dataIndex: 'tags',
            render: (_, record) => (
                <>
                    {
                        record?.tags.length > 0 ?
                            // @ts-ignore
                            record.tags.map((tag: TagInput) => {
                                return <Tag key={tag.id}>{tag.tagName}</Tag>
                            })
                            : '-'
                    }
                </>
            ),
            width: 200,
        },
        // {
        //     title: 'Trạng thái',
        //     dataIndex: 'status',
        //     key: 'status',
        //     render: (text, record) => <>
        //         <Dropdown menu={getStatusItems(text, record)} trigger={['click']}>
        //             <a onClick={(e) => e.preventDefault()} className='text-[12px]'>
        //                 <span className='text-[12px] flex gap-x-[5px] items-center'>
        //                     <Tooltip title="Thay đổi trạng thái">
        //                         {text}
        //                     </Tooltip>
        //                     <DownOutlined />
        //                 </span>
        //             </a>
        //         </Dropdown>
        //     </>,
        // },
        {
            title: t('manga.table.action'),
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <Link to={`/mangas/edit/${record.id}`}>
                        {t('table.col.edit')}
                    </Link>
                    <Popconfirm
                        title={t('manga.form.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                            onDeleleManga(record.id);
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <a onClick={e => e.stopPropagation()} className="text-red-400 hover:text-red-500">
                            {t('table.col.delete')}
                        </a>
                    </Popconfirm>

                </Space>
            ),
        },
    ];

    const [mangaFilter, setMangaFilter] = React.useState<MangaFilterInput>({
        status: 'ALL',
        q: ''
    });

    const onChangeTable = (page: TablePaginationConfig) => {
        console.log('page', page);
        pageConfig.current = page.current || 1;
        onfilterManga();
    }
    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 100,
        showSizeChanger: false,
    });
    const [mangaData, setMangaData] = React.useState<MangaInput[]>([]);
    const [tableLoading, setTableLoading] = React.useState<boolean>(false);
    const onfilterManga = () => {
        setTableLoading(true);
        mangaService.filterManga(mangaFilter, (pageConfig?.current || 1) - 1, (pageConfig.pageSize || 10))
            .then((res: AxiosResponse<{
                totalElements: number | undefined;
                content: MangaInput[],
                total: number
            }>) => {
                console.log('manga data; ', res.data.content);
                setMangaData(res.data.content.map((e: MangaInput, index: number) => {
                    // @ts-ignore
                    e.index = index + 1;
                    // @ts-ignore
                    e.key = e.id;
                    return e;
                }));
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements
                });
                mangaStatusCalc.ALL = res.data.totalElements;
                setMangaStatusCalc(mangaStatusCalc)
            })
            .finally(() => setTableLoading(false));
    }


    const getStatusItems = (status: MangaStatus, record: MangaInput): MenuProps => {
        return {
            items: mangaStatus.filter((e: MangaStatus) => e !== status).map((e: MangaStatus) => {
                return {
                    key: e,
                    label: e,
                    onClick: () => {
                        console.log('e', e);
                        setTableLoading(true);
                        mangaService.changeStatus(record.id, e)
                            .then(() => {
                                dispatch(showNofification({
                                    type: 'success',
                                    message: 'Thay đổi trạng thái thành công!'
                                }));
                                setMangaData(mangaData.map((item: MangaInput) => {
                                    if (item.id === record.id) {
                                        item.status = e;
                                    }
                                    return item;
                                }));
                            })
                            .catch(err => {
                                console.log('err', err);
                                dispatch(showNofification({
                                    type: 'error',
                                    message: 'Thay đổi trạng thái thất bại!'
                                }));
                            })
                            .finally(() => setTableLoading(false));
                    }
                }
            })
        }
    };
    const getReleaseStatus = (status: ReleaseStatus, record: MangaInput): MenuProps => {
        return {
            items: releaseStatus.filter((e: ReleaseStatus) => e !== status).map((e: ReleaseStatus) => {
                return {
                    key: e,
                    label: t(`manga.eReleaseStatus.${e}`),
                    onClick: () => {
                        console.log('e', e);
                        console.log('e', e);
                        setTableLoading(true);
                        mangaService.changeReleaseStatus(record.id, e)
                            .then(() => {
                                dispatch(showNofification({
                                    type: 'success',
                                    message: 'Thay đổi trạng thái thành công!'
                                }));
                                setMangaData(mangaData.map((item: MangaInput) => {
                                    if (item.id === record.id) {
                                        item.mangaStatus = e;
                                    }
                                    return item;
                                }));
                            })
                            .catch(err => {
                                console.log('err', err);
                                dispatch(showNofification({
                                    type: 'error',
                                    message: 'Thay đổi trạng thái thất bại!'
                                }));
                            })
                            .finally(() => setTableLoading(false))
                    }
                }
            })
        }
    }
    const onSearch = () => {
        onfilterManga();
    }

    const onDeleleManga = (id: number | string) => {
        mangaService.deleteById(id)
            .then(() => {

                setMangaData(mangaData?.filter((e: MangaInput) => e.id !== id));
                dispatch(showNofification({
                    type: 'success',
                    message: t('manga.form.errors.delete-success')
                }));
            })
            .catch((err) => {
                console.log('delete manga error', err);
                dispatch(showNofification({
                    type: 'error',
                    message: t('manga.form.errors.delete-failed')
                }));
            });
    }

    useEffect(() => {
        onfilterManga();
    }, [mangaFilter.status]);
    return (
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <p className="text-[23px] font-[400]">Manga</p>
                <Button className="font-medium" onClick={() => navigate('/mangas/add')}>Add new</Button>
            </div>
            
            <div className="flex justify-end">
                <div className="flex space-x-3 items-center hidden">
                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'ALL' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'ALL' })}>
                        <p className="m-0">All</p><p className="m-0">({mangaStatusCalc.ALL})</p>
                    </div>
                    <div>
                        <p className="m-0">|</p>
                    </div>

                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'DELETED' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'DELETED' })}>
                        <p className="m-0">Deleted</p><p className="m-0">({mangaStatusCalc.DELETED})</p>
                    </div>
                </div>
                <div>
                    <Search placeholder={`${t('placeholders.search')}`} value={mangaFilter.q} onChange={e => setMangaFilter({ ...mangaFilter, q: e.target.value })} onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>
            <Table columns={columns} loading={tableLoading} pagination={pageConfig} dataSource={mangaData} onChange={onChangeTable} />
        </div>

    )
}
export default MangaPage;