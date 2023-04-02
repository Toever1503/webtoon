import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import mangaService, { MangaFilterInput, MangaInput } from '../../services/manga/MangaService';
import { useNavigate } from 'react-router-dom';
import { AxiosResponse } from 'axios';


const { Search } = Input;

const columns: ColumnsType<MangaInput> = [
    {
        title: 'STT',
        dataIndex: 'stt',
        key: 'stt',
    },
    {
        title: 'Name',
        dataIndex: 'title',
        key: 'name',
        render: (text) => <a>{text}</a>,
    },
    {
        title: 'Genres',
        dataIndex: 'age',
        key: 'age',
    },
    {
        title: 'Authors',
        dataIndex: 'address',
        key: 'address',
    },
    {
        title: 'Tags',
        key: 'tags',
        dataIndex: 'tags',
    },
    {
        title: 'Action',
        key: 'action',
        render: (_, record) => (
            <Space size="middle">
                <a>Edit</a>
                <a>Delete</a>
            </Space>
        ),
    },
];




const onSearch = (value: string) => console.log(value);

const MangaPage: React.FC = () => {
    const navigate = useNavigate();
    const [mangaFilter, setMangaFilter] = React.useState<MangaFilterInput>({
        status: 'ALL',
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
    const [mangaData, setMangaData] = React.useState<MangaInput[]>();
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
                    e.stt = index + 1;
                    return e;
                }));
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements
                });
            })
            .finally(() => setTableLoading(false));
    }
    useEffect(() => {
        onfilterManga();
    }, [mangaFilter]);
    return (
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <p className="text-[23px] font-[400]">Manga</p>
                <Button className="font-medium" onClick={() => navigate('/mangas/add')}>Add new</Button>
            </div>
            <div className="flex justify-between">
                <div className="flex space-x-3 items-center">
                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'ALL' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'ALL' })}>
                        <p className="m-0">All</p><p className="m-0">(2)</p>
                    </div>
                    <div>
                        <p className="m-0">|</p>
                    </div>
                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'PUBLISHED' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'PUBLISHED' })}>
                        <p className="m-0">Published</p><p className="m-0">(2)</p>
                    </div>

                    <div>
                        <p className="m-0">|</p>
                    </div>

                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'DRAFTED' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'DRAFTED' })}>
                        <p className="m-0">Drafted</p><p className="m-0">(2)</p>
                    </div>
                    <div>
                        <p className="m-0">|</p>
                    </div>

                    <div className={'flex space-x-[2px] cursor-pointer hover:text-blue-400' + (mangaFilter.status === 'DELETED' ? ' text-blue-400' : '')} onClick={() => setMangaFilter({ ...mangaFilter, status: 'DELETED' })}>
                        <p className="m-0">Deleted</p><p className="m-0">(2)</p>
                    </div>
                </div>
                <div>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>
            <Table columns={columns} loading={tableLoading} pagination={pageConfig} dataSource={mangaData} onChange={onChangeTable} />
        </div>

    )
}
export default MangaPage;