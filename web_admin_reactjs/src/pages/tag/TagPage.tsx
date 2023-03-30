import React, { useEffect, useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps, Modal } from 'antd';
import type { ColumnsType, TableProps } from 'antd/es/table';
import AddUpdateTagModal from './component/AddUpdateTagModal';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../stores';
import tagService, { TagInput } from '../../services/TagService';
import { deleteTagById, setTagData, TagModel } from '../../stores/features/manga/tagSlice';
import { ExclamationCircleFilled } from '@ant-design/icons';
import { showNofification } from '../../stores/features/notification/notificationSlice';

const { Search } = Input;
const { confirm } = Modal;

const TagPage: React.FC = () => {
    const tagData = useSelector((state: RootState) => state.tag);
    const tagDataContent = useSelector((state: RootState) => state.tag.data);

    const dispatch = useDispatch();

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: tagData.size,
        total: tagData.totalElements,
        showSizeChanger: false,
    });

    const [addUpdateTagModal, setAddUpdateTagModal] = useState<object>({
        title: 'Add new tag',
        visible: false,
        type: 'add',
        record: {},
        setVisible: (visible: boolean) => setAddUpdateTagModal({ ...addUpdateTagModal, visible }),
        incrementTotalElements: () => setPageConfig({ ...pageConfig, current: pageConfig.total ? pageConfig.total + 1 : 1 }),
    });

    const columns: ColumnsType<TagModel> = [
        {
            title: 'STT',
            dataIndex: 'stt',
            key: 'stt',
        },
        {
            title:'Name',
            dataIndex: 'tagName',
            key: 'tagName',
        },
        {
            title: 'Slug',
            dataIndex: 'slug',
            key: 'slug',
        },
        // {
        //     title: 'Total Manga',
        //     dataIndex: 'mangaCount',
        //     key: 'mangaCount',
        // },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => updateTag(record)}>Edit</a>
                    <a onClick={() => deleteTag(record)}>Delete</a>
                </Space>
            ),
            width: 200,
        },
    ];

    const [tblLoading, setTblLoading] = useState<boolean>(false);
    const [searchVal, setSearchVal] = useState<string>('');

    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', page);
        const currentPage = page.current ? page.current - 1 : 0;
        filterTag(searchVal, currentPage);
    }

    const onSearch = (value: string) => {
        console.log('search', value);
        setSearchVal(value);
        const currentPage = pageConfig.current ? pageConfig.current - 1 : 0;
        filterTag(value, currentPage);
    };

    const addNewGenre = () => {
        setAddUpdateTagModal({ ...addUpdateTagModal, visible: true, title: 'Add new tag', type: 'add' });
        console.log('add new genre', addUpdateTagModal);
    };

    const updateTag = (record: TagModel) => {
        console.log('update  tag', record)
        setAddUpdateTagModal({ ...addUpdateTagModal, visible: true, title: 'Update tag', type: 'update', record });
    };

    const deleteTag = (record: TagModel) => {
        console.log('delete  tag', record);

        confirm({
            title: 'Are you sure delete this tag?',
            icon: <ExclamationCircleFilled />,
            okText: 'Yes',
            okType: 'danger',
            cancelText: 'No',
            onOk() {
                console.log('OK');
                setTblLoading(true);
                tagService.deleteTag([record.id])
                    .then((res) => {
                        console.log('delete tag', res);
                        dispatch(deleteTagById(record.id));
                        dispatch(showNofification({
                            type: 'success',
                            message: 'Delete tag successfully',
                        }));
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

    const filterTag = (s = '', page = 0, size = pageConfig.pageSize, sort = 'id,desc') => {
        setTblLoading(true);
        tagService.filterTag({ s, page, size, sort })
            .then((res) => {
                console.log('tag', res.data);
                dispatch(setTagData({
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
            filterTag();
            setHasInitial(true);
        }
        else
            setPageConfig({ ...pageConfig, total: tagData.totalElements });

    }, [tagData]);
    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-bold">Tag</p>
                    <Button className="font-medium" onClick={addNewGenre}>Add new</Button>
                    {/* @ts-ignore */}
                    <AddUpdateTagModal config={addUpdateTagModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={tagDataContent} loading={tblLoading} onChange={onPageChange} pagination={pageConfig} />
        </div>
    )
}

export default TagPage;