import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps, Modal } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import AddUpdateGenreModal from './component/modal/AddUpdateGenreModal';
import { GenreModel } from '../../stores/features/manga/genreSlice';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../stores';
import { deleteGenreById, setGenreData } from '../../stores/features/manga/genreSlice';
import { ExclamationCircleFilled } from "@ant-design/icons";
import genreService, { GenreInput } from "../../services/manga/GenreService";
import { showNofification } from '../../stores/features/notification/notificationSlice';
import { reIndexTbl } from '../../utils/indexData';
import { useTranslation } from 'react-i18next';


const { Search } = Input;
const { confirm } = Modal;

const MangaGenrePage: React.FC = () => {
    const genreData = useSelector((state: RootState) => state.genre);

    const {t} = useTranslation();
    const dispatch = useDispatch();

    const [dataSource, setDataSource] = useState<GenreModel[]>([]);
    const [tblLoading, setTblLoading] = useState<boolean>(false);
    const [searchVal, setSearchVal] = useState<string>('');

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 0,
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
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: 'Tên',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Slug',
            dataIndex: 'slug',
            key: 'slug',
        },
        {
            title: 'Tổng số truyện',
            dataIndex: 'mangaCount',
            key: 'mangaCount',
        },
        {
            title: 'Hành động',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => updateGenre(record)}>Sửa</a>
                    <a onClick={() => deleteGenre(record)}>Xóa</a>
                </Space>
            ),
            width: 200,
        },
    ];




    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', page);
        const currentPage = pageConfig.current ? pageConfig.current - 1 : 0;
        pageConfig.current = page.current;
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
                        dispatch(showNofification({
                            type: 'success',
                            message: 'Delete genre successfully',
                        }))
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
                    data: res.data.content.map((item: GenreInput, index: number) => ({ ...item, key: item.id, stt: index + 1 })),
                    totalElements: res.data.totalElements
                }));

                setPageConfig({ ...pageConfig, total: res.data.totalElements });
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

        console.log('change data: ', genreData);
        setDataSource(reIndexTbl(pageConfig.current || 0, pageConfig.pageSize || 10, genreData.data));
    }, [genreData]);


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Danh sách thể loại</p>
                    <Button className="font-medium" onClick={addNewGenre}>Thêm mới</Button>
                    {/* @ts-ignore */}
                    <AddUpdateGenreModal config={addUpdateGenreModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder={`${t('placeholders.search')}`} onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={dataSource} loading={tblLoading} onChange={onPageChange} pagination={pageConfig} />
        </div>
    )
}

export default MangaGenrePage;