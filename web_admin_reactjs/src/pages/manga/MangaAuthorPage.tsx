import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps, Modal } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../stores';
import { AuthorModel, deleteAuthorById, setAuthorData } from '../../stores/features/manga/authorSlice';
import tagService, { TagInput } from "../../services/TagService";
import { deleteTagById, setTagData, TagModel } from "../../stores/features/manga/tagSlice";
import authorService from "../../services/manga/AuthorService";
import AddUpdateAuthorModal from "./component/modal/AddUpdateAuthorModal";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { showNofification } from '../../stores/features/notification/notificationSlice';

const { Search } = Input;
const { confirm } = Modal;

const MangaAuthorPage: React.FC = () => {
    const authorData = useSelector((state: RootState) => state.author);
    const authorDataContent = useSelector((state: RootState) => state.author.data);
    const dispatch = useDispatch();
    const [searchVal, setSearchVal] = useState<string>('');

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: authorData.current,
        pageSize: authorData.size,
        total: authorData.totalElements,
        showSizeChanger: false,
    });

    const [addUpdateAuthorModal, setAddUpdateAuthorModal] = useState<object>({
        title: 'Add new author',
        visible: false,
        type: 'add',
        setVisible: (visible: boolean) => setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible }),
        incrementTotalElements: () => setPageConfig({ ...pageConfig, current: pageConfig.total ? pageConfig.total + 1 : 1 }),
    });

    const columns: ColumnsType<AuthorModel> = [
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
                    <a onClick={() => updateAuthor(record)}>Edit</a>
                    <a onClick={() => deleteAuthor(record)}>Delete</a>
                </Space>
            ),
            width: 200,
        },
    ];



    const [tblLoading, setTblLoading] = useState<boolean>(false);
    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', page);
        const currentPage = page.current ? page.current - 1 : 0;
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
                console.log('tag', res.data);
                dispatch(setAuthorData({
                    data: res.data.content.map((item: TagInput, index: number) => ({ ...item, key: item.id, stt: index + 1 })),
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
            filterAuthor();
            setHasInitial(true);
        }
        else
            setPageConfig({ ...pageConfig, total: authorData.totalElements });

    }, [authorData]);


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Author</p>
                    <Button className="font-medium" onClick={addNewAuthor}>Add new</Button>
                    {/* @ts-ignore */}
                    {/* <AddUpdateGenreModal config={addUpdateGenreModal} /> need create new */}
                    <AddUpdateAuthorModal config={addUpdateAuthorModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={authorDataContent} onChange={onPageChange} loading={tblLoading} pagination={pageConfig} />
        </div>
    )
}

export default MangaAuthorPage;