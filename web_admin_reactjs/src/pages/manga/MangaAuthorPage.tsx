import React, { useState } from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input, ModalProps } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useSelector } from 'react-redux';
import { RootState } from '../../stores';
import { AuthorModel } from '../../stores/features/manga/authorSlice';


const { Search } = Input;


const MangaAuthorPage: React.FC = () => {
    const authorData = useSelector((state: RootState) => state.author.data);

    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 10,
        showSizeChanger: false,
    });

    const [addUpdateAuthorModal, setAddUpdateAuthorModal] = useState<object>({
        title: 'Add new author',
        visible: false,
        type: 'add',
        setVisible: (visible: boolean) => setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible }),
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

    


    const onPageChange = (page: TablePaginationConfig) => {
        console.log('page', page);
    }

    const onSearch = (value: string) => console.log(value);

    const addNewAuthor = () => {
        setAddUpdateAuthorModal({ ...addUpdateAuthorModal, visible: true, title: 'Add new author', type: 'add' });
        console.log('add new author', addUpdateAuthorModal);

    };

    const updateAuthor = (record: AuthorModel) => {
        console.log('update  author', record)
    };

    const deleteAuthor = (record: AuthorModel) => {
        console.log('delete  author', record)
    };


    return (
        <div className="space-y-3 py-3">
            <div className='flex justify-between items-center'>
                <div className="flex space-x-3">
                    <p className="text-[23px] font-[400]">Author</p>
                    <Button className="font-medium" onClick={addNewAuthor}>Add new</Button>
                    {/* @ts-ignore */}
                    {/* <AddUpdateGenreModal config={addUpdateGenreModal} /> need create new */}
                </div>

                <div className='search-genre flex justify-end'>
                    <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
                </div>
            </div>

            <Table columns={columns} dataSource={authorData} onChange={onPageChange} pagination={pageConfig} />
        </div>
    )
}

export default MangaAuthorPage;