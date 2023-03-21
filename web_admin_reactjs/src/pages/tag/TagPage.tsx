import React, { useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import AddUpdateTagModal from './component/AddUpdateTagModal';
import { GenreModel } from '../../stores/features/manga/genreSlice';
import { useSelector } from 'react-redux';
import { RootState } from '../../stores';

const { Search } = Input;

const TagPage: React.FC = () => {
    const tagData = useSelector((state: RootState) => state.tag.data);

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 10,
        showSizeChanger: false,
    });

    const [addUpdateTagModal, setAddUpdateTagModal] = useState<object>({
        title: 'Add new tag',
        visible: false,
        type: 'add',
        setVisible: (visible: boolean) => setAddUpdateTagModal({ ...addUpdateTagModal, visible }),
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
    }

    const onSearch = (value: string) => console.log(value);

    const addNewGenre = () => {
        setAddUpdateTagModal({ ...addUpdateTagModal, visible: true, title: 'Add new tag', type: 'add' });
        console.log('add new genre', addUpdateTagModal);

    };

    const updateGenre = (record: GenreModel) => {
        console.log('update  genre', record)
    };

    const deleteGenre = (record: GenreModel) => {
        console.log('delete  genre', record)
    };


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Tag</p>
                    <Button className="font-medium" onClick={addNewGenre}>Add new</Button>
                    {/* @ts-ignore */}
                    <AddUpdateTagModal config={addUpdateTagModal} />
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={tagData} onChange={onPageChange} pagination={pageConfig} />
        </div>
    )
}

export default TagPage;