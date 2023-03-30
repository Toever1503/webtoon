import React, {useEffect, useState} from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps, Modal } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import AddUpdateGenreModal from './component/AddUpdateGenreModal';
import { GenreModel } from '../../stores/features/manga/genreSlice';
import {useDispatch, useSelector} from 'react-redux';
import { RootState } from '../../stores';
import {deleteGenreById, setGenreData} from '../../stores/features/manga/genreSlice';
import {ExclamationCircleFilled} from "@ant-design/icons";
import genreService from "../../services/manga/GenreService";


const { Search } = Input;
const { confirm } = Modal;

const MangaGenrePage: React.FC = () => {
    const genreData = useSelector((state: RootState) => state.genre.data);
    const genreDataContent = useSelector((state: RootState) => state.tag.data);

    const dispatch = useDispatch();

    const [tblLoading, setTblLoading] = useState<boolean>(false);
    const [searchVal, setSearchVal] = useState<string>('');

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: genreData.current,
        pageSize: genreData.size,
        total: genreData.totalElements,
        showSizeChanger: false,
    });

    const [addUpdateGenreModal, setAddUpdateGenreModal] = useState<object>({
        title: 'Add new genre',
        visible: false,
        type: 'add',
        setVisible: (visible: boolean) => setAddUpdateGenreModal({ ...addUpdateGenreModal, visible }),
        incrementTotalElements: () => setPageConfig({ ...pageConfig, current: pageConfig.total ? pageConfig.total + 1 : 1 }),
    });

    const columns: ColumnsType<GenreModel> = [
        {
            title: 'STT',
            dataIndex: 'stt',
            key: 'stt',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Slug',
            dataIndex: 'slug',
            key: 'slug',
        },
        {
            title: 'Total Manga',
            dataIndex: 'mangaCount',
            key: 'mangaCount',
        },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => updateGenre(record)}>Edit</a>
                    <a onClick={() => deleteGenre(record)}>Delete</a>
                </Space>
            ),
            width: 200,
        },
    ];

    


    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', page);
        const currentPage = pageConfig.current ? pageConfig.current - 1 : 0;
        filterGenre(searchVal, currentPage);
    }

    const onSearch = (value: string) => {
        setSearchVal(value);
        const currentPage = pageConfig.current ? pageConfig.current - 1 : 0;
        filterGenre(value, currentPage);
    };

    const addNewGenre = () => {
        setAddUpdateGenreModal({ ...addUpdateGenreModal, visible: true, title: 'Add new genre', type: 'add' });
        console.log('add new genre', addUpdateGenreModal);

    };

    const updateGenre = (record: GenreModel) => {
        console.log('update  genre', record)
        setAddUpdateGenreModal({ ...addUpdateGenreModal, visible: true, title: 'Update tag', type: 'update', record });
    };

    const deleteGenre = (record: GenreModel) => {
        console.log('delete  genre', record);
            confirm({
                title: 'Are you sure delete this tag?',
                icon: <ExclamationCircleFilled />,
                okText: 'Yes',
                okType: 'danger',
                cancelText: 'No',
                onOk() {
                    console.log('OK');
                    setTblLoading(true);
                    genreService.deleteGenre([record.id])
                        .then((res) => {
                            console.log('delete tag', res);
                            dispatch(deleteGenreById({ id: record.id }));
                        })
                        .finally(() => {
                            setTblLoading(false);
                        });
                },
                onCancel() {
                    console.log('Cancel');
                },
            });
    };

    const filterGenre = (s = '', page = 0, size = pageConfig.pageSize, sort = 'id,desc') => {
        setTblLoading(true);
        genreService.filterGenre({ s, page, size, sort })
            .then((res) => {
                console.log('tag', res.data);
                dispatch(setGenreData({
                    data: res.data.content.map((item: TagInput) => ({ ...item, key: item.id })),
                    totalElements: res.data.totalElements
                }));

                setPageConfig({ ...pageConfig, current: page + 1, pageSize: size, total: res.data.totalElements });
            })
            .finally(() => {
                setTblLoading(false);
            });
    };

    const [hasInitial, setHasInitial] = useState<boolean>(false);
    useEffect(() => {
        if (!hasInitial) {
            filterGenre();
            setHasInitial(true);
        }
        else
            setPageConfig({ ...pageConfig, total: genreData.totalElements });

    }, [genreData]);


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Genre</p>
                    <Button className="font-medium" onClick={addNewGenre}>Add new</Button>
                    {/* @ts-ignore */}
                    <AddUpdateGenreModal config={addUpdateGenreModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={genreData} loading={tblLoading} onChange={onPageChange} pagination={pageConfig} />
        </div>
    )
}

export default MangaGenrePage;