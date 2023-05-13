import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps, Modal } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../stores';
import { AuthorModel, deleteAuthorById, setAuthorData } from '../../stores/features/manga/authorSlice';
import tagService, { TagInput } from "../../services/TagService";
import { deleteTagById, setTagData, TagModel } from "../../stores/features/manga/tagSlice";
import authorService, { AuthorInput } from "../../services/manga/AuthorService";
import AddUpdateAuthorModal from "./component/modal/AddUpdateAuthorModal";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { showNofification } from '../../stores/features/notification/notificationSlice';
import { reIndexTbl } from '../../utils/indexData';
import { useTranslation } from 'react-i18next';

const { Search } = Input;
const { confirm } = Modal;

const MangaAuthorPage: React.FC = () => {
    const { t } = useTranslation();
    const authorData = useSelector((state: RootState) => state.author);

    const dispatch = useDispatch();
    const [searchVal, setSearchVal] = useState<string>('');

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: authorData.pageSize,
        total: 0,
        showSizeChanger: false,
    });

    const [dataSource, setDataSource] = useState<AuthorModel[]>(reIndexTbl(pageConfig.current || 0, pageConfig.pageSize || 10, authorData.data));

    const [addUpdateAuthorModal, setAddUpdateAuthorModal] = useState<object>({
        title: 'Thêm mới tác giả',
        visible: false,
        type: 'add',
        setVisible: (visible: boolean) => setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible }),
        incrementTotalElements: () => setPageConfig({ ...pageConfig, current: pageConfig.total ? pageConfig.total + 1 : 1 }),
    });

    const columns: ColumnsType<AuthorModel> = [
        {
            title: 'STT',
            dataIndex: 'index',
            key: 'stt',
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
            render: (text) => <>
                {text ? text : '0'}
            </>
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => updateAuthor(record)}>Sửa</a>
                    <a onClick={() => deleteAuthor(record)}>Xóa</a>
                </Space>
            ),
            width: 200,
        },
    ];



    const [tblLoading, setTblLoading] = useState<boolean>(false);
    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', pageConfig);
        const currentPage = page.current ? page.current - 1 : 0;
        pageConfig.current = page.current;
        setPageConfig({ ...pageConfig });
        filterAuthor(searchVal, currentPage);
    }

    const onSearch = (value: string) => {
        console.log('search', value);
        setSearchVal(value);
        const currentPage = pageConfig.current ? pageConfig.current - 1 : 0;
        filterAuthor(value, currentPage);
    };

    const addNewAuthor = () => {
        setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible: true, title: 'Add new author', type: 'add' });
        console.log('add new author', addUpdateAuthorModal);

    };

    const updateAuthor = (record: AuthorModel) => {
        console.log('update  author', record)
        setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible: true, title: 'Update tag', type: 'update', record });
    };

    const deleteAuthor = (record: AuthorModel) => {
        console.log('delete  author', record)

        confirm({
            title: 'Are you sure delete this tag?',
            icon: <ExclamationCircleFilled />,
            okText: 'Yes',
            okType: 'danger',
            cancelText: 'No',
            onOk() {
                console.log('OK');
                setTblLoading(true);
                authorService.deleteAuthor([record.id])
                    .then((res) => {
                        console.log('delete tag', res);
                        dispatch(deleteAuthorById({ id: record.id }));
                        dispatch(showNofification({
                            type: 'success',
                            message: 'Delete author successfully',
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

    const filterAuthor = (s = '', page = 0, size = pageConfig.pageSize, sort = 'id,desc') => {
        setTblLoading(true);
        authorService.filterAuthor({ s, page, size, sort })
            .then((res) => {
                console.log('author', res.data);

                dispatch(setAuthorData({
                    data: reIndexTbl(pageConfig.current || 0, pageConfig.pageSize || 10, res.data.content.map((item: AuthorModel) => ({ ...item, key: item.id }))),
                    totalElements: res.data.totalElements
                }));

                setPageConfig({ ...pageConfig, total: res.data.totalElements });
            })
            .finally(() => {
                setTblLoading(false);
            });
    };

    const [hasInitialized, setHasInitialized] = useState<boolean>(false);
    useEffect(() => {
        if (!hasInitialized) {
            filterAuthor();
            setHasInitialized(true);
        }

        setDataSource(reIndexTbl(pageConfig.current || 0, pageConfig.pageSize || 10, authorData.data));

        console.log('author data', authorData.data);

    }, [authorData]);


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Danh sách tác giả</p>
                    <Button className="font-medium" onClick={addNewAuthor}>Thêm mới</Button>
                    {/* @ts-ignore */}
                    {/* <AddUpdateGenreModal config={addUpdateGenreModal} /> need create new */}
                    <AddUpdateAuthorModal config={addUpdateAuthorModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder={`${t('placeholders.search')}`} onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={dataSource} onChange={onPageChange} loading={tblLoading} pagination={pageConfig} />
        </div>
    )
}

export default MangaAuthorPage;